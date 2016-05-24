package com.tngtech.demo.weather.resources;

import com.mercateo.common.rest.schemagen.link.relation.Relation;
import com.mercateo.common.rest.schemagen.link.relation.RelationContainer;

public enum WeatherRel implements RelationContainer {

    QUERY, STATIONS, STATISTICS, UPDATE;

    @Override
    public Relation getRelation() {
        return Relation.of(name().toLowerCase());
    }
}
