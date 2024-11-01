package com.savindu.Todo.Application.service.impl;

import com.savindu.Todo.Application.dto.request.RegisterRequest;
import com.savindu.Todo.Application.entity.AppUser;
import com.savindu.Todo.Application.repository.UserRepository;
import com.savindu.Todo.Application.service.JwtService;
import com.savindu.Todo.Application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @Override
    public UserDetails loadUserByUsername(String email) {
        AppUser user = userRepository.findByEmail(email).orElseThrow();
        if(user == null){
            throw new RuntimeException("User not found");
        }
        UserDetails build = User.withUsername(user.getEmail()).password(user.getPassword()).build();
        return build;



    }

    @Override
    public HashMap<String, Object> registerUser(RegisterRequest registerRequest) {
        HashMap<String, Object> response = new HashMap<>();

        try {

            if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                response.put("status", "error");
                response.put("message", "User already exists with email: " + registerRequest.getEmail());
                return response;
            }


            AppUser user = new AppUser();
            user.setEmail(registerRequest.getEmail());
            user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
            user.setFirstname(registerRequest.getFirstname());
            user.setLastname(registerRequest.getLastname());

            AppUser savedUser = userRepository.save(user);


            String token = jwtService.createJwtToken(savedUser);


            response.put("status", "success");
            response.put("token", token);
            response.put("message", "User registered successfully");
        } catch (DataAccessException dae) {

            response.put("status", "error");
            response.put("message", "Database error occurred while registering user. Please try again later.");

            System.err.println("Database Error: " + dae.getMessage());
        } catch (Exception e) {

            response.put("status", "error");
            response.put("message", "An unexpected error occurred. Please contact support.");

            System.err.println("Unexpected Error: " + e.getMessage());
        }

        return response;
    }
}
