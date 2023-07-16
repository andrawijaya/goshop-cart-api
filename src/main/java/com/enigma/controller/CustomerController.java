package com.enigma.controller;

import com.enigma.entity.Customer;
import com.enigma.entity.ProductInventory;
import com.enigma.entity.request.customer.CustomerCreateRequest;
import com.enigma.entity.request.productInventory.ProductInventoryRequest;
import com.enigma.entity.response.ErrorResponse;
import com.enigma.entity.response.PagingResponse;
import com.enigma.entity.response.SuccessResponse;
import com.enigma.exception.NotFoundException;
import com.enigma.exception.UnauthorizedException;
import com.enigma.service.ICustomerService;
import com.enigma.service.IProductInventoryService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private Logger logger = LoggerFactory.getLogger(CustomerController.class);
    private ICustomerService customerService;

    private ModelMapper modelMapper;

    @Autowired
    public CustomerController(ICustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    // Get All Data
    @GetMapping
    public ResponseEntity getAllCustomer(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "id") String sortBy
    ) throws Exception {
        Page<Customer> customers = customerService.list(page, size, direction, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(new PagingResponse<>("Success get customer list", customers));
    }

    // Create Data
    @PostMapping
    public ResponseEntity createCustomer(@Valid @RequestBody CustomerCreateRequest customerCreateRequest) throws Exception {

        Customer customer = modelMapper.map(customerCreateRequest, Customer.class);
        Customer result = customerService.create(customer);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>("Success register customer", result));
    }

    // Delete Data
    @DeleteMapping("/{id}")
    public ResponseEntity deleteByIdCustomer(@PathVariable("id") String id) throws Exception {
        customerService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Data with id " + id + " has been Delete !");
    }

    // Update Data
    @PutMapping("/{id}")
    public ResponseEntity updateProductInventory(@PathVariable("id") String id, @RequestBody CustomerCreateRequest customerCreateRequest) throws Exception {
        Customer newCustomer = modelMapper.map(customerCreateRequest, Customer.class);

        newCustomer.setCustomerId(id);
        customerService.update(newCustomer, id);
        Customer result = newCustomer;
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Data with id " + id + " has been Updated !", result));
    }

    // find by id
    @GetMapping("/{id}")
    public ResponseEntity findByIdCustomer(@PathVariable("id") String id) throws Exception {
        Optional<Customer> productInventory = customerService.getById(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(new SuccessResponse<>("Success get customer By id", productInventory));
    }

    // find By Filter
    @GetMapping(params = {"keyword", "value"})
    public ResponseEntity getByFilterCustomer(@RequestParam @NotBlank(message = "invalid keyword required") String keyword, @RequestParam String value) throws Exception {
        List<Customer> result = customerService.getByFilter(keyword, value);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Success get customer by " + keyword, result));
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
