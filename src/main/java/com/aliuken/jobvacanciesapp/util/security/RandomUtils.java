package com.aliuken.jobvacanciesapp.util.security;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.RandomCharactersEnum;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import org.jspecify.annotations.NonNull;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.IntSupplier;
import java.util.function.IntUnaryOperator;
import java.util.function.Supplier;

public class RandomUtils {
	public static IntUnaryOperator RANDOM_INT_GENERATOR = (boundInt) -> ThreadLocalRandom.current().nextInt(boundInt);
	public static IntSupplier RANDOM_INT_GENERATOR_0_99 = () -> ThreadLocalRandom.current().nextInt(100);
	public static Supplier<String> UUID_GENERATOR = () -> UUID.randomUUID().toString();

	private RandomUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
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

	public static <T extends Enum<T>> T getRandomValue(final @NonNull Class<T> enumClass) {
		final T[] possibleValues = enumClass.getEnumConstants();
		final T randomValue = RandomUtils.getRandomValue(possibleValues);
		return randomValue;
	}

	public static <T> T getRandomValue(final T... possibleValues) {
		final T randomValue;
		if(LogicalUtils.isNotNullNorEmpty(possibleValues)) {
			randomValue = possibleValues[ThreadLocalRandom.current().nextInt(possibleValues.length)];
		} else {
			randomValue = null;
		}
		return randomValue;
	}
}