package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.relation.Rel;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Link;
import java.util.List;

import static com.tngtech.demo.weather.lib.OptionalUtils.collect;

@Component
public class StationLinkCreator {

    @Inject
    @Named("stationLinkFactory")
    private LinkFactory<StationResource> stationLinkFactory;

    public List<Link> createForName(String stationName) {
        return collect(
                stationLinkFactory.forCall(Rel.SELF, r -> r.getStation(stationName)),
                stationLinkFactory.forCall(Rel.DELETE, r -> r.deleteStation(stationName))
        );
    }
}
