package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.Constants;

import java.util.Optional;

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
}
