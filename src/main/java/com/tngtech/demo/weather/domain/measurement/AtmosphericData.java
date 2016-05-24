package com.tngtech.demo.weather.domain.measurement;

import lombok.Builder;
import lombok.Data;

import javax.annotation.Nullable;

@Data
@Builder
public class AtmosphericData {

    /**
     * temperature in degrees celsius
     */
    @Nullable
    public final DataPoint temperature;

    /**
     * wind speed in km/h
     */
    @Nullable
    public final DataPoint wind;

    /**
     * humidity in percent
     */
    @Nullable
    public final DataPoint humidity;

    /**
     * precipitation in cm
     */
    @Nullable
    public final DataPoint precipitation;

    /**
     * pressure in mmHg
     */
    @Nullable
    public final DataPoint pressure;

    /**
     * cloud cover percent from 0 - 100 (integer)
     */
    @Nullable
    public final DataPoint cloudCover;

    /**
     * the last time this data was updated, in milliseconds since UTC epoch
     */
    public final long lastUpdateTime;
}
