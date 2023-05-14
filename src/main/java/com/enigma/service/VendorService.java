package com.enigma.service;

import com.enigma.entity.Category;
import com.enigma.entity.Price;
import com.enigma.entity.Vendor;
import com.enigma.exception.EntityExistException;
import com.enigma.exception.NotFoundException;
import com.enigma.repository.IVendorRepository;
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
public class VendorService implements IVendorService {

    private IVendorRepository vendorRepository;

    private ICategoryService categoryService;

    private IPriceService priceService;

    private ModelMapper modelMapper;

    public VendorService(IVendorRepository vendorRepository, ModelMapper modelMapper, ICategoryService categoryService, IPriceService priceService){
        this.vendorRepository = vendorRepository;
        this.modelMapper = modelMapper;
        this.categoryService = categoryService;
        this.priceService = priceService;
    }

    List<Vendor> findByNameContains(String value) {
        List<Vendor> vendors = vendorRepository.findByNameContains(value);
        if (vendors.isEmpty()) {
            throw new NotFoundException("Vendor with " + value + " name is not found");
        }

        return vendors;
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
    public Page<Vendor> list(Integer page, Integer size, String direction, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.valueOf(direction), sortBy);
        Pageable pageable = PageRequest.of((page - 1), size, sort);
        Page<Vendor> result = vendorRepository.findAll(pageable);
        return result;
    }

    @Override
    public Vendor create(Vendor data) throws Exception {
        try {
            Optional<Category> getCategory = categoryService.getById(data.getCategory().getCategoryId());
            if(getCategory.isEmpty()){
                throw new NotFoundException("Id Category is not found");
            }

            Optional<Price> getPrice = priceService.getById(data.getPrice().getPriceId());
            if(getPrice.isEmpty()){
                throw new NotFoundException("Id Price is not found");
            }

            data.setCategory(getCategory.get());
            data.setPrice(getPrice.get());
            Vendor newVendor = vendorRepository.save(data);
            return newVendor;
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistException("Data is exist");
        }
    }

    @Override
    public Optional<Vendor> getById(String id) throws Exception {
        Optional<Vendor> vendor = vendorRepository.findById(id);
        if(vendor.isEmpty()){
            throw new NotFoundException("vendorId is not found");
        }
        return vendor;
    }

    @Override
    public List<Vendor> getByFilter(String keyword, String value) throws Exception {
        switch (keyword) {
            case "name":
                return findByNameContains(value);
            default:
                return vendorRepository.findAll();
        }
    }

    @Override
    public void update(Vendor data, String id) throws Exception {
        try {
            Vendor existingVendor = getById(id).get();
            Optional<Category> getCategory = categoryService.getById(data.getCategory().getCategoryId());
            Optional<Price> getPrice = priceService.getById(data.getPrice().getPriceId());

            data.setCategory(getCategory.get());
            data.setPrice(getPrice.get());
            modelMapper.map(data, existingVendor);
            vendorRepository.save(existingVendor);
        } catch (Exception e) {
            throw new NotFoundException("Update Failed Because Id Not Found");
        }
    }

    @Override
    public void delete(String id) throws Exception {
        try{
            Vendor vendorExisting = getById(id).get();
            vendorRepository.delete(vendorExisting);
        }catch (Exception e){
            throw new NotFoundException("Delete Failed Because Id Not Found");
        }
    }
}
