package com.aliuken.jobvacanciesapp.util.javase.stream.superinterface;

import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.stream.Stream;

@FunctionalInterface
public interface StreamStrategy {
    <T> @NonNull Stream<T> build(@NonNull Collection<T> collection);
}