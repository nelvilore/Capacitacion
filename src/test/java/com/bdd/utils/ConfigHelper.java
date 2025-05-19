package com.bdd.utils;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigValue;
import net.serenitybdd.core.environment.EnvironmentSpecificConfiguration;
import net.thucydides.core.util.EnvironmentVariables;
import net.thucydides.core.environment.SystemEnvironmentVariables;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class ConfigHelper {

    private static final EnvironmentVariables environmentVariables = SystemEnvironmentVariables.createEnvironmentVariables();

    public static String getProperty(String key) {
        return environmentVariables.getProperty(key) != null
                ? environmentVariables.getProperty(key)
                : System.getProperty(key);
    }

    public static void loadAllEnvironmentProperties() {
        Properties props = new Properties();
        System.out.println("Loading all environment properties...");
        Config fullConfig = EnvironmentSpecificConfiguration.from(environmentVariables).getConfig("environments");
        String environment = getEnvironment();
        Config envConfig;
        System.out.println("Environment: " + environment);
        System.out.println(fullConfig.getConfig(environment).toString());

        if (fullConfig.hasPath("environments") && environment != null && fullConfig.hasPath("environments." + environment)) {
            envConfig = fullConfig.getConfig("environments." + environment);
        } else {
            envConfig = fullConfig;
        }

        for (Map.Entry<String, ConfigValue> entry : envConfig.entrySet()) {
            if (entry.getKey().startsWith(environment)) {
                String key = entry.getKey().substring(environment.length() + 1);
                String value = envConfig.getString(entry.getKey());
                key = key.replaceFirst("^" + environment + "\\.", "");
                props.setProperty(key, value);
                System.setProperty(key, value);
            }
        }

    }


    public static String getEnvironmentProperty(String key) {
        return EnvironmentSpecificConfiguration.from(environmentVariables)
                .getProperty(key);
    }

    public static String getEnvironment() {
        return environmentVariables.getProperty("environment") != null
                ? environmentVariables.getProperty("environment")
                : "default";
    }
}