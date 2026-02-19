package com.aliuken.jobvacanciesapp.util.javase.stream;

import java.util.Collection;
import java.util.stream.Stream;

@FunctionalInterface
public interface StreamStrategy {
    <T> Stream<T> build(Collection<T> collection);
}