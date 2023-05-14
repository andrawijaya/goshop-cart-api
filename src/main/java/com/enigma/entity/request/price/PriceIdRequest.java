package com.enigma.entity.request.price;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
public class PriceIdRequest {
    @NotBlank(message = "price_id is required")
    @Getter
    @Setter
    private String priceId;

}
