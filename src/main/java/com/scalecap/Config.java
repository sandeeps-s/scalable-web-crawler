package com.scalecap;

import java.io.IOException;
import java.util.Properties;

class Config {

    private static final String PROPERTIES_FILE_NAME = "web-crawler.properties";

    private final Properties properties;

    Config() {
        try {
            properties = new Properties();
            properties.load(getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME));
        } catch (IOException e) {
            throw new IllegalStateException("Error while reading:" + PROPERTIES_FILE_NAME);
        }
    }

    String searchURITemplate() {
        return properties.getProperty("search.url.template");
    }


}
