package com.aliuken.jobvacanciesapp.util.javase;

import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionalUtils {

	//Converts a consumer to a function returning Void
	public static <T> Function<T,Void> convertConsumerToFunction(final Consumer<T> consumer) {
		final Function<T,Void> function = list -> {
			consumer.accept(list);
			return null;
		};
		return function;
	}
}
