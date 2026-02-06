package com.kumara.backed.dto;

public record LoginRequest(
    String email,
    String password
) {}