package com.enigma.entity.request.auth;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank(message = "email is required")
    @Setter
    @Getter
    private String email;

    @NotBlank(message = "password is required")
    @Setter @Getter
    private String password;
}
