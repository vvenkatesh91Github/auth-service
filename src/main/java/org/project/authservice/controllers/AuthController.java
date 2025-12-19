package org.project.authservice.controllers;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.project.authservice.dtos.LoginDTO;
import org.project.authservice.dtos.UserDTO;
import org.project.authservice.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        String email = loginDTO.getEmail();
        String password = loginDTO.getPassword();
        try {
            String jwtToken = authService.login(email, password);
            ResponseCookie cookie = ResponseCookie.from("JWT_TOKEN", jwtToken)
                    .httpOnly(true)
                    .secure(true) // must match HTTPS
                    .path("/")
                    .sameSite("None") // allows cross-site requests
                    .maxAge(Duration.ofHours(1))
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Unauthorized: " + e.getMessage()));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody UserDTO userDTO) {
        try {
            authService.signup(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Signup failed: " + e.getMessage()));
        }
    }
}
