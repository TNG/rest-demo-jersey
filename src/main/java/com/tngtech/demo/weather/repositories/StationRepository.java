package com.tngtech.demo.weather.repositories;

import com.mercateo.common.rest.schemagen.types.WithId;
import com.tngtech.demo.weather.domain.Station;
import javaslang.collection.Stream;
import javaslang.control.Option;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class StationRepository {
    private final ConcurrentHashMap<UUID, Station> stationsById = new ConcurrentHashMap<>();

    public void addStation(WithId<Station> newStation) {
        stationsById.put(newStation.id, newStation.object);
    }

    public Option<WithId<Station>> getStationById(UUID stationId) {
        return Option.of(stationsById.get(stationId))
                .map(station -> WithId.create(stationId, station));
    }

    public Stream<WithId<Station>> getStations() {
        return getStations(0, Integer.MAX_VALUE);
    }

    public Stream<WithId<Station>> getStations(final Integer offset, final Integer limit) {
        return Stream.ofAll(stationsById.entrySet())
                .drop(offset)
                .take(limit)
                .map(WithId::create);
    }

    public void removeStation(UUID stationId) {
        stationsById.remove(stationId);
    }

    public Long getTotalCount() {
        return stationsById.mappingCount();
    }
}
