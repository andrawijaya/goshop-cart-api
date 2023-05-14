package com.enigma.service;

import com.enigma.entity.Category;
import com.enigma.exception.EntityExistException;
import com.enigma.exception.NotFoundException;
import com.enigma.repository.ICategoryRepository;
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
public class CategoryService implements ICategoryService {

    private ICategoryRepository categoryRepository;

    private ModelMapper modelMapper;

    public CategoryService(ICategoryRepository categoryRepository, ModelMapper modelMapper){
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;

    }

    @Override
    public Category create(Category data) throws Exception {

        try{
            return categoryRepository.save(data);

        }catch (DataIntegrityViolationException e){
            throw new EntityExistException();
        }
    }

//    @Override
//    public List<Category> list() throws Exception {
//        List<Category> categoryList = categoryRepository.findAll();
//        return categoryList;
//    }

    @Override
    public Page<Category> list(Integer page, Integer size, String direction, String sortBy) throws Exception {
        Sort sort = Sort.by(Sort.Direction.valueOf(direction), sortBy);
        Pageable pageable = PageRequest.of((page - 1), size, sort);
        Page<Category> result = categoryRepository.findAll(pageable);
        return result;
    }

    @Override
    public Optional<Category> getById(String id) throws Exception {
        Optional<Category> category = categoryRepository.findById(id);
        if(category.isEmpty()){
            throw new NotFoundException("categoryId is not found");
        }
        return category;
    }

    @Override
    public void update(Category data, String id) throws Exception {
        try {
            Category existingCategory = getById(id).get();
            modelMapper.map(data, existingCategory);
            categoryRepository.save(existingCategory);
        } catch (Exception e) {
            throw new NotFoundException("Update Failed Because Id Not Found");
        }
    }

    @Override
    public void delete(String id) throws Exception {
        try{
            Category existingCategory = getById(id).get();
            categoryRepository.delete(existingCategory);
        }catch (Exception e){
            throw new NotFoundException("Delete Failed Because Id Not Found");
        }
    }
}
