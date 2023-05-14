package com.enigma.service;

import com.enigma.entity.Category;
import com.enigma.entity.Price;
import com.enigma.entity.Product;
import com.enigma.entity.Vendor;
import com.enigma.exception.EntityExistException;
import com.enigma.exception.NotFoundException;
import com.enigma.repository.IProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService implements IProductService{

    private IProductRepository productRepository;

    private IVendorService vendorService;

    private IPriceService priceService;

    private ModelMapper modelMapper;

    public ProductService(IProductRepository productRepository, ModelMapper modelMapper, IVendorService vendorService, IPriceService priceService){
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
        this.vendorService = vendorService;
        this.priceService = priceService;
    }

    List<Product> findByNameContains(String value) {
        List<Product> products = productRepository.findByNameContains(value);
        if (products.isEmpty()) {
            throw new NotFoundException("Product with " + value + " name is not found");
        }

        return products;
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
    public Page<Product> list(Integer page, Integer size, String direction, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.valueOf(direction), sortBy);
        Pageable pageable = PageRequest.of((page - 1), size, sort);
        Page<Product> result = productRepository.findAll(pageable);
        return result;
    }

    @Override
    public Product create(Product data) throws Exception {
        try {
            Optional<Vendor> newVendor = vendorService.getById(data.getVendor().getVendorId());
            Optional<Price> newPrice = priceService.getById(data.getPrice().getPriceId());

            data.setVendor(newVendor.get());
            data.setPrice(newPrice.get());
            Product newProduct = productRepository.save(data);
            return newProduct;
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistException("Data is exist");
        }
    }

    @Override
    public Optional<Product> getById(String id) throws Exception {
        Optional<Product> product = productRepository.findById(id);
        if(product.isEmpty()){
            throw new NotFoundException("productCode is not found");
        }
        return product;
    }

    @Override
    public List<Product> getByFilter(String keyword, String value) throws Exception {
        switch (keyword) {
            case "name":
                return findByNameContains(value);
            default:
                return productRepository.findAll();
        }
    }

    @Override
    public void update(Product data, String id) throws Exception {
        try {
            Product existingProduct = getById(id).get();
            Optional<Vendor> getVendor = vendorService.getById(data.getVendor().getVendorId());
            if(getVendor.isEmpty()){
                throw new NotFoundException("Id Vendor is not found");
            }

            Optional<Price> getPrice = priceService.getById(data.getPrice().getPriceId());
            if(getPrice.isEmpty()){
                throw new NotFoundException("Id Price is not found");
            }

            data.setVendor(getVendor.get());
            data.setPrice(getPrice.get());
            modelMapper.map(data, existingProduct);
            productRepository.save(existingProduct);
        } catch (Exception e) {
            throw new NotFoundException("Update Failed Because Id Not Found");
        }
    }

    @Override
    public void delete(String id) throws Exception {
        try{
            Product productExisting = getById(id).get();
            productRepository.delete(productExisting);
        }catch (Exception e){
            throw new NotFoundException("Delete Failed Because Id Not Found");
        }
    }
}
