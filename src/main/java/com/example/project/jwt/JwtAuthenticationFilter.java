package com.example.project.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final UserDetailsService userService;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, UserDetailsService userService) {
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            var token = this.recoverTokenFromCookie(request);
            if (token == null) {
                token = this.recoverToken(request);
            }
            if (token != null) {
                var login = tokenProvider.validateToken(token);
                var user = userService.loadUserByUsername(login);
                var authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), null,
                        user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            SecurityContextHolder.getContext().setAuthentication(null);
            Cookie cookie = new Cookie("accessToken", null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }

    private String recoverTokenFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        return List.of(cookies).stream().filter(c -> (c.getName().equals("accessToken"))).findFirst()
                .map(c -> c.getValue()).orElse(null);
    }
}
