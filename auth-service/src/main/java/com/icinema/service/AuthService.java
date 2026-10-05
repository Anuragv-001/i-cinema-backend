package com.icinema.service;
import org.springframework.stereotype.Service;

import com.icinema.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.icinema.dto.RegisterRequest;
import com.icinema.dto.LoginRequest;
import com.icinema.dto.LoginResponse;
import com.icinema.entity.User;
import com.icinema.security.JwtService;


@Service
public class AuthService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public User register(RegisterRequest request) {
        if(userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }
        String token = jwtService.generateToken(user);
        
        return new LoginResponse(token, user.getEmail(), user.getName(), user.getRole());
    }
}
