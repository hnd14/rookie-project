package com.example.project.config;

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

import com.example.project.jwt.JwtAuthenticationFilter;
import com.example.project.jwt.TokenProvider;

@Configuration
public class AuthConfig {
    // @Bean 
    // AuditorAware<String> auditorAware(){
    //     return new ApplicationAuditAware();
    // }

    @Bean
    JwtAuthenticationFilter filter(TokenProvider tokenProvider, UserDetailsService userService){
        return new JwtAuthenticationFilter(tokenProvider, userService);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider (UserDetailsService userService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtAuthenticationFilter authFilter) throws Exception{
        return httpSecurity.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers(HttpMethod.GET, "/**").permitAll()
            .requestMatchers(HttpMethod.POST,"/auth/**").permitAll()
            .requestMatchers(HttpMethod.POST,"/store/sign-up").permitAll()
            .anyRequest().authenticated())
        .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
    }

    @Bean
    PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }


    @Bean
    AuthenticationManager authenticationManagerJwt (AuthenticationConfiguration authenticationConfiguration)
        throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
