package com.tngtech.demo.weather;

import io.swagger.jaxrs.config.BeanConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

@Component
public class WeatherServerApplication extends ResourceConfig {

    public WeatherServerApplication() {
        register(JacksonFeature.class);
        //register(LoggingFilter.class);


        // Register resources and providers using package-scanning.
        final String resourceBasePackage = "com.tngtech.demo.weather.resources";
        packages(resourceBasePackage);

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.2");
        beanConfig.setSchemes(new String[]{"http"});
        beanConfig.setHost("localhost:9090");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage(resourceBasePackage);
        beanConfig.setScan(true);

        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);
    }

}
