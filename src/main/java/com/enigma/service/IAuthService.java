package com.enigma.service;

import com.enigma.entity.UserCredential;
import com.enigma.entity.request.auth.LoginRequest;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface IAuthService {
    // create
    UserCredential create(UserCredential data) throws Exception;

    // Get All with pagination
    Page<UserCredential> list(Integer page, Integer size, String direction, String sortBy) throws Exception;

    List<UserCredential> getAll() throws Exception;
    // getBy id
    Optional<UserCredential> getByEmail(String id) throws Exception;

    // getby key and value
    List<UserCredential> getByFilter(String keyword, String value) throws Exception;

    String login(LoginRequest data);

    boolean logout(String token);

    // update
    void update(UserCredential data, String id) throws Exception;

    // delete
    void delete(String id) throws Exception;
}
