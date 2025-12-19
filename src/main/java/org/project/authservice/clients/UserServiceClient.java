package org.project.authservice.clients;

import lombok.RequiredArgsConstructor;
import org.project.authservice.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${user.service.base-url}")
    private String userServiceBaseUrl;

    public Optional<UserDTO> getUserByEmail(String email) {
        String url = userServiceBaseUrl + "/users/email/{email}";

        try {
            ResponseEntity<UserDTO> response =
                    restTemplate.getForEntity(url, UserDTO.class, email);
            return Optional.ofNullable(response.getBody());
        } catch (RuntimeException ex) {
            return Optional.empty();
        }
    }

    public void createUser(UserDTO userDTO) {
        String url = userServiceBaseUrl + "/users";
        restTemplate.postForEntity(url, userDTO, Void.class);
    }
}
