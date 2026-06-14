package com.crowdcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class CrowdCareApplication {

    private static ConfigurableApplicationContext springContext;

    public static void startSpring(String[] args) {
        springContext = SpringApplication.run(CrowdCareApplication.class, args);
    }

    public static void stopSpring() {
        if (springContext != null) {
            springContext.close();
        }
    }

    public static ConfigurableApplicationContext getContext() {
        return springContext;
    }
}