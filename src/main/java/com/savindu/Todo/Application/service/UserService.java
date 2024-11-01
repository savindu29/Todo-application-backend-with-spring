package com.savindu.Todo.Application.service;

import com.savindu.Todo.Application.dto.request.RegisterRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.HashMap;

public interface UserService  extends UserDetailsService {
    UserDetails loadUserByUsername(String email);
    HashMap<String, Object> registerUser(RegisterRequest registerRequest);

}
