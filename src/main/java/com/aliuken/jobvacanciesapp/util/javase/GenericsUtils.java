package com.aliuken.jobvacanciesapp.util.javase;

public class GenericsUtils {

	private GenericsUtils() throws InstantiationException {
		throw new InstantiationException(StringUtils.getStringJoined("Cannot instantiate class ", GenericsUtils.class.getName()));
	}

	@SuppressWarnings("unchecked")
	public static <T, U> U cast(final T originalObject) {
		final U finalObject = (U) originalObject;
		return finalObject;
	}
}
