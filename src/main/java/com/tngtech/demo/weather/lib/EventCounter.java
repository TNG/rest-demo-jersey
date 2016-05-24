package com.tngtech.demo.weather.lib;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.HashSet;
import javaslang.collection.Set;
import javaslang.collection.Stream;
import javaslang.control.Option;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class EventCounter<T> {
    private ConcurrentHashMap<T, AtomicInteger> countByEvent = new ConcurrentHashMap<>();

    public int increment(T event) {
        return countByEvent
                .computeIfAbsent(event, k -> new AtomicInteger())
                .incrementAndGet();
    }

    public int countOf(T event) {
        return Option.of(countByEvent.get(event))
                .map(AtomicInteger::get)
                .getOrElse(0);
    }

    public double fractionOf(T event) {
        final int totalCount = totalCount();
        return totalCount != 0
                ? (double) countOf(event) / totalCount
                : 0.0;
    }

    public int totalCount() {
        return Stream.ofAll(countByEvent
                .values())
                .map(AtomicInteger::get)
                .fold(0, (left, right) -> left + right);
    }

    public Set<T> events() {
        return HashSet.ofAll(countByEvent.keySet());
    }

    public Stream<Tuple2<T, AtomicInteger>> stream() {
        return Stream.ofAll(countByEvent.entrySet())
                .map(entry -> Tuple.of(entry.getKey(), entry.getValue()));
    }
}
