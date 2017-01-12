package com.tngtech.demo.weather.resources.config;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.boot.context.embedded.jetty.JettyServerCustomizer;

public class JettyConfigurer implements JettyServerCustomizer {

    @Override
    public void customize(Server server) {
        WebAppContext webAppContext = (WebAppContext) server.getHandler();
        try {
            try (Resource staticResource = Resource.newClassPathResource("/static");
                 Resource webjarsResource = Resource.newClassPathResource("/META-INF/resources/webjars/")) {
                webAppContext.setContextPath("/");
                webAppContext.setBaseResource( //
                        new ResourceCollection( //
                                staticResource, //
                                webjarsResource //
                        ));
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}