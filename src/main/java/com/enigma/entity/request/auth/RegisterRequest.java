package com.enigma.entity.request.auth;

import com.enigma.entity.request.customer.CustomerCreateRequest;
import com.enigma.entity.request.userCredential.UserCredentialRequest;
import com.enigma.util.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
public class RegisterRequest {
    @NotBlank(message = "email is required")
    @Setter
    @Getter
    private String firstName;

    @NotBlank(message = "password is required")
    @Setter @Getter
    private String lastName;

    @Getter @Setter
    private UserCredentialRequest auth;
}
