package com.tngtech.demo.weather.repositories;

import com.tngtech.demo.weather.domain.measurement.AtmosphericData;
import com.tngtech.demo.weather.domain.measurement.DataPoint;
import javaslang.collection.Seq;
import javaslang.collection.Stream;
import javaslang.control.Option;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WeatherDataRepository {

    private final ConcurrentHashMap<UUID, StationDataRepository> stationDataRepositoryByStationId = new ConcurrentHashMap<>();

    private final Provider<StationDataRepository> stationDataRepositoryProvider;

    @Inject
    public WeatherDataRepository(Provider<StationDataRepository> stationDataRepositoryProvider) {
        this.stationDataRepositoryProvider = stationDataRepositoryProvider;
    }

    /**
     * Update data for station with stationId, creates a new {@link StationDataRepository} if {@code stationId} is not known.
     *  @param stationId id which is used to reference the station
     * @param data      data to be updated
     */
    public void update(UUID stationId, DataPoint data) {
        stationDataRepositoryByStationId
                .computeIfAbsent(stationId, k -> stationDataRepositoryProvider.get())
                .update(data);
    }

    public Seq<AtmosphericData> getWeatherData() {
        return Stream.ofAll(stationDataRepositoryByStationId.values())
                .map(StationDataRepository::toData);
    }

    public Option<AtmosphericData> getWeatherDataFor(UUID stationId) {
        return Option.of(stationDataRepositoryByStationId.get(stationId))
                .map(StationDataRepository::toData);
    }

    public void removeStation(UUID stationId) {
        stationDataRepositoryByStationId.remove(stationId);
    }
}
