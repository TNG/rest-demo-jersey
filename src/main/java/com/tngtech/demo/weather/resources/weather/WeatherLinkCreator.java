package com.tngtech.demo.weather.resources.weather;

import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.tngtech.demo.weather.resources.WeatherRel;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Link;
import java.util.List;

import static com.tngtech.demo.weather.lib.OptionalUtils.collect;

@Component
public class WeatherLinkCreator {

    @Inject
    @Named("weatherLinkFactory")
    private LinkFactory<WeatherResource> weatherLinkFactory;

    public List<Link> create() {
        return collect(
                weatherLinkFactory.forCall(WeatherRel.STATISTICS, WeatherResource::getStatistics),
                weatherLinkFactory.forCall(WeatherRel.QUERY, r -> r.queryWeatherByLocation(49.0, 11.0, null))
        );
    }

    public List<Link> createForStationName(String stationName) {
        return collect(
                weatherLinkFactory.forCall(WeatherRel.QUERY, r -> r.queryWeatherByStationName(stationName, null)),
                weatherLinkFactory.forCall(WeatherRel.UPDATE, r -> r.updateWeather(stationName, null))
        );
    }
}
