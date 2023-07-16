package com.enigma.controller;

import com.enigma.entity.Customer;
import com.enigma.entity.UserCredential;
import com.enigma.entity.request.auth.AuthRequest;
import com.enigma.entity.request.auth.LoginRequest;
import com.enigma.entity.request.auth.RegisterRequest;
import com.enigma.entity.request.customer.CustomerCreateRequest;
import com.enigma.entity.request.customer.CustomerIdRequest;
import com.enigma.entity.request.userCredential.UserCredentialRequest;
import com.enigma.entity.response.ErrorResponse;
import com.enigma.entity.response.PagingResponse;
import com.enigma.entity.response.SuccessResponse;
import com.enigma.exception.NotFoundException;
import com.enigma.exception.UnauthorizedException;
import com.enigma.service.IAuthService;
import com.enigma.service.ICustomerService;
import com.enigma.util.Role;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/auths")
public class AuthController {

    private Logger logger = LoggerFactory.getLogger(AuthController.class);
    private IAuthService authService;

    private ICustomerService customerService;

    private ModelMapper modelMapper;

    @Autowired
    public AuthController(IAuthService authService, ICustomerService customerService, ModelMapper modelMapper) {
        this.authService = authService;
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    // Get All Data
    @GetMapping("/userCredential/getAll")
    public ResponseEntity getAllCustomer(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "5") Integer size,
            @RequestParam(defaultValue = "DESC") String direction,
            @RequestParam(defaultValue = "email") String sortBy
    ) throws Exception {
        Page<UserCredential> userCredentials = authService.list(page, size, direction, sortBy);
        return ResponseEntity.status(HttpStatus.OK).body(new PagingResponse<>("Success get auths list", userCredentials));
    }

    // Create Data
    @PostMapping("/register/customer")
    public ResponseEntity registerCustomer(@Valid @RequestBody RegisterRequest registerRequest) throws Exception {
        UserCredential userCredential = modelMapper.map(registerRequest.getAuth(), UserCredential.class);
        userCredential.setRole(Role.CUSTOMER);
        UserCredential resultUserCredential = authService.create(userCredential);
        System.out.println("register : " + registerRequest);

//        AuthRequest authRequest = new AuthRequest();
//        authRequest.setEmail(registerRequest.getEmail());
//        authRequest.setPassword(registerRequest.getPassword());
//        authRequest.setRole(registerRequest.getRole());
//        authRequest.setCustomer();
//        System.out.println("authreq : " + authRequest);

        Customer customer = modelMapper.map(registerRequest, Customer.class);

        System.out.println("customer : " + userCredential);

//        UserCredential resultCredential = authService.create(userCredential);
//        CustomerIdRequest customerId = new CustomerIdRequest();
//        customerId.setCustomerId(resultCustomer.getCustomerId());

        customer.setUserCredential(resultUserCredential);
//        UserCredential getmap = modelMapper.map(userCredential, UserCredential.class);
        System.out.println("userCredential : " + customer);
//        System.out.println("mapping : " + getmap);
//
        Customer resultCustomer = customerService.create(customer);

        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>("Success register " +resultUserCredential.getRole(), "with email "+resultUserCredential.getEmail()));
    }

    @PostMapping("/register/admin")
    public ResponseEntity registerAdmin(@Valid @RequestBody UserCredentialRequest userCredentialRequest) throws Exception {
        UserCredential userCredential = modelMapper.map(userCredentialRequest, UserCredential.class);
        userCredential.setRole(Role.ADMIN);
        UserCredential resultUserCredential = authService.create(userCredential);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse<>("Success register " +resultUserCredential.getRole(), "with email "+resultUserCredential.getEmail()));
    }

    @GetMapping("/logout")
    public ResponseEntity doLogout(@RequestParam String token){
        if (authService.logout(token)){
            return ResponseEntity.ok("Logout success");
        }else {
            return ResponseEntity.ok("Token not found");
        }
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequest loginRequest){
        String token  = authService.login(loginRequest);
        System.out.println("token : " + token);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Success login", token));
    }

    // Delete Data
    @DeleteMapping("/userCredential/delete/{id}")
    public ResponseEntity deleteByIdCustomer(@PathVariable("id") String id) throws Exception {
        authService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body("Data with id " + id + " has been Delete !");
    }

    // Update Data
    @PutMapping("/userCredential/update/{id}")
    public ResponseEntity updateProductInventory(@PathVariable("id") String id, @RequestBody UserCredentialRequest userCredentialRequest) throws Exception {
        UserCredential newUserCredential = modelMapper.map(userCredentialRequest, UserCredential.class);

        newUserCredential.setEmail(id);
        authService.update(newUserCredential, id);
        UserCredential result = newUserCredential;
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse<>("Data with id " + id + " has been Updated !", result));
    }

    // find by id
    @GetMapping("/userCredential/getBy/{email}")
    public ResponseEntity findByEmail(@PathVariable("id") String email) throws Exception {
        Optional<UserCredential> userCredential = authService.getByEmail(email);
        return ResponseEntity.status(HttpStatus.FOUND).body(new SuccessResponse<>("Success get user By email", userCredential));
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



}