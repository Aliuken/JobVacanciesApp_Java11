package com.aliuken.jobvacanciesapp.util.javase.stream.superclass;

import com.aliuken.jobvacanciesapp.util.javase.stream.ParallelStreamStrategy;
import com.aliuken.jobvacanciesapp.util.javase.stream.SequentialStreamStrategy;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Stream;

public abstract class StreamStrategy {
    public abstract <T> @NonNull Stream<T> build(@NonNull Collection<T> collection);
    public abstract <T> @NonNull Stream<@NonNull Supplier<T>> buildWithSuppliers(@NonNull Collection<@NonNull Supplier<T>> collection);

    public static @NonNull StreamStrategy getInstance(boolean isParallel) {
        final StreamStrategy streamStrategy;
        if(isParallel) {
            streamStrategy = ParallelStreamStrategy.getInstance();
        } else {
            streamStrategy = SequentialStreamStrategy.getInstance();
        }
        return streamStrategy;
    }
}