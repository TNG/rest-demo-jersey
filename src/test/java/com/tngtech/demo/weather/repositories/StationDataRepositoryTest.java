package com.tngtech.demo.weather.repositories;

import com.tngtech.demo.weather.domain.measurement.DataPoint;
import com.tngtech.demo.weather.domain.measurement.DataPointType;
import com.tngtech.demo.weather.lib.TimestampFactory;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class StationDataRepositoryTest {

    @Mock
    private TimestampFactory timestampFactory;

    @InjectMocks
    private StationDataRepository repository;

    @Test
    public void shouldAcceptWindDataInValidRange() {
        repository.update(createData(DataPointType.WIND, 0.0));

        Assertions.assertThat(repository.toData().wind).isEqualTo(createData(DataPointType.WIND, 0.0));
    }

    @Test
    public void shouldIgnoreWindDataInValidRange() {
        repository.update(createData(DataPointType.WIND, -0.0001));

        Assertions.assertThat(repository.toData().wind).isNull();
    }

    @Test
    public void shouldAcceptTemperatureDataAtLowestValidValue() {
        final DataPoint data = createData(DataPointType.TEMPERATURE, -50.0);
        repository.update(data);

        Assertions.assertThat(repository.toData().temperature).isEqualTo(data);
    }

    @Test
    public void shouldAcceptTemperatureDataAtHighestValidValue() {
        final DataPoint data = createData(DataPointType.TEMPERATURE, 99.999999);
        repository.update(data);

        Assertions.assertThat(repository.toData().temperature).isEqualTo(data);
    }

    @Test
    public void shouldIgnoreTemperatureDataBelowLowestValidValue() {
        repository.update(createData(DataPointType.TEMPERATURE, -50.0001));

        Assertions.assertThat(repository.toData().temperature).isNull();
    }

    @Test
    public void shouldIgnoreTemperatureDataAboveHighestValidValue() {
        repository.update(createData(DataPointType.TEMPERATURE, 100.0));

        Assertions.assertThat(repository.toData().temperature).isNull();
    }

    private DataPoint createData(DataPointType type, double mean) {
        return DataPoint.builder().type(type).first(0).median(1).last(0).count(1).mean(mean).build();
    }
}