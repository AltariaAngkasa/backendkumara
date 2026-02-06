package com.kumara.backed.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired private JwtUtils jwtUtils;
    @Autowired private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. Ambil Token dari Header Request
            String jwt = parseJwt(request);

            // 2. Cek apakah Token Ada & Valid
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                
                // 3. Ambil Email dari Token
                String email = jwtUtils.getEmailFromJwtToken(jwt);

                // 4. Ambil Data User lengkap dari Database
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                // 5. Set status "Sedang Login" di sistem keamanan Spring
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.err.println("Gagal set user authentication: " + e.getMessage());
        }

        // Lanjut ke proses berikutnya (masuk ke Controller)
        filterChain.doFilter(request, response);
    }

    // Helper: Ambil text setelah kata "Bearer "
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Hapus 7 karakter awal ("Bearer ")
        }

        return null;
    }
}