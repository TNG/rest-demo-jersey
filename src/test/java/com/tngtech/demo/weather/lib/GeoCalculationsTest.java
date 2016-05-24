package com.tngtech.demo.weather.lib;

import org.assertj.core.data.Offset;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class GeoCalculationsTest {

    private GeoCalculations geoCalculations = new GeoCalculations();

    @Test
    public void calculateDistanceHalfwayAroundTheEquatorShouldYieldHalveEarthCircumference() {
        Point point1 = new Point(0, 0);
        Point point2 = new Point(0, 180);

        assertThat(geoCalculations.calculateDistance(point1, point2)).isEqualTo(20020.741, Offset.offset(1e-3));
        assertThat(geoCalculations.calculateDistance(point2, point1)).isEqualTo(20020.741, Offset.offset(1e-3));
    }

    @Test
    public void calculateDistanceFromTheEquatorToTheNorthernTropicShouldYieldRoughly2600Kilometers() {
        Point point1 = new Point(0, 11);
        Point point2 = new Point(23.5, 11);

        assertThat(geoCalculations.calculateDistance(point1, point2)).isEqualTo(2613.819, Offset.offset(1e-3));
        assertThat(geoCalculations.calculateDistance(point2, point1)).isEqualTo(2613.819, Offset.offset(1e-3));
    }

    static class Point implements com.tngtech.demo.weather.domain.gis.Point {
        private final double latitude;
        private final double longitude;

        public Point(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        public double latitude() {
            return latitude;
        }

        @Override
        public double longitude() {
            return longitude;
        }
    }
}