package com.tngtech.demo.weather.repositories;

import com.tngtech.demo.weather.domain.Station;
import org.assertj.core.api.Condition;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

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
        assertThat(repository.getStationByName("foo")).isEmpty();
    }

    @Test
    public void anAddedStationShouldBePersisted() {
        repository.addStation("foo", 49.0, 11.0);

        assertThat(repository.getStationByName("foo")).isNotEmpty().have(new Condition<Station>() {
            @Override
            public boolean matches(Station station) {
                return station.name.equals("foo") &&
                        station.latitude() == 49.0 &&
                        station.longitude() == 11.0;
            }
        });
    }

    @Test
    public void removingANonExistentStationShouldBeIgnored() {
        repository.removeStation("bar");

        assertThat(repository.getStations().toList()).isEmpty();
    }

    @Test
    public void removedStationShouldDisappearFromRepository() {
        repository.addStation("foo", 49.0, 11.0);

        repository.removeStation("foo");

        assertThat(repository.getStationByName("foo")).isEmpty();
    }

}