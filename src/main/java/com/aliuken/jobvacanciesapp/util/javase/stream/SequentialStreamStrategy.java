package com.aliuken.jobvacanciesapp.util.javase.stream;

import com.aliuken.jobvacanciesapp.util.javase.stream.superinterface.StreamStrategy;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.stream.Stream;

public class SequentialStreamStrategy implements StreamStrategy {
    @Override
    public <T> @NonNull Stream<T> build(@NonNull Collection<T> collection) {
        return collection.stream();
    }
}