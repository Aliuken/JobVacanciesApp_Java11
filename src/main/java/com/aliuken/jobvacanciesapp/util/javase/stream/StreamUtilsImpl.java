package com.aliuken.jobvacanciesapp.util.javase.stream;

import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.superinterface.StreamStrategy;
import com.aliuken.jobvacanciesapp.util.javase.stream.superinterface.StreamUtils;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class StreamUtilsImpl<T> implements StreamUtils<T> {

    private final @NonNull StreamStrategy streamStrategy;

    private StreamUtilsImpl(final @NonNull StreamStrategy streamStrategy) {
        this.streamStrategy = streamStrategy;
    }

    // ==========================================================
    // StreamUtils factory methods
    // ==========================================================

    private static final @NonNull StreamUtils<?> SEQUENTIAL = new StreamUtilsImpl<>(new SequentialStreamStrategy());
    private static final @NonNull StreamUtils<?> PARALLEL = new StreamUtilsImpl<>(new ParallelStreamStrategy());

    public static @NonNull StreamUtils<?> sequential() {
        return SEQUENTIAL;
    }

    public static @NonNull StreamUtils<?> parallel() {
        return PARALLEL;
    }

    public static @NonNull StreamUtils<?> getInstance(final boolean isParallel) {
        final StreamUtils<?> streamUtils;
        if(isParallel) {
            streamUtils = PARALLEL;
        } else {
            streamUtils = SEQUENTIAL;
        }
        return streamUtils;
    }

    // ==========================================================
    // Join arrays
    // ==========================================================

    @Override
    public @NonNull T[] joinArrays(@NonNull IntFunction<T @NonNull []> arrayGenerator, T[] array1, T[] array2) {
        final Object[][] objectMatrix = new Object[][]{array1, array2};
        final T[][] arrays = GenericsUtils.cast(objectMatrix);

        final T[] elementArray = this.joinArrays(arrayGenerator, arrays);
        return elementArray;
    }

    @Override
    public @NonNull T[] joinArrays(@NonNull IntFunction<T @NonNull []> arrayGenerator, T[][] arrays) {
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
    public @NonNull List<T> joinLists(List<T> list1, List<T> list2) {
        final Object[] objectArray = new Object[]{list1, list2};
        final List<T>[] lists = GenericsUtils.cast(objectArray);

        final List<T> elementList = this.joinLists(lists);
        return elementList;
    }

    @Override
    public @NonNull List<T> joinLists(List<T>[] lists) {
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
    public @NonNull Set<T> joinSets(Set<T> set1, Set<T> set2) {
        final Object[] objectArray = new Object[]{set1, set2};
        final Set<T>[] sets = GenericsUtils.cast(objectArray);

        final Set<T> elementSet = this.joinSets(sets);
        return elementSet;
    }

    @Override
    public @NonNull Set<T> joinSets(Set<T>[] sets) {
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
    public <U> @NonNull U[] convertArray(T @NonNull [] initialArray, @NonNull Function<T, U> conversionFunction,
            @NonNull Class<T> inputClass, @NonNull Class<U> outputClass, @NonNull IntFunction<U @NonNull []> arrayGenerator) {

        final U[] finalArray = buildStream(initialArray)
            .map(conversionFunction)
            .toArray(arrayGenerator);
        return finalArray;
    }

    @Override
    public <U> @NonNull List<U> convertList(@NonNull List<T> initialList, @NonNull Function<T, U> conversionFunction,
            @NonNull Class<T> inputClass, @NonNull Class<U> outputClass) {

        final List<U> finalList = buildStream(initialList)
            .map(conversionFunction)
            .collect(Collectors.toCollection(ArrayList::new));
        return finalList;
    }

    @Override
    public <U> @NonNull Set<U> convertSet(@NonNull Set<T> initialSet, @NonNull Function<T, U> conversionFunction,
            @NonNull Class<T> inputClass, @NonNull Class<U> outputClass) {

        final Set<U>  finalSet = buildStream(initialSet)
            .map(conversionFunction)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return finalSet;
    }

    // ==========================================================
    // First element
    // ==========================================================

    @Override
    public @NonNull T getFirstElementFilteredByCondition(@NonNull Collection<T> initialElements,
             @NonNull Predicate<@NonNull T> elementCondition) {

        final T finalElement = buildStream(initialElements)
            .filter(element -> element != null)
            .filter(elementCondition)
            .findFirst()
            .orElseThrow();
        return finalElement;
    }

    @Override
    public @NonNull T getFirstElementFilteredByCondition(@NonNull Collection<T> initialElements,
            @NonNull Predicate<@NonNull T> elementCondition, @NonNull T fallbackElement) {

        final T finalElement = buildStream(initialElements)
            .filter(element -> element != null)
            .filter(elementCondition)
            .findFirst()
            .orElse(fallbackElement);
        return finalElement;
    }

    @Override
    public @NonNull T getFirstElementFilteredByConditionLazily(@NonNull Collection<@NonNull Supplier<T>> initialElementSuppliers,
            @NonNull Predicate<@NonNull T> elementCondition) {

        final T finalElement = buildStream(initialElementSuppliers)
                .map(elementSupplier -> elementSupplier.get())
                .filter(element -> element != null)
                .filter(elementCondition)
                .findFirst()
                .orElseThrow();
        return finalElement;
    }

    @Override
    public @NonNull T getFirstElementFilteredByConditionLazily(@NonNull Collection<@NonNull Supplier<T>> initialElementSuppliers,
            @NonNull Predicate<@NonNull T> elementCondition, @NonNull Supplier<@NonNull T> fallbackElementSupplier) {

        final T finalElement = buildStream(initialElementSuppliers)
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
    public @NonNull Stream<T> ofNullableArray(T[] array) {
        final Stream<T> elementStream;
        if(array != null) {
            elementStream = this.buildStream(array);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }

    @Override
    public @NonNull Stream<T> ofNullableCollection(Collection<T> collection) {
        final Stream<T> elementStream;
        if(collection != null) {
            elementStream = this.buildStream(collection);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }

    @Override
    public <K, V> @NonNull Stream<Map.Entry<K, V>> ofNullableMap(Map<K, V> map) {
        final Stream<Map.Entry<K, V>> elementStream;
        if(map != null) {
            final Set<Map.Entry<K, V>> mapEntrySet = map.entrySet();
            elementStream = this.buildStream(mapEntrySet);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }

    @Override
    public <E extends Enum<E>> @NonNull Stream<E> ofEnum(Class<E> enumClass) {
        final Stream<E> elementStream;
        if(enumClass != null) {
            final EnumSet<E> enumSet = EnumSet.allOf(enumClass);
            elementStream = this.buildStream(enumSet);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }

    // ==========================================================
    // Reusable internal methods
    // ==========================================================

    private <E> @NonNull Stream<E> buildStream(E @NonNull [] array) {
        final List<E> list = Arrays.asList(array);
        final Stream<E> elementStream = streamStrategy.build(list);
        return elementStream;
    }

    private <E> @NonNull Stream<E> buildStream(final @NonNull Collection<E> collection) {
        final Stream<E> elementStream = streamStrategy.build(collection);
        return elementStream;
    }
}
