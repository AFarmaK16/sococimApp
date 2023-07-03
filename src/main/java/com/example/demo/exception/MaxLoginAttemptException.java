package com.example.demo.exception;

import java.util.concurrent.ExecutionException;

public class MaxLoginAttemptException extends ExecutionException {
        public MaxLoginAttemptException(String message) {
            super(message);
        }
    }

