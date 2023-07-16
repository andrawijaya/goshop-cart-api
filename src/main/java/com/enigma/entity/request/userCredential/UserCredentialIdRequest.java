package com.enigma.entity.request.userCredential;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
public class UserCredentialIdRequest {
    @NotNull(message = "id is required")
    @Getter
    @Setter
    private int id;
}
