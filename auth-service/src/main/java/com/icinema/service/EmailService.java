package com.icinema.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otp){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("I-Cinema Password Reset OTP");
        message.setText(
            "Hello,\n\n" +
            "Your I-Cinema password reset OTP is: "+ otp + "\n\n" 
            + "This OTP is Valid for 5 Minutes.\n\n"+
            "If you did not request a password reset, please ignore this email.\n\n" +
            "Regards,\n"+
            "I-Cinema Team"
        );
        mailSender.send(message);
    }
}
