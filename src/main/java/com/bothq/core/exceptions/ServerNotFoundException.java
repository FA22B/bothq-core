package com.bothq.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ServerNotFoundException extends NoSuchElementException {
    public ServerNotFoundException() {
        super("Server not found");
    }

    public ServerNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public ServerNotFoundException(Throwable cause) {
        super(cause);
    }

    public ServerNotFoundException(String s) {
        super(s);
    }
}
