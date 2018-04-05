package com.tngtech.demo.weather.resources.config;

import com.tngtech.demo.weather.resources.RootResource;
import com.tngtech.demo.weather.resources.stations.StationsResource;
import com.tngtech.demo.weather.resources.weather.WeatherResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath("/api")
public class JerseyConfiguration extends ResourceConfig {

    private static final String RESOURCE_BASE_PACKAGE = "com.tngtech.demo.weather.resources";

    public JerseyConfiguration() {
        register(JacksonFeature.class);
        register(LoggingFeature.class);

        // Register resources and providers using package-scanning.
        //packages(RESOURCE_BASE_PACKAGE);
        // There still seem to be problems with nested jars and Jersey, registering resources manually by now
        register(RootResource.class);
        register(StationsResource.class);
        register(WeatherResource.class);
    }
}
