package com.t1.openschool.atumanov.log_http_boot_starter.filter;

public class ConsoleLogger implements LogProcessor{

    @Override
    public void processLog(String logEntry) {
        System.out.println(logEntry);
    }
}
