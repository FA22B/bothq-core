package com.bothq.core.bothqcore.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class CustomErrorConfiguration {
    public ResponseEntity<String> handleException() {
        return new ResponseEntity<>("Test", HttpStatus.UNAUTHORIZED);
    }
}
