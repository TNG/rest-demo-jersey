package com.tngtech.demo.weather.resources;

import com.mercateo.common.rest.schemagen.JerseyResource;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/")
@Component
@AllArgsConstructor
public class RootResource implements JerseyResource {

    private final RootHyperSchemaCreator rootHyperSchemaCreator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectWithSchema<Void> getRoot() {
        return rootHyperSchemaCreator.create();
    }
}
