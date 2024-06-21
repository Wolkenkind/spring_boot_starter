package com.t1.openschool.atumanov.log_http_boot_starter.filter.utility;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LoggingResponseWrapper extends HttpServletResponseWrapper {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    public LoggingResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return new ServletOutputStream() {
            @Override
            public boolean isReady() {
                try {
                    return getResponse().getOutputStream().isReady();
                } catch (IOException e) {
                    throw new RuntimeException("Exception when getting original output stream from HttpServletResponseWrapper: " + e.getMessage());
                }
            }

            @Override
            public void setWriteListener(WriteListener listener) {
                try {
                    getResponse().getOutputStream().setWriteListener(listener);
                } catch (IOException e) {
                    throw new RuntimeException("Exception when getting original output stream from HttpServletResponseWrapper: " + e.getMessage());
                }
            }

            @Override
            public void write(int b) throws IOException {
                baos.write(b);
                getResponse().getOutputStream().write(b);
            }
        };
    }

    public String getContent() {
        return new String(baos.toByteArray());
    }
}
