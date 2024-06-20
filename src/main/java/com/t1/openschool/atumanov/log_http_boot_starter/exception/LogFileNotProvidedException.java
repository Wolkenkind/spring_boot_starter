package com.t1.openschool.atumanov.log_http_boot_starter.exception;

public class LogFileNotProvidedException extends RuntimeException {

    public LogFileNotProvidedException() {

    }

    public LogFileNotProvidedException(String message) {
        super(message);
    }
}
