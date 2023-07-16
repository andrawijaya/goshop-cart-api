package com.enigma.service;

import com.enigma.entity.*;
import com.enigma.exception.EntityExistException;
import com.enigma.exception.NotFoundException;
import com.enigma.repository.IOrderRepository;
import com.enigma.repository.IProductInventoryRepository;
import com.enigma.repository.IProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public class OrderService implements IOrderService{
    private IOrderRepository orderRepository;

    private IProductInventoryService productInventoryService;

    private ICustomerService customerService;

    private ModelMapper modelMapper;

    public OrderService(IOrderRepository orderRepository, ModelMapper modelMapper, IProductInventoryService productInventoryService, ICustomerService customerService){
        this.orderRepository = orderRepository;
        this.modelMapper = modelMapper;
        this.productInventoryService = productInventoryService;
        this.customerService = customerService;
    }

    List<Order> findByNameContains(String value) {
        List<Order> orders = orderRepository.findByNameContains(value);
        if (orders.isEmpty()) {
            throw new NotFoundException("Order with " + value + " name is not found");
        }

        return orders;
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
    public Page<Order> list(Integer page, Integer size, String direction, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.valueOf(direction), sortBy);
        Pageable pageable = PageRequest.of((page - 1), size, sort);
        Page<Order> result = orderRepository.findAll(pageable);
        return result;
    }

    @Override
    public Order create(Order data) throws Exception {
        try {
            Optional<Customer> getCustomer = customerService.getById(data.getCustomer().getCustomerId());
            Optional<ProductInventory> getProductInventory = productInventoryService.getById(data.getProductInventory().getId());

            data.setCustomer(getCustomer.get());
            data.setProductInventory(getProductInventory.get());
            Order newOrder = orderRepository.save(data);
            return newOrder;
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistException("Data is exist");
        }
    }

    @Override
    public Optional<Order> getById(String id) throws Exception {
        Optional<Order> order = orderRepository.findById(id);
        if(order.isEmpty()){
            throw new NotFoundException("order id is not found");
        }
        return order;
    }

    @Override
    public List<Order> getByFilter(String keyword, String value) throws Exception {
        switch (keyword) {
            case "name":
                return findByNameContains(value);
            default:
                return orderRepository.findAll();
        }
    }

    @Override
    public void update(Order data, String id) throws Exception {
        try {
            Order orderProduct = getById(id).get();
            Optional<Customer> getCustomer = customerService.getById(data.getCustomer().getCustomerId());
            Optional<ProductInventory> getProductInventory = productInventoryService.getById(data.getProductInventory().getId());

            data.setCustomer(getCustomer.get());
            data.setProductInventory(getProductInventory.get());
            modelMapper.map(data, orderProduct);
            orderRepository.save(orderProduct);
        } catch (Exception e) {
            throw new NotFoundException("Update Failed Because Id Not Found");
        }
    }

    @Override
    public void delete(String id) throws Exception {
        try{
            Order orderExisting = getById(id).get();
            orderRepository.delete(orderExisting);
        }catch (Exception e){
            throw new NotFoundException("Delete Failed Because Id Not Found");
        }
    }
}
