package com.enigma.entity.request.customer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
public class CustomerIdRequest {
    @NotBlank(message = "customer_id is required")
    @Getter
    @Setter
    private String customerId;

}