package com.tngtech.demo.weather.resources.weather;

import com.mercateo.common.rest.schemagen.JerseyResource;
import com.tngtech.demo.weather.domain.gis.Location;
import com.tngtech.demo.weather.domain.measurement.AtmosphericData;
import com.tngtech.demo.weather.domain.measurement.DataPoint;
import com.tngtech.demo.weather.repositories.StationRepository;
import com.tngtech.demo.weather.repositories.WeatherDataRepository;
import com.tngtech.demo.weather.resources.Paths;
import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

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
import java.util.UUID;

import static com.tngtech.demo.weather.resources.Paths.RADIUS;
import static com.tngtech.demo.weather.resources.Paths.STATION_ID;
import static com.tngtech.demo.weather.resources.Paths.STATISTICS;

/**
 * A REST implementation of the WeatherCollector API. Accessible only to station
 * weather collection sites via secure VPN.
 */

@Path(Paths.WEATHER)
@Component
@AllArgsConstructor
@Api(value = Paths.WEATHER, description = "weather resource")
@Slf4j
public class WeatherResource implements JerseyResource {

    private final StationRepository stationRepository;

    private final WeatherDataRepository weatherDataRepository;

    private final WeatherController handler;

    @GET
    @Path("/{" + Paths.LATITUDE + "}/{" + Paths.LONGITUDE + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AtmosphericData> queryWeatherByLocation(@PathParam(Paths.LATITUDE) Double latitude,
                                                        @PathParam(Paths.LONGITUDE) Double longitude,
                                                        @DefaultValue("250.0")
                                                        @QueryParam(Paths.RADIUS) Double radius) {
        val location = new Location(latitude, longitude);
        val atmosphericDatas = handler.queryWeather(location, radius);
        return atmosphericDatas.toJavaList();
    }

    /**
     * Update the airports atmospheric information for a particular pointType with
     * json formatted data point information.
     *
     * @param stationId the station name
     * @param dataPoint data structure containing point type and mean, first, second, third and count values
     * @return HTTP Response code
     */
    @POST
    @Path("/{" + Paths.STATION_ID + "}")
    public Response updateWeather(@PathParam(Paths.STATION_ID) UUID stationId,
                                  DataPoint dataPoint) {

        if (stationRepository.getStationById(stationId).isEmpty()) {
            log.debug("updateWeather({}, {}, {}) not accepted", stationId, dataPoint);
            return Response.status(Response.Status.NOT_ACCEPTABLE).build();
        }
        log.debug("updateWeather({}, {}, {})", stationId, dataPoint);

        weatherDataRepository.update(stationId, dataPoint);

        return Response.status(Response.Status.OK).build();
    }


    /**
     * Retrieve the most up to date atmospheric information from the given station and other airports in the given
     * radius.
     *
     * @param stationId the station name
     * @param radius    the radius, in km, from which to collect weather data
     * @return an HTTP Response and a list of {@link AtmosphericData} from the requested station and
     * stations in the given radius
     */
    @GET
    @Path("/{" + STATION_ID + "}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AtmosphericData> queryWeatherByStation(@PathParam(STATION_ID) UUID stationId,
                                                       @QueryParam(RADIUS) @DefaultValue("0.0") Double radius) {
        log.debug("weather({}, {})", stationId, radius);

        radius = radius == null ? 0.0 : radius;

        return handler.queryWeather(stationId, radius).toJavaList();
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
