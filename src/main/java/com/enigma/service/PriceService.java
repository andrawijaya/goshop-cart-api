package com.enigma.service;

import com.enigma.entity.Category;
import com.enigma.entity.Price;
import com.enigma.exception.EntityExistException;
import com.enigma.exception.NotFoundException;
import com.enigma.repository.ICategoryRepository;
import com.enigma.repository.IPriceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PriceService implements IPriceService {


    private IPriceRepository priceRepository;

    private ModelMapper modelMapper;

    public PriceService(IPriceRepository priceRepository, ModelMapper modelMapper){
        this.priceRepository = priceRepository;
        this.modelMapper = modelMapper;

    }

    @Override
    public Price create(Price data) throws Exception {
        try{
            return priceRepository.save(data);

        }catch (DataIntegrityViolationException e){
            throw new EntityExistException();
        }
    }


    @Override
    public Page<Price> list(Integer page, Integer size, String direction, String sortBy) throws Exception {
        Sort sort = Sort.by(Sort.Direction.valueOf(direction), sortBy);
        Pageable pageable = PageRequest.of((page - 1), size, sort);
        Page<Price> result = priceRepository.findAll(pageable);
        return result;
    }

    @Override
    public Optional<Price> getById(String id) throws Exception {
        Optional<Price> price = priceRepository.findById(id);
        if(price.isEmpty()){
            throw new NotFoundException("priceId is not found");
        }
        return price;
    }

    @Override
    public void update(Price data, String id) throws Exception {
        try {
            Price existingPrice = getById(id).get();
            modelMapper.map(data, existingPrice);
            priceRepository.save(existingPrice);
        } catch (Exception e) {
            throw new NotFoundException("Update Failed Because Id Not Found");
        }
    }

    @Override
    public void delete(String id) throws Exception {
        try{
            Price existingPrice = getById(id).get();
            priceRepository.delete(existingPrice);
        }catch (Exception e){
            throw new NotFoundException("Delete Failed Because Id Not Found");
        }
    }
}
