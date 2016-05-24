package com.tngtech.demo.weather.lib;

import com.tngtech.demo.weather.domain.gis.Point;
import org.springframework.stereotype.Component;

@Component
public class GeoCalculations {
    private static final double EARTH_RADIUS = 6372.8;

    /**
     * Haversine distance between two points.
     *
     * @param point1
     * @param point2
     * @return the distance in KM
     */

    public double calculateDistance(Point point1, Point point2) {
        double deltaLat = Math.toRadians(point2.latitude() - point1.latitude());
        double deltaLon = Math.toRadians(point2.longitude() - point1.longitude());
        double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.pow(Math.sin(deltaLon / 2), 2)
                * Math.cos(point1.latitude()) * Math.cos(point2.latitude());
        double c = 2 * Math.asin(Math.sqrt(a));
        return EARTH_RADIUS * c;
    }
}
