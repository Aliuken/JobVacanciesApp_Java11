package com.aliuken.jobvacanciesapp.util.javase.stream.superinterface;

import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface StreamUtils<T> {

	public abstract @NonNull T[] joinArrays(final @NonNull IntFunction<T @NonNull []> arrayGenerator, final T[] array1, final T[] array2);

	public abstract @NonNull T[] joinArrays(final @NonNull IntFunction<T @NonNull []> arrayGenerator, final T[][] arrays);

	public abstract @NonNull List<T> joinLists(final List<T> list1, final List<T> list2);

	public abstract @NonNull List<T> joinLists(final List<T>[] lists);

	public abstract @NonNull Set<T> joinSets(final Set<T> set1, final Set<T> set2);

	public abstract @NonNull Set<T> joinSets(final Set<T>[] sets);

	public abstract <U> @NonNull U[] convertArray(final T @NonNull [] initialArray, final @NonNull Function<T,U> conversionFunction, final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass, final @NonNull IntFunction<@NonNull U @NonNull []> arrayGenerator);

	public abstract <U> @NonNull List<U> convertList(final @NonNull List<T> initialList, final @NonNull Function<T,U> conversionFunction, final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass);

	public abstract <U> @NonNull Set<U> convertSet(final @NonNull Set<T> initialSet, final @NonNull Function<T,U> conversionFunction, final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass);

	public abstract @NonNull T getFirstElementFilteredByCondition(@NonNull Collection<T> initialElements, @NonNull Predicate<T> elementCondition);

	public abstract @NonNull T getFirstElementFilteredByCondition(@NonNull Collection<T> initialElements, @NonNull Predicate<T> elementCondition, @NonNull T fallbackElement);

	public abstract @NonNull T getFirstElementFilteredByConditionLazily(@NonNull Collection<Supplier<T>> initialElementSuppliers, @NonNull Predicate<T> elementCondition);

	public abstract @NonNull T getFirstElementFilteredByConditionLazily(@NonNull Collection<@NonNull Supplier<T>> initialElementSuppliers, @NonNull Predicate<T> elementCondition, @NonNull Supplier<@NonNull T> fallbackElementSupplier);

	public abstract @NonNull Stream<T> ofNullableArray(final T[] array);

	public abstract @NonNull Stream<T> ofNullableCollection(final Collection<T> collection);

	public abstract <K,V> @NonNull Stream<Map.Entry<K,V>> ofNullableMap(Map<K,V> map);

	public abstract <E extends Enum<E>> @NonNull Stream<E> ofEnum(final Class<E> enumClass);
}
