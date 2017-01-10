package com.tngtech.demo.weather;

import com.mercateo.common.rest.schemagen.link.LinkFactoryContext;
import com.mercateo.common.rest.schemagen.link.LinkFactoryContextDefault;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@Import(WeatherServerConfiguration.class)
public class WeatherServerTestConfiguration {
    @Bean
    LinkFactoryContext linkFactoryContext() throws URISyntaxException {
        return new LinkFactoryContextDefault(new URI("http://host/path"), x -> true, (x,y) -> true);
    }
}
