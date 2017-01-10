package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.relation.Rel;
import com.mercateo.common.rest.schemagen.link.relation.RelationContainer;
import com.tngtech.demo.weather.resources.WeatherRel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import javax.ws.rs.core.Link;
import java.util.List;
import java.util.Optional;

import static com.mercateo.common.rest.schemagen.util.OptionalUtil.collect;

@Component
public class StationsLinkCreator {

    private final LinkFactory<StationsResource> stationsLinkFactory;

    @Autowired
    public StationsLinkCreator(@Named("stationsLinkFactory") LinkFactory<StationsResource> stationsLinkFactory) {
        this.stationsLinkFactory = stationsLinkFactory;
    }

    public List<Link> create(Integer offset, Integer limit) {
        return collect(
                create(WeatherRel.STATIONS, offset, limit)
        );
    }

    public Optional<Link> create(RelationContainer rel, Integer offset, Integer limit) {
        return stationsLinkFactory.forCall(rel, r -> r.getStations(offset, limit));
    }

    public Optional<Link> forCreate() {
        return stationsLinkFactory.forCall(Rel.CREATE, r -> r.addStation(null));
    }
}
