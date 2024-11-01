package com.savindu.Todo.Application.service.impl;

import com.savindu.Todo.Application.dto.request.AuthenticationRequest;
import com.savindu.Todo.Application.dto.request.RegisterRequest;
import com.savindu.Todo.Application.entity.AppUser;
import com.savindu.Todo.Application.repository.UserRepository;
import com.savindu.Todo.Application.service.JwtService;
import com.savindu.Todo.Application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        AppUser user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())

                .build();
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

    @Override
    public HashMap<String, Object> login(AuthenticationRequest request) {
        HashMap<String, Object> response = new HashMap<>();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            Optional<AppUser> userOptional = userRepository.findByEmail(request.getEmail());
            if (userOptional.isPresent()) {
                AppUser appUser = userOptional.get();
                String jwtToken = jwtService.createJwtToken(appUser);
                response.put("status", "success");
                response.put("token", jwtToken);
                response.put("message", "Login successful");
            } else {
                response.put("status", "error");
                response.put("message", "User not found with email: " + request.getEmail());
            }
        } catch (BadCredentialsException e) {
            response.put("status", "error");
            response.put("message", "Invalid credentials. Please check your email and password.");
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            System.err.println("Unexpected Error: " + e.getMessage());
        }

        return response;
    }
}
