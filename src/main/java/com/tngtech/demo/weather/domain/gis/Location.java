package com.tngtech.demo.weather.domain.gis;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location implements Point {

    public final double latitude;

    public final double longitude;

    @Override
    public double latitude() {
        return latitude;
    }

    @Override
    public double longitude() {
        return longitude;
    }
}
