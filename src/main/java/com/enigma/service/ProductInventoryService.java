package com.enigma.service;

import com.enigma.entity.*;
import com.enigma.exception.EntityExistException;
import com.enigma.exception.NotFoundException;
import com.enigma.repository.IProductInventoryRepository;
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
public class ProductInventoryService implements IProductInventoryService {

    private IProductInventoryRepository productInventoryRepository;

    private IProductService productService;

    private IPriceService priceService;

    private IVendorService vendorService;

    private ModelMapper modelMapper;


    public ProductInventoryService(IProductInventoryRepository productInventoryRepository, IProductService productService, IVendorService vendorService, IPriceService priceService, ModelMapper modelMapper){
        this.productInventoryRepository = productInventoryRepository;
        this.productService = productService;
        this.vendorService = vendorService;
        this.priceService = priceService;
        this.modelMapper = modelMapper;
    }


    List<ProductInventory> findByNameContains(String value) {
        List<ProductInventory> productInventories = productInventoryRepository.findByNameContains(value);
        if (productInventories.isEmpty()) {
            throw new NotFoundException("Product Inventory with " + value + " name is not found");
        }

        return productInventories;
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
    public Page<ProductInventory> list(Integer page, Integer size, String direction, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.valueOf(direction), sortBy);
        Pageable pageable = PageRequest.of((page - 1), size, sort);
        Page<ProductInventory> result = productInventoryRepository.findAll(pageable);
        return result;
    }

    @Override
    public ProductInventory create(ProductInventory data) throws Exception {
        try {
            Optional<Product> getProduct = productService.getById(data.getProduct().getProductCode());
            Optional<Vendor> getVendor = vendorService.getById(data.getVendor().getVendorId());
            Optional<Price> getPrice = priceService.getById(data.getProductUnit().getPriceId());

            data.setProduct(getProduct.get());
            data.setVendor(getVendor.get());
            data.setProductUnit(getPrice.get());
            ProductInventory newProductInventory = productInventoryRepository.save(data);
            return newProductInventory;
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistException("Data is exist");
        }
    }

    @Override
    public Optional<ProductInventory> getById(String id) throws Exception {
        Optional<ProductInventory> productInventory = productInventoryRepository.findById(id);
        if(productInventory.isEmpty()){
            throw new NotFoundException("id is not found");
        }
        return productInventory;
    }

    @Override
    public List<ProductInventory> getByFilter(String keyword, String value) throws Exception {
        switch (keyword) {
            case "name":
                return findByNameContains(value);
            default:
                return productInventoryRepository.findAll();
        }
    }

    @Override
    public void update(ProductInventory data, String id) throws Exception {
        try {
            ProductInventory existingProductInventory = getById(id).get();
            Optional<Product> geProduct = productService.getById(data.getProduct().getProductCode());
            Optional<Vendor> getVendor = vendorService.getById(data.getVendor().getVendorId());

            data.setProduct(geProduct.get());
            data.setVendor(getVendor.get());
            modelMapper.map(data, existingProductInventory);
            productInventoryRepository.save(existingProductInventory);
        } catch (Exception e) {
            throw new NotFoundException("Update Failed Because Id Not Found");
        }
    }

    @Override
    public void delete(String id) throws Exception {
        try{
            ProductInventory productInventoryExisting = getById(id).get();
            productInventoryRepository.delete(productInventoryExisting);
        }catch (Exception e){
            throw new NotFoundException("Delete Failed Because Id Not Found");
        }
    }
}
