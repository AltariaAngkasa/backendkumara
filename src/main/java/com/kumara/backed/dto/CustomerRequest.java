package com.kumara.backed.dto;

public record CustomerRequest(
    String name,
    String phone,
    String address,
    String notes
) {}