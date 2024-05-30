package com.bothq.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PluginNotFoundException extends NoSuchElementException {
    public PluginNotFoundException() {
        super("Plugin not found");
    }

    public PluginNotFoundException(String s, Throwable cause) {
        super(s, cause);
    }

    public PluginNotFoundException(Throwable cause) {
        super(cause);
    }

    public PluginNotFoundException(String s) {
        super(s);
    }
}
