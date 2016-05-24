package com.tngtech.demo.weather.lib.schemagen;

import com.mercateo.common.rest.schemagen.JsonHyperSchema;
import com.mercateo.common.rest.schemagen.JsonHyperSchemaCreator;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchema;
import com.mercateo.common.rest.schemagen.types.ObjectWithSchemaCreator;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.ws.rs.core.Link;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.tngtech.demo.weather.lib.OptionalUtils.collect;

@Component
public class HyperSchemaCreator {

    @Inject
    private ObjectWithSchemaCreator objectWithSchemaCreator;

    @Inject
    private JsonHyperSchemaCreator jsonHyperSchemaCreator;

    @SafeVarargs
    public final <T> ObjectWithSchema<T> create(T object, Optional<Link>... links) {
        JsonHyperSchema hyperSchema = jsonHyperSchemaCreator.from(collect(links));
        return objectWithSchemaCreator.create(object, hyperSchema);
    }

    @SafeVarargs
    public final <T> ObjectWithSchema<T> create(T object, List<Link>... linkArray) {
        ArrayList<Link> links = new ArrayList<>();
        Arrays.stream(linkArray).forEach(links::addAll);

        JsonHyperSchema hyperSchema = jsonHyperSchemaCreator.from(links);
        return objectWithSchemaCreator.create(object, hyperSchema);
    }
}
