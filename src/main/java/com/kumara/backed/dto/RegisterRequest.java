package com.kumara.backed.dto;

public record RegisterRequest(
    String fullName,
    String email,
    String password,
    String businessName
) {}