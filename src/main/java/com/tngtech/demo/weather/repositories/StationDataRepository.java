package com.tngtech.demo.weather.repositories;

import com.tngtech.demo.weather.domain.measurement.AtmosphericData;
import com.tngtech.demo.weather.domain.measurement.DataPoint;
import com.tngtech.demo.weather.domain.measurement.DataPointType;
import com.tngtech.demo.weather.lib.TimestampFactory;
import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.HashMap;
import javaslang.collection.List;
import javaslang.collection.Map;
import javaslang.control.Option;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
class StationDataRepository {
    private static final Map<DataPointType, Predicate<DataPoint>> acceptanceRuleByType = HashMap.ofEntries(
            Tuple.of(DataPointType.WIND, (Predicate<DataPoint>) dataPoint -> dataPoint.mean >= 0.0),
            Tuple.of(DataPointType.TEMPERATURE, (Predicate<DataPoint>) dataPoint -> dataPoint.mean >= -50.0 && dataPoint.mean < 100.0),
            Tuple.of(DataPointType.HUMIDITY, (Predicate<DataPoint>) dataPoint -> dataPoint.mean >= 0.0 && dataPoint.mean < 100.0),
            Tuple.of(DataPointType.PRESSURE, (Predicate<DataPoint>) dataPoint -> dataPoint.mean >= 650.0 && dataPoint.mean < 800.0),
            Tuple.of(DataPointType.CLOUDCOVER, (Predicate<DataPoint>) dataPoint -> dataPoint.mean >= 0 && dataPoint.mean < 100.0),
            Tuple.of(DataPointType.PRECIPITATION, (Predicate<DataPoint>) dataPoint -> dataPoint.mean >= 0 && dataPoint.mean < 100.0)
    );

    private static final List<Tuple2<DataPointType, BiConsumer<AtmosphericData.AtmosphericDataBuilder, DataPoint>>> typesWithBuilderMethods = List.of(
            Tuple.of(DataPointType.WIND, (BiConsumer<AtmosphericData.AtmosphericDataBuilder, DataPoint>) AtmosphericData.AtmosphericDataBuilder::wind),
            Tuple.of(DataPointType.TEMPERATURE, (BiConsumer<AtmosphericData.AtmosphericDataBuilder, DataPoint>) AtmosphericData.AtmosphericDataBuilder::temperature),
            Tuple.of(DataPointType.HUMIDITY, (BiConsumer<AtmosphericData.AtmosphericDataBuilder, DataPoint>) AtmosphericData.AtmosphericDataBuilder::humidity),
            Tuple.of(DataPointType.PRESSURE, (BiConsumer<AtmosphericData.AtmosphericDataBuilder, DataPoint>) AtmosphericData.AtmosphericDataBuilder::pressure),
            Tuple.of(DataPointType.CLOUDCOVER, (BiConsumer<AtmosphericData.AtmosphericDataBuilder, DataPoint>) AtmosphericData.AtmosphericDataBuilder::cloudCover),
            Tuple.of(DataPointType.PRECIPITATION, (BiConsumer<AtmosphericData.AtmosphericDataBuilder, DataPoint>) AtmosphericData.AtmosphericDataBuilder::precipitation)
    );

    private final ConcurrentHashMap<DataPointType, DataPoint> dataPointByType = new ConcurrentHashMap<>();

    private long lastUpdateTime = 0L;

    private final TimestampFactory timestampFactory;

    @Inject
    public StationDataRepository(TimestampFactory timestampFactory) {
        this.timestampFactory = timestampFactory;
    }

    void update(DataPoint data) {
        final Boolean shouldAddData =
                acceptanceRuleByType
                        .get(data.type)
                        .map(rulePredicate -> rulePredicate.test(data))
                        .getOrElse(true);

        if (shouldAddData) {
            dataPointByType.put(data.type, data);
            lastUpdateTime = timestampFactory.getCurrentTimestamp();
        }
    }

    public AtmosphericData toData() {
        final AtmosphericData.AtmosphericDataBuilder builder = AtmosphericData.builder();

        typesWithBuilderMethods.forEach(dataTypeAndBuilderMethod -> {
            DataPointType dataType = dataTypeAndBuilderMethod._1();
            BiConsumer<AtmosphericData.AtmosphericDataBuilder, DataPoint> builderAdapter = dataTypeAndBuilderMethod._2();

            Option.of(dataPointByType
                    .get(dataType))
                    .forEach(data -> {
                        builderAdapter.accept(builder, data);
                    });
        });

        builder.lastUpdateTime(lastUpdateTime);

        return builder.build();
    }
}
