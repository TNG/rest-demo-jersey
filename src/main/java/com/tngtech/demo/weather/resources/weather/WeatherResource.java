package com.tngtech.demo.weather.resources.weather;

import com.mercateo.common.rest.schemagen.JerseyResource;
import com.tngtech.demo.weather.domain.gis.Location;
import com.tngtech.demo.weather.domain.measurement.AtmosphericData;
import com.tngtech.demo.weather.domain.measurement.DataPoint;
import com.tngtech.demo.weather.repositories.StationRepository;
import com.tngtech.demo.weather.repositories.WeatherDataRepository;
import com.tngtech.demo.weather.resources.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

import static com.tngtech.demo.weather.resources.Paths.STATISTICS;
import static com.tngtech.demo.weather.resources.Paths.RADIUS;
import static com.tngtech.demo.weather.resources.Paths.STATION_NAME;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to station
 * weather collection sites via secure VPN.
 */

@Path(Paths.WEATHER)
public class WeatherResource implements JerseyResource {
    private final static Logger log = LoggerFactory.getLogger(WeatherResource.class);

    @Inject
    private StationRepository stationRepository;

    @Inject
    private WeatherDataRepository weatherDataRepository;

    @Inject
    private WeatherHandler handler;

    @GET
    @Path("/{" + Paths.LATITUDE + "}/{" + Paths.LONGITUDE + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AtmosphericData> queryWeatherByLocation(@PathParam(Paths.LATITUDE) Double latitude,
                                              @PathParam(Paths.LONGITUDE) Double longitude,
                                              @DefaultValue("250.0")
                                              @QueryParam(Paths.RADIUS) Double radius) {
        Location location = Location.builder().latitude(latitude).longitude(longitude).build();
        javaslang.collection.List<AtmosphericData> atmosphericDatas = handler.queryWeather(location, radius);
        return atmosphericDatas.toJavaList();
    }

    /**
     * Update the airports atmospheric information for a particular pointType with
     * json formatted data point information.
     *
     * @param stationName      the station name
     * @param dataPoint     data structure containing point type and mean, first, second, third and count values
     * @return HTTP Response code
     */
    @POST
    @Path("/{" + Paths.STATION_NAME + "}")
    public Response updateWeather(@PathParam(Paths.STATION_NAME) String stationName,
                                  DataPoint dataPoint) {

        if (stationRepository.getStationByName(stationName).isEmpty()) {
            log.debug("updateWeather({}, {}, {}) not accepted", stationName, dataPoint);
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        log.debug("updateWeather({}, {}, {})", stationName, dataPoint);

        weatherDataRepository.update(stationName, dataPoint);

        return Response.status(Response.Status.OK).build();
    }


    /**
     * Retrieve the most up to date atmospheric information from the given station and other airports in the given
     * radius.
     *
     * @param stationName     the station name
     * @param radius the radius, in km, from which to collect weather data
     * @return an HTTP Response and a list of {@link AtmosphericData} from the requested station and
     * stations in the given radius
     */
    @GET
    @Path("/{" + STATION_NAME + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AtmosphericData> queryWeatherByStationName(@PathParam(STATION_NAME) String stationName,
                                              @QueryParam(RADIUS) @DefaultValue("0.0") Double radius) {
        log.debug("weather({}, {})", stationName, radius);

        radius = radius == null ? 0.0 : radius;

        return handler.queryWeather(stationName, radius).toJavaList();
    }

    /**
     * Retrieve health and status information for the the query api. Returns information about how the number
     * of datapoints currently held in memory, the frequency of requests for each IATA code and the frequency of
     * requests for each radius.
     *
     * @return a JSON formatted dict with health information.
     */
    @GET
    @Path(STATISTICS)
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Object> getStatistics() {
        log.debug("getStatistics()");

        return handler.getStatistics();
    }

}
