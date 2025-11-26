package com.aliuken.jobvacanciesapp.util.javase;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;

public class FunctionalUtils {

	//Converts a function to a consumer
	public static <T, U> Consumer<T> convertFunctionToConsumer(Function<T, U> function) {
		final Consumer<T> consumer = t -> function.apply(t);
		return consumer;
	}

	//Converts a consumer to a function with Void output
	public static <T> Function<T,Void> convertConsumerToFunction(final Consumer<T> consumer) {
		final Function<T,Void> function = input -> {
			consumer.accept(input);
			return null;
		};
		return function;
	}

	//Converts a callable to a function with Void input
	public static <T> Function<Void,T> convertCallableToFunction(final Callable<T> callable) {
		final Function<Void,T> function = input -> {
			try {
				return callable.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
		return function;
	}

	//Converts a runnable to a function with Void input and output
	public static Function<Void,Void> convertRunnableToFunction(final Runnable runnable) {
		final Function<Void,Void> function = input -> {
			runnable.run();
			return null;
		};
		return function;
	}

	//Converts a callable to a runnable
	public static <T> Runnable convertCallableToRunnable(final Callable<T> callable) {
		final Runnable runnable = () -> {
			try {
				callable.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
		return runnable;
	}

	//Converts a runnable to a callable with Void output
	public static Callable<Void> convertRunnableToCallable(final Runnable runnable) {
		final Callable<Void> callable = () -> {
			runnable.run();
			return null;
		};
		return callable;
	}
}
