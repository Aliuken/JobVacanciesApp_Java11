package com.aliuken.jobvacanciesapp.util;

import java.util.StringJoiner;

public class StringUtils {
	private static final String ALPHANUMERIC_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final int ALPHANUMERIC_CHARACTERS_SIZE = ALPHANUMERIC_CHARACTERS.length();

	public static String getStringJoined(CharSequence... newElements) {
		final StringJoiner stringJoiner = new StringJoiner("");

		if(newElements != null) {
			for(final CharSequence newElement : newElements) {
				stringJoiner.add(newElement);
			}
		}

		final String stringJoined = stringJoiner.toString();
		return stringJoined;
	}

	public static String getStringJoinedWithDelimiters(CharSequence delimiter, CharSequence prefix, CharSequence suffix, CharSequence... newElements) {
		final StringJoiner stringJoiner;
		if(prefix != null && suffix != null) {
			stringJoiner = new StringJoiner(delimiter, prefix, suffix);
		} else {
			stringJoiner = new StringJoiner(delimiter);
		}

		if(newElements != null) {
			for(final CharSequence newElement : newElements) {
				stringJoiner.add(newElement);
			}
		}

		final String stringJoined = stringJoiner.toString();
		return stringJoined;
	}

	/**
	 * Metodo para generar una cadena aleatoria de longitud N
	 */
	public static String getRandomAlphaNumeric(int count) {
		StringBuilder stringBuilder = new StringBuilder();
		while (count-- != 0) {
			int characterPosition = (int) (Math.random() * ALPHANUMERIC_CHARACTERS_SIZE);
			char character = ALPHANUMERIC_CHARACTERS.charAt(characterPosition);
			stringBuilder.append(character);
		}
		return stringBuilder.toString();
	}

}