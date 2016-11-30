package com.tngtech.demo.weather.resources.weather;

import com.mercateo.common.rest.schemagen.types.WithId;
import com.tngtech.demo.weather.domain.Station;
import com.tngtech.demo.weather.domain.measurement.AtmosphericData;
import com.tngtech.demo.weather.domain.measurement.DataPoint;
import com.tngtech.demo.weather.domain.measurement.DataPointType;
import com.tngtech.demo.weather.repositories.StationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WeatherResourceIntegrationTest {

    @Autowired
    private WeatherResource weatherResource;

    @Autowired
    private StationRepository stationRepository;

    private DataPoint dataPoint;

    private WithId<Station> station1;
    private WithId<Station> station2;
    private WithId<Station> station3;
    private WithId<Station> station4;

    @Before
    public void setUp() throws Exception {
        station1 = WithId.create(Station.builder().name("ABC").latitude(49.0).longitude(11.0).build());
        stationRepository.addStation(station1);
        station2 = WithId.create(Station.builder().name("DEF").latitude(50.0).longitude(10.0).build());
        stationRepository.addStation(station2);
        station3 = WithId.create(Station.builder().name("GHI").latitude(51.0).longitude(12.0).build());
        stationRepository.addStation(station3);
        station4 = WithId.create(Station.builder().name("JKL").latitude(55.0).longitude(13.0).build());
        stationRepository.addStation(station4);

        dataPoint = DataPoint.builder()
                .type(DataPointType.WIND)
                .count(10).first(10).median(20).last(30).mean(22).build();
        weatherResource.updateWeather(station1.id, dataPoint);
        weatherResource.queryWeatherByStation(station1.id, null);
    }

    @Test
    public void pingShouldReturnDatasizeAndIataFreqInformation() throws Exception {
        Map<String, Object> ping = weatherResource.getStatistics();
        assertThat(ping.get("datasize")).isEqualTo(1);
        assertThat(((Map<UUID, Double>) ping.get("station_freq")).entrySet()).hasSize(4);
    }

    @Test
    public void weatherQueryShouldReturnPreviouslyUploadedData() throws Exception {
        List<AtmosphericData> ais = weatherResource.queryWeatherByStation(station1.id, null);
        assertThat(ais.get(0).wind).isEqualTo(dataPoint);
    }

    @Test
    public void weatherQueryWithIncludingNearbyRadiusShouldReturnMultipleResults() throws Exception {
        // check datasize response

        weatherResource.updateWeather(station2.id, dataPoint);
        dataPoint = dataPoint.toBuilder().mean(40).build();
        weatherResource.updateWeather(station3.id, dataPoint);
        dataPoint = dataPoint.toBuilder().mean(30).build();
        weatherResource.updateWeather(station4.id, dataPoint);

        List<AtmosphericData> ais = weatherResource.queryWeatherByStation(station2.id, 300.0);
        assertThat(ais).hasSize(3);
    }

    @Test
    public void consecutiveWeatherUpdatesOfDifferentTypeShouldBeAccumulated() throws Exception {
        DataPoint windDp = DataPoint.builder()
                .type(DataPointType.WIND)
                .count(10).first(10).median(20).last(30).mean(22).build();
        weatherResource.updateWeather(station1.id, windDp);
        weatherResource.queryWeatherByStation(station1.id, null);

        Map<String, Object> ping = weatherResource.getStatistics();
        assertThat(ping.get("datasize")).isEqualTo(1);

        DataPoint cloudCoverDp = DataPoint.builder()
                .type(DataPointType.CLOUDCOVER)
                .count(4).first(10).median(60).last(100).mean(50).build();
        weatherResource.updateWeather(station1.id, cloudCoverDp);

        List<AtmosphericData> ais = weatherResource.queryWeatherByStation(station1.id, null);
        assertThat(ais.get(0).wind).isEqualTo(windDp);
        assertThat(ais.get(0).cloudCover).isEqualTo(cloudCoverDp);
    }

    @Test
    public void addingWeatherForNonExistentStationReturns406() throws Exception {
        UUID stationId = UUID.randomUUID();
        Response response = weatherResource.updateWeather(stationId, null);
        assertThat(response.getStatus()).isEqualTo(406);
    }


}