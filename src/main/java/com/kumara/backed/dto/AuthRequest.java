// package com.kumara.backed.dto;


// // Request saat User Register
// public record RegisterRequest(
//     String fullName,
//     String email,
//     String password,
//     String businessName
// ) {}

// // Request saat User Login
// public record LoginRequest(
//     String email,
//     String password
// ) {}

// // Response balik ke Flutter (User ID penting untuk disimpan di HP)
// public record AuthResponse(
//     Long userId,
//     String fullName,
//     String email,
//     String token // Nanti bisa diisi JWT, sementara kita kirim dummy/session ID
// ) {}