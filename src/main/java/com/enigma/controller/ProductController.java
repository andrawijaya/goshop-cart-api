package com.enigma.controller;

import com.enigma.entity.Product;
import com.enigma.entity.request.product.ProductRequest;
import com.enigma.entity.response.ErrorResponse;
import com.enigma.entity.response.PagingResponse;
import com.enigma.entity.response.SuccessResponse;
import com.enigma.exception.NotFoundException;
import com.enigma.service.IProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.BindException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(ProductController.class);
    private IProductService productService;

    private ModelMapper modelMapper;

    @Autowired
    public ProductController(IProductService productService, ModelMapper modelMapper){
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    // Get All Data
    @GetMapping
    public ResponseEntity getAllProduct(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "productCode") String sortBy
    ) throws Exception {
        Page<Product> products = productService.list(page, size, direction, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(new PagingResponse<>("Success get product list", products));
    }

    // Create Data
    @PostMapping
    public ResponseEntity createProduct(@Valid @RequestBody ProductRequest productRequest) throws Exception{

        Product product = modelMapper.map(productRequest, Product.class);
        Product result = productService.create(product);
        System.out.println("result : " + result);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>("Success create product", result));
    }

    // Delete Data
    @DeleteMapping("/{id}")
    public ResponseEntity deleteByIdProduct(@PathVariable("id") String id) throws Exception{
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Data with id " + id + " has been Delete !");
    }

    // Update Data
    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable("id") String id, @RequestBody ProductRequest productRequest) throws Exception{
        Product newProduct = modelMapper.map(productRequest, Product.class);
        System.out.println("mapper : " + newProduct);
        System.out.println("id : " + id);
        newProduct.setProductCode(id);
        productService.update(newProduct, id);
        Product result = newProduct;
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Data with id "+ id + " has been Updated !",result));
    }

    // find by id
    @GetMapping("/{id}")
    public ResponseEntity findByIdProduct(@PathVariable("id") String id) throws Exception{
        Optional<Product> price = productService.getById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(new SuccessResponse<>("Success get product By id", price));
    }

    // find By Filter
    @GetMapping(params = {"keyword", "value"})
    public ResponseEntity getByFilterProduct(@RequestParam @NotBlank(message = "invalid keyword required") String keyword, @RequestParam String value) throws Exception {
        List<Product> result = productService.getByFilter(keyword, value);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Success get product by " + keyword, result));
    }

    // Exception Handling
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity handleNoSuchElementFoundException(NotFoundException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("500", exception.getMessage()));
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity handleNotValidException(MethodArgumentNotValidException exception) {
        String msgStr = exception.getMessage();
        List<String> msgList = Arrays.asList(msgStr.split("\\s*;\\s*"));
        String strErrorMsg = msgList.get(5);
        String[] listErrorMsg = strErrorMsg.split("\\[|\\]");
        String currentMsg = listErrorMsg[1];
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("400", currentMsg));
    }



}
