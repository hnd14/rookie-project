package com.example.project.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.example.project.jwt.JwtAuthenticationFilter;
import com.example.project.jwt.TokenProvider;

@Configuration
public class AuthConfig {

    @Bean
    JwtAuthenticationFilter filter(TokenProvider tokenProvider, UserDetailsService userService) {
        return new JwtAuthenticationFilter(tokenProvider, userService);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthenticationFilter authFilter)
            throws Exception {
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource((request) -> {
                    CorsConfiguration configuration = new CorsConfiguration();
                    configuration.setAllowedOriginPatterns(List.of("*localhost:[*]"));
                    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
                    configuration.setAllowedHeaders(
                            List.of("Authorization", "Content-type", "Access-Control-Allow-Credentials"));
                    configuration.setAllowCredentials(Boolean.TRUE);
                    return configuration;
                }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/store/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/sign-in").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/verify").permitAll()
                        .requestMatchers(HttpMethod.POST, "/store/sign-up").permitAll()
                        .requestMatchers(HttpMethod.GET, "/auth/log-out").authenticated()
                        .requestMatchers(HttpMethod.GET, "/auth/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/auth/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/auth/me/password").authenticated()
                        .requestMatchers(HttpMethod.POST, "/store/products/{id}/ratings").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/store/me").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/store/products/{id}/ratings").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/store/products/{id}/ratings").authenticated()
                        .requestMatchers(HttpMethod.GET, "/store-back/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/store-back/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/store-back/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/store-back/**").hasRole("ADMIN")
                        .anyRequest().denyAll())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManagerJwt(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
