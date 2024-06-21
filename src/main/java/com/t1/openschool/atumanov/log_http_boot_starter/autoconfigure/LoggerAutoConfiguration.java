package com.t1.openschool.atumanov.log_http_boot_starter.autoconfigure;

import com.t1.openschool.atumanov.log_http_boot_starter.filter.ConsoleLogger;
import com.t1.openschool.atumanov.log_http_boot_starter.filter.FileLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.util.List;

import static com.t1.openschool.atumanov.log_http_boot_starter.filter.FilterConfigParams.*;

@AutoConfiguration
@EnableConfigurationProperties(LoggerProperties.class)
public class LoggerAutoConfiguration {

    @Autowired
    private LoggerProperties properties;

    @Bean
    @ConditionalOnProperty(prefix = CONFIG_PREFIX, name = LOGGING_FORMAT, havingValue = FORMAT_CONSOLE, matchIfMissing = true)
    public ConsoleLogger consoleWriter() {
        return new ConsoleLogger();
    }

    @Bean
    @ConditionalOnProperty(prefix = CONFIG_PREFIX, name = LOGGING_FORMAT, havingValue = FORMAT_FILE, matchIfMissing = false)
    public FileLogger fileWriter() {
        //try {
            return new FileLogger(properties.getLoggingFile(), properties.getRewriteLog());
//        } catch (IOException e) {
//            System.out.println("Error trying to create FileLogger: " + e.getMessage());
//            throw new RuntimeException("Cannot create FileLogger");
//        }
    }

}
