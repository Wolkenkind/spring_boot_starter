package com.t1.openschool.atumanov.log_http_boot_starter.init;

import com.t1.openschool.atumanov.log_http_boot_starter.exception.WrongConfigurationException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.Properties;

import static com.t1.openschool.atumanov.log_http_boot_starter.filter.utility.FilterConfigParams.*;

public class LoggerEnvironmentPostProcessor implements EnvironmentPostProcessor {

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        //check properties for value validity
        String levelPropertyValue = environment.getProperty(CONFIG_PREFIX + "." + LOGGING_LEVEL);
        if(levelPropertyValue != null) {
            levelPropertyValue = levelPropertyValue.toUpperCase();
            if(!LEVELS_LIST.contains(levelPropertyValue)) {
                System.out.print("'" + CONFIG_PREFIX + "." + LOGGING_LEVEL + "' property value must be one of the following: ");
                LEVELS_LIST.forEach((item) -> System.out.print(item + " "));
                System.out.println();
                throw new WrongConfigurationException("Wrong value set for configuration property '" + CONFIG_PREFIX + "." + LOGGING_LEVEL + "'!");
            }
        }
        String formatPropertyValue = environment.getProperty(CONFIG_PREFIX + "." + LOGGING_FORMAT);
        if(formatPropertyValue != null) {
            formatPropertyValue = formatPropertyValue.toUpperCase();
            if(!FORMATS_LIST.contains(formatPropertyValue)) {
                System.out.print("'" + CONFIG_PREFIX + "." + LOGGING_FORMAT + "' property value must be one of the following: ");
                FORMATS_LIST.forEach((item) -> System.out.print(item + " "));
                System.out.println();
                throw new WrongConfigurationException("Wrong value set for configuration property '" + CONFIG_PREFIX + "." + LOGGING_FORMAT + "'!");
            }
            if(formatPropertyValue.equals(FORMAT_FILE) && environment.getProperty(CONFIG_PREFIX + "." + LOGGING_FILE) == null) {
                System.out.println("Please use configuration property " + CONFIG_PREFIX + "." + LOGGING_FILE + " to provide a full path and file name to write the HTTP log in!");
                throw new WrongConfigurationException("Configuration property '" + CONFIG_PREFIX + "." + LOGGING_FILE + "' not set!");
            }
        }
        //update properties with uppercase
        Properties props = new Properties();
        props.setProperty(CONFIG_PREFIX + "." + LOGGING_LEVEL, levelPropertyValue);
        props.setProperty(CONFIG_PREFIX + "." + LOGGING_FORMAT, formatPropertyValue);
        environment.getPropertySources().addFirst(new PropertiesPropertySource("uppercasedProps", props));
    }
}
