package com.enigma.entity.request.product;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
public class ProductIdRequest {
    @NotBlank(message = "productCode is required")
    @Getter
    @Setter
    private String productCode;

}
