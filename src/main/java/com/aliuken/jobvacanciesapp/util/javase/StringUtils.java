package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.Constants;
import org.jspecify.annotations.NonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.stream.Stream;

public class StringUtils {

	private StringUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	public static @NonNull String lowerCaseFirstCharacter(@NonNull String string) {
		if(string.isEmpty()) {
			return Constants.EMPTY_STRING;
		}

		char[] c = string.toCharArray();
		c[0] = Character.toLowerCase(c[0]);

		string = new String(c);
		return string;
	}

	public static @NonNull String upperCaseFirstCharacter(@NonNull String string) {
		if(string.isEmpty()) {
			return Constants.EMPTY_STRING;
		}

		char[] c = string.toCharArray();
		c[0] = Character.toUpperCase(c[0]);

		string = new String(c);
		return string;
	}

	public static @NonNull String getStringJoined(final CharSequence @NonNull ... elementsVarargs) {
		final String stringJoined;
		if(elementsVarargs.length > 0) {
			final CharSequence delimiter = Constants.EMPTY_STRING;
			final StringJoiner stringJoiner = new StringJoiner(delimiter);
			for(final CharSequence element : elementsVarargs) {
				stringJoiner.add(element);
			}
			stringJoined = stringJoiner.toString();
			Objects.requireNonNull(stringJoined, "stringJoined cannot be null");
		} else {
			stringJoined = Constants.EMPTY_STRING;
		}
		return stringJoined;
	}

	public static @NonNull String getStringJoinedWithDelimiters(CharSequence delimiter, CharSequence prefix, CharSequence suffix, final Collection<String> elements) {
		final String stringJoined;
		if(LogicalUtils.isNotNullNorEmpty(elements)) {
			if(delimiter == null) {
				delimiter = Constants.EMPTY_STRING;
			}

			if(prefix == null) {
				prefix = Constants.EMPTY_STRING;
			}

			if(suffix == null) {
				suffix = Constants.EMPTY_STRING;
			}

			final StringJoiner stringJoiner = new StringJoiner(delimiter, prefix, suffix);
			for(final CharSequence element : elements) {
				stringJoiner.add(element);
			}
			stringJoined = stringJoiner.toString();
			Objects.requireNonNull(stringJoined, "stringJoinedWithDelimiters cannot be null");
		} else {
			stringJoined = Constants.EMPTY_STRING;
		}
		return stringJoined;
	}

	public static <K extends Comparable<K>,V> @NonNull String getMapString(final Map<K,V> map) {
		Stream<Map.Entry<K,V>> mapEntryStream = Constants.SEQUENTIAL_STREAM_UTILS.ofNullableMap(map);
		mapEntryStream = mapEntryStream.sorted(Map.Entry.comparingByKey());

		final CharSequence delimiter = Constants.EMPTY_STRING;
		final StringJoiner stringJoiner = new StringJoiner(delimiter);
		mapEntryStream.forEach(mapEntry -> {
			final String keyString = String.valueOf(mapEntry.getKey());
			final String valueString = String.valueOf(mapEntry.getValue());
			stringJoiner.add(Constants.MAP_ENTRY_PREFIX + keyString + Constants.KEY_VALUE_SEPARATOR + valueString);
		});

		final String stringJoined = stringJoiner.toString();
		Objects.requireNonNull(stringJoined, "mapString cannot be null");
		return stringJoined;
	}

	public static @NonNull String getUrlWithoutParametersAndFragment(final @NonNull URI initialURI) throws URISyntaxException {
		final URI finalURI = new URI(initialURI.getScheme(), initialURI.getAuthority(), initialURI.getPath(), null, null);
		final String result = finalURI.toString();
		Objects.requireNonNull(result, "urlWithoutParametersAndFragment cannot be null");
		return result;
	}

	public static @NonNull String getUrlPath(final @NonNull URI initialURI) throws URISyntaxException {
		final URI finalURI = new URI(null, null, initialURI.getPath(), null, null);
		final String result = finalURI.toString();
		Objects.requireNonNull(result, "urlPath cannot be null");
		return result;
	}

	public static @NonNull String getUrlParameters(final @NonNull URI initialURI) throws URISyntaxException {
		final URI finalURI = new URI(null, null, null, initialURI.getQuery(), null);
		final String result = finalURI.toString();
		Objects.requireNonNull(result, "urlParameters cannot be null");
		return result;
	}
}