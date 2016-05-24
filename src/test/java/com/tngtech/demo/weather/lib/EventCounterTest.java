package com.tngtech.demo.weather.lib;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(MockitoJUnitRunner.class)
public class EventCounterTest {

    @InjectMocks
    private EventCounter<String> eventCounter;

    @Test
    public void emptyEventCounterShouldHaveTotalCountOfZero() {
        assertThat(eventCounter.totalCount()).isEqualTo(0);
    }

    @Test
    public void nonExistentEventShouldHaveACountOfZero() {
        assertThat(eventCounter.countOf("foo")).isEqualTo(0);
    }

    @Test
    public void addingEventsShouldIncreaseTotalCount() {
        eventCounter.increment("foo");
        assertThat(eventCounter.totalCount()).isEqualTo(1);

        eventCounter.increment("bar");
        assertThat(eventCounter.totalCount()).isEqualTo(2);

        eventCounter.increment("foo");
        assertThat(eventCounter.totalCount()).isEqualTo(3);
    }

    @Test
    public void addingEventsShouldIncreaseIndividualCount() {
        eventCounter.increment("foo");
        assertThat(eventCounter.countOf("foo")).isEqualTo(1);

        eventCounter.increment("bar");
        assertThat(eventCounter.countOf("foo")).isEqualTo(1);

        eventCounter.increment("foo");
        assertThat(eventCounter.countOf("foo")).isEqualTo(2);
    }

    @Test
    public void fractionOfEmptyEventCounterShouldReturnZero() {
        assertThat(eventCounter.fractionOf("foo")).isEqualTo(0.0);
    }

    @Test
    public void addingEventsShouldUpdateIndividualFraction() {
        eventCounter.increment("foo");
        assertThat(eventCounter.fractionOf("foo")).isEqualTo(1);

        eventCounter.increment("bar");
        assertThat(eventCounter.countOf("foo")).isEqualTo(1);

        eventCounter.increment("foo");
        assertThat(eventCounter.countOf("foo")).isEqualTo(2);
    }
}