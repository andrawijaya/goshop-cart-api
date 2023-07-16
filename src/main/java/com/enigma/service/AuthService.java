package com.enigma.service;

import com.enigma.entity.Customer;
import com.enigma.entity.UserCredential;
import com.enigma.entity.request.auth.LoginRequest;
import com.enigma.exception.EntityExistException;
import com.enigma.exception.NotFoundException;
import com.enigma.exception.UnauthorizedException;
import com.enigma.repository.IAuthRepository;
import com.enigma.repository.ICustomerRepository;
import com.enigma.repository.IUserCredentialRepository;
import com.enigma.util.JwtUtil;
import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService implements IAuthService{

    private final List<String > tokenStorage = new ArrayList<>();

    private IAuthRepository authRepository;

    private ICustomerRepository customerRepository;

    private PasswordEncoder passwordEncoder;

    private JwtUtil jwtUtil;

    private ModelMapper modelMapper;


    public AuthService(IAuthRepository authRepository, ICustomerRepository customerRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, ModelMapper modelMapper){
        this.authRepository = authRepository;
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }


    List<UserCredential> findByNameContains(String value) {
        List<UserCredential> customers =  authRepository.findByNameContains(value);
        if (customers.isEmpty()) {
            throw new NotFoundException("Auth with " + value + " name is not found");
        }

        return customers;
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
    public Page<UserCredential> list(Integer page, Integer size, String direction, String sortBy) {
        Sort sort = Sort.by(Sort.Direction.valueOf(direction), sortBy);
        Pageable pageable = PageRequest.of((page - 1), size, sort);
        Page<UserCredential> result = authRepository.findAll(pageable);
        return result;
    }

    @Override
    public List<UserCredential> getAll() throws Exception {
        List<UserCredential> userCredentialList = authRepository.findAll();
        return userCredentialList;
    }

    @Override
    public UserCredential create(UserCredential data) throws Exception {

        try {
            data.setPassword(passwordEncoder.encode(data.getPassword()));
            UserCredential newUserCredential = authRepository.save(data);
            return newUserCredential;
        } catch (DataIntegrityViolationException e) {
            throw new EntityExistException("Data is exist");
        }
    }

    @Override
    public String login(LoginRequest data)  {
//        try {
            Optional<UserCredential> user = authRepository.findById(data.getEmail());
            boolean emailCheck = data.getEmail().equals(user.get().getEmail());
            System.out.println("email check : " + emailCheck);
            boolean passwordCheck = passwordEncoder.matches(data.getPassword(), user.get().getPassword());
            if (emailCheck && passwordCheck) {
                String token = jwtUtil.generateToken(data.getEmail());
                return token;
            } else {
                throw new UnauthorizedException("Email or Password incorrect");
            }
//        }catch (Exception e){
//            throw new RuntimeException(e.getMessage());
//        }
    }

    @Override
    public boolean logout(String token) {
        return tokenStorage.remove(token);
    }

    @Override
    public Optional<UserCredential> getByEmail(String email) throws Exception {
        Optional<UserCredential> userCredential = authRepository.findById(email);
        if(userCredential.isEmpty()){
            throw new NotFoundException("userCredential id is not found");
        }
        return userCredential;
    }

    @Override
    public List<UserCredential> getByFilter(String keyword, String value) throws Exception {
        switch (keyword) {
            case "name":
                return findByNameContains(value);
            default:
                return authRepository.findAll();
        }
    }

    @Override
    public void update(UserCredential data, String email) throws Exception {
        try {
            UserCredential userCredential= getByEmail(email).get();

            modelMapper.map(data, userCredential);
            authRepository.save(userCredential);
        } catch (Exception e) {
            throw new NotFoundException("Update Failed Because Id Not Found");
        }
    }

    @Override
    public void delete(String email) throws Exception {
        try{
            UserCredential userCredentialExisting = getByEmail(email).get();
            authRepository.delete(userCredentialExisting);
        }catch (Exception e){
            throw new NotFoundException("Delete Failed Because Id Not Found");
        }
    }

}
