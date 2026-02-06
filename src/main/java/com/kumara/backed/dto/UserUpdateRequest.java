package com.kumara.backed.dto;

public record UserUpdateRequest(
    String fullName,
    String avatarUrl // Nanti bisa diisi URL gambar jika sudah ada cloud storage
) {}