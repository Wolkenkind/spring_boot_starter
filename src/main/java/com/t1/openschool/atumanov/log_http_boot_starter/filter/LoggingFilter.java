package com.t1.openschool.atumanov.log_http_boot_starter.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static com.t1.openschool.atumanov.log_http_boot_starter.filter.FilterConfigParams.*;


public class LoggingFilter extends OncePerRequestFilter {

    private Properties config;

    private LogProcessor processor;

    public LoggingFilter(Properties configurationProperties, LogProcessor processor) {
        this.config = configurationProperties;
        this.processor = processor;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //need to avoid errors related to read of request body
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest(request);
        String logLevel = config.getProperty(LOGGING_LEVEL);
        LogRequest(cachedRequest, logLevel);
        //need to avoid errors related to read of response body
        LoggingResponseWrapper wrappedResponse = new LoggingResponseWrapper(response);

        long start = System.nanoTime();
        filterChain.doFilter(cachedRequest, wrappedResponse);
        long end = System.nanoTime();

        LogResponse(wrappedResponse, logLevel);
        processor.processLog("Request processing time: " + (end - start) / 1000_000 + "ms\n");
    }

    private void LogRequest(CachedBodyHttpServletRequest request, String logLevel) throws IOException {
        String requestLogEntry;

        requestLogEntry = "Incoming request to '" + request.getRequestURI() + "', request method: '" + request.getMethod() + "'";

        if(logLevel.equals(LEVEL_DEBUG) || logLevel.equals(LEVEL_TRACE)) {
            requestLogEntry += "\nIP address: " + request.getRemoteAddr();

            String body = request.getReader().lines().reduce((s1, s2) -> s1 + s2).orElse("");
            if(!body.equals("")) {
                requestLogEntry += "\nRequest body: '" + body + "';";
            } else {
                requestLogEntry += "\nNo request body;";
            }
        }
        if(logLevel.equals(LEVEL_TRACE)) {
            requestLogEntry += "\nAuthentication scheme: '" + request.getAuthType() + "'; remote user: '" + request.getRemoteUser() + "' remote port: " + request.getRemotePort() + ";";

            Principal principal = request.getUserPrincipal();
            if(principal != null) {
                requestLogEntry += "\nPrincipal name: '" + request.getUserPrincipal().getName() + "'; Principal class: '" + request.getUserPrincipal().getClass();
            } else {
                requestLogEntry += "\nPrincipal not provided;";
            }

            HttpSession session = request.getSession(false);
            if(session != null) {
                try {
                    requestLogEntry += "\nCurrent session: id = '" + session.getId() + "';";
                    requestLogEntry += " created: " + LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getCreationTime()), ZoneOffset.UTC) + ";";
                    requestLogEntry += " accessed: " + LocalDateTime.ofInstant(Instant.ofEpochMilli(session.getLastAccessedTime()), ZoneOffset.UTC) + ";";
                    requestLogEntry += " max inactive interval: " + session.getMaxInactiveInterval() + ";";
                    List<String> attributes = Collections.list(session.getAttributeNames());
                    for(String attrName: attributes) {
                        requestLogEntry += "\nAttribute: '" + attrName + "' value: '" + session.getAttribute(attrName) + ";";
                    }
                } catch (IllegalStateException e) {
                    requestLogEntry += "\nCurrent session: invalidated, cannot get data";
                }
            } else {
                requestLogEntry += "\nNo current session";
            }

            Map<String, String[]> queryParameters = request.getParameterMap();
            if(!queryParameters.isEmpty()) {
                for(String key: queryParameters.keySet()) {
                    requestLogEntry += "\nQuery parameter: '" + key + "' value: '" + Arrays.stream(queryParameters.get(key)).reduce((s1, s2) -> s1  + " " + s2).orElse("") + "';";
                }
            }

            //List<String> headerNames = Collections.list(request.getHeaderNames());
            Set<String> headerSet = new HashSet<>(Collections.list(request.getHeaderNames()));
            for(String headerName : headerSet) {
                String hValue = Collections.list(request.getHeaders(headerName)).stream().reduce((s1, s2) -> s1  + ", " + s2).orElse("");
                requestLogEntry += "\nHeader '" + headerName + "' value: '" + hValue + "';";
            }
        }

        processor.processLog(requestLogEntry + "\n");
    }

    private void LogResponse(LoggingResponseWrapper wrappedResponse, String logLevel) {
        String responseLogEntry = "Outgoing response with status code '" + wrappedResponse.getStatus() + "'";

        if(logLevel.equals(LEVEL_DEBUG) || logLevel.equals(LEVEL_TRACE)) {
            responseLogEntry += "\nResponse body: '" + wrappedResponse.getContent() + "';";
        }

        if(logLevel.equals(LEVEL_TRACE)) {
            Set<String> headerSet = new HashSet<>(wrappedResponse.getHeaderNames());
            for(String headerName : headerSet) {
                String hValue = new ArrayList<>(wrappedResponse.getHeaders(headerName)).stream().reduce((s1, s2) -> s1  + ", " + s2).orElse("");
                responseLogEntry += "\nHeader '" + headerName + "' value: '" + hValue + "';";
            }
        }

        processor.processLog(responseLogEntry + "\n");
    }
}
