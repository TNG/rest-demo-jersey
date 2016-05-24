package com.tngtech.demo.weather.resources.weather;

import com.tngtech.demo.weather.WeatherServer;
import com.tngtech.demo.weather.domain.Station;
import com.tngtech.demo.weather.domain.measurement.AtmosphericData;
import com.tngtech.demo.weather.domain.measurement.DataPoint;
import com.tngtech.demo.weather.domain.measurement.DataPointType;
import com.tngtech.demo.weather.repositories.StationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(WeatherServer.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WeatherResourceIntegrationTest {

    private WeatherResource weatherResource;

    @Inject
    private StationRepository stationRepository;

    private DataPoint dataPoint;

    @Inject
    private AutowireCapableBeanFactory autowireBeanFactory;

    @Before
    public void setUp() throws Exception {
        weatherResource = new WeatherResource();
        autowireBeanFactory.autowireBean(weatherResource);

        stationRepository.addStation(Station.builder().name("ABC").latitude(49.0).longitude(11.0).build());
        stationRepository.addStation(Station.builder().name("DEF").latitude(50.0).longitude(10.0).build());
        stationRepository.addStation(Station.builder().name("GHI").latitude(51.0).longitude(12.0).build());
        stationRepository.addStation(Station.builder().name("JKL").latitude(55.0).longitude(13.0).build());

        dataPoint = DataPoint.builder()
                .type(DataPointType.WIND)
                .count(10).first(10).median(20).last(30).mean(22).build();
        weatherResource.updateWeather("ABC", dataPoint);
        weatherResource.queryWeatherByStationName("ABC", null);
    }

    @Test
    public void pingShouldReturnDatasizeAndIataFreqInformation() throws Exception {
        Map<String, Object> ping = weatherResource.getStatistics();
        assertThat(ping.get("datasize")).isEqualTo(1);
        assertThat(((Map<String, Double>) ping.get("station_freq")).entrySet()).hasSize(4);
    }

    @Test
    public void weatherQueryShouldReturnPreviouslyUploadedData() throws Exception {
        List<AtmosphericData> ais = weatherResource.queryWeatherByStationName("ABC", null);
        assertThat(ais.get(0).wind).isEqualTo(dataPoint);
    }

    @Test
    public void weatherQueryWithIncludingNearbyRadiusShouldReturnMultipleResults() throws Exception {
        // check datasize response
        weatherResource.updateWeather("DEF", dataPoint);
        dataPoint = dataPoint.toBuilder().mean(40).build();
        weatherResource.updateWeather("GHI", dataPoint);
        dataPoint = dataPoint.toBuilder().mean(30).build();
        weatherResource.updateWeather("JKL", dataPoint);

        List<AtmosphericData> ais = weatherResource.queryWeatherByStationName("DEF", 300.0);
        assertThat(ais).hasSize(3);
    }

    @Test
    public void consecutiveWeatherUpdatesOfDifferentTypeShouldBeAccumulated() throws Exception {
        DataPoint windDp = DataPoint.builder()
                .type(DataPointType.WIND)
                .count(10).first(10).median(20).last(30).mean(22).build();
        weatherResource.updateWeather("ABC", windDp);
        weatherResource.queryWeatherByStationName("ABC", null);

        Map<String, Object> ping = weatherResource.getStatistics();
        assertThat(ping.get("datasize")).isEqualTo(1);

        DataPoint cloudCoverDp = DataPoint.builder()
                .type(DataPointType.CLOUDCOVER)
                .count(4).first(10).median(60).last(100).mean(50).build();
        weatherResource.updateWeather("ABC", cloudCoverDp);

        List<AtmosphericData> ais = weatherResource.queryWeatherByStationName("ABC", null);
        assertThat(ais.get(0).wind).isEqualTo(windDp);
        assertThat(ais.get(0).cloudCover).isEqualTo(cloudCoverDp);
    }

    @Test
    public void addingWeatherForNonExistentStationReturns406() throws Exception {
        Response response = weatherResource.updateWeather("FOO", null);
        assertThat(response.getStatus()).isEqualTo(406);
    }


}