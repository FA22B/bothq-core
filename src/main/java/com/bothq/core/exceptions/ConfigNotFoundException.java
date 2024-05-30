package com.bothq.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ConfigNotFoundException extends NoSuchElementException {
    public ConfigNotFoundException() {
        super("Config not found");
    }

    public ConfigNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public ConfigNotFoundException(Throwable cause) {
        super(cause);
    }

    public ConfigNotFoundException(String s) {
        super(s);
    }
}
