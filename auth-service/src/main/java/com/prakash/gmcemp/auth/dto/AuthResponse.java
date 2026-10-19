package com.prakash.gmcemp.auth.dto;

public record AuthResponse(String accessToken, String tokenType, long expiresInSeconds) {}
