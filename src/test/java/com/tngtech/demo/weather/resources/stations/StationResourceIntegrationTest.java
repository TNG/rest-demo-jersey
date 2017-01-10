package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.mercateo.common.rest.schemagen.types.WithId;
import com.tngtech.demo.weather.domain.Station;
import com.tngtech.demo.weather.repositories.StationRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class StationResourceIntegrationTest {

    @Inject
    private StationResource stationResource;

    @Inject
    private StationRepository stationRepository;

    @Test
    public void gettigNonExistentStationReturns404() throws Exception {
        UUID stationId = UUID.randomUUID();
        assertThatThrownBy(() -> {
            stationResource.getStation(stationId);
        }).isInstanceOf(NotFoundException.class).hasMessage("Station with id " + stationId + " was not found");
    }

    @Test
    public void gettingExistingStationReturnsData() {
        WithId<Station> newStation = WithId.create(Station.builder().name("FOO").latitude(49.0).longitude(11.0).build());
        stationRepository.addStation(newStation);

        ObjectWithSchema<WithId<Station>> response = stationResource.getStation(newStation.id);
        Station station = response.object.object;

        assertThat(station).isNotNull();

        assertThat(station.name).isEqualTo("FOO");
        assertThat(station.latitude()).isEqualTo(49.0);
        assertThat(station.longitude()).isEqualTo(11);
    }

    @Test
    public void removingExistingStationShouldWork() {
        WithId<Station> newStation = WithId.create(Station.builder().name("FOO").latitude(49.0).longitude(11.0).build());
        stationRepository.addStation(newStation);

        Response response = stationResource.deleteStation(newStation.id);

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());

        assertThatThrownBy(() -> stationResource.getStation(newStation.id)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void removingNonExistingStationShouldWork() {
        UUID nonExistentStationId = UUID.randomUUID();
        Response response = stationResource.deleteStation(nonExistentStationId);

        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());

        assertThatThrownBy(() -> stationResource.getStation(nonExistentStationId)).isInstanceOf(NotFoundException.class);
    }

}