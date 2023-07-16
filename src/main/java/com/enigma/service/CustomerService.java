package com.enigma.service;

import com.enigma.entity.*;
import com.enigma.exception.EntityExistException;
import com.enigma.exception.NotFoundException;
import com.enigma.repository.ICustomerRepository;
import com.enigma.repository.IProductInventoryRepository;
import com.enigma.repository.IUserCredentialRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements ICustomerService{

    private ICustomerRepository customerRepository;

    private ModelMapper modelMapper;


    public CustomerService(ICustomerRepository customerRepository, IUserCredentialRepository userCredentialRepository, ModelMapper modelMapper){
        this.customerRepository = customerRepository;
        this.modelMapper = modelMapper;
    }


    List<Customer> findByNameContains(String value) {
        List<Customer> customers =  customerRepository.findByNameContains(value);
        if (customers.isEmpty()) {
            throw new NotFoundException("Customer with " + value + " name is not found");
        }

        return customers;
    }

//    List<Product> findByPriceContains(String value) {
//        List<Product> products = productRepository.findByPriceContains(Integer.valueOf(value));
//        System.out.println("debug : " + products);
//        if (products.isEmpty()) {
//            throw new NotFoundException("Product with " + value + " price is not found");
//        }
//
//        return products;
//    }

    @Override
    public Page<Customer> list(Integer page, Integer size, String direction, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.valueOf(direction), sortBy);
        Pageable pageable = PageRequest.of((page - 1), size, sort);
        Page<Customer> result = customerRepository.findAll(pageable);
        return result;
    }

    @Override
    public Customer create(Customer data) throws Exception {
        try {
            Customer newCustomer = customerRepository.save(data);
            customerRepository.save(newCustomer);
            return newCustomer;
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistException("Data is exist");
        }
    }

    @Override
    public Optional<Customer> getById(String id) throws Exception {
        Optional<Customer> customers = customerRepository.findById(id);
        if(customers.isEmpty()){
            throw new NotFoundException("customer id is not found");
        }
        return customers;
    }

    @Override
    public List<Customer> getByFilter(String keyword, String value) throws Exception {
        switch (keyword) {
            case "name":
                return findByNameContains(value);
            default:
                return customerRepository.findAll();
        }
    }

    @Override
    public void update(Customer data, String id) throws Exception {
        try {
            Customer existingCustomer= getById(id).get();

            modelMapper.map(data, existingCustomer);
            customerRepository.save(existingCustomer);
        } catch (Exception e) {
            throw new NotFoundException("Update Failed Because Id Not Found");
        }
    }

    @Override
    public void delete(String id) throws Exception {
        try{
            Customer customerExisting = getById(id).get();
            customerRepository.delete(customerExisting);
        }catch (Exception e){
            throw new NotFoundException("Delete Failed Because Id Not Found");
        }
    }
}
