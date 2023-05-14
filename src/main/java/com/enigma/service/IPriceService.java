package com.enigma.service;

import com.enigma.entity.Category;
import com.enigma.entity.Price;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface IPriceService {
    // create
    Price create(Price data) throws Exception;

    // Get All with pagination
    Page<Price> list(Integer page, Integer size, String direction, String sortBy) throws Exception;

    // getBy id
    Optional<Price> getById(String id) throws Exception;

    // update
    void update(Price data, String id) throws Exception;

    // delete
    void delete(String id) throws Exception;
}
