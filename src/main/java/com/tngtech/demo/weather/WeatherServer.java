package com.tngtech.demo.weather;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@ServletComponentScan(basePackages = "com.tngtech.demo.weather")
public class WeatherServer extends SpringBootServletInitializer {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(WeatherServer.class, args);
    }
}
