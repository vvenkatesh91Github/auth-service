package org.project.authservice.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String hashPassword(String raw) {
        return encoder.encode(raw);
    }

    public boolean matchPassword(String raw, String hashed) {
        return encoder.matches(raw, hashed);
    }
}
