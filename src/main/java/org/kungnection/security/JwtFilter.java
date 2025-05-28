package org.kungnection.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws IOException, ServletException {

        String authHeader = request.getHeader("Authorization");
        System.out.println("🧪 進入 JwtFilter, authHeader: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                Long userId = jwtUtil.extractUserId(token);
                System.out.println("✅ JWT OK, extracted userId = " + userId);
                request.setAttribute("userId", userId);
            } else {
                System.out.println("❌ JWT 驗證失敗");
            }
        } else {
            System.out.println("❌ 無 Authorization header");
        }

        filterChain.doFilter(request, response);
    }
}