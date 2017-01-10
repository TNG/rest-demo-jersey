package com.tngtech.demo.weather;

import com.mercateo.common.rest.schemagen.JsonHyperSchemaCreator;
import com.mercateo.common.rest.schemagen.JsonSchemaGenerator;
import com.mercateo.common.rest.schemagen.RestJsonSchemaGenerator;
import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.LinkFactoryContext;
import com.mercateo.common.rest.schemagen.link.LinkFactoryContextDefault;
import com.mercateo.common.rest.schemagen.link.LinkMetaFactory;
import com.mercateo.common.rest.schemagen.link.helper.BaseUriCreator;
import com.mercateo.common.rest.schemagen.link.helper.BaseUriCreatorDefault;
import com.mercateo.common.rest.schemagen.plugin.FieldCheckerForSchema;
import com.mercateo.common.rest.schemagen.plugin.MethodCheckerForLink;
import com.mercateo.common.rest.schemagen.types.HyperSchemaCreator;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchemaCreator;
import com.mercateo.common.rest.schemagen.types.PaginatedResponseBuilderCreator;
import com.tngtech.demo.weather.resources.stations.StationResource;
import com.tngtech.demo.weather.resources.stations.StationsResource;
import com.tngtech.demo.weather.resources.weather.WeatherResource;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Configuration
@Slf4j
public class WeatherServerConfiguration {

    @Bean
    public JsonSchemaGenerator jsonSchemaGenerator() {
        return new RestJsonSchemaGenerator();
    }

    @Bean
    public LinkMetaFactory linkMetaFactory(JsonSchemaGenerator jsonSchemaGenerator, LinkFactoryContext linkFactoryContext)
            throws URISyntaxException {
        return LinkMetaFactory.create(jsonSchemaGenerator, linkFactoryContext);
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

    @Bean
    HyperSchemaCreator hyperSchemaCreator(ObjectWithSchemaCreator objectWithSchemaCreator, JsonHyperSchemaCreator jsonHyperSchemaCreator) {
        return new HyperSchemaCreator(objectWithSchemaCreator, jsonHyperSchemaCreator);
    }

    @Bean
    @Scope(value = "request")
    HttpServletRequest httpServletRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
    }

    @Bean
    @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
    LinkFactoryContext linkFactoryContext(HttpServletRequest httpServletRequest, BaseUriCreator baseUriCreator) throws URISyntaxException {
        val defaultBaseUri = new URI(httpServletRequest.getRequestURL().toString());
        val requestHeaders = requestHeaders(httpServletRequest);

        val baseUri = baseUriCreator.createBaseUri(defaultBaseUri, requestHeaders);

        return new LinkFactoryContextDefault(baseUri, scope -> true, (field, callContext) -> true);
    }

    private HashMap<String, List<String>> requestHeaders(HttpServletRequest httpServletRequest) {
        val requestHeaders = new HashMap<String, List<String>>();

        val headerNames = httpServletRequest.getHeaderNames();

        for (String headerName : Collections.list(headerNames)) {
            requestHeaders.computeIfAbsent(headerName, ignored -> new ArrayList<String>()).add(headerName);
        }

        return requestHeaders;
    }

    @Bean
    BaseUriCreator baseUriCreator() {
        return new BaseUriCreatorDefault();
    }
}
