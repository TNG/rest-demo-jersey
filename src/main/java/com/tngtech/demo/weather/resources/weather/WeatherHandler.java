package com.tngtech.demo.weather.resources.weather;

import com.tngtech.demo.weather.domain.gis.Point;
import com.tngtech.demo.weather.domain.measurement.AtmosphericData;
import com.tngtech.demo.weather.lib.EventCounter;
import com.tngtech.demo.weather.lib.GeoCalculations;
import com.tngtech.demo.weather.lib.TimestampFactory;
import com.tngtech.demo.weather.repositories.StationRepository;
import com.tngtech.demo.weather.repositories.WeatherDataRepository;
import javaslang.collection.List;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
class WeatherHandler {

    private static final int MILLISECONDS_PER_DAY = 86400000;

    @Inject
    private WeatherDataRepository weatherDataRepository;

    @Inject
    private StationRepository stationRepository;

    @Inject
    private GeoCalculations geoCalculations;

    @Inject
    private EventCounter<UUID> requestFrequency;

    @Inject
    private EventCounter<Double> radiusFrequency;

    @Inject
    private TimestampFactory timestampFactory;

    Map<String, Object> getStatistics() {
        Map<String, Object> result = new HashMap<>();

        result.put("datasize", getCountOfDataUpdatedSinceADayAgo());

        result.put("station_freq", getStationFractions());

        result.put("radius_freq", getRadiusHistogram());

        return result;
    }

    /**
     * Retrieve the most up to date atmospheric information from the given stationId and other airports in the given
     * radius.
     *
     * @param stationId the three letter stationId code
     * @param radius    the radius, in km, from which to collect weather data
     * @return an HTTP Response and a list of {@link AtmosphericData} from the requested stationId and
     * airports in the given radius
     */
    List<AtmosphericData> queryWeather(UUID stationId, Double radius) {

        updateRequestFrequency(stationId, radius);

        List<AtmosphericData> retval;
        if (radius == 0) {
            return weatherDataRepository
                    .getWeatherDataFor(stationId)
                    .toList();
        } else {
            return stationRepository
                    .getStationById(stationId)
                    .toList()
                    .flatMap(centerStation -> stationRepository
                            .getStations()
                            .filter(otherStation -> geoCalculations
                                    .calculateDistance(otherStation.object, centerStation.object) <= radius)
                            .map(station -> station.id)
                            .flatMap(weatherDataRepository::getWeatherDataFor)
                            .toList());
        }
    }

    List<AtmosphericData> queryWeather(Point location, Double radius) {
        return List.empty();
    }

    private int getCountOfDataUpdatedSinceADayAgo() {
        final long oneDayAgo = timestampFactory.getCurrentTimestamp() - MILLISECONDS_PER_DAY;

        return weatherDataRepository
                .getWeatherData()
                .count(data -> data.lastUpdateTime > oneDayAgo);
    }

    private Map<UUID, Double> getStationFractions() {
        Map<UUID, Double> freq = new HashMap<>();
        stationRepository.getStations().map(station -> station.id).forEach(stationId ->
                freq.put(stationId, requestFrequency.fractionOf(stationId))
        );
        return freq;
    }

    private int[] getRadiusHistogram() {
        int maxRange = radiusFrequency.events().max().getOrElse(1000.0).intValue();
        int binSize = 10;

        final int binCount = maxRange / binSize + 1;

        int[] hist = new int[binCount];

        radiusFrequency
                .stream()
                .forEach(tuple -> {
                            final Double radius = tuple._1();
                            int binIndex = radius.intValue() / 10;

                            final int radiusFrequency = tuple._2().get();
                            hist[binIndex] += radiusFrequency;
                        }
                );

        return hist;
    }

    private void updateRequestFrequency(UUID stationId, Double radius) {
        stationRepository
                .getStationById(stationId)
                .forEach(station -> {
                            requestFrequency.increment(station.id);
                            radiusFrequency.increment(radius);
                        }
                );
    }
}
