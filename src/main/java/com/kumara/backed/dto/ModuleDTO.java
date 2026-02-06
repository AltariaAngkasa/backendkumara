package com.kumara.backed.dto;

public record ModuleDTO(
    Long id,
    String title,
    String description,
    String videoUrl, // Link Youtube
    String thumbnailAsset, // Nama file gambar (nanti kita pakai aset lokal di Flutter)
    String duration, // Misal: "5 Menit"
    String level // "Pemula", "Menengah"
) {}