package com.tngtech.demo.weather.resources;

import com.mercateo.common.rest.schemagen.JerseyResource;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Api(value = "/", description = "root resource")
@Component
@AllArgsConstructor
public class RootResource implements JerseyResource {

    private final RootHyperSchemaCreator rootHyperSchemaCreator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get all possible links")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    public ObjectWithSchema<Void> getRoot() {
        return rootHyperSchemaCreator.create();
    }
}
