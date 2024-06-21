package com.t1.openschool.atumanov.log_http_boot_starter.exception;

public class WrongConfigurationException extends RuntimeException {

    public WrongConfigurationException() {

    }

    public WrongConfigurationException(String message) {
        super(message);
    }
}
