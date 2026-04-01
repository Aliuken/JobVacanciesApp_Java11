package com.aliuken.jobvacanciesapp.util.javase.stream;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.dto.StreamUtilsPair;
import com.aliuken.jobvacanciesapp.util.javase.stream.superclass.StreamStrategy;
import com.aliuken.jobvacanciesapp.util.javase.stream.superinterface.StreamUtils;
import com.aliuken.jobvacanciesapp.util.spring.di.BeanFactoryUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Data
public class StreamUtilsImpl<T> implements StreamUtils<T> {
    private static final @NonNull Map<Class<?>, StreamUtilsPair<?>> CACHE = new ConcurrentHashMap<>();

    private final @NonNull Class<T> elementClass;
    private final @NonNull StreamStrategy streamStrategy;

    private StreamUtilsImpl(final @NonNull Class<T> elementClass, final @NonNull StreamStrategy streamStrategy) {
        this.elementClass = elementClass;
        this.streamStrategy = streamStrategy;
    }

    // ==========================================================
    // StreamUtils factory methods
    // ==========================================================

    public static <E> @NonNull StreamUtils<E> getInstance(final @NonNull Class<E> elementClass) {
        final ConfigPropertiesBean configPropertiesBean = BeanFactoryUtils.getBean(ConfigPropertiesBean.class);
        final boolean useParallelStreams = configPropertiesBean.isUseParallelStreams();

        final StreamUtils<E> streamUtils = StreamUtilsImpl.getInstance(elementClass, useParallelStreams);
        return streamUtils;
    }

    public static <E> @NonNull StreamUtils<E> getInstance(final @NonNull Class<E> elementClass, final boolean isParallel) {
        final StreamUtilsPair<?> streamUtilsPair = CACHE.computeIfAbsent(elementClass, elementClassAux -> StreamUtilsImpl.createStreamUtilsPair(elementClassAux));
        final StreamUtilsPair<E> finalStreamUtilsPair = GenericsUtils.cast(streamUtilsPair);

        final StreamUtils<E> streamUtils = finalStreamUtilsPair.get(isParallel);
        return streamUtils;
    }

    private static <E> @NonNull StreamUtilsPair<E> createStreamUtilsPair(final @NonNull Class<E> elementClass) {
        final StreamUtils<E> sequentialStreamUtils = new StreamUtilsImpl<>(elementClass, StreamStrategy.getInstance(false));
        final StreamUtils<E> parallelStreamUtils = new StreamUtilsImpl<>(elementClass, StreamStrategy.getInstance(true));

        StreamUtilsPair<E> streamUtilsPair = new StreamUtilsPair<>(sequentialStreamUtils, parallelStreamUtils);
        return streamUtilsPair;
    }

    // ==========================================================
    // Join arrays
    // ==========================================================

    @Override
    public T @NonNull [] joinArrays(final @NonNull IntFunction<T @NonNull []> arrayGenerator, final T[] array1, final T[] array2) {
        final Object[][] objectMatrix = new Object[][]{array1, array2};
        final T[][] arrays = GenericsUtils.cast(objectMatrix);

        final T[] elementArray = this.joinArrays(arrayGenerator, arrays);
        return elementArray;
    }

    @Override
    public T @NonNull [] joinArrays(final @NonNull IntFunction<T @NonNull []> arrayGenerator, final T[][] arrays) {
        if (arrays == null) {
            // "IntFunction<T[]> generator" es equivalente a la function "size -> new T[size]" o "T[]::new";
            final T[] emptyElementArray = arrayGenerator.apply(0);
            Objects.requireNonNull(emptyElementArray, "emptyElementArray cannot be null");
            return emptyElementArray;
        }

        final T[] elementArray = Arrays.stream(arrays)
            .filter(elementArrayAux -> elementArrayAux != null)
            .flatMap(elementArrayAux -> buildStream(elementArrayAux))
            .toArray(arrayGenerator);
        return elementArray;
    }

    // ==========================================================
    // Join lists
    // ==========================================================

    @Override
    public @NonNull List<T> joinLists(final List<T> list1, final List<T> list2) {
        final Object[] objectArray = new Object[]{list1, list2};
        final List<T>[] lists = GenericsUtils.cast(objectArray);

        final List<T> elementList = this.joinLists(lists);
        return elementList;
    }

    @Override
    public @NonNull List<T> joinLists(final List<T>[] lists) {
        if (lists == null) {
            return new ArrayList<>();
        }

        final List<T> elementList = Arrays.stream(lists)
            .filter(elementListAux -> elementListAux != null)
            .flatMap(elementListAux -> buildStream(elementListAux))
            .collect(Collectors.toCollection(ArrayList::new));
        return elementList;
    }

    // ==========================================================
    // Join sets
    // ==========================================================

    @Override
    public @NonNull Set<T> joinSets(final Set<T> set1, final Set<T> set2) {
        final Object[] objectArray = new Object[]{set1, set2};
        final Set<T>[] sets = GenericsUtils.cast(objectArray);

        final Set<T> elementSet = this.joinSets(sets);
        return elementSet;
    }

    @Override
    public @NonNull Set<T> joinSets(final Set<T>[] sets) {
        if (sets == null) {
            return new LinkedHashSet<>();
        }

        final Set<T> elementSet = Arrays.stream(sets)
            .filter(elementSetAux -> elementSetAux != null)
            .flatMap(elementSetAux -> buildStream(elementSetAux))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return elementSet;
    }

    // ==========================================================
    // Conversions
    // ==========================================================

    @Override
    public <U> U @NonNull [] convertArray(final T @NonNull [] initialArray, final @NonNull Function<T, U> conversionFunction,
                                         final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass, final @NonNull IntFunction<U @NonNull []> arrayGenerator) {

        final U[] finalArray = buildStream(initialArray)
            .map(conversionFunction)
            .toArray(arrayGenerator);
        return finalArray;
    }

    @Override
    public <U> @NonNull List<U> convertList(final @NonNull List<T> initialList, final @NonNull Function<T, U> conversionFunction,
                                            final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass) {

        final List<U> finalList = buildStream(initialList)
            .map(conversionFunction)
            .collect(Collectors.toCollection(ArrayList::new));
        return finalList;
    }

    @Override
    public <U> @NonNull Set<U> convertSet(final @NonNull Set<T> initialSet, final @NonNull Function<T, U> conversionFunction,
                                          final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass) {

        final Set<U>  finalSet = buildStream(initialSet)
            .map(conversionFunction)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return finalSet;
    }

    // ==========================================================
    // First element in array
    // ==========================================================

    @Override
    public @NonNull T getFirstElementFilteredByCondition(final T @NonNull [] initialElements,
                                                         final @NonNull Predicate<@NonNull T> elementCondition) {

        final T finalElement = buildStream(initialElements)
                .filter(element -> element != null)
                .filter(elementCondition)
                .findFirst()
                .orElseThrow();
        return finalElement;
    }

    @Override
    public @NonNull T getFirstElementFilteredByCondition(final T @NonNull [] initialElements,
                                                         final @NonNull Predicate<@NonNull T> elementCondition,
                                                         final @NonNull T fallbackElement) {

        final T finalElement = buildStream(initialElements)
                .filter(element -> element != null)
                .filter(elementCondition)
                .findFirst()
                .orElse(fallbackElement);
        return finalElement;
    }

    @Override
    public @NonNull T getFirstElementFilteredByConditionLazily(final @NonNull Supplier<T> @NonNull [] initialElementSuppliers,
                                                               final @NonNull Predicate<@NonNull T> elementCondition) {

        final T finalElement = buildStreamWithSuppliers(initialElementSuppliers)
                .map(elementSupplier -> elementSupplier.get())
                .filter(element -> element != null)
                .filter(elementCondition)
                .findFirst()
                .orElseThrow();
        return finalElement;
    }

    @Override
    public @NonNull T getFirstElementFilteredByConditionLazily(final @NonNull Supplier<T> @NonNull [] initialElementSuppliers,
                                                               final @NonNull Predicate<@NonNull T> elementCondition,
                                                               final @NonNull Supplier<@NonNull T> fallbackElementSupplier) {

        final T finalElement = buildStreamWithSuppliers(initialElementSuppliers)
                .map(elementSupplier -> elementSupplier.get())
                .filter(element -> element != null)
                .filter(elementCondition)
                .findFirst()
                .orElseGet(fallbackElementSupplier);
        return finalElement;
    }

    // ==========================================================
    // First element in collection
    // ==========================================================

    @Override
    public @NonNull T getFirstElementFilteredByCondition(final @NonNull Collection<T> initialElements,
                                                         final @NonNull Predicate<@NonNull T> elementCondition) {

        final T finalElement = buildStream(initialElements)
            .filter(element -> element != null)
            .filter(elementCondition)
            .findFirst()
            .orElseThrow();
        return finalElement;
    }

    @Override
    public @NonNull T getFirstElementFilteredByCondition(final @NonNull Collection<T> initialElements,
                                                         final @NonNull Predicate<@NonNull T> elementCondition,
                                                         final @NonNull T fallbackElement) {

        final T finalElement = buildStream(initialElements)
            .filter(element -> element != null)
            .filter(elementCondition)
            .findFirst()
            .orElse(fallbackElement);
        return finalElement;
    }

    @Override
    public @NonNull T getFirstElementFilteredByConditionLazily(final @NonNull Collection<@NonNull Supplier<T>> initialElementSuppliers,
                                                               final @NonNull Predicate<@NonNull T> elementCondition) {

        final T finalElement = buildStreamWithSuppliers(initialElementSuppliers)
            .map(elementSupplier -> elementSupplier.get())
            .filter(element -> element != null)
            .filter(elementCondition)
            .findFirst()
            .orElseThrow();
        return finalElement;
    }

    @Override
    public @NonNull T getFirstElementFilteredByConditionLazily(final @NonNull Collection<@NonNull Supplier<T>> initialElementSuppliers,
                                                               final @NonNull Predicate<@NonNull T> elementCondition,
                                                               final @NonNull Supplier<@NonNull T> fallbackElementSupplier) {

        final T finalElement = buildStreamWithSuppliers(initialElementSuppliers)
            .map(elementSupplier -> elementSupplier.get())
            .filter(element -> element != null)
            .filter(elementCondition)
            .findFirst()
            .orElseGet(fallbackElementSupplier);
        return finalElement;
    }

    // ==========================================================
    // Stream factory methods
    // ==========================================================

    @Override
    public @NonNull Stream<T> ofNullableArray(final T[] array) {
        final Stream<T> elementStream;
        if(array != null) {
            elementStream = this.buildStream(array);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }

    @Override
    public @NonNull Stream<T> ofNullableCollection(final Collection<T> collection) {
        final Stream<T> elementStream;
        if(collection != null) {
            elementStream = this.buildStream(collection);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }

    // ==========================================================
    // Reusable internal methods
    // ==========================================================

    private @NonNull Stream<T> buildStream(final T @NonNull [] array) {
        final List<T> list = Arrays.asList(array);
        final Stream<T> elementStream = streamStrategy.build(list);
        return elementStream;
    }

    private @NonNull Stream<@NonNull Supplier<T>> buildStreamWithSuppliers(final @NonNull Supplier<T> @NonNull [] array) {
        final List<@NonNull Supplier<T>> list = Arrays.asList(array);
        final Stream<@NonNull Supplier<T>> elementSupplierStream = streamStrategy.buildWithSuppliers(list);
        return elementSupplierStream;
    }

    private @NonNull Stream<T> buildStream(final @NonNull Collection<T> collection) {
        final Stream<T> elementStream = streamStrategy.build(collection);
        return elementStream;
    }

    private @NonNull Stream<@NonNull Supplier<T>> buildStreamWithSuppliers(final @NonNull Collection<@NonNull Supplier<T>> collection) {
        final Stream<@NonNull Supplier<T>> elementSupplierStream = streamStrategy.buildWithSuppliers(collection);
        return elementSupplierStream;
    }
}
