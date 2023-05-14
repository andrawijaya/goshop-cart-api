package com.enigma.entity.request.price;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
public class PriceRequest {
    @NotNull(message = "price cannot empty !")
    @Getter
    @Setter
    private int productPrice;
}
