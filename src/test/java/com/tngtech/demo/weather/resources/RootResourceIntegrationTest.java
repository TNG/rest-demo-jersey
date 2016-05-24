package com.tngtech.demo.weather.resources;

import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.tngtech.demo.weather.WeatherServer;
import com.tngtech.demo.weather.domain.Station;
import com.tngtech.demo.weather.domain.measurement.AtmosphericData;
import com.tngtech.demo.weather.domain.measurement.DataPoint;
import com.tngtech.demo.weather.domain.measurement.DataPointType;
import com.tngtech.demo.weather.repositories.StationRepository;
import com.tngtech.demo.weather.resources.weather.WeatherResource;
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
public class RootResourceIntegrationTest {

    private RootResource rootResource;

    @Inject
    private StationRepository stationRepository;

    private DataPoint dataPoint;

    @Inject
    private AutowireCapableBeanFactory autowireBeanFactory;

    @Before
    public void setUp() throws Exception {
        rootResource = new RootResource();
                autowireBeanFactory.autowireBean(rootResource);

        stationRepository.addStation(Station.builder().name("BOS").latitude(49.0).longitude(11.0).build());
        stationRepository.addStation(Station.builder().name("JFK").latitude(49.0).longitude(11.0).build());
        stationRepository.addStation(Station.builder().name("EWR").latitude(49.0).longitude(11.0).build());
        stationRepository.addStation(Station.builder().name("LGA").latitude(55.0).longitude(11.0).build());
    }

    @Test
    public void callingRootResourceShouldReturnAvailableLinks() {
        ObjectWithSchema<Void> root = rootResource.getRoot();

        assertThat(root.schema.getByRel(WeatherRel.STATIONS)).isPresent();
        assertThat(root.schema.getByRel(WeatherRel.QUERY)).isPresent();
        assertThat(root.schema.getByRel(WeatherRel.STATISTICS)).isPresent();
    }

}