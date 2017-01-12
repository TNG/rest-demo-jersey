package com.tngtech.demo.weather.resources;

import com.mercateo.common.rest.schemagen.types.HyperSchemaCreator;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.tngtech.demo.weather.resources.stations.StationsLinkCreator;
import com.tngtech.demo.weather.resources.weather.WeatherLinkCreator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class RootHyperSchemaCreator {
    private final StationsLinkCreator stationsLinkCreator;

    private final WeatherLinkCreator weatherLinkCreator;

    private final HyperSchemaCreator hyperSchemaCreator;

    ObjectWithSchema<Void> create() {
        return hyperSchemaCreator.create(null,
                stationsLinkCreator.create(null, null),
                weatherLinkCreator.create());
    }
}
