package com.tngtech.demo.weather;

import com.mercateo.common.rest.schemagen.JsonHyperSchemaCreator;
import com.mercateo.common.rest.schemagen.JsonSchemaGenerator;
import com.mercateo.common.rest.schemagen.RestJsonSchemaGenerator;
import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory;
import com.mercateo.common.rest.schemagen.plugin.FieldCheckerForSchema;
import com.mercateo.common.rest.schemagen.plugin.MethodCheckerForLink;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchemaCreator;
import com.mercateo.common.rest.schemagen.types.PaginatedResponseBuilderCreator;
import com.tngtech.demo.weather.resources.stations.StationResource;
import com.tngtech.demo.weather.resources.stations.StationsResource;
import com.tngtech.demo.weather.resources.weather.WeatherResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.inject.Named;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ServiceLoader;

@Configuration
public class WeatherServerConfiguration {

    @Bean
    public JsonSchemaGenerator jsonSchemaGenerator() {
        return new RestJsonSchemaGenerator();
    }

    @Bean
    public LinkMetaFactory linkMetaFactory(JsonSchemaGenerator jsonSchemaGenerator,
                                           FieldCheckerForSchema fieldCheckerForSchema,
                                           MethodCheckerForLink methodCheckerForLink)
            throws URISyntaxException {
        return LinkMetaFactory.create(jsonSchemaGenerator, new URI("/"),
                methodCheckerForLink, fieldCheckerForSchema);
    }

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
    LinkFactory<StationResource> stationResourceLinkFactory(LinkFactory<StationsResource> stationsResourceLinkFactory) {
        return stationsResourceLinkFactory.subResource(StationsResource::stationSubResource, StationResource.class);
    }

    @Bean
    @Named("weatherLinkFactory")
    LinkFactory<WeatherResource> weatherResourceLinkFactory(LinkMetaFactory linkMetaFactory) {
        return linkMetaFactory.createFactoryFor(WeatherResource.class);
    }

    @Bean
    JsonHyperSchemaCreator jsonHyperSchemaCreator() {
        return new JsonHyperSchemaCreator();
    }

    @Bean
    ObjectWithSchemaCreator objectWithSchemaCreator() {
        return new ObjectWithSchemaCreator();
    }

    @Bean
    PaginatedResponseBuilderCreator paginatedResponseBuilderCreator() {
        return new PaginatedResponseBuilderCreator();
    }
}
