package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.Constants;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class FunctionalUtils {
	// SUMMARY FUNCTIONAL INTERFACES
	// Function<T,U>:   U apply(T t);
	// Consumer<T>:     void accept(T t);
	// Supplier<T>:     T get();
	// Callable<T>:     T call() throws Exception;
	// Runnable:        void run();

	private FunctionalUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	//Converts a function to a consumer
	public static <T,U> Consumer<T> convertFunctionToConsumer(final Function<T,U> function) {
		if (function == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The function must not be null"));
		}
		final Consumer<T> consumer = t -> function.apply(t);
		return consumer;
	}

	//Converts a consumer to a function with Void output
	public static <T> Function<T,Void> convertConsumerToFunction(final Consumer<T> consumer) {
		if (consumer == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The consumer must not be null"));
		}
		final Function<T,Void> function = input -> {
			consumer.accept(input);
			return null;
		};
		return function;
	}

	//Converts a supplier to a function with Void input
	public static <T> Function<Void,T> convertSupplierToFunction(Supplier<T> supplier) {
		if (supplier == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The supplier must not be null"));
		}
		final Function<Void,T> function = input -> supplier.get();
		return function;
	}

	//Converts a callable to a function with Void input
	public static <T> Function<Void,T> convertCallableToFunction(final Callable<T> callable) {
		if (callable == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The callable must not be null"));
		}
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
		if (runnable == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The runnable must not be null"));
		}
		final Function<Void,Void> function = input -> {
			runnable.run();
			return null;
		};
		return function;
	}

	//Converts a callable to a runnable
	public static <T> Runnable convertCallableToRunnable(final Callable<T> callable) {
		if (callable == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The callable must not be null"));
		}
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
		if (runnable == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The runnable must not be null"));
		}
		final Callable<Void> callable = () -> {
			runnable.run();
			return null;
		};
		return callable;
	}

	//Converts a supplier to a callable
	public static <T> Callable<T> convertSupplierToCallable(Supplier<T> supplier) {
		if (supplier == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The supplier must not be null"));
		}
		final Callable<T> callable = () -> supplier.get();
		return callable;
	}

	//Converts a callable to a supplier
	public static <T> Supplier<T> convertCallableToSupplier(Callable<T> callable) {
		if (callable == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The callable must not be null"));
		}
		final Supplier<T> supplier = () -> {
			try {
				return callable.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		};
		return supplier;
	}

	//Converts a supplier to a runnable
	public static <T> Runnable convertSupplierToRunnable(Supplier<T> supplier) {
		if (supplier == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The supplier must not be null"));
		}
		final Runnable runnable = () -> supplier.get();
		return runnable;
	}

	//Converts a runnable to a supplier with Void output
	public static Supplier<Void> convertRunnableToSupplier(Runnable runnable) {
		if (runnable == null) {
			throw new IllegalArgumentException(StringUtils.getStringJoined("The runnable must not be null"));
		}
		final Supplier<Void> supplier = () -> {
			runnable.run();
			return null;
		};
		return supplier;
	}
}
