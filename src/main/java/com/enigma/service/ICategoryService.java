package com.enigma.service;

import com.enigma.entity.Category;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    // create
    Category create(Category data) throws Exception;

    // Get All with pagination
    Page<Category> list(Integer page, Integer size, String direction, String sortBy) throws Exception;

//    List<Category> list() throws Exception;

    // getBy id
    Optional<Category> getById(String id) throws Exception;

    // update
    void update(Category data, String id) throws Exception;

    // delete
    void delete(String id) throws Exception;
}
