package com.prakash.gmcemp.auth.service;

import com.prakash.gmcemp.auth.audit.Auditable;
import com.prakash.gmcemp.auth.dto.*;
import com.prakash.gmcemp.auth.model.AppUser;
import com.prakash.gmcemp.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import com.prakash.gmcemp.auth.exception.BusinessException;

@Service
public class AuthService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    private final long expirationMinutes;

    public AuthService(UserRepository repository, PasswordEncoder encoder,
                       AuthenticationManager authenticationManager, JwtEncoder jwtEncoder,
                       @Value("${jwt.expiration-minutes}") long expirationMinutes) {
        this.repository = repository;
        this.encoder = encoder;
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
        this.expirationMinutes = expirationMinutes;
    }

    @Auditable(
        action = "REGISTER_USER",
        entityType = "USER")
    @Transactional
public void register(RegisterRequest request) {

    if (repository.existsByUsername(request.username())) {
        throw new BusinessException(
                "Username already exists");
    }

    AppUser user = new AppUser();

    user.setUsername(request.username());

    user.setPassword(encoder.encode(request.password()));

    String role =request.role() == null|| request.role().isBlank()
                    ? "USER"
                    : request.role().toUpperCase();

    if (!role.equals("USER") && !role.equals("ADMIN")) {

        throw new BusinessException("Role must be USER or ADMIN");
    }

    user.setRole(role);

    repository.save(user);
}

@Auditable(
        action = "USER_LOGIN",
        entityType = "USER")
 public AuthResponse login(LoginRequest request) {

    Authentication auth =
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()));

    Instant now = Instant.now();

    Instant expiresAt =
            now.plusSeconds(expirationMinutes * 60);

    String scope =
            auth.getAuthorities()
                    .stream()
                    .map(authority -> authority.getAuthority())
                    .reduce((a, b) -> a + " " + b)
                    .orElse("");

    JwtClaimsSet claims =
            JwtClaimsSet.builder()
                    .issuer("gmcemp-auth-service")
                    .issuedAt(now)
                    .expiresAt(expiresAt)
                    .subject(auth.getName())
                    .claim("scope", scope)
                    .build();

    JwsHeader header =
            JwsHeader.with(MacAlgorithm.HS256)
                    .build();

    String token =
            jwtEncoder.encode(
                    JwtEncoderParameters.from(
                            header,
                            claims))
                    .getTokenValue();

    return new AuthResponse(
            token,
            "Bearer",
            expirationMinutes * 60);
}
}
