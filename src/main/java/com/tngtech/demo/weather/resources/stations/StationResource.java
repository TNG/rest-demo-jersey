package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.JerseyResource;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.mercateo.common.rest.schemagen.types.WithId;
import com.tngtech.demo.weather.domain.Station;
import com.tngtech.demo.weather.repositories.StationRepository;
import com.tngtech.demo.weather.repositories.WeatherDataRepository;
import com.tngtech.demo.weather.resources.Roles;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static com.tngtech.demo.weather.resources.Paths.STATION_ID;

@Component
@AllArgsConstructor
@Slf4j
public class StationResource implements JerseyResource {

    private final StationRepository stationRepository;

    private final WeatherDataRepository weatherDataRepository;

    private final StationHyperSchemaCreator stationHyperSchemaCreator;

    /**
     * Retrieve station data, including latitude and longitude for a particular station
     *
     * @param stationId the station name
     * @return an HTTP Response with a json representation of {@link Station}
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectWithSchema<WithId<Station>> getStation(@PathParam(STATION_ID) UUID stationId) {
        log.debug("getStation('{})'", stationId);

        return stationRepository.getStationById(stationId)
                .map(stationHyperSchemaCreator::create
                ).getOrElseThrow(() ->
                        new NotFoundException("Station with id " + stationId + " was not found"));
    }

    /**
     * Remove an station from the known station list
     *
     * @param stationId the station name
     * @return HTTP Repsonse code for the delete operation
     */
    @DELETE
    @RolesAllowed(Roles.ADMIN)
    public Response deleteStation(@PathParam(STATION_ID) UUID stationId) {
        log.debug("deleteStation({})", stationId);

        stationRepository.removeStation(stationId);
        weatherDataRepository.removeStation(stationId);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
