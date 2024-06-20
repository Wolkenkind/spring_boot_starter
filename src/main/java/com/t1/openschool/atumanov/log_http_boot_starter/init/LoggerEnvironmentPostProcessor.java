package com.t1.openschool.atumanov.log_http_boot_starter.init;

import com.t1.openschool.atumanov.log_http_boot_starter.exception.LogFileNotProvidedException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

import static com.t1.openschool.atumanov.log_http_boot_starter.filter.FilterConfigParams.*;

public class LoggerEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String formatPropertyValue = environment.getProperty(CONFIG_PREFIX + "." + LOGGING_FORMAT);
        if(formatPropertyValue != null && formatPropertyValue.equals(FORMAT_FILE)) {
            if(environment.getProperty(CONFIG_PREFIX + "." + LOGGING_FILE) == null) {
                System.out.println("Please use configuration property " + CONFIG_PREFIX + "." + LOGGING_FILE + " to provide a full path and file name to write the HTTP log in!");
                throw new LogFileNotProvidedException("Configuration property '" + CONFIG_PREFIX + "." + LOGGING_FILE + "' not set!");
            }
        }
    }
}
