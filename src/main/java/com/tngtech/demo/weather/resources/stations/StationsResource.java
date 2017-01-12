package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.JerseyResource;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.mercateo.common.rest.schemagen.types.PaginatedList;
import com.mercateo.common.rest.schemagen.types.PaginatedResponse;
import com.mercateo.common.rest.schemagen.types.WithId;
import com.tngtech.demo.weather.domain.Station;
import com.tngtech.demo.weather.repositories.StationRepository;
import com.tngtech.demo.weather.resources.Paths;
import com.tngtech.demo.weather.resources.Roles;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import static java.util.Objects.requireNonNull;

//@Path(Paths.STATIONS)
@Path("/test")
@AllArgsConstructor
@Component
@Api(value = Paths.STATIONS, description = "stations resource")
@Slf4j
public class StationsResource implements JerseyResource {

    private final StationRepository stationRepository;

    private final StationsHyperschemaCreator stationsHyperschemaCreator;

    /**
     * Return a list of known airports as a json formatted list
     *
     * @return HTTP Response code and a json formatted list of IATA codes
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public PaginatedResponse<WithId<Station>> getStations(@QueryParam(Paths.OFFSET) @DefaultValue("0") Integer offset,
                                                          @QueryParam(Paths.LIMIT) @DefaultValue("100") Integer limit) {
        log.debug("getStations({}, {})", offset, limit);
        offset = offset == null ? 0 : offset;
        limit = limit == null ? 2000 : limit;

        val paginatedStations = new PaginatedList<WithId<Station>>(
                stationRepository.getTotalCount().intValue(),
                offset,
                limit,
                stationRepository.getStations(offset, limit).toJavaList());

        return stationsHyperschemaCreator.createPaginatedResponse(paginatedStations);
    }

    /**
     * Add a new station to the known stations list.
     *
     * @param station new station parameters
     * @return HTTP Response code for the add operation
     */
    @POST
    @RolesAllowed(Roles.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectWithSchema<WithId<Station>> addStation(Station station) {
        requireNonNull(station);

        log.debug("addStation({}, {}, {})", station.name, station.latitude(), station.longitude());

        val stationWithId = WithId.create(station);
        stationRepository.addStation(stationWithId);

        return stationsHyperschemaCreator.create(stationWithId);
    }

    @Path("/{" + Paths.STATION_ID + "}")
    public Class<StationResource> stationSubResource() {
        log.trace("stationSubResource()");
        return StationResource.class;
    }
}
