package com.tngtech.demo.weather.repositories;

import com.tngtech.demo.weather.domain.Station;
import javaslang.collection.Stream;
import javaslang.control.Option;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class StationRepository {
    private final ConcurrentHashMap<String, Station> stationsByName = new ConcurrentHashMap<>();

    public void addStation(Station newStation) {
        stationsByName.put(newStation.name, newStation);
    }

    public Option<Station> getStationByName(String stationName) {
        return Option.of(stationsByName.get(stationName));
    }

    public Stream<Station> getStations() {
        return getStations(0, Integer.MAX_VALUE);
    }

    public Stream<Station> getStations(final Integer offset, final Integer limit) {
        return Stream.ofAll(stationsByName.values()).drop(offset).take(limit);
    }

    public void removeStation(String name) {
        stationsByName.remove(name);
    }

    public void addStation(String name, Double latitude, Double longitude) {
        final Station station = Station.builder()
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .build();

        addStation(station);
    }

    public Long getTotalCount() {
        return stationsByName.mappingCount();
    }
}
