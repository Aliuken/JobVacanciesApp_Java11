package com.aliuken.jobvacanciesapp.util.javase.stream;

import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.superclass.StreamUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelStreamUtils extends StreamUtils {

	private static final ParallelStreamUtils SINGLETON_INSTANCE = new ParallelStreamUtils();

	private ParallelStreamUtils(){}

	public static ParallelStreamUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	public <T> Stream<T> ofNullableArray(final T[] array) {
		final Stream<T[]> arrayStream = Stream.ofNullable(array).parallel();
		final Stream<T> elementStream = arrayStream.flatMap(Arrays::stream).parallel();
		return elementStream;
	}

	@Override
	public <T> Stream<T> ofNullableCollection(final Collection<T> collection) {
		final Stream<Collection<T>> collectionStream = Stream.ofNullable(collection).parallel();
		final Stream<T> elementStream = collectionStream.flatMap(Collection::stream).parallel();
		return elementStream;
	}

	@Override
	public <K,V> Stream<Map.Entry<K,V>> ofNullableMap(final Map<K,V> map) {
		final Set<Map.Entry<K,V>> mapEntrySet;
		if(map != null) {
			mapEntrySet = map.entrySet();
		} else {
			mapEntrySet = null;
		}

		final Stream<Set<Map.Entry<K,V>>> collectionStream = Stream.ofNullable(mapEntrySet).parallel();
		final Stream<Map.Entry<K,V>> elementStream = collectionStream.flatMap(Collection::stream).parallel();
		return elementStream;
	}

	@Override
	public <T extends Enum<T>> Stream<T> ofEnum(final Class<T> enumClass) {
		final Stream<T> elementStream = EnumSet.allOf(enumClass).stream().parallel();
		return elementStream;
	}

	@Override
	public <T> T[] joinArrays(IntFunction<T[]> generator, final T[] array1, final T[] array2) {
		final Object[][] objectMatrix = new Object[][]{array1, array2};
		final T[][] arrays = GenericsUtils.cast(objectMatrix);

		final T[] elementArray = this.joinArrays(generator, arrays);

		return elementArray;
	}

	@Override
	public <T> T[] joinArrays(IntFunction<T[]> generator, final T[][] arrays) {
		Stream<T[]> arrayStream = Stream.empty();
		arrayStream = arrayStream.parallel();
		if(arrays != null) {
			for(final T[] newArray : arrays) {
				final Stream<T[]> newArrayStream = Stream.ofNullable(newArray).parallel();
				arrayStream = Stream.concat(arrayStream, newArrayStream).parallel();
			}
		}

		final Stream<T> elementStream = arrayStream.flatMap(Arrays::stream).parallel();
		final T[] elementArray = elementStream.toArray(generator);

		return elementArray;
	}

	@Override
	public <T> List<T> joinLists(final List<T> list1, final List<T> list2) {
		final Object[] objectArray = new Object[]{list1, list2};
		final List<T>[] lists = GenericsUtils.cast(objectArray);

		final List<T> elementList = this.joinLists(lists);

		return elementList;
	}

	@Override
	public <T> List<T> joinLists(final List<T>[] lists) {
		Stream<List<T>> listStream = Stream.empty();
		listStream = listStream.parallel();
		if(lists != null) {
			for(final List<T> newList : lists) {
				final Stream<List<T>> newListStream = Stream.ofNullable(newList).parallel();
				listStream = Stream.concat(listStream, newListStream).parallel();
			}
		}

		final Stream<T> elementStream = listStream.flatMap(Collection::stream).parallel();
		final List<T> elementList = elementStream.collect(Collectors.toCollection(ArrayList::new));

		return elementList;
	}

	@Override
	public <T> Set<T> joinSets(final Set<T> set1, final Set<T> set2) {
		final Object[] objectArray = new Object[]{set1, set2};
		final Set<T>[] sets = GenericsUtils.cast(objectArray);

		final Set<T> elementSet = this.joinSets(sets);

		return elementSet;
	}

	@Override
	public <T> Set<T> joinSets(final Set<T>[] sets) {
		Stream<Set<T>> setStream = Stream.empty();
		setStream = setStream.parallel();
		if(sets != null) {
			for(final Set<T> newSet : sets) {
				final Stream<Set<T>> newSetStream = Stream.ofNullable(newSet).parallel();
				setStream = Stream.concat(setStream, newSetStream).parallel();
			}
		}

		final Stream<T> elementStream = setStream.flatMap(Collection::stream).parallel();
		final Set<T> elementSet = elementStream.collect(Collectors.toCollection(LinkedHashSet::new));

		return elementSet;
	}

	@Override
	public <T,U> U[] convertArray(final T[] initialArray, final Function<T,U> conversionFunction, final Class<T> inputClass, final Class<U> outputClass, final IntFunction<U[]> arrayGenerator) {
		final Stream<T> initialStream = this.ofNullableArray(initialArray);
		final Stream<U> finalStream = initialStream.map(conversionFunction).parallel();
		final U[] finalArray = finalStream.toArray(arrayGenerator);
		return finalArray;
	}

	@Override
	public <T,U> List<U> convertList(final List<T> initialList, final Function<T,U> conversionFunction, final Class<T> inputClass, final Class<U> outputClass) {
		final Stream<T> initialStream = this.ofNullableCollection(initialList);
		final Stream<U> finalStream = initialStream.map(conversionFunction).parallel();
		final List<U> finalList = finalStream.collect(Collectors.toCollection(ArrayList::new));
		return finalList;
	}

	@Override
	public <T,U> Set<U> convertSet(final Set<T> initialSet, final Function<T,U> conversionFunction, final Class<T> inputClass, final Class<U> outputClass) {
		final Stream<T> initialStream = this.ofNullableCollection(initialSet);
		final Stream<U> finalStream = initialStream.map(conversionFunction).parallel();
		final Set<U> finalSet = finalStream.collect(Collectors.toCollection(LinkedHashSet::new));
		return finalSet;
	}
}