package com.icinema.service;

import org.springframework.stereotype.Service;

import com.icinema.repository.UserRepository;
import com.icinema.entity.PasswordResetToken;
import com.icinema.repository.PasswordResetTokenRepository;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import com.icinema.entity.User;



@Service 
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResetService(UserRepository userRepository, 
        PasswordResetTokenRepository tokenRepository, 
        PasswordEncoder passwordEncoder,
         EmailService emailService){
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public void generateOtp(String email){
        if(!userRepository.existsByEmail(email)){
            throw new RuntimeException("Email not registered");
        }
        String otp = String.format("%06d", new Random().nextInt(1000000));
        PasswordResetToken token = new PasswordResetToken();
        token.setEmail(email);
        token.setOtp(otp);
        token.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        token.setVerified(false);

        tokenRepository.save(token);
        emailService.sendOtpEmail(email, otp);;
    }

    public void verifyOtp(String email,String otp){
        PasswordResetToken token = tokenRepository.findTopByEmailOrderByIdDesc(email)
                                            .orElseThrow(() -> new RuntimeException("OTP not found"));
        
        if(LocalDateTime.now().isAfter(token.getExpiryTime())){
            throw new RuntimeException("OTP Expired");
        }
        if(!token.getOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }
        token.setVerified(true);
        tokenRepository.save(token);
    }

    public void resetPassword(String email, String newPassword){
        PasswordResetToken token = tokenRepository.findTopByEmailOrderByIdDesc(email)
                                            .orElseThrow(() -> new RuntimeException("OTP Verification Required"));
        if(!token.isVerified()){
            throw new RuntimeException("OTP Verification Required");
        }
        if(LocalDateTime.now().isAfter(token.getExpiryTime())){
            throw new RuntimeException("OTP Expired");
        }

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        tokenRepository.delete(token);

    }
}
