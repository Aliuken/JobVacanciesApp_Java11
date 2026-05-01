package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamStaticUtils;
import org.jspecify.annotations.NonNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Optional;
import java.util.function.IntFunction;

public class GenericsUtils {

	private GenericsUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	@SuppressWarnings("unchecked")
	public static <T, U> U cast(final T originalObject) {
		final U finalObject = (U) originalObject;
		return finalObject;
	}

	public static <T> T unpackOptional(final Optional<T> optionalElement) {
		final T element;
		if(optionalElement != null) {
			element = optionalElement.orElse(null);
		} else {
			element = null;
		}
		return element;

		//Alternative version:
//		final T element;
//		if(optionalElement != null && optionalElement.isPresent()) {
//			element = optionalElement.get();
//		} else {
//			element = null;
//		}
//		return element;
	}

	public static <T> T @NonNull [] getCollectionAsArray(final @NonNull Class<T> collectionElementClass, final @NonNull Collection<T> collection) {
		final IntFunction<T[]> arrayGenerator = GenericsUtils.getArrayGenerator(collectionElementClass);

		final T[] collectionElementArray = collection.toArray(arrayGenerator);
		return collectionElementArray;
	}

	@SuppressWarnings("unchecked")
	public static <T> @NonNull IntFunction<T[]> getArrayGenerator(final @NonNull Class<T> collectionElementClass) {
		final IntFunction<T[]> arrayGenerator = size -> (T[]) Array.newInstance(collectionElementClass, size);
		return arrayGenerator;
	}
}
