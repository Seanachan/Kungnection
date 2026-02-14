package org.kungnection.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT authentication filter that validates tokens on each request.
 */
public class JwtFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);
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
        log.debug("Processing JWT filter for request: {}", request.getRequestURI());

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            if (jwtUtil.validateToken(token)) {
                Long userId = jwtUtil.extractUserId(token);
                log.debug("JWT validated successfully for userId={}", userId);
                request.setAttribute("userId", userId);
            } else {
                log.warn("JWT validation failed");
            }
        } else {
            log.debug("No Authorization header present");
        }

        filterChain.doFilter(request, response);
    }
}
