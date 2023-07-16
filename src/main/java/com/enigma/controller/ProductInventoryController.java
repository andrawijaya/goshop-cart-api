package com.enigma.controller;

import com.enigma.entity.Product;
import com.enigma.entity.ProductInventory;
import com.enigma.entity.request.product.ProductRequest;
import com.enigma.entity.request.productInventory.ProductInventoryRequest;
import com.enigma.entity.response.ErrorResponse;
import com.enigma.entity.response.PagingResponse;
import com.enigma.entity.response.SuccessResponse;
import com.enigma.exception.NotFoundException;
import com.enigma.exception.UnauthorizedException;
import com.enigma.service.IProductInventoryService;
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
@RequestMapping("/inventories")
public class ProductInventoryController {

    private Logger logger = LoggerFactory.getLogger(ProductInventoryController.class);
    private IProductInventoryService productInventoryService;

    private ModelMapper modelMapper;

    @Autowired
    public ProductInventoryController(IProductInventoryService productInventoryService, ModelMapper modelMapper){
        this.productInventoryService = productInventoryService;
        this.modelMapper = modelMapper;
    }

    // Get All Data
    @GetMapping
    public ResponseEntity getAllProductInventory(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "id") String sortBy
    ) throws Exception {
        Page<ProductInventory> productInventories = productInventoryService.list(page, size, direction, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(new PagingResponse<>("Success get product inventory list", productInventories));
    }

    // Create Data
    @PostMapping
    public ResponseEntity createProductInventory(@Valid @RequestBody ProductInventoryRequest productInventoryRequest) throws Exception{

        ProductInventory productInventory = modelMapper.map(productInventoryRequest, ProductInventory.class);
        System.out.println("inventory : "+ productInventory);
        ProductInventory result = productInventoryService.create(productInventory);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>("Success create product inventory", result));
    }

    // Delete Data
    @DeleteMapping("/{id}")
    public ResponseEntity deleteByIdProductInventory(@PathVariable("id") String id) throws Exception{
        productInventoryService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Data with id " + id + " has been Delete !");
    }

    // Update Data
    @PutMapping("/{id}")
    public ResponseEntity updateProductInventory(@PathVariable("id") String id, @RequestBody ProductInventoryRequest productInventoryRequest) throws Exception{
        ProductInventory newProductInventory = modelMapper.map(productInventoryRequest, ProductInventory.class);

        newProductInventory.setId(id);
        productInventoryService.update(newProductInventory, id);
        ProductInventory result = newProductInventory;
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Data with id "+ id + " has been Updated !",result));
    }

    // find by id
    @GetMapping("/{id}")
    public ResponseEntity findByIdProductInventory(@PathVariable("id") String id) throws Exception{
        Optional<ProductInventory> productInventory = productInventoryService.getById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(new SuccessResponse<>("Success get product inventory By id", productInventory));
    }

    // find By Filter
    @GetMapping(params = {"keyword", "value"})
    public ResponseEntity getByFilterProductInventory(@RequestParam @NotBlank(message = "invalid keyword required") String keyword, @RequestParam String value) throws Exception {
        List<ProductInventory> result = productInventoryService.getByFilter(keyword, value);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Success get product inventory by " + keyword, result));
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

    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedException(UnauthorizedException exception) {
        logger.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("500", exception.getMessage()));
    }

}

