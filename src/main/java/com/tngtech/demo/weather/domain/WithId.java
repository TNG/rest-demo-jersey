package com.tngtech.demo.weather.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode
@ToString
public class WithId<T> {
    public final UUID id;

    @JsonUnwrapped
    public final T object;

    private WithId(
            @JsonProperty("id") UUID id,
            @JsonProperty("name") T object
    ) {
        this.id = id;
        this.object = object;
    }

    public static <T> WithId<T> create(T payload) {
        return create(UUID.randomUUID(), payload);
    }

    public static <T> WithId<T> create(Map.Entry<UUID, T> entry ) {
        return create(entry.getKey(), entry.getValue());
    }

    public static <T> WithId<T> create(UUID id, T payload) {
        return new WithId<>(id, payload);
    }


}
