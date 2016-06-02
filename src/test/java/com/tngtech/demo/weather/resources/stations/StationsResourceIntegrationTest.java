package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.mercateo.common.rest.schemagen.types.PaginatedResponse;
import com.tngtech.demo.weather.WeatherServer;
import com.tngtech.demo.weather.domain.Station;
import com.tngtech.demo.weather.domain.WithId;
import com.tngtech.demo.weather.repositories.StationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WeatherServer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StationsResourceIntegrationTest {

    private StationsResource stationsResource;

    @Inject
    private StationRepository stationRepository;

    @Inject
    private AutowireCapableBeanFactory autowireBeanFactory;

    @Before
    public void setUp() throws Exception {
        stationsResource = new StationsResource();
        autowireBeanFactory.autowireBean(stationsResource);
    }

    @Test
    public void getStationsShouldReturnListOfStations() {
        stationRepository.addStation(WithId.create(Station.builder().name("FOO").latitude(49.0).longitude(11.0).build()));
        PaginatedResponse<WithId<Station>> response = stationsResource.getStations(0, 1000);

        assertThat(response.object).isNotNull();

        assertThat(response.object.members).extracting("object").extracting("object").extracting("name").contains("FOO");
    }

    @Test
    public void gettingExistingStationReturnsData() {
        ObjectWithSchema<WithId<Station>> newStation = stationsResource.addStation(Station.builder().name("FOO").latitude(49.0).longitude(11.0).build());

        Station station = stationRepository.getStationById(newStation.object.id).get().object;
        assertThat(station).isNotNull();

        assertThat(station.name).isEqualTo("FOO");
        assertThat(station.latitude()).isEqualTo(49.0);
        assertThat(station.longitude()).isEqualTo(11);
    }

}