package com.icinema.controller;

import com.icinema.service.AuthService;
import com.icinema.service.PasswordResetService;
import com.icinema.dto.RegisterRequest;
import com.icinema.dto.ResetPasswordRequest;
import com.icinema.dto.VerifyOtpRequest;
import com.icinema.dto.ForgotPasswordRequest;
import com.icinema.dto.LoginRequest;
import com.icinema.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import com.icinema.dto.LoginResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;


@RestController
@RequestMapping("/api/auth")
// @CrossOrigin("http://localhost:5173")
public class AuthController {
    private final AuthService authService;

    private final PasswordResetService passwordResetService;

    public AuthController(AuthService authService, PasswordResetService passwordResetService) {
        this.authService = authService;
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request){
        try{
            User user = authService.register(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request){
        try{
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request
    ) {
        try {
            passwordResetService.generateOtp(request.getEmail());

            return ResponseEntity.ok(
                    "OTP sent successfully to your email."
            );

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request
    ) {
        try {
            passwordResetService.verifyOtp(
                    request.getEmail(),
                    request.getOtp()
            );

            return ResponseEntity.ok("OTP verified successfully");

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        try {
            passwordResetService.resetPassword(
                    request.getEmail(),
                    request.getNewPassword()
            );

            return ResponseEntity.ok("Password reset successfully");

        } catch (RuntimeException e) {
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile(Authentication authentication) {
        return ResponseEntity.ok("Authenticated user: " +authentication.getName());
    }
    
}
