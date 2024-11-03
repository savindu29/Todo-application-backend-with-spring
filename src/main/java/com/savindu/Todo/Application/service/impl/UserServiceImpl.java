package com.savindu.Todo.Application.service.impl;

import com.savindu.Todo.Application.Exception.UserAlreadyExistsException;
import com.savindu.Todo.Application.Exception.UserNotFoundException;
import com.savindu.Todo.Application.dto.request.AuthenticationRequest;
import com.savindu.Todo.Application.dto.request.RegisterRequest;
import com.savindu.Todo.Application.dto.response.AuthenticationResponse;
import com.savindu.Todo.Application.entity.AppUser;
import com.savindu.Todo.Application.repository.UserRepository;
import com.savindu.Todo.Application.service.UserService;
import com.savindu.Todo.Application.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .build();
    }

    @Override
    public HashMap<String, Object> registerUser(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            logger.error("User already exists with email: " + registerRequest.getEmail());
            throw new UserAlreadyExistsException("User already exists with email: " + registerRequest.getEmail());
        }

        AppUser user = new AppUser();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setFirstname(registerRequest.getFirstname());
        user.setLastname(registerRequest.getLastname());
        user.setCreatedAt(LocalDateTime.now());

        AppUser savedUser = userRepository.save(user);
        logger.info("User registered successfully with ID: " + savedUser.getId());
        String token = jwtUtil.createJwtToken(savedUser);

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("token", token);
        response.put("message", "User registered successfully");
        response.put("user", new AuthenticationResponse(savedUser.getId(), savedUser.getFirstname(), savedUser.getLastname(), savedUser.getEmail()));
        return response;
    }

    @Override
    public HashMap<String, Object> login(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );


        AppUser appUser = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        String jwtToken = jwtUtil.createJwtToken(appUser);

        HashMap<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("token", jwtToken);
        response.put("message", "Login successful");
        response.put("user", new AuthenticationResponse(appUser.getId(), appUser.getFirstname(), appUser.getLastname(), appUser.getEmail()));
        logger.info("User logged in successfully with ID: " + appUser.getId());
        return response;


    }
}
