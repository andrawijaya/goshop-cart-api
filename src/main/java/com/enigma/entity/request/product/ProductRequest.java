package com.enigma.entity.request.product;
import com.enigma.entity.request.price.PriceIdRequest;
import com.enigma.entity.request.vendor.VendorIdRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
public class ProductRequest {
    @NotBlank(message = "product name is required")
    @Getter
    @Setter
    private String productName;

    @Getter @Setter
    private VendorIdRequest vendor;

    @Getter @Setter
    private PriceIdRequest price;

}
