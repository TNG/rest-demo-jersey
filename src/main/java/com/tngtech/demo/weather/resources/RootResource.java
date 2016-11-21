package com.tngtech.demo.weather.resources;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.mercateo.common.rest.schemagen.JerseyResource;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.tngtech.demo.weather.lib.schemagen.HyperSchemaCreator;
import com.tngtech.demo.weather.resources.stations.StationsLinkCreator;
import com.tngtech.demo.weather.resources.weather.WeatherLinkCreator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import static java.util.Objects.requireNonNull;

@Path("/")
@Api(value = "/", description = "root resource")
public class RootResource implements JerseyResource {

    @Inject
    private StationsLinkCreator stationsLinkCreator;

    @Inject
    private WeatherLinkCreator weatherLinkCreator;

    @Inject
    private HyperSchemaCreator hyperSchemaCreator;

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all possible links")
    @ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
    public ObjectWithSchema<Void> getRoot() {
        return hyperSchemaCreator.create(null, stationsLinkCreator.create(), weatherLinkCreator
                .create());
    }
}
