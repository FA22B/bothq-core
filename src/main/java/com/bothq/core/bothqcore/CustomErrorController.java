package com.bothq.core.bothqcore;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomErrorController implements ErrorController {
    @RequestMapping("/error")
    public ResponseEntity<String> handleError() {
        return ResponseEntity.ok().build();

//        return ResponseEntity
//                .status(HttpStatus.NOT_FOUND)
//                .build();
    }


}
