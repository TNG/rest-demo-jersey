package com.tngtech.demo.weather.repositories;

import com.tngtech.demo.weather.domain.measurement.AtmosphericData;
import com.tngtech.demo.weather.domain.measurement.DataPoint;
import com.tngtech.demo.weather.domain.measurement.DataPointType;
import io.vavr.control.Option;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import javax.inject.Provider;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        assertThat(weatherDataRepository.getWeatherDataFor(UUID.randomUUID())).isEmpty();
    }

    @Test
    public void shouldCreateDataRepositoryAndAddNewDataWhenUpdating() {
        UUID stationId = UUID.randomUUID();
        DataPoint dataPoint = mock(DataPoint.class);

        weatherDataRepository.update(stationId, dataPoint);

        assertThat(weatherDataRepository.getWeatherData().size()).isEqualTo(1);
        verify(stationDataRepository).update(dataPoint);
    }

    @Test
    public void shouldReuseDataRepositoryAndAddNewDataWhenUpdatingAnExistingRepository() {
        UUID stationId = UUID.randomUUID();
        DataPoint dataPoint1 = DataPoint.builder().type(DataPointType.WIND).mean(5.5).build();
        weatherDataRepository.update(stationId, dataPoint1);

        DataPoint dataPoint2 = DataPoint.builder().type(DataPointType.HUMIDITY).mean(25.5).build();
        weatherDataRepository.update(stationId, dataPoint2);

        assertThat(weatherDataRepository.getWeatherData().size()).isEqualTo(1);
        verify(dataRepositoryProvider).get();
        verify(stationDataRepository).update(dataPoint1);
        verify(stationDataRepository).update(dataPoint2);
    }

    @Test
    public void getWeatherDataForReturnsDataIfStationIsKnown() {
        UUID stationId = UUID.randomUUID();
        DataPoint dataPoint = mock(DataPoint.class);
        weatherDataRepository.update(stationId, dataPoint);
        AtmosphericData atmosphericData = mock(AtmosphericData.class);
        when(stationDataRepository.toData()).thenReturn(atmosphericData);

        final Option<AtmosphericData> result = weatherDataRepository.getWeatherDataFor(stationId);

        assertThat(result).contains(atmosphericData);
    }

    @Test
    public void removeStationIgnoresIfStationDoesNotExist() {
        UUID stationId = UUID.randomUUID();
        weatherDataRepository.removeStation(stationId);

        assertThat(weatherDataRepository.getWeatherData().toList()).isEmpty();
    }

    @Test
    public void removeStationRemovesExistingStationRepository() {
        UUID stationId = UUID.randomUUID();
        DataPoint dataPoint = mock(DataPoint.class);
        weatherDataRepository.update(stationId, dataPoint);

        weatherDataRepository.removeStation(stationId);

        assertThat(weatherDataRepository.getWeatherData().toList()).isEmpty();
    }
}