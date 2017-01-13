package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.mercateo.common.rest.schemagen.types.PaginatedResponse;
import com.mercateo.common.rest.schemagen.types.WithId;
import com.tngtech.demo.weather.domain.Station;
import com.tngtech.demo.weather.repositories.StationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StationsResourceIntegrationTest {

    @Autowired
    private StationsResource stationsResource;

    @Autowired
    private StationRepository stationRepository;

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

    @Test
    public void getSubresource() {
        final Class<StationResource> stationResourceClass = stationsResource.stationSubResource();

        assertThat(stationResourceClass).isNotNull();
    }

}