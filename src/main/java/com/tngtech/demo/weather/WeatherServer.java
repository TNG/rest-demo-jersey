package com.tngtech.demo.weather;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.tngtech.demo.weather")
public class WeatherServer extends SpringBootServletInitializer {

    @Value("${server.port}")
    private String serverPort;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WeatherServer.class, args);
    }
}
