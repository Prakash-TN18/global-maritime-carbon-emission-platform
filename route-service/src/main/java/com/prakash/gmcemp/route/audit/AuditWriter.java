package com.prakash.gmcemp.route.audit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditWriter {

    private final AuditLogRepository repository;

    public AuditWriter(
            AuditLogRepository repository) {

        this.repository = repository;
    }

    @Transactional(
            propagation = Propagation.REQUIRES_NEW)
    public void save(AuditLog auditLog) {

        repository.save(auditLog);
    }
}