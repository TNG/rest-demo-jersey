package com.tngtech.demo.weather.resources;

import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.mercateo.common.rest.schemagen.types.WithId;
import com.tngtech.demo.weather.domain.Station;
import com.tngtech.demo.weather.repositories.StationRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class RootResourceIntegrationTest {

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private RootResource rootResource;

    @Before
    public void setUp() throws Exception {
        stationRepository.addStation(WithId.create(Station.builder().name("ABC").latitude(49.0).longitude(11.0).build()));
        stationRepository.addStation(WithId.create(Station.builder().name("DEF").latitude(49.0).longitude(11.0).build()));
        stationRepository.addStation(WithId.create(Station.builder().name("GHI").latitude(49.0).longitude(11.0).build()));
        stationRepository.addStation(WithId.create(Station.builder().name("JKL").latitude(55.0).longitude(11.0).build()));
    }

    @Test
    public void callingRootResourceShouldReturnAvailableLinks() {
        ObjectWithSchema<Void> root = rootResource.getRoot();

        assertThat(root.schema.getByRel(WeatherRel.STATIONS)).isPresent();
        assertThat(root.schema.getByRel(WeatherRel.QUERY)).isPresent();
        assertThat(root.schema.getByRel(WeatherRel.STATISTICS)).isPresent();
    }

}