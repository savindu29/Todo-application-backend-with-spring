package com.savindu.Todo.Application.service;

import com.savindu.Todo.Application.dto.request.AuthenticationRequest;
import com.savindu.Todo.Application.dto.request.RegisterRequest;
import com.savindu.Todo.Application.dto.response.AppResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;

public interface UserService  extends UserDetailsService {
    UserDetails loadUserByUsername(String email);
    AppResponse<Object> registerUser(RegisterRequest registerRequest);
    AppResponse<Object> login(AuthenticationRequest request);

}
