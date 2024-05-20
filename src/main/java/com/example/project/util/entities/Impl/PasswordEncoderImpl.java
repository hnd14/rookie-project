package com.example.project.util.entities.Impl;

import org.springframework.stereotype.Component;

import com.example.project.util.entities.PasswordEncoder;

@Component
public class PasswordEncoderImpl implements PasswordEncoder{
    @Override
    public String encode(CharSequence rawPassword) {
        return String.valueOf(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }

}
