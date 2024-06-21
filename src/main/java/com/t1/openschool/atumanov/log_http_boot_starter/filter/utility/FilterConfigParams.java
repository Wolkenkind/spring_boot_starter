package com.t1.openschool.atumanov.log_http_boot_starter.filter.utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FilterConfigParams {
    public static final String CONFIG_PREFIX = "log.http.starter";
    public static final String LOGGING_LEVEL = "loggingLevel";
    public static final String LOGGING_FORMAT = "loggingFormat";
    public static final String LOGGING_FILE = "loggingFile";
    public static final String LOGGING_REWRITE = "rewriteLog";

    public static final String LEVEL_INFO = "INFO";
    public static final String LEVEL_DEBUG = "DEBUG";
    public static final String LEVEL_TRACE = "TRACE";
    public static final String FORMAT_CONSOLE = "CONSOLE";
    public static final String FORMAT_FILE = "FILE";

    public static final List<String> LEVELS_LIST = new ArrayList<>(Arrays.asList(new String[]{LEVEL_INFO, LEVEL_DEBUG, LEVEL_TRACE}));
    public static final List<String> FORMATS_LIST = new ArrayList<>(Arrays.asList(new String[]{FORMAT_CONSOLE, FORMAT_FILE}));
}
