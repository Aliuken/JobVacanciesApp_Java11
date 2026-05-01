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
public class StreamUtilsImpl<E> implements StreamUtils<E> {
    private static final @NonNull Map<Class<?>, StreamUtilsPair<?>> CACHE = new ConcurrentHashMap<>();

    private final @NonNull Class<E> elementClass;
    private final @NonNull StreamStrategy streamStrategy;

    private StreamUtilsImpl(final @NonNull Class<E> elementClass, final @NonNull StreamStrategy streamStrategy) {
        this.elementClass = elementClass;
        this.streamStrategy = streamStrategy;
    }

    // ==========================================================
    // StreamUtils factory methods
    // ==========================================================

    public static <E2> @NonNull StreamUtils<E2> getInstance(final @NonNull Class<E2> elementClass) {
        final ConfigPropertiesBean configPropertiesBean = BeanFactoryUtils.getBean(ConfigPropertiesBean.class);
        final boolean useParallelStreams = configPropertiesBean.isUseParallelStreams();

        final StreamUtils<E2> streamUtils = StreamUtilsImpl.getInstance(elementClass, useParallelStreams);
        return streamUtils;
    }

    public static <E2> @NonNull StreamUtils<E2> getInstance(final @NonNull Class<E2> elementClass, final boolean isParallel) {
        final StreamUtilsPair<?> streamUtilsPair = CACHE.computeIfAbsent(elementClass, elementClassAux -> StreamUtilsImpl.createStreamUtilsPair(elementClassAux));
        final StreamUtilsPair<E2> finalStreamUtilsPair = GenericsUtils.cast(streamUtilsPair);

        final StreamUtils<E2> streamUtils = finalStreamUtilsPair.get(isParallel);
        return streamUtils;
    }

    private static <E2> @NonNull StreamUtilsPair<E2> createStreamUtilsPair(final @NonNull Class<E2> elementClass) {
        final StreamUtils<E2> sequentialStreamUtils = new StreamUtilsImpl<>(elementClass, StreamStrategy.getInstance(false));
        final StreamUtils<E2> parallelStreamUtils = new StreamUtilsImpl<>(elementClass, StreamStrategy.getInstance(true));

        StreamUtilsPair<E2> streamUtilsPair = new StreamUtilsPair<>(sequentialStreamUtils, parallelStreamUtils);
        return streamUtilsPair;
    }

    // ==========================================================
    // Join arrays
    // ==========================================================

    @Override
    public E @NonNull [] joinArrays(final @NonNull IntFunction<E @NonNull []> arrayGenerator, final E[] array1, final E[] array2) {
        final Object[][] objectMatrix = new Object[][]{array1, array2};
        final E[][] arrays = GenericsUtils.cast(objectMatrix);

        final E[] elementArray = this.joinArrays(arrayGenerator, arrays);
        return elementArray;
    }

    @Override
    public E @NonNull [] joinArrays(final @NonNull IntFunction<E @NonNull []> arrayGenerator, final E[][] arrays) {
        if (arrays == null) {
            // "IntFunction<T[]> generator" es equivalente a la function "size -> new T[size]" o "T[]::new";
            final E[] emptyElementArray = arrayGenerator.apply(0);
            Objects.requireNonNull(emptyElementArray, "emptyElementArray cannot be null");
            return emptyElementArray;
        }

        final E[] elementArray = Arrays.stream(arrays)
            .filter(elementArrayAux -> elementArrayAux != null)
            .flatMap(elementArrayAux -> buildStream(elementArrayAux))
            .toArray(arrayGenerator);
        return elementArray;
    }

    // ==========================================================
    // Join lists
    // ==========================================================

    @Override
    public @NonNull List<E> joinLists(final List<E> list1, final List<E> list2) {
        final Object[] objectArray = new Object[]{list1, list2};
        final List<E>[] lists = GenericsUtils.cast(objectArray);

        final List<E> elementList = this.joinLists(lists);
        return elementList;
    }

    @Override
    public @NonNull List<E> joinLists(final List<E>[] lists) {
        if (lists == null) {
            return new ArrayList<>();
        }

        final List<E> elementList = Arrays.stream(lists)
            .filter(elementListAux -> elementListAux != null)
            .flatMap(elementListAux -> buildStream(elementListAux))
            .collect(Collectors.toCollection(ArrayList::new));
        return elementList;
    }

    // ==========================================================
    // Join sets
    // ==========================================================

    @Override
    public @NonNull Set<E> joinSets(final Set<E> set1, final Set<E> set2) {
        final Object[] objectArray = new Object[]{set1, set2};
        final Set<E>[] sets = GenericsUtils.cast(objectArray);

        final Set<E> elementSet = this.joinSets(sets);
        return elementSet;
    }

    @Override
    public @NonNull Set<E> joinSets(final Set<E>[] sets) {
        if (sets == null) {
            return new LinkedHashSet<>();
        }

        final Set<E> elementSet = Arrays.stream(sets)
            .filter(elementSetAux -> elementSetAux != null)
            .flatMap(elementSetAux -> buildStream(elementSetAux))
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return elementSet;
    }

    // ==========================================================
    // Conversions
    // ==========================================================

    @Override
    public <E2> E2 @NonNull [] convertArray(final E @NonNull [] initialArray, final @NonNull Function<E, E2> conversionFunction,
                                            final @NonNull Class<E> inputClass, final @NonNull Class<E2> outputClass, final @NonNull IntFunction<E2 @NonNull []> arrayGenerator) {

        final E2[] finalArray = buildStream(initialArray)
            .map(conversionFunction)
            .toArray(arrayGenerator);
        return finalArray;
    }

    @Override
    public <E2> @NonNull List<E2> convertList(final @NonNull List<E> initialList, final @NonNull Function<E, E2> conversionFunction,
                                              final @NonNull Class<E> inputClass, final @NonNull Class<E2> outputClass) {

        final List<E2> finalList = buildStream(initialList)
            .map(conversionFunction)
            .collect(Collectors.toCollection(ArrayList::new));
        return finalList;
    }

    @Override
    public <E2> @NonNull Set<E2> convertSet(final @NonNull Set<E> initialSet, final @NonNull Function<E, E2> conversionFunction,
                                            final @NonNull Class<E> inputClass, final @NonNull Class<E2> outputClass) {

        final Set<E2>  finalSet = buildStream(initialSet)
            .map(conversionFunction)
            .collect(Collectors.toCollection(LinkedHashSet::new));
        return finalSet;
    }

    // ==========================================================
    // First element in array
    // ==========================================================

    @Override
    public @NonNull E getFirstElementFilteredByCondition(final E @NonNull [] initialElements,
                                                         final @NonNull Predicate<@NonNull E> elementCondition) {

        final E finalElement = buildStream(initialElements)
                .filter(element -> element != null)
                .filter(elementCondition)
                .findFirst()
                .orElseThrow();
        return finalElement;
    }

    @Override
    public @NonNull E getFirstElementFilteredByCondition(final E @NonNull [] initialElements,
                                                         final @NonNull Predicate<@NonNull E> elementCondition,
                                                         final @NonNull E fallbackElement) {

        final E finalElement = buildStream(initialElements)
                .filter(element -> element != null)
                .filter(elementCondition)
                .findFirst()
                .orElse(fallbackElement);
        return finalElement;
    }

    @Override
    public @NonNull E getFirstElementFilteredByConditionLazily(final @NonNull Supplier<E> @NonNull [] initialElementSuppliers,
                                                               final @NonNull Predicate<@NonNull E> elementCondition) {

        final E finalElement = buildStreamWithSuppliers(initialElementSuppliers)
                .map(elementSupplier -> elementSupplier.get())
                .filter(element -> element != null)
                .filter(elementCondition)
                .findFirst()
                .orElseThrow();
        return finalElement;
    }

    @Override
    public @NonNull E getFirstElementFilteredByConditionLazily(final @NonNull Supplier<E> @NonNull [] initialElementSuppliers,
                                                               final @NonNull Predicate<@NonNull E> elementCondition,
                                                               final @NonNull Supplier<@NonNull E> fallbackElementSupplier) {

        final E finalElement = buildStreamWithSuppliers(initialElementSuppliers)
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
    public @NonNull E getFirstElementFilteredByCondition(final @NonNull Collection<E> initialElements,
                                                         final @NonNull Predicate<@NonNull E> elementCondition) {

        final E finalElement = buildStream(initialElements)
            .filter(element -> element != null)
            .filter(elementCondition)
            .findFirst()
            .orElseThrow();
        return finalElement;
    }

    @Override
    public @NonNull E getFirstElementFilteredByCondition(final @NonNull Collection<E> initialElements,
                                                         final @NonNull Predicate<@NonNull E> elementCondition,
                                                         final @NonNull E fallbackElement) {

        final E finalElement = buildStream(initialElements)
            .filter(element -> element != null)
            .filter(elementCondition)
            .findFirst()
            .orElse(fallbackElement);
        return finalElement;
    }

    @Override
    public @NonNull E getFirstElementFilteredByConditionLazily(final @NonNull Collection<@NonNull Supplier<E>> initialElementSuppliers,
                                                               final @NonNull Predicate<@NonNull E> elementCondition) {

        final E finalElement = buildStreamWithSuppliers(initialElementSuppliers)
            .map(elementSupplier -> elementSupplier.get())
            .filter(element -> element != null)
            .filter(elementCondition)
            .findFirst()
            .orElseThrow();
        return finalElement;
    }

    @Override
    public @NonNull E getFirstElementFilteredByConditionLazily(final @NonNull Collection<@NonNull Supplier<E>> initialElementSuppliers,
                                                               final @NonNull Predicate<@NonNull E> elementCondition,
                                                               final @NonNull Supplier<@NonNull E> fallbackElementSupplier) {

        final E finalElement = buildStreamWithSuppliers(initialElementSuppliers)
            .map(elementSupplier -> elementSupplier.get())
            .filter(element -> element != null)
            .filter(elementCondition)
            .findFirst()
            .orElseGet(fallbackElementSupplier);
        return finalElement;
    }

    // ==========================================================
    // Resolve element
    // ==========================================================

    @Override
    public @NonNull E resolveElement(E initialElement, @NonNull Supplier<@NonNull E> fallbackElementSupplier) {
        final Predicate<E> elementCondition = element -> true;
        final E finalElement = resolveElement(initialElement, elementCondition, fallbackElementSupplier);
        return finalElement;
    }

    @Override
    public @NonNull E resolveElement(E initialElement, @NonNull Predicate<E> elementCondition, @NonNull Supplier<@NonNull E> fallbackElementSupplier) {
        if (initialElement != null && elementCondition.test(initialElement)) {
            return initialElement;
        }

        return fallbackElementSupplier.get();
    }

    @Override
    public E resolveElement(E initialElement, @NonNull Predicate<E> elementCondition) {
        if (initialElement != null && elementCondition.test(initialElement)) {
            return initialElement;
        }

        return null;
    }

    // ==========================================================
    // Stream factory methods
    // ==========================================================

    @Override
    public @NonNull Stream<E> ofNullableArray(final E[] array) {
        final Stream<E> elementStream;
        if(array != null) {
            elementStream = this.buildStream(array);
        } else {
            elementStream = Stream.empty();
        }
        return elementStream;
    }

    @Override
    public @NonNull Stream<E> ofNullableCollection(final Collection<E> collection) {
        final Stream<E> elementStream;
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

    private @NonNull Stream<E> buildStream(final E @NonNull [] array) {
        final List<E> list = Arrays.asList(array);
        final Stream<E> elementStream = streamStrategy.build(list);
        return elementStream;
    }

    private @NonNull Stream<@NonNull Supplier<E>> buildStreamWithSuppliers(final @NonNull Supplier<E> @NonNull [] array) {
        final List<@NonNull Supplier<E>> list = Arrays.asList(array);
        final Stream<@NonNull Supplier<E>> elementSupplierStream = streamStrategy.buildWithSuppliers(list);
        return elementSupplierStream;
    }

    private @NonNull Stream<E> buildStream(final @NonNull Collection<E> collection) {
        final Stream<E> elementStream = streamStrategy.build(collection);
        return elementStream;
    }

    private @NonNull Stream<@NonNull Supplier<E>> buildStreamWithSuppliers(final @NonNull Collection<@NonNull Supplier<E>> collection) {
        final Stream<@NonNull Supplier<E>> elementSupplierStream = streamStrategy.buildWithSuppliers(collection);
        return elementSupplierStream;
    }
}
