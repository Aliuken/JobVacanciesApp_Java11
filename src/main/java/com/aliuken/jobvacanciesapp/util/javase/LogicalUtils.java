package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.Constants;

import java.util.Collection;
import java.util.Map;

public class LogicalUtils {

	private LogicalUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	//----------------------------------------------------------------------------------------------------------------------------

	// String methods

	public static boolean isNullOrEmptyString(final String string) {
		final boolean result = (string == null || string.isEmpty());
		return result;
	}

	public static boolean isNotNullNorEmptyString(final String string) {
		final boolean result = (string != null && !string.isEmpty());
		return result;
	}

	public static boolean isNullOrBlank(String string) {
		if(string == null) {
			return true;
		}
		string = string.strip();
		if(string.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean isNotNullNorBlank(String string) {
		if(string == null) {
			return false;
		}
		string = string.strip();
		if(string.isEmpty()) {
			return false;
		}
		return true;
	}

	public static boolean contains(final String string, final String searchedString) {
		final boolean result = (string != null && searchedString != null && string.contains(searchedString));
		return result;
	}

	//----------------------------------------------------------------------------------------------------------------------------

	// Array methods

	public static boolean isNullOrEmpty(final byte[] array) {
		final boolean result = (array == null || array.length == 0);
		return result;
	}

	public static boolean isNotNullNorEmpty(final byte[] array) {
		final boolean result = (array != null && array.length > 0);
		return result;
	}

	public static <E> boolean isNullOrEmpty(final E[] array) {
		final boolean result = (array == null || array.length == 0);
		return result;
	}

	public static <E> boolean isNotNullNorEmpty(final E[] array) {
		final boolean result = (array != null && array.length > 0);
		return result;
	}

	public static <E> boolean contains(final E[] array, final E searchedElement) {
		if(array != null && searchedElement != null) {
			for(final E element : array) {
				if(searchedElement.equals(element)) {
					return true;
				}
			}
		}

		return false;
	}

	//----------------------------------------------------------------------------------------------------------------------------

	// Collection methods

	public static <E> boolean isNullOrEmpty(final Collection<E> collection) {
		final boolean result = (collection == null || collection.isEmpty());
		return result;
	}

	public static <E> boolean isNotNullNorEmpty(final Collection<E> collection) {
		final boolean result = (collection != null && !collection.isEmpty());
		return result;
	}

	public static <E> boolean contains(final Collection<E> collection, final E searchedElement) {
		final boolean result = (collection != null && searchedElement != null && collection.contains(searchedElement));
		return result;
	}

	//----------------------------------------------------------------------------------------------------------------------------

	// Map methods

	public static <K,V> boolean isNullOrEmpty(final Map<K,V> map) {
		final boolean result = (map == null || map.isEmpty());
		return result;
	}

	public static <K,V> boolean isNotNullNorEmpty(final Map<K,V> map) {
		final boolean result = (map != null && !map.isEmpty());
		return result;
	}

	public static <K,V> boolean containsKey(final Map<K,V> map, final K searchedKey) {
		final boolean result = (map != null && searchedKey != null && map.containsKey(searchedKey));
		return result;
	}

	public static <K,V> boolean containsValue(final Map<K,V> map, final V searchedValue) {
		final boolean result = (map != null && searchedValue != null && map.containsValue(searchedValue));
		return result;
	}

	public static <K,V> boolean containsKeyWithValue(final Map<K,V> map, final K searchedKey, final V searchedValue) {
		if(map != null && searchedKey != null) {
			final V value = map.get(searchedKey);

			final boolean result;
			if(searchedValue != null) {
				result = (searchedValue.equals(value));
			} else {
				result = (value == null);
			}
			return result;
		}
		return false;
	}
}