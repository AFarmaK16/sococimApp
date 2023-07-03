package com.example.demo.services;

import java.util.Random;

public class OtpService {
    public String generateOTP() {
        int otpLength = 6; // Set the desired length of the OTP
        Random random = new Random();
        StringBuilder otpBuilder = new StringBuilder();

        for (int i = 0; i < otpLength; i++) {
            int digit = random.nextInt(10);
            otpBuilder.append(digit);
        }

        String otp = otpBuilder.toString();
        // Send the OTP to the user through the chosen delivery method
        // (e.g., SMS, email, or push notification)
        sendOTPToUser(otp);

        return otp;
    }

    private void sendOTPToUser(String otp) {
        // Code to send OTP to the user (e.g., via SMS, email, or push notification)
    }
}
