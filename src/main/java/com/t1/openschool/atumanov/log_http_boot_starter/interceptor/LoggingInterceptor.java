package com.t1.openschool.atumanov.log_http_boot_starter.interceptor;

import com.t1.openschool.atumanov.log_http_boot_starter.logger.LogProcessor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static com.t1.openschool.atumanov.log_http_boot_starter.filter.utility.FilterConfigParams.LEVEL_TRACE;
import static com.t1.openschool.atumanov.log_http_boot_starter.filter.utility.FilterConfigParams.LOGGING_LEVEL;

@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private static String ERROR_STRING = "error";

    private Properties config;

    private LogProcessor processor;

    public LoggingInterceptor(Properties config, LogProcessor processor) {
        this.config = config;
        this.processor = processor;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String logLevel = config.getProperty(LOGGING_LEVEL);

        String logEntry = "Outgoing response with status code '" + response.getStatus() + "'";

        if(logLevel.equals(LEVEL_TRACE)) {
            Set<String> headerSet = new HashSet<>(response.getHeaderNames());
            for(String headerName : headerSet) {
                String hValue = new ArrayList<>(response.getHeaders(headerName)).stream().reduce((s1, s2) -> s1  + ", " + s2).orElse("");
                logEntry += "\nHeader '" + headerName + "' value: '" + hValue + "';";
            }
        }

        processor.processLog(logEntry + "\n");
    }
}
