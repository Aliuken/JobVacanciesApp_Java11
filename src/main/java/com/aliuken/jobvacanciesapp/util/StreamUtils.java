package com.aliuken.jobvacanciesapp.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public class StreamUtils {

	public static <T> Stream<T> ofNullableArray(T[] array) {
		final Stream<T[]> arrayStream = Stream.ofNullable(array);
		final Stream<T> elementStream = arrayStream.flatMap(Arrays::stream);
		return elementStream;
	}

	public static <T> Stream<T> ofNullableArrayParallel(T[] array) {
		final Stream<T> elementStream = StreamUtils.ofNullableArray(array);
		return elementStream.parallel();
	}

	public static <T> Stream<T> ofNullableCollection(Collection<T> collection) {
		final Stream<Collection<T>> collectionStream = Stream.ofNullable(collection);
		final Stream<T> elementStream = collectionStream.flatMap(Collection::stream);
		return elementStream;
	}

	public static <T> Stream<T> ofNullableCollectionParallel(Collection<T> collection) {
		final Stream<T> elementStream = StreamUtils.ofNullableCollection(collection);
		return elementStream.parallel();
	}

}