package com.t1.openschool.atumanov.log_http_boot_starter.autoconfigure;

import com.t1.openschool.atumanov.log_http_boot_starter.logger.LogProcessor;
import com.t1.openschool.atumanov.log_http_boot_starter.filter.LoggingFilter;
import com.t1.openschool.atumanov.log_http_boot_starter.interceptor.LoggingInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

import static com.t1.openschool.atumanov.log_http_boot_starter.filter.utility.FilterConfigParams.*;

@AutoConfiguration
public class FilterWebMvcAutoConfigurer implements WebMvcConfigurer {

    @Value("${" + CONFIG_PREFIX + "." + LOGGING_LEVEL + ":" + LEVEL_INFO + "}")
    private String logLevel;

    @Value("${" + CONFIG_PREFIX + "." + LOGGING_FORMAT + ":" + FORMAT_CONSOLE + "}")
    private String logFormat;

    @Autowired
    private LogProcessor processor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        Properties config = new Properties();
        config.setProperty(LOGGING_LEVEL, logLevel);
        config.setProperty(LOGGING_FORMAT, logFormat);
        registry.addInterceptor(new LoggingInterceptor(config, processor));
    }

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilterFilterRegistrationBean() {
        FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
        Properties config = new Properties();
        config.setProperty(LOGGING_LEVEL, logLevel);
        config.setProperty(LOGGING_FORMAT, logFormat);
        registrationBean.setFilter(new LoggingFilter(config, processor));
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(OrderedFilter.HIGHEST_PRECEDENCE + 1);
        return registrationBean;
    }
}
