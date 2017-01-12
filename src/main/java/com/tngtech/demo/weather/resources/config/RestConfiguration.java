package com.tngtech.demo.weather.resources.config;

import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory;
import com.mercateo.common.rest.schemagen.plugin.FieldCheckerForSchema;
import com.mercateo.common.rest.schemagen.plugin.MethodCheckerForLink;
import com.mercateo.rest.schemagen.spring.JerseyHateoasConfiguration;
import com.tngtech.demo.weather.resources.stations.StationResource;
import com.tngtech.demo.weather.resources.stations.StationsResource;
import com.tngtech.demo.weather.resources.weather.WeatherResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.inject.Named;

@Configuration
@Slf4j
@Import(JerseyHateoasConfiguration.class)
public class RestConfiguration {

    @Bean
    public FieldCheckerForSchema fieldCheckerForSchema() {
        return (field, callContext) -> true;
    }

    @Bean
    public MethodCheckerForLink methodCheckerForLink() {
        return scope -> true;
    }

    @Bean
    @Named("stationsLinkFactory")
    LinkFactory<StationsResource> stationsResourceLinkFactory(LinkMetaFactory linkMetaFactory) {
        return linkMetaFactory.createFactoryFor(StationsResource.class);
    }

    @Bean
    @Named("stationLinkFactory")
    LinkFactory<StationResource> stationResourceLinkFactory(
            LinkFactory<StationsResource> stationsResourceLinkFactory) {
        return stationsResourceLinkFactory.subResource(StationsResource::stationSubResource,
                StationResource.class);
    }

    @Bean
    @Named("weatherLinkFactory")
    LinkFactory<WeatherResource> weatherResourceLinkFactory(LinkMetaFactory linkMetaFactory) {
        return linkMetaFactory.createFactoryFor(WeatherResource.class);
    }

    @Bean
    EmbeddedServletContainerFactory containerFactory() {
        final JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory = new JettyEmbeddedServletContainerFactory();
        jettyEmbeddedServletContainerFactory.addServerCustomizers(new JettyConfigurer());
        return jettyEmbeddedServletContainerFactory;
    }
}
