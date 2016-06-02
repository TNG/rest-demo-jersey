package com.tngtech.demo.weather.resources.weather;

import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.tngtech.demo.weather.resources.WeatherRel;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Link;
import java.util.List;
import java.util.UUID;

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

    public List<Link> createForStation(UUID stationId) {
        return collect(
                weatherLinkFactory.forCall(WeatherRel.QUERY, r -> r.queryWeatherByStation(stationId, null)),
                weatherLinkFactory.forCall(WeatherRel.UPDATE, r -> r.updateWeather(stationId, null))
        );
    }
}
