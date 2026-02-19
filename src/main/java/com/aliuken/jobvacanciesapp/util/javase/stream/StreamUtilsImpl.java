package com.aliuken.jobvacanciesapp.util.javase.stream;

import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.superclass.StreamUtils;
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

public final class StreamUtilsImpl implements StreamUtils {

    private final @NonNull StreamStrategy streamStrategy;

    private StreamUtilsImpl(final @NonNull StreamStrategy streamStrategy) {
        this.streamStrategy = streamStrategy;
    }

    // ==========================================================
    // StreamUtils factory methods
    // ==========================================================

    private static final StreamUtils SEQUENTIAL = new StreamUtilsImpl(Collection::stream);
    private static final StreamUtils PARALLEL = new StreamUtilsImpl(Collection::parallelStream);

    public static @NonNull StreamUtils sequential() {
        return SEQUENTIAL;
    }

    public static @NonNull StreamUtils parallel() {
        return PARALLEL;
    }

    public static @NonNull StreamUtils getInstance(final boolean isParallel) {
        final StreamUtils streamUtils;
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
    public <T> @NonNull T[] joinArrays(@NonNull IntFunction<T[]> generator, T[] array1, T[] array2) {
        final Object[][] objectMatrix = new Object[][]{array1, array2};
        final T[][] arrays = GenericsUtils.cast(objectMatrix);

        final T[] elementArray = this.joinArrays(generator, arrays);
        return elementArray;
    }

    @Override
    public <T> @NonNull T[] joinArrays(@NonNull IntFunction<T[]> generator, T[][] arrays) {
        if (arrays == null) {
            // "IntFunction<T[]> generator" es equivalente a la function "size -> new T[size]";
            final T[] emptyElementArray = generator.apply(0);
            Objects.requireNonNull(emptyElementArray, "emptyElementArray cannot be null");
            return emptyElementArray;
        }

        final T[] elementArray = Arrays.stream(arrays)
            .filter(Objects::nonNull)
            .flatMap(elementArrayAux -> buildStream(elementArrayAux))
            .toArray(generator);
        Objects.requireNonNull(elementArray, "elementArray cannot be null");
        return elementArray;
    }

    // ==========================================================
    // Join lists
    // ==========================================================

    @Override
    public <T> @NonNull List<T> joinLists(List<T> list1, List<T> list2) {
        final Object[] objectArray = new Object[]{list1, list2};
        final List<T>[] lists = GenericsUtils.cast(objectArray);

        final List<T> elementList = this.joinLists(lists);
        return elementList;
    }

    @Override
    public <T> @NonNull List<T> joinLists(List<T>[] lists) {
        if (lists == null) {
            return new ArrayList<>();
        }

        final List<T> elementList = Arrays.stream(lists)
            .filter(Objects::nonNull)
            .flatMap(elementListAux -> buildStream(elementListAux))
            .collect(Collectors.toCollection(ArrayList::new));
        return elementList;
    }

    // ==========================================================
    // Join sets
    // ==========================================================

    @Override
    public <T> @NonNull Set<T> joinSets(Set<T> set1, Set<T> set2) {
        final Object[] objectArray = new Object[]{set1, set2};
        final Set<T>[] sets = GenericsUtils.cast(objectArray);

        final Set<T> elementSet = this.joinSets(sets);
        return elementSet;
    }

    @Override
    public <T> @NonNull Set<T> joinSets(Set<T>[] sets) {
        if (sets == null) {
            return new LinkedHashSet<>();
        }

        final Set<T> elementSet = Arrays.stream(sets)
            .filter(Objects::nonNull)
            .flatMap(elementSetAux -> buildStream(elementSetAux))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return elementSet;
    }

    // ==========================================================
    // Conversions
    // ==========================================================

    @Override
    public <T, U> @NonNull U[] convertArray(T[] initialArray, @NonNull Function<T, U> conversionFunction,
            @NonNull Class<T> inputClass, @NonNull Class<U> outputClass, @NonNull IntFunction<U[]> arrayGenerator) {

        final U[] finalArray = buildStream(initialArray)
            .map(conversionFunction)
            .toArray(arrayGenerator);

        Objects.requireNonNull(finalArray, "finalArray cannot be null");
        return finalArray;
    }

    @Override
    public <T, U> @NonNull List<U> convertList(List<T> initialList, @NonNull Function<T, U> conversionFunction,
            @NonNull Class<T> inputClass, @NonNull Class<U> outputClass) {

        final List<U> finalList = buildStream(initialList)
            .map(conversionFunction)
            .collect(Collectors.toCollection(ArrayList::new));
        return finalList;
    }

    @Override
    public <T, U> @NonNull Set<U> convertSet(Set<T> initialSet, @NonNull Function<T, U> conversionFunction,
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
    public <T> @NonNull T getFirstElementFilteredByCondition(@NonNull Collection<T> initialElements,
             @NonNull Predicate<T> elementCondition) {

        final T finalElement = buildStream(initialElements)
                .filter(elementCondition)
                .findFirst()
                .orElseThrow();
        return finalElement;
    }

    @Override
    public <T> @NonNull T getFirstElementFilteredByCondition(@NonNull Collection<T> initialElements,
            @NonNull Predicate<T> elementCondition, @NonNull T fallbackElement) {

        final T finalElement = buildStream(initialElements)
            .filter(elementCondition)
            .findFirst()
            .orElse(fallbackElement);
        return finalElement;
    }

    @Override
    public <T> @NonNull T getFirstElementFilteredByConditionLazily(@NonNull Collection<Supplier<T>> initialElementSuppliers,
            @NonNull Predicate<T> elementCondition) {

        final T finalElement = buildStream(initialElementSuppliers)
                .map(elementSupplier -> elementSupplier.get())
                .filter(elementCondition)
                .findFirst()
                .orElseThrow();
        return finalElement;
    }

    @Override
    public <T> @NonNull T getFirstElementFilteredByConditionLazily(@NonNull Collection<Supplier<T>> initialElementSuppliers,
            @NonNull Predicate<T> elementCondition, @NonNull Supplier<@NonNull T> fallbackElementSupplier) {

        final T finalElement = buildStream(initialElementSuppliers)
            .map(elementSupplier -> elementSupplier.get())
            .filter(elementCondition)
            .findFirst()
            .orElseGet(fallbackElementSupplier);
        return finalElement;
    }

    // ==========================================================
    // Stream factory methods
    // ==========================================================

    @Override
    public <T> @NonNull Stream<T> ofNullableArray(T[] array) {
        final Stream<T> elementStream = this.buildStream(array);
        return elementStream;
    }

    @Override
    public <T> @NonNull Stream<T> ofNullableCollection(Collection<T> collection) {
        final Stream<T> elementStream = this.buildStream(collection);
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
    public <T extends Enum<T>> @NonNull Stream<T> ofEnum(Class<T> enumClass) {
        final Stream<T> elementStream;
        if(enumClass != null) {
            final EnumSet<T> enumSet = EnumSet.allOf(enumClass);
            elementStream = this.buildStream(enumSet);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }

    // ==========================================================
    // Reusable internal methods
    // ==========================================================

    private <T> @NonNull Stream<T> buildStream(T[] array) {
        final Stream<T> elementStream;
        if(array != null) {
            final List<T> list = Arrays.asList(array);
            elementStream = streamStrategy.build(list);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }

    private <T> @NonNull Stream<T> buildStream(final Collection<T> collection) {
        final Stream<T> elementStream;
        if(collection != null) {
            elementStream = streamStrategy.build(collection);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }
}
