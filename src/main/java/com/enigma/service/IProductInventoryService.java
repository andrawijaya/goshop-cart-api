package com.enigma.service;

import com.enigma.entity.Product;
import com.enigma.entity.ProductInventory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IProductInventoryService {

    // create
    ProductInventory create(ProductInventory data) throws Exception;

    // Get All with pagination
    Page<ProductInventory> list(Integer page, Integer size, String direction, String sortBy) throws Exception;

    // getBy id
    Optional<ProductInventory> getById(String id) throws Exception;

    // getby key and value
    List<ProductInventory> getByFilter(String keyword, String value) throws Exception;


    // update
    void update(ProductInventory data, String id) throws Exception;

    // delete
    void delete(String id) throws Exception;

}
