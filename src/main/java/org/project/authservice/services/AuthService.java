package org.project.authservice.services;

import org.project.authservice.clients.UserServiceClient;
import org.project.authservice.dtos.UserDTO;
import org.project.authservice.security.JwtUtil;
import org.project.authservice.security.PasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserServiceClient userServiceClient;
    private final PasswordService passwordService;
    private final JwtUtil jwtUtil;

    public String login(String email, String password) {
        Optional<UserDTO> userDTOOptional = userServiceClient.getUserByEmail(email);

        if(userDTOOptional.isPresent()) {
            UserDTO user = userDTOOptional.get();
            if (!"ACTIVE".equals(user.getStatus())) {
                throw new RuntimeException("User inactive");
            }
            if (!passwordService.matchPassword(password, user.getHashedPassword())) {
                throw new RuntimeException("Invalid credentials");
            }
            return jwtUtil.generateToken(user.getName(), user.getRoles());
        }
        throw new RuntimeException("User not found");
    }

    public void signup(UserDTO userDTO) {
        Optional<UserDTO> userOptional = userServiceClient.getUserByEmail(userDTO.getEmail());
        if (userOptional.isPresent()) {
            throw new RuntimeException("Email already in use");
        } else {
            userDTO.setHashedPassword(passwordService.hashPassword(userDTO.getPassword()));
            if (userDTO.getStatus() == null) userDTO.setStatus("ACTIVE");
            if (userDTO.getRoles() == null) userDTO.setRoles("ROLE_USER");
            userServiceClient.createUser(userDTO);
        }
    }
}
