package com.aliuken.jobvacanciesapp.util.javase;

import java.util.Collection;
import java.util.Map;
import java.util.StringJoiner;
import java.util.stream.Stream;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.RandomCharactersEnum;

public class StringUtils {

	private StringUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	public static String getStringJoined(final CharSequence... elementsVararg) {
		final CharSequence delimiter = Constants.EMPTY_STRING;

		final StringJoiner stringJoiner = new StringJoiner(delimiter);
		if(elementsVararg != null) {
			for(final CharSequence element : elementsVararg) {
				stringJoiner.add(element);
			}
		}

		final String stringJoined = stringJoiner.toString();
		return stringJoined;
	}

	public static String getStringJoinedWithDelimiters(CharSequence delimiter, CharSequence prefix, CharSequence suffix, final Collection<String> elements) {
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

	public static <K extends Comparable<K>,V> String getMapString(final Map<K,V> map) {
		Stream<Map.Entry<K,V>> mapEntryStream = Constants.SEQUENTIAL_STREAM_UTILS.ofNullableMap(map);
		mapEntryStream = mapEntryStream.sorted(Map.Entry.comparingByKey());

		final CharSequence delimiter = Constants.EMPTY_STRING;
		final StringJoiner stringJoiner = new StringJoiner(delimiter);
		mapEntryStream.forEach(mapEntry -> stringJoiner.add(Constants.MAP_ENTRY_PREFIX + String.valueOf(mapEntry.getKey()) + Constants.KEY_VALUE_SEPARATOR + String.valueOf(mapEntry.getValue())));

		final String stringJoined = stringJoiner.toString();
		return stringJoined;
	}

	public static String getRandomString(final RandomCharactersEnum randomCharactersEnum, int size) {
		final String result;
		if(randomCharactersEnum != null && randomCharactersEnum.getCharacters() != null && size > 0) {
			final String characters = randomCharactersEnum.getCharacters();
			final int charactersSize = randomCharactersEnum.getCharactersSize();

			final StringBuilder stringBuilder = new StringBuilder();
			do {
				final double randomDouble = Math.random();
				final double randomPositionDouble = randomDouble * charactersSize;
				final int randomPosition = (int) randomPositionDouble;
				final char character = characters.charAt(randomPosition);
				stringBuilder.append(character);
				size--;
			} while(size > 0);
			result = stringBuilder.toString();
		} else {
			result = Constants.EMPTY_STRING;
		}
		return result;
	}
}