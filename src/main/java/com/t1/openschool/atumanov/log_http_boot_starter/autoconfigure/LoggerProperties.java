package com.t1.openschool.atumanov.log_http_boot_starter.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static com.t1.openschool.atumanov.log_http_boot_starter.filter.FilterConfigParams.CONFIG_PREFIX;


@ConfigurationProperties(prefix = CONFIG_PREFIX)
public class LoggerProperties {
    /**
     * Level of logging for HttpLogger, possible values: INFO, DEBUG, TRACE
     */
    private String loggingLevel;
    /**
     * Format of logging for HttpLogger, possible values: CONSOLE, FILE
     */
    private String loggingFormat;

    /**
     * Path to the log file to write in case when FILE format is used
     */
    private String loggingFile;

    public String getLoggingLevel() {
        return loggingLevel;
    }

    public String getLoggingFormat() {
        return loggingFormat;
    }

    public void setLoggingLevel(String loggingLevel) {
        this.loggingLevel = loggingLevel;
    }

    public void setLoggingFormat(String loggingFormat) {
        this.loggingFormat = loggingFormat;
    }

    public String getLoggingFile() {
        return loggingFile;
    }

    public void setLoggingFile(String loggingFile) {
        this.loggingFile = loggingFile;
    }
}
