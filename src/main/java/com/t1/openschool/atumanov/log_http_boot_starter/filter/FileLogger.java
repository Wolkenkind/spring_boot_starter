package com.t1.openschool.atumanov.log_http_boot_starter.filter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileLogger implements LogProcessor{

    private String logFile;

    public FileLogger(String filePath) throws IOException {
        logFile = filePath;
    }

    @Override
    public void processLog(String logEntry) {
        try {
            Files.writeString(Path.of(logFile), logEntry, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Error occurred trying to write to file '" + logFile + "': " + e.getMessage());
        }
    }
}
