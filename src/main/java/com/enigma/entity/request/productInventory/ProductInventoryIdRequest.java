package com.enigma.entity.request.productInventory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
public class ProductInventoryIdRequest {
    @NotBlank(message = "id is required")
    @Getter
    @Setter
    private String id;
}
