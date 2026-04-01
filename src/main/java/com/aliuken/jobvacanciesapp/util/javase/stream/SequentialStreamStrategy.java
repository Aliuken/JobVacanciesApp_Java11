package com.aliuken.jobvacanciesapp.util.javase.stream;

import com.aliuken.jobvacanciesapp.util.javase.stream.superclass.StreamStrategy;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class SequentialStreamStrategy extends StreamStrategy {
    private static final @NonNull SequentialStreamStrategy SINGLETON_INSTANCE = new SequentialStreamStrategy();

    private SequentialStreamStrategy(){}

    public static @NonNull SequentialStreamStrategy getInstance() {
        return SINGLETON_INSTANCE;
    }

    @Override
    public <T> @NonNull Stream<T> build(@NonNull Collection<T> collection) {
        final Stream<T> stream = collection.stream();
        return stream;
    }

    @Override
    public <T> @NonNull Stream<@NonNull Supplier<T>> buildWithSuppliers(@NonNull Collection<@NonNull Supplier<T>> collection) {
        Stream<Supplier<T>> stream = collection.stream();
        return stream;
    }
}