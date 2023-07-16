package com.enigma.controller;

import com.enigma.entity.Category;
import com.enigma.entity.request.category.CategoryRequest;
import com.enigma.entity.response.ErrorResponse;
import com.enigma.entity.response.PagingResponse;
import com.enigma.entity.response.SuccessResponse;
import com.enigma.exception.UnauthorizedException;
import com.enigma.service.ICategoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    private ICategoryService categoryService;

    private ModelMapper modelMapper;
    private Logger logger = LoggerFactory.getLogger(CategoryController.class);


    @Autowired
    public CategoryController(ICategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    // Get All With Paging
    @GetMapping
    public ResponseEntity getAllCategory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "categoryId") String sortBy
    ) throws Exception {
        Page<Category> categories = categoryService.list(page, size, direction, sortBy);
        System.out.println("params : " + categories);
        return ResponseEntity.status(HttpStatus.OK).body(new PagingResponse<>("Success get product list", categories));
    }

    // Create category
    @PostMapping
    public ResponseEntity createCategory(@Valid @RequestBody CategoryRequest categoryRequest) throws Exception {
        Category category = modelMapper.map(categoryRequest, Category.class);
        Category result = categoryService.create(category);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>("Success create product", result));
    }


    // delete category
    @DeleteMapping("/{id}")
    public ResponseEntity deleteByIdCategory(@PathVariable("id") String id) throws Exception {
        categoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Data with id " + id + " has been Delete !");
    }

    // update category
    @PutMapping("/{id}")
    public ResponseEntity updateCategory(@PathVariable("id") String id, @RequestBody CategoryRequest categoryRequest) throws Exception {
        Category newCategory = modelMapper.map(categoryRequest, Category.class);
        newCategory.setCategoryId(id);
        categoryService.update(newCategory, id);
        Category result = newCategory;
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Data with id " + id + " has been Updated !", result));
    }

    // find by id
    @GetMapping("/{id}")
    public ResponseEntity findByIdCategory(@PathVariable("id") String id) throws Exception {
        Optional<Category> price = categoryService.getById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(new SuccessResponse<>("Success get product By id", price));
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(UnauthorizedException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("500", exception.getMessage()));
    }

}
