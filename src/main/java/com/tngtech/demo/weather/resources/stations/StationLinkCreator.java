package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.relation.Rel;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Link;
import java.util.List;
import java.util.UUID;

import static com.tngtech.demo.weather.lib.OptionalUtils.collect;

@Component
public class StationLinkCreator {

    private final LinkFactory<StationResource> stationLinkFactory;

    @Inject
    public StationLinkCreator(@Named("stationLinkFactory") LinkFactory<StationResource> stationLinkFactory) {
        this.stationLinkFactory = stationLinkFactory;
    }

    public List<Link> createFor(UUID stationId) {
        return collect(
                stationLinkFactory.forCall(Rel.SELF, r -> r.getStation(stationId)),
                stationLinkFactory.forCall(Rel.DELETE, r -> r.deleteStation(stationId))
        );
    }
}
