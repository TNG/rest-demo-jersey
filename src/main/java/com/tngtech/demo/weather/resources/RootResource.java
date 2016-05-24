package com.tngtech.demo.weather.resources;

import com.mercateo.common.rest.schemagen.JerseyResource;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.tngtech.demo.weather.lib.schemagen.HyperSchemaCreator;
import com.tngtech.demo.weather.resources.stations.StationsLinkCreator;
import com.tngtech.demo.weather.resources.weather.WeatherLinkCreator;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
public class RootResource implements JerseyResource {

    @Inject
    private StationsLinkCreator stationsLinkCreator;

    @Inject
    private WeatherLinkCreator weatherLinkCreator;

    @Inject
    private HyperSchemaCreator hyperSchemaCreator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectWithSchema<Void> getRoot() {
        return hyperSchemaCreator.create(
                null,
                stationsLinkCreator.create(),
                weatherLinkCreator.create()
        );
    }
}
