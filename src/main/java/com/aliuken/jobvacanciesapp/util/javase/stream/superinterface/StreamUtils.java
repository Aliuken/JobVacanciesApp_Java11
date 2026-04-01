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

	@NonNull T[] joinArrays(@NonNull IntFunction<T @NonNull []> arrayGenerator, T[] array1, T[] array2);

	@NonNull T[] joinArrays(@NonNull IntFunction<T @NonNull []> arrayGenerator, T[][] arrays);

	@NonNull List<T> joinLists(List<T> list1, List<T> list2);

	@NonNull List<T> joinLists(List<T>[] lists);

	@NonNull Set<T> joinSets(Set<T> set1, Set<T> set2);

	@NonNull Set<T> joinSets(Set<T>[] sets);

	<U> U @NonNull [] convertArray(T @NonNull [] initialArray, @NonNull Function<T, U> conversionFunction,
								  @NonNull Class<T> inputClass, @NonNull Class<U> outputClass, @NonNull IntFunction<U @NonNull []> arrayGenerator);

	<U> @NonNull List<U> convertList(@NonNull List<T> initialList, @NonNull Function<T, U> conversionFunction,
									 @NonNull Class<T> inputClass, @NonNull Class<U> outputClass);

	<U> @NonNull Set<U> convertSet(@NonNull Set<T> initialSet, @NonNull Function<T, U> conversionFunction,
								   @NonNull Class<T> inputClass, @NonNull Class<U> outputClass);

	T getFirstElementFilteredByCondition(T @NonNull [] initialElements,
										 @NonNull Predicate<T> elementCondition);

	T getFirstElementFilteredByCondition(T @NonNull [] initialElements,
										 @NonNull Predicate<T> elementCondition,
										 T fallbackElement);

	T getFirstElementFilteredByConditionLazily(@NonNull Supplier<T> @NonNull [] initialElementSuppliers,
											   @NonNull Predicate<T> elementCondition);

	T getFirstElementFilteredByConditionLazily(@NonNull Supplier<T> @NonNull [] initialElementSuppliers,
											   @NonNull Predicate<T> elementCondition,
											   @NonNull Supplier<T> fallbackElementSupplier);

	T getFirstElementFilteredByCondition(@NonNull Collection<T> initialElements,
										 @NonNull Predicate<T> elementCondition);

	T getFirstElementFilteredByCondition(@NonNull Collection<T> initialElements,
										 @NonNull Predicate<T> elementCondition,
										 T fallbackElement);

	T getFirstElementFilteredByConditionLazily(@NonNull Collection<@NonNull Supplier<T>> initialElementSuppliers,
											   @NonNull Predicate<T> elementCondition);

	T getFirstElementFilteredByConditionLazily(@NonNull Collection<@NonNull Supplier<T>> initialElementSuppliers,
											   @NonNull Predicate<T> elementCondition,
											   @NonNull Supplier<T> fallbackElementSupplier);

	@NonNull Stream<T> ofNullableArray(T[] array);

	@NonNull Stream<T> ofNullableCollection(Collection<T> collection);
}
