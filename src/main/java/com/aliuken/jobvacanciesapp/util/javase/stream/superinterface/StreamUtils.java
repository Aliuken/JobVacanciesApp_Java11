package com.aliuken.jobvacanciesapp.util.javase.stream.superinterface;

import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface StreamUtils<E> {

	@NonNull E[] joinArrays(@NonNull IntFunction<E @NonNull []> arrayGenerator, E[] array1, E[] array2);

	@NonNull E[] joinArrays(@NonNull IntFunction<E @NonNull []> arrayGenerator, E[][] arrays);

	@NonNull List<E> joinLists(List<E> list1, List<E> list2);

	@NonNull List<E> joinLists(List<E>[] lists);

	@NonNull Set<E> joinSets(Set<E> set1, Set<E> set2);

	@NonNull Set<E> joinSets(Set<E>[] sets);

	<E2> E2 @NonNull [] convertArray(E @NonNull [] initialArray, @NonNull Function<E, E2> conversionFunction,
	                                 @NonNull Class<E> inputClass, @NonNull Class<E2> outputClass, @NonNull IntFunction<E2 @NonNull []> arrayGenerator);

	<E2> @NonNull List<E2> convertList(@NonNull List<E> initialList, @NonNull Function<E, E2> conversionFunction,
	                                   @NonNull Class<E> inputClass, @NonNull Class<E2> outputClass);

	<E2> @NonNull Set<E2> convertSet(@NonNull Set<E> initialSet, @NonNull Function<E, E2> conversionFunction,
	                                 @NonNull Class<E> inputClass, @NonNull Class<E2> outputClass);

	@NonNull E getFirstElementFilteredByCondition(E @NonNull [] initialElements,
	                                     @NonNull Predicate<E> elementCondition);

	@NonNull E getFirstElementFilteredByCondition(E @NonNull [] initialElements,
	                                     @NonNull Predicate<E> elementCondition,
	                                     E fallbackElement);

	@NonNull E getFirstElementFilteredByConditionLazily(@NonNull Supplier<E> @NonNull [] initialElementSuppliers,
	                                           @NonNull Predicate<E> elementCondition);

	@NonNull E getFirstElementFilteredByConditionLazily(@NonNull Supplier<E> @NonNull [] initialElementSuppliers,
	                                           @NonNull Predicate<E> elementCondition,
	                                           @NonNull Supplier<E> fallbackElementSupplier);

	@NonNull E getFirstElementFilteredByCondition(@NonNull Collection<E> initialElements,
	                                     @NonNull Predicate<E> elementCondition);

	@NonNull E getFirstElementFilteredByCondition(@NonNull Collection<E> initialElements,
	                                     @NonNull Predicate<E> elementCondition,
	                                     E fallbackElement);

	@NonNull E getFirstElementFilteredByConditionLazily(@NonNull Collection<@NonNull Supplier<E>> initialElementSuppliers,
	                                           @NonNull Predicate<E> elementCondition);

	@NonNull E getFirstElementFilteredByConditionLazily(@NonNull Collection<@NonNull Supplier<E>> initialElementSuppliers,
	                                           @NonNull Predicate<E> elementCondition,
	                                           @NonNull Supplier<E> fallbackElementSupplier);

	@NonNull E resolveElement(E initialElement, @NonNull Supplier<E> fallbackElementSupplier);

	@NonNull E resolveElement(E initialElement, @NonNull Predicate<E> elementCondition, @NonNull Supplier<E> fallbackElementSupplier);

	E resolveElement(E initialElement, @NonNull Predicate<E> elementCondition);

	@NonNull Stream<E> ofNullableArray(E[] array);

	@NonNull Stream<E> ofNullableCollection(Collection<E> collection);
}
