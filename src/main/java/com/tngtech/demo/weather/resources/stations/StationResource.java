package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.JerseyResource;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.tngtech.demo.weather.domain.Station;
import com.tngtech.demo.weather.lib.schemagen.HyperSchemaCreator;
import com.tngtech.demo.weather.repositories.StationRepository;
import com.tngtech.demo.weather.repositories.WeatherDataRepository;
import com.tngtech.demo.weather.resources.Roles;
import com.tngtech.demo.weather.resources.weather.WeatherLinkCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.tngtech.demo.weather.resources.Paths.STATION_NAME;

public class StationResource implements JerseyResource {

    private final static Logger log = LoggerFactory.getLogger(StationResource.class);

    @Inject
    private StationRepository stationRepository;

    @Inject
    private WeatherDataRepository weatherDataRepository;

    @Inject
    private HyperSchemaCreator hyperSchemaCreator;

    @Inject
    private StationLinkCreator stationLinkCreator;

    @Inject
    private WeatherLinkCreator weatherLinkCreator;

    /**
     * Retrieve station data, including latitude and longitude for a particular station
     *
     * @param stationName the station name
     * @return an HTTP Response with a json representation of {@link Station}
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectWithSchema<Station> getStation(@PathParam(STATION_NAME) String stationName) {
        log.debug("getStation('{})'", stationName);

        return stationRepository.getStationByName(stationName)
                .map(station ->
                        hyperSchemaCreator.create(
                                station,
                                stationLinkCreator.createForName(station.name),
                                weatherLinkCreator.createForStationName(station.name)
                        )
                ).getOrElseThrow(() ->
                        new NotFoundException("Station with name '" + stationName + "' was not found"));
    }


    /**
     * Remove an station from the known station list
     *
     * @param stationName the station name
     * @return HTTP Repsonse code for the delete operation
     */
    @DELETE
    @RolesAllowed(Roles.ADMIN)
    public Response deleteStation(@PathParam(STATION_NAME) String stationName) {
        log.debug("deleteStation({})", stationName);

        stationRepository.removeStation(stationName);
        weatherDataRepository.removeStation(stationName);

        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
