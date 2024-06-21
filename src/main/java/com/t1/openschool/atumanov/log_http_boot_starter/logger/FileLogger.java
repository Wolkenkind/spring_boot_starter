package com.t1.openschool.atumanov.log_http_boot_starter.logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileLogger implements LogProcessor{

    private String logFile;
    private boolean isRewriting = false;
    private boolean initialized = false;

    public FileLogger(String filePath) {
        this(filePath, false);
    }

    public FileLogger(String filePath, boolean rewrite) {
        logFile = filePath;
        this.isRewriting = rewrite;
    }

    public boolean isRewriting() {
        return isRewriting;
    }

    public void setIsRewriting(boolean isRewriting) {
        this.isRewriting = isRewriting;
    }

    public String getLogFile() {
        return logFile;
    }

    private void init() {
        if(isRewriting) {
            try {
                Files.delete(Paths.get(logFile));
            } catch (IOException e) {
                System.out.println("Could not rewrite logfile '" + logFile + "', caught exception: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
        initialized = true;
    }

    @Override
    public void processLog(String logEntry) {
        if(!initialized){
            init();
        }
        try {
            List<StandardOpenOption> options = new ArrayList<>();
            options.add(StandardOpenOption.CREATE);
            options.add(StandardOpenOption.WRITE);
            options.add(StandardOpenOption.APPEND);
            Files.writeString(Path.of(logFile), logEntry, options.toArray(new StandardOpenOption[options.size()]));
        } catch (IOException e) {
            System.out.println("Error occurred trying to write to file '" + logFile + "': " + e.getMessage());
        }
    }
}
