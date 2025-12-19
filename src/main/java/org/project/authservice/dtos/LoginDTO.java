package org.project.authservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDTO {
    private String email;
    private String password;
}
