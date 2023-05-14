package com.enigma.entity.request.vendor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
public class VendorIdRequest {
    @NotBlank(message = "vendorId is required")
    @Getter
    @Setter
    private String vendorId;

}
