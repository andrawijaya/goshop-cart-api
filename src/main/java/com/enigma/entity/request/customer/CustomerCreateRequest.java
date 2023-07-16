package com.enigma.entity.request.customer;

import com.enigma.entity.request.price.PriceIdRequest;
import com.enigma.entity.request.product.ProductIdRequest;
import com.enigma.entity.request.userCredential.UserCredentialIdRequest;
import com.enigma.entity.request.userCredential.UserCredentialRequest;
import com.enigma.entity.request.vendor.VendorIdRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
public class CustomerCreateRequest {

    @NotBlank(message = "firstName is required")
    @Getter @Setter
    private String firstName;

    @NotBlank(message = "lastName is required")
    @Getter @Setter
    private String lastName;


}
