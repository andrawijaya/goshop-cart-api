package com.enigma.service;

import com.enigma.entity.Customer;
import com.enigma.entity.ProductInventory;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    // create
    Customer create(Customer data) throws Exception;

    // Get All with pagination
    Page<Customer> list(Integer page, Integer size, String direction, String sortBy) throws Exception;

    // getBy id
    Optional<Customer> getById(String id) throws Exception;

    // getby key and value
    List<Customer> getByFilter(String keyword, String value) throws Exception;


    // update
    void update(Customer data, String id) throws Exception;

    // delete
    void delete(String id) throws Exception;
}
