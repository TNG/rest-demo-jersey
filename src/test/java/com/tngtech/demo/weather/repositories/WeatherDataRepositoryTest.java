package com.tngtech.demo.weather.repositories;

import com.tngtech.demo.weather.domain.measurement.AtmosphericData;
import com.tngtech.demo.weather.domain.measurement.DataPoint;
import com.tngtech.demo.weather.domain.measurement.DataPointType;
import javaslang.control.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Provider;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WeatherDataRepositoryTest {

    @Mock
    private StationDataRepository stationDataRepository;

    @Spy
    private Provider<StationDataRepository> dataRepositoryProvider = new Provider<StationDataRepository>() {
        @Override
        public StationDataRepository get() {
            return stationDataRepository;
        }
    };

    @InjectMocks
    private WeatherDataRepository weatherDataRepository;

    @Test
    public void returnsEmptyWeatherDataAfterInitialization() {
        assertThat(weatherDataRepository.getWeatherData().toList()).isEmpty();
    }

    @Test
    public void returnsEmptyElementAfterInitialization() {
        assertThat(weatherDataRepository.getWeatherDataFor("foo")).isEmpty();
    }

    @Test
    public void shouldCreateDataRepositoryAndAddNewDataWhenUpdating() {
        DataPoint dataPoint = mock(DataPoint.class);

        weatherDataRepository.update("foo", dataPoint);

        assertThat(weatherDataRepository.getWeatherData().size()).isEqualTo(1);
        verify(stationDataRepository).update(dataPoint);
    }

    @Test
    public void shouldReuseDataRepositoryAndAddNewDataWhenUpdatingAnExistingRepository() {
        DataPoint dataPoint1 = DataPoint.builder().type(DataPointType.WIND).mean(5.5).build();
        weatherDataRepository.update("foo", dataPoint1);

        DataPoint dataPoint2 = DataPoint.builder().type(DataPointType.HUMIDITY).mean(25.5).build();
        weatherDataRepository.update("foo", dataPoint2);

        assertThat(weatherDataRepository.getWeatherData().size()).isEqualTo(1);
        verify(dataRepositoryProvider).get();
        verify(stationDataRepository).update(dataPoint1);
        verify(stationDataRepository).update(dataPoint2);
    }

    @Test
    public void getWeatherDataForReturnsDataIfStationIsKnown() {
        DataPoint dataPoint = mock(DataPoint.class);
        weatherDataRepository.update("foo", dataPoint);
        AtmosphericData atmosphericData = mock(AtmosphericData.class);
        when(stationDataRepository.toData()).thenReturn(atmosphericData);

        final Option<AtmosphericData> result = weatherDataRepository.getWeatherDataFor("foo");

        assertThat(result).contains(atmosphericData);
    }

    @Test
    public void removeStationIgnoresIfStationDoesNotExist() {
        weatherDataRepository.removeStation("bar");

        assertThat(weatherDataRepository.getWeatherData().toList()).isEmpty();
    }

    @Test
    public void removeStationRemovesExistingStationRepository() {
        DataPoint dataPoint = mock(DataPoint.class);
        weatherDataRepository.update("foo", dataPoint);

        weatherDataRepository.removeStation("foo");

        assertThat(weatherDataRepository.getWeatherData().toList()).isEmpty();
    }
}