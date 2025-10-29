package com.aliuken.jobvacanciesapp.util.javase.stream.superclass;

import com.aliuken.jobvacanciesapp.util.javase.stream.ParallelStreamUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.SequentialStreamUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Stream;

public abstract class StreamUtils {

	public static StreamUtils getInstance(final boolean isParallel) {
		final StreamUtils streamUtils;
		if(isParallel) {
			streamUtils = ParallelStreamUtils.getInstance();
		} else {
			streamUtils = SequentialStreamUtils.getInstance();
		}
		return streamUtils;
	}

	public abstract <T> Stream<T> ofNullableArray(final T[] array);

	public abstract <T> Stream<T> ofNullableCollection(final Collection<T> collection);

	public abstract <K,V> Stream<Map.Entry<K,V>> ofNullableMap(Map<K,V> map);

	public abstract <T extends Enum<T>> Stream<T> ofEnum(final Class<T> enumClass);

	public abstract <T> T[] joinArrays(IntFunction<T[]> generator, final T[] array1, final T[] array2);

	public abstract <T> T[] joinArrays(IntFunction<T[]> generator, final T[][] arrays);

	public abstract <T> List<T> joinLists(final List<T> list1, final List<T> list2);

	public abstract <T> List<T> joinLists(final List<T>[] lists);

	public abstract <T> Set<T> joinSets(final Set<T> set1, final Set<T> set2);

	public abstract <T> Set<T> joinSets(final Set<T>[] sets);

	public abstract <T,U> U[] convertArray(final T[] initialArray, final Function<T,U> conversionFunction, final Class<T> inputClass, final Class<U> outputClass, final IntFunction<U[]> arrayGenerator);

	public abstract <T,U> List<U> convertList(final List<T> initialList, final Function<T,U> conversionFunction, final Class<T> inputClass, final Class<U> outputClass);

	public abstract <T,U> Set<U> convertSet(final Set<T> initialSet, final Function<T,U> conversionFunction, final Class<T> inputClass, final Class<U> outputClass);

}
