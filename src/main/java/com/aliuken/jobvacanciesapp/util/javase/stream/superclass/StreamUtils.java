package com.aliuken.jobvacanciesapp.util.javase.stream.superclass;

import com.aliuken.jobvacanciesapp.util.javase.stream.ParallelStreamUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.SequentialStreamUtils;
import org.jspecify.annotations.NonNull;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public abstract class StreamUtils {

	public static @NonNull StreamUtils getInstance(final boolean isParallel) {
		final StreamUtils streamUtils;
		if(isParallel) {
			streamUtils = ParallelStreamUtils.getInstance();
		} else {
			streamUtils = SequentialStreamUtils.getInstance();
		}
		return streamUtils;
	}

	public abstract <T> @NonNull Stream<T> ofNullableArray(final T[] array);

	public abstract <T> @NonNull Stream<T> ofNullableCollection(final Collection<T> collection);

	public abstract <K,V> @NonNull Stream<Map.Entry<K,V>> ofNullableMap(Map<K,V> map);

	public abstract <T extends Enum<T>> @NonNull Stream<T> ofEnum(final Class<T> enumClass);

	public abstract <T> @NonNull T[] joinArrays(final @NonNull IntFunction<T[]> generator, final T[] array1, final T[] array2);

	public abstract <T> @NonNull T[] joinArrays(final @NonNull IntFunction<T[]> generator, final T[][] arrays);

	public abstract <T> @NonNull List<T> joinLists(final List<T> list1, final List<T> list2);

	public abstract <T> @NonNull List<T> joinLists(final List<T>[] lists);

	public abstract <T> @NonNull Set<T> joinSets(final Set<T> set1, final Set<T> set2);

	public abstract <T> @NonNull Set<T> joinSets(final Set<T>[] sets);

	public abstract <T,U> @NonNull U[] convertArray(final T[] initialArray, final @NonNull Function<T,U> conversionFunction, final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass, final @NonNull IntFunction<U[]> arrayGenerator);

	public abstract <T,U> @NonNull List<U> convertList(final List<T> initialList, final @NonNull Function<T,U> conversionFunction, final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass);

	public abstract <T,U> @NonNull Set<U> convertSet(final Set<T> initialSet, final @NonNull Function<T,U> conversionFunction, final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass);

}
