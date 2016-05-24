package com.tngtech.demo.weather.resources.stations;

import com.mercateo.common.rest.schemagen.link.LinkFactory;
import com.mercateo.common.rest.schemagen.link.relation.Rel;
import com.tngtech.demo.weather.resources.WeatherRel;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.core.Link;
import java.util.List;

import static com.tngtech.demo.weather.lib.OptionalUtils.collect;

@Component
public class StationsLinkCreator {

    @Inject
    @Named("stationsLinkFactory")
    private LinkFactory<StationsResource> stationsLinkFactory;

    public List<Link> create() {
            return collect(
                    stationsLinkFactory.forCall(WeatherRel.STATIONS, r -> r.getStations(null, null))
            );
    }
}
