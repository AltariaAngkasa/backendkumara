package com.kumara.backed.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${kumara.app.jwtSecret}")
    private String jwtSecret;

    @Value("${kumara.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // 1. GENERATE TOKEN (Membuat Stempel)
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email) // Kita simpan email di dalam token
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256) // Tanda tangan digital
                .compact();
    }

    // Helper untuk mengambil Key
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(convertToBase64(jwtSecret)));
    }
    
    // Trik agar secret key text biasa bisa dipakai (Encode ke Base64 dulu)
    private String convertToBase64(String key) {
        return java.util.Base64.getEncoder().encodeToString(key.getBytes());
    }

    // 2. AMBIL EMAIL DARI TOKEN
    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // 3. VALIDASI TOKEN (Cek Stempel Asli/Palsu/Kadaluarsa)
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }
}