package com.tngtech.demo.weather;

import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class WeatherServerApplication extends ResourceConfig {

    public WeatherServerApplication() {
        register(JacksonFeature.class);
        //register(LoggingFilter.class);

        // Register resources and providers using package-scanning.
        packages("com.tngtech.demo.weather.resources");
    }

}
