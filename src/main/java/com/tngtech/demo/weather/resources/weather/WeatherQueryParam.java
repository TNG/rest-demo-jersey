package com.tngtech.demo.weather.resources.weather;

import com.tngtech.demo.weather.resources.Paths;
import lombok.AllArgsConstructor;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

@AllArgsConstructor
public class WeatherQueryParam {
    @QueryParam(Paths.LATITUDE)
    final Double latitude;

    @QueryParam(Paths.LONGITUDE)
    final Double longitude;

    @DefaultValue("250.0")
    @QueryParam(Paths.RADIUS)
    final Double radius;
}
