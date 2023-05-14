package com.enigma.service;

import com.enigma.entity.Category;
import com.enigma.entity.Product;
import com.enigma.entity.Vendor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IVendorService {
    // create
    Vendor create(Vendor data) throws Exception;

    // Get All with pagination
    Page<Vendor> list(Integer page, Integer size, String direction, String sortBy) throws Exception;

    // getBy id
    Optional<Vendor> getById(String id) throws Exception;

    List<Vendor> getByFilter(String keyword, String value) throws Exception;


    // update
    void update(Vendor data, String id) throws Exception;

    // delete
    void delete(String id) throws Exception;
}
