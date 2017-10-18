package com.tngtech.demo.weather.resources.weather;

import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.parameter.CallContext;
import com.tngtech.demo.weather.resources.WeatherRel;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import javax.ws.rs.core.Link;
import java.util.List;
import java.util.UUID;

import static com.mercateo.common.rest.schemagen.util.OptionalUtil.collect;


@Component
public class WeatherLinkCreator {

    private final LinkFactory<WeatherResource> weatherLinkFactory;

    @Autowired
    public WeatherLinkCreator(@Named("weatherLinkFactory") LinkFactory<WeatherResource> weatherLinkFactory) {
        this.weatherLinkFactory = weatherLinkFactory;
    }

    public List<Link> create() {
        val callContext = CallContext.create();
        val weatherQueryParam = callContext.builderFor(WeatherQueryParam.class).defaultValue(new WeatherQueryParam(null, null, 250.0)).build();

        return collect(
                weatherLinkFactory.forCall(WeatherRel.STATISTICS, WeatherResource::getStatistics),
                weatherLinkFactory.forCall(WeatherRel.QUERY, r -> r.queryWeatherByLocation(weatherQueryParam.get()))
        );
    }

    public List<Link> createForStation(UUID stationId) {
        return collect(
                weatherLinkFactory.forCall(WeatherRel.QUERY, r -> r.queryWeatherByStation(stationId, null)),
                weatherLinkFactory.forCall(WeatherRel.UPDATE, r -> r.updateWeather(stationId, null))
        );
    }
}
