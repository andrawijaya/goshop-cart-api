package com.enigma.service;

import com.enigma.entity.Product;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    // create
    Product create(Product data) throws Exception;

    // Get All with pagination
    Page<Product> list(Integer page, Integer size, String direction, String sortBy) throws Exception;

    // getBy id
    Optional<Product> getById(String id) throws Exception;

    // getby key and value
    List<Product> getByFilter(String keyword, String value) throws Exception;


    // update
    void update(Product data, String id) throws Exception;

    // delete
    void delete(String id) throws Exception;


}
