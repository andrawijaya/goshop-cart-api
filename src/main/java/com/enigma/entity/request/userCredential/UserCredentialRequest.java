package com.enigma.entity.request.userCredential;

import com.enigma.entity.Customer;
import com.enigma.entity.request.customer.CustomerCreateRequest;
import com.enigma.entity.request.customer.CustomerIdRequest;
import com.enigma.entity.request.price.PriceIdRequest;
import com.enigma.entity.request.product.ProductIdRequest;
import com.enigma.entity.request.vendor.VendorIdRequest;
import com.enigma.util.Role;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
public class UserCredentialRequest {
    @NotBlank(message = "email is required")
    @Setter @Getter
    private String email;

    @NotBlank(message = "password is required")
    @Setter @Getter
    private String password;

    @Setter @Getter
    private Role role;

}
