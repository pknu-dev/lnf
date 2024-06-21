package org.pknudev.common;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static final String filename = "application.properties";
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(filename)) {
            if (input == null) {
                throw new RuntimeException("unable to load " + filename);
            }
            properties.load(input);
        } catch (Exception e) {
            throw new RuntimeException("failed to load config file", e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
