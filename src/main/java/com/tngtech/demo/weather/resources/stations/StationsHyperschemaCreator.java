package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.types.HyperSchemaCreator;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.mercateo.common.rest.schemagen.types.PaginatedList;
import com.mercateo.common.rest.schemagen.types.PaginatedResponse;
import com.mercateo.common.rest.schemagen.types.PaginatedResponseBuilderCreator;
import com.mercateo.common.rest.schemagen.types.WithId;
import com.tngtech.demo.weather.domain.Station;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
class StationsHyperschemaCreator {
    private final StationLinkCreator stationLinkCreator;

    private final StationsLinkCreator stationsLinkCreator;

    private final PaginatedResponseBuilderCreator responseBuilderCreator;

    private final HyperSchemaCreator hyperSchemaCreator;

    PaginatedResponse<WithId<Station>> createPaginatedResponse(PaginatedList<WithId<Station>> paginatedStations) {
        return responseBuilderCreator.<WithId<Station>, WithId<Station>>builder()
                .withList(paginatedStations)
                .withPaginationLinkCreator(stationsLinkCreator::create)
                .withContainerLinks(
                        stationsLinkCreator.forCreate()
                )
                .withElementMapper(station ->
                        hyperSchemaCreator.create(station, stationLinkCreator.createFor(station.id)))
                .build();
    }

    ObjectWithSchema<WithId<Station>> create(WithId<Station> stationWithId) {
        return hyperSchemaCreator.create(stationWithId, stationLinkCreator.createFor(stationWithId.id));
    }
}
