package com.enigma.entity.request.productInventory;

import com.enigma.entity.request.category.CategoryIdRequest;
import com.enigma.entity.request.price.PriceIdRequest;
import com.enigma.entity.request.product.ProductIdRequest;
import com.enigma.entity.request.vendor.VendorIdRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@ToString
public class ProductInventoryRequest {
    @NotNull(message = "stock is required")
    @Getter @Setter
    private int stock;

    @Getter @Setter
    private  PriceIdRequest productUnit;

    @Getter @Setter
    private ProductIdRequest product;

    @Getter @Setter
    private VendorIdRequest vendor;

}
