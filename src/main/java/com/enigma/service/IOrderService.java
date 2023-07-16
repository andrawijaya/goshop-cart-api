package com.enigma.service;

import com.enigma.entity.Order;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IOrderService {
    // create
    Order create(Order data) throws Exception;

    // Get All with pagination
    Page<Order> list(Integer page, Integer size, String direction, String sortBy) throws Exception;

    // getBy id
    Optional<Order> getById(String id) throws Exception;

    // getby key and value
    List<Order> getByFilter(String keyword, String value) throws Exception;


    // update
    void update(Order data, String id) throws Exception;

    // delete
    void delete(String id) throws Exception;
}
