package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.Constants;
import org.jspecify.annotations.NonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.Map;
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

		char c[] = string.toCharArray();
		c[0] = Character.toLowerCase(c[0]);

		string = new String(c);
		return string;
	}

	public static @NonNull String upperCaseFirstCharacter(@NonNull String string) {
		if(string.isEmpty()) {
			return Constants.EMPTY_STRING;
		}

		char c[] = string.toCharArray();
		c[0] = Character.toUpperCase(c[0]);

		string = new String(c);
		return string;
	}

	public static @NonNull String getStringJoined(final @NonNull CharSequence... elementsVarargs) {
		final CharSequence delimiter = Constants.EMPTY_STRING;

		final StringJoiner stringJoiner = new StringJoiner(delimiter);
		if(elementsVarargs != null) {
			for(final CharSequence element : elementsVarargs) {
				stringJoiner.add(element);
			}
		}

		final String stringJoined = stringJoiner.toString();
		return stringJoined;
	}

	public static @NonNull String getStringJoinedWithDelimiters(CharSequence delimiter, CharSequence prefix, CharSequence suffix, final @NonNull Collection<String> elements) {
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
		if(elements != null) {
			for(final CharSequence element : elements) {
				stringJoiner.add(element);
			}
		}

		final String stringJoined = stringJoiner.toString();
		return stringJoined;
	}

	public static <K extends Comparable<K>,V> @NonNull String getMapString(final Map<K,V> map) {
		Stream<Map.Entry<K,V>> mapEntryStream = Constants.SEQUENTIAL_STREAM_UTILS.ofNullableMap(map);
		mapEntryStream = mapEntryStream.sorted(Map.Entry.comparingByKey());

		final CharSequence delimiter = Constants.EMPTY_STRING;
		final StringJoiner stringJoiner = new StringJoiner(delimiter);
		mapEntryStream.forEach(mapEntry -> stringJoiner.add(Constants.MAP_ENTRY_PREFIX + String.valueOf(mapEntry.getKey()) + Constants.KEY_VALUE_SEPARATOR + String.valueOf(mapEntry.getValue())));

		final String stringJoined = stringJoiner.toString();
		return stringJoined;
	}

	public static @NonNull String getUrlWithoutParametersAndFragment(final @NonNull URI initialURI) throws URISyntaxException {
		final URI finalURI = new URI(initialURI.getScheme(), initialURI.getAuthority(), initialURI.getPath(), null, null);

		final String result = finalURI.toString();
		return result;
	}

	public static @NonNull String getUrlPath(final @NonNull URI initialURI) throws URISyntaxException {
		final URI finalURI = new URI(null, null, initialURI.getPath(), null, null);

		final String result = finalURI.toString();
		return result;
	}

	public static @NonNull String getUrlParameters(final @NonNull URI initialURI) throws URISyntaxException {
		final URI finalURI = new URI(null, null, null, initialURI.getQuery(), null);

		final String result = finalURI.toString();
		return result;
	}
}