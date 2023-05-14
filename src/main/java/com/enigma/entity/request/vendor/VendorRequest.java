package com.enigma.entity.request.vendor;
import com.enigma.entity.request.category.CategoryIdRequest;
import com.enigma.entity.request.price.PriceIdRequest;
import com.enigma.entity.request.product.ProductIdRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

public class VendorRequest {
    @NotBlank(message = "vendor name is required")
    @Getter
    @Setter
    private String vendorName;

    @Getter @Setter
    private CategoryIdRequest category;

    @Getter @Setter
    private PriceIdRequest price;
}
