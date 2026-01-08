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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SequentialStreamUtils extends StreamUtils {

	private static final @NonNull SequentialStreamUtils SINGLETON_INSTANCE = new SequentialStreamUtils();

	private SequentialStreamUtils(){}

	public static @NonNull SequentialStreamUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	public <T> @NonNull Stream<T> ofNullableArray(final T[] array) {
		final Stream<T[]> arrayStream = Stream.ofNullable(array);
		final Stream<T> elementStream = arrayStream.flatMap(Arrays::stream);
		return elementStream;
	}

	@Override
	public <T> @NonNull Stream<T> ofNullableCollection(final Collection<T> collection) {
		final Stream<Collection<T>> collectionStream = Stream.ofNullable(collection);
		final Stream<T> elementStream = collectionStream.flatMap(Collection::stream);
		return elementStream;
	}

	@Override
	public <K,V> @NonNull Stream<Map.Entry<K,V>> ofNullableMap(final Map<K,V> map) {
		final Set<Map.Entry<K,V>> mapEntrySet;
		if(map != null) {
			mapEntrySet = map.entrySet();
		} else {
			mapEntrySet = null;
		}

		final Stream<Set<Map.Entry<K,V>>> collectionStream = Stream.ofNullable(mapEntrySet);
		final Stream<Map.Entry<K,V>> elementStream = collectionStream.flatMap(Collection::stream);
		return elementStream;
	}

	@Override
	public <T extends Enum<T>> @NonNull Stream<T> ofEnum(final Class<T> enumClass) {
		final Stream<T> elementStream = EnumSet.allOf(enumClass).stream();
		return elementStream;
	}

	@Override
	public <T> @NonNull T[] joinArrays(final @NonNull IntFunction<T[]> generator, final T[] array1, final T[] array2) {
		final Object[][] objectMatrix = new Object[][]{array1, array2};
		final T[][] arrays = GenericsUtils.cast(objectMatrix);

		final T[] elementArray = this.joinArrays(generator, arrays);
		return elementArray;
	}

	@Override
	public <T> @NonNull T[] joinArrays(final @NonNull IntFunction<T[]> generator, final T[][] arrays) {
		Stream<T[]> arrayStream = Stream.empty();
		if(arrays != null) {
			for(final T[] newArray : arrays) {
				final Stream<T[]> newArrayStream = Stream.ofNullable(newArray);
				arrayStream = Stream.concat(arrayStream, newArrayStream);
			}
		}

		final Stream<T> elementStream = arrayStream.flatMap(Arrays::stream);
		final T[] elementArray = elementStream.toArray(generator);
		Objects.requireNonNull(elementArray, "elementArray cannot be null");
		return elementArray;
	}

	@Override
	public <T> @NonNull List<T> joinLists(final List<T> list1, final List<T> list2) {
		final Object[] objectArray = new Object[]{list1, list2};
		final List<T>[] lists = GenericsUtils.cast(objectArray);

		final List<T> elementList = this.joinLists(lists);
		return elementList;
	}

	@Override
	public <T> @NonNull List<T> joinLists(final List<T>[] lists) {
		Stream<List<T>> listStream = Stream.empty();
		if(lists != null) {
			for(final List<T> newList : lists) {
				final Stream<List<T>> newListStream = Stream.ofNullable(newList);
				listStream = Stream.concat(listStream, newListStream);
			}
		}

		final Stream<T> elementStream = listStream.flatMap(Collection::stream);
		final List<T> elementList = elementStream.collect(Collectors.toCollection(ArrayList::new));
		return elementList;
	}

	@Override
	public <T> @NonNull Set<T> joinSets(final Set<T> set1, final Set<T> set2) {
		final Object[] objectArray = new Object[]{set1, set2};
		final Set<T>[] sets = GenericsUtils.cast(objectArray);

		final Set<T> elementSet = this.joinSets(sets);
		return elementSet;
	}

	@Override
	public <T> @NonNull Set<T> joinSets(final Set<T>[] sets) {
		Stream<Set<T>> setStream = Stream.empty();
		if(sets != null) {
			for(final Set<T> newSet : sets) {
				final Stream<Set<T>> newSetStream = Stream.ofNullable(newSet);
				setStream = Stream.concat(setStream, newSetStream);
			}
		}

		final Stream<T> elementStream = setStream.flatMap(Collection::stream);
		final Set<T> elementSet = elementStream.collect(Collectors.toCollection(LinkedHashSet::new));
		return elementSet;
	}

	@Override
	public <T,U> @NonNull U[] convertArray(final T[] initialArray, final @NonNull Function<T,U> conversionFunction, final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass, final @NonNull IntFunction<U[]> arrayGenerator) {
		final Stream<T> initialStream = this.ofNullableArray(initialArray);
		final Stream<U> finalStream = initialStream.map(conversionFunction);
		final U[] finalArray = finalStream.toArray(arrayGenerator);
		Objects.requireNonNull(finalArray, "finalArray cannot be null");
		return finalArray;
	}

	@Override
	public <T,U> @NonNull List<U> convertList(final List<T> initialList, final @NonNull Function<T,U> conversionFunction, final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass) {
		final Stream<T> initialStream = this.ofNullableCollection(initialList);
		final Stream<U> finalStream = initialStream.map(conversionFunction);
		final List<U> finalList = finalStream.collect(Collectors.toCollection(ArrayList::new));
		return finalList;
	}

	@Override
	public <T,U> @NonNull Set<U> convertSet(final Set<T> initialSet, final @NonNull Function<T,U> conversionFunction, final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass) {
		final Stream<T> initialStream = this.ofNullableCollection(initialSet);
		final Stream<U> finalStream = initialStream.map(conversionFunction);
		final Set<U> finalSet = finalStream.collect(Collectors.toCollection(LinkedHashSet::new));
		return finalSet;
	}
}