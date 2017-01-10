package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.types.HyperSchemaCreator;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.mercateo.common.rest.schemagen.types.WithId;
import com.tngtech.demo.weather.domain.Station;
import com.tngtech.demo.weather.resources.weather.WeatherLinkCreator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class StationHyperSchemaCreator {

    private final HyperSchemaCreator hyperSchemaCreator;

    private final StationLinkCreator stationLinkCreator;

    private final WeatherLinkCreator weatherLinkCreator;

    ObjectWithSchema<WithId<Station>> create(WithId<Station> station) {
        return hyperSchemaCreator.create(
                station,
                stationLinkCreator.createFor(station.id),
                weatherLinkCreator.createForStation(station.id)
        );
    }
}
