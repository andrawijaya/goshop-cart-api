package com.enigma.entity.request.auth;

import com.enigma.entity.request.customer.CustomerCreateRequest;
import com.enigma.entity.request.customer.CustomerIdRequest;
import com.enigma.util.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
public class AuthRequest {
    @NotBlank(message = "email is required")
    @Setter
    @Getter
    private String email;

    @NotBlank(message = "password is required")
    @Setter @Getter
    private String password;

    @Setter @Getter
    private Role role;

    @Getter @Setter
    private CustomerIdRequest customer;
}
