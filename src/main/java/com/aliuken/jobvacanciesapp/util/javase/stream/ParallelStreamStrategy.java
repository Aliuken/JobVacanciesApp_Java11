package com.aliuken.jobvacanciesapp.util.javase.stream;

import com.aliuken.jobvacanciesapp.util.javase.stream.superclass.StreamStrategy;
import com.aliuken.jobvacanciesapp.util.javase.time.DateTimeUtils;
import org.jspecify.annotations.NonNull;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class ParallelStreamStrategy extends StreamStrategy {
    private static final @NonNull ParallelStreamStrategy SINGLETON_INSTANCE = new ParallelStreamStrategy();

    private ParallelStreamStrategy(){}

    public static @NonNull ParallelStreamStrategy getInstance() {
        return SINGLETON_INSTANCE;
    }

    @Override
    public <T> @NonNull Stream<T> build(@NonNull Collection<T> collection) {
        final Stream<T> stream = collection.parallelStream();
        return stream;
    }

    @Override
    public <T> @NonNull Stream<@NonNull Supplier<T>> buildWithSuppliers(@NonNull Collection<@NonNull Supplier<T>> collection) {
        Stream<Supplier<T>> stream = collection.parallelStream();
        return stream;
    }
}