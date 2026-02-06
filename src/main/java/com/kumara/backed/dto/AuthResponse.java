package com.kumara.backed.dto;

public record AuthResponse(
    Long userId,
    String fullName,
    String email,
    String role,
    String businessName,
    String token
) {}