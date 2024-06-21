package com.t1.openschool.atumanov.log_http_boot_starter.autoconfigure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import static com.t1.openschool.atumanov.log_http_boot_starter.filter.FilterConfigParams.CONFIG_PREFIX;


@ConfigurationProperties(prefix = CONFIG_PREFIX)
public class LoggerProperties {
    /**
     * Level of logging for HttpLogger, possible values: INFO, DEBUG, TRACE. Default (when missing): INFO
     */
    private String loggingLevel;
    /**
     * Format of logging for HttpLogger, possible values: CONSOLE, FILE. Default (when missing): CONSOLE
     */
    private String loggingFormat;

    /**
     * Path to the log file to write in case when FILE format is used
     */
    private String loggingFile;

    /**
     * Rewrites the logfile if set to TRUE, appends to it if set to FALSE. Default (when missing): FALSE
     */
    private boolean rewriteLog = false;

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

    public boolean getRewriteLog() {
        return rewriteLog;
    }

    public void setRewriteLog(boolean rewriteLog) {
        this.rewriteLog = rewriteLog;
    }
}
