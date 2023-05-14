package com.enigma.entity.request.category;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@ToString
public class CategoryRequest {
    @NotBlank(message = "categoryName is required")
    @Getter
    @Setter
    private String categoryName;
}
