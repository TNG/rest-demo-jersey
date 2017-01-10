package com.tngtech.demo.weather.repositories;

import com.mercateo.common.rest.schemagen.types.WithId;
import com.tngtech.demo.weather.domain.Station;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StationRepositoryTest {

    @InjectMocks
    private StationRepository repository;

    @Test
    public void shouldBeEmptyAfterInitialization() {
        assertThat(repository.getStations().toList()).isEmpty();
    }

    @Test
    public void shouldReturnEmptyResultIfStationIsNotFound() {
        assertThat(repository.getStationById(UUID.randomUUID())).isEmpty();
    }

    @Test
    public void anAddedStationShouldBePersisted() {
        WithId<Station> stationWithId = WithId.create(Station.builder().name("foo").latitude(49.0).longitude(11.0).build());
        repository.addStation(stationWithId);

        assertThat(repository.getStationById(stationWithId.id)).isNotEmpty().have(new Condition<WithId<Station>>() {
            @Override
            public boolean matches(WithId<Station> station) {
                return station.object.name.equals("foo") &&
                        station.object.latitude() == 49.0 &&
                        station.object.longitude() == 11.0;
            }
        });
    }

    @Test
    public void removingANonExistentStationShouldBeIgnored() {
        repository.removeStation(UUID.randomUUID());

        assertThat(repository.getStations().toList()).isEmpty();
    }

    @Test
    public void removedStationShouldDisappearFromRepository() {
        WithId<Station> stationWithId = WithId.create(Station.builder().name("foo").latitude(49.0).longitude(11.0).build());
        repository.addStation(stationWithId);

        repository.removeStation(stationWithId.id);

        assertThat(repository.getStationById(stationWithId.id)).isEmpty();
    }

}