package com.tngtech.demo.weather.lib;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TimestampFactoryTest {

    @Test
    public void shouldCreateCurrentTimestamp() {
        long currentTimeMillis = System.currentTimeMillis();
        assertThat(new TimestampFactory().getCurrentTimestamp()).isBetween(currentTimeMillis, currentTimeMillis + 10);
    }

}