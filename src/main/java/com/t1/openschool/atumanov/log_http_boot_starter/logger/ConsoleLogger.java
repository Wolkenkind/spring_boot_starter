package com.t1.openschool.atumanov.log_http_boot_starter.logger;

public class ConsoleLogger implements LogProcessor{

    @Override
    public void processLog(String logEntry) {
        System.out.println(logEntry);
    }
}
