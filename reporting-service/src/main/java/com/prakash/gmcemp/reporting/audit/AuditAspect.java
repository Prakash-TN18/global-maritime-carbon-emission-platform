package com.prakash.gmcemp.reporting.audit;

import java.util.Optional;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.*;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class AuditAspect {

    private final AuditWriter auditWriter;

    @Value("${spring.application.name}")
    private String serviceName;

    public AuditAspect(
            AuditWriter auditWriter) {

        this.auditWriter = auditWriter;
    }

    @Around("@annotation(auditable)")
    public Object auditMethod(
            ProceedingJoinPoint joinPoint,
            Auditable auditable)
            throws Throwable {

        long startTime = System.currentTimeMillis();

        try {

            Object result = joinPoint.proceed();

            saveAudit(
                    auditable,
                    joinPoint,
                    "SUCCESS",
                    null,
                    System.currentTimeMillis() - startTime
            );

            return result;

        } catch (Throwable ex) {

            saveAudit(
                    auditable,
                    joinPoint,
                    "FAILED",
                    limitMessage(ex.getMessage()),
                    System.currentTimeMillis() - startTime
            );

            throw ex;
        }
    }

    private void saveAudit(
            Auditable auditable,
            ProceedingJoinPoint joinPoint,
            String status,
            String errorMessage,
            long executionTimeMs) {

        try {

            AuditLog auditLog = new AuditLog();

            auditLog.setServiceName(serviceName);
            auditLog.setAction(auditable.action());
            auditLog.setEntityType(auditable.entityType());
            auditLog.setMethodName(
                    joinPoint.getSignature()
                            .toShortString());
            auditLog.setUsername(getUsername());
            auditLog.setRequestUri(getRequestUri());
            auditLog.setStatus(status);
            auditLog.setErrorMessage(errorMessage);
            auditLog.setExecutionTimeMs(executionTimeMs);

            auditWriter.save(auditLog);

        } catch (Exception auditException) {

            /*
             * Audit failure must not stop the main
             * business request.
             */
        }
    }

    private String getUsername() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()) {

            return "ANONYMOUS";
        }

        return authentication.getName();
    }

    private String getRequestUri() {

        RequestAttributes attributes =
                RequestContextHolder
                        .getRequestAttributes();

        if (!(attributes
                instanceof ServletRequestAttributes servletAttributes)) {

            return null;
        }

        HttpServletRequest request =
                servletAttributes.getRequest();

        return request.getMethod()
                + " "
                + request.getRequestURI();
    }

    private String limitMessage(String message) {

        return Optional.ofNullable(message)
                .map(value ->
                        value.length() > 1000
                                ? value.substring(0, 1000)
                                : value)
                .orElse(null);
    }
}
