package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.Constants;

import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class FunctionalUtils {
	/*
		SUMMARY FUNCTIONAL INTERFACES
		Function<T,U>:      U apply(T t)
		UnaryOperator<T>:   T apply(T t)
		Predicate<T>:		boolean test(T t)
		Consumer<T>:        void accept(T t)
		Runnable:           void run()
		Supplier<T>:		T get()
		Callable<T>:		T call() throws Exception
	*/

	private FunctionalUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

	//------------------------------------------------------------------------------------------------------------------
	// Conversions to function, identity function or identity unary operator
	//------------------------------------------------------------------------------------------------------------------

	//Converts a consumer to a function with Void output
	public static <T> Function<T,Void> convertConsumerToFunction(final Consumer<T> consumer) {
		if(consumer == null) {
			throw new IllegalArgumentException("The consumer must not be null");
		}
		final Function<T,Void> function = input -> {
			consumer.accept(input);
			return null;
		};
		return function;
	}

	//Converts a consumer to an identity function
	public static <T> Function<T,T> convertConsumerToIdentityFunction(final Consumer<T> consumer) {
		if(consumer == null) {
			throw new IllegalArgumentException("The consumer must not be null");
		}
		final Function<T,T> function = input -> {
			consumer.accept(input);
			return input;
		};
		return function;
	}

	//Converts a consumer to an identity unary operator
	public static <T> UnaryOperator<T> convertConsumerToIdentityUnaryOperator(final Consumer<T> consumer) {
		if(consumer == null) {
			throw new IllegalArgumentException("The consumer must not be null");
		}
		final UnaryOperator<T> unaryOperator = input -> {
			consumer.accept(input);
			return input;
		};
		return unaryOperator;
	}

	//Converts a runnable to an identity function of Void
	public static Function<Void,Void> convertRunnableToIdentityFunction(final Runnable runnable) {
		if(runnable == null) {
			throw new IllegalArgumentException("The runnable must not be null");
		}
		final Function<Void,Void> function = input -> {
			runnable.run();
			return null;
		};
		return function;
	}

	//Converts a runnable to an identity unary operator of Void
	public static UnaryOperator<Void> convertRunnableToIdentityUnaryOperator(final Runnable runnable) {
		if(runnable == null) {
			throw new IllegalArgumentException("The runnable must not be null");
		}
		final UnaryOperator<Void> unaryOperator = input -> {
			runnable.run();
			return null;
		};
		return unaryOperator;
	}

	//Converts a supplier to a function with Void input
	public static <T> Function<Void,T> convertSupplierToFunction(final Supplier<T> supplier) {
		if(supplier == null) {
			throw new IllegalArgumentException("The supplier must not be null");
		}
		final Function<Void,T> function = input -> supplier.get();
		return function;
	}

	//Converts a supplier to an identity function of Void
	public static <T> Function<Void,Void> convertSupplierToIdentityFunction(final Supplier<T> supplier) {
		if(supplier == null) {
			throw new IllegalArgumentException("The supplier must not be null");
		}
		final Function<Void,Void> function = input -> {
			supplier.get();
			return null;
		};
		return function;
	}

	//Converts a supplier to an identity unary operator of Void
	public static <T> UnaryOperator<Void> convertSupplierToIdentityUnaryOperator(final Supplier<T> supplier) {
		if(supplier == null) {
			throw new IllegalArgumentException("The supplier must not be null");
		}
		final UnaryOperator<Void> unaryOperator = input -> {
			supplier.get();
			return null;
		};
		return unaryOperator;
	}

	//Converts a callable to a function with Void input
	public static <T> Function<Void,T> convertCallableToFunction(final Callable<T> callable) {
		if(callable == null) {
			throw new IllegalArgumentException("The callable must not be null");
		}
		final Function<Void,T> function = input -> {
			try {
				return callable.call();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		};
		return function;
	}

	//Converts a callable to an identity function of Void
	public static <T> Function<Void,Void> convertCallableToIdentityFunction(final Callable<T> callable) {
		if(callable == null) {
			throw new IllegalArgumentException("The callable must not be null");
		}
		final Function<Void,Void> function = input -> {
			try {
				callable.call();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		};
		return function;
	}

	//Converts a callable to an identity unary operator of Void
	public static <T> UnaryOperator<Void> convertCallableToIdentityUnaryOperator(final Callable<T> callable) {
		if(callable == null) {
			throw new IllegalArgumentException("The callable must not be null");
		}
		final UnaryOperator<Void> function = input -> {
			try {
				callable.call();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
			return null;
		};
		return function;
	}

	//------------------------------------------------------------------------------------------------------------------
	// Conversions from function, unary operator or predicate
	//------------------------------------------------------------------------------------------------------------------

	//Converts a function to a consumer
	public static <T,U> Consumer<T> convertFunctionToConsumer(final Function<T,U> function) {
		if(function == null) {
			throw new IllegalArgumentException("The function must not be null");
		}
		final Consumer<T> consumer = input -> {
			function.apply(input);
		};
		return consumer;
	}

	//Converts a unary operator to a consumer
	public static <T> Consumer<T> convertUnaryOperatorToConsumer(final UnaryOperator<T> unaryOperator) {
		if(unaryOperator == null) {
			throw new IllegalArgumentException("The unaryOperator must not be null");
		}
		final Consumer<T> consumer = input -> {
			unaryOperator.apply(input);
		};
		return consumer;
	}

	//Converts a unary operator to a function
	public static <T> Function<T, T> convertUnaryOperatorToFunction(final UnaryOperator<T> unaryOperator) {
		if(unaryOperator == null) {
			throw new IllegalArgumentException("The unaryOperator must not be null");
		}
		final Function<T, T> function = input -> unaryOperator.apply(input);
		return function;
	}

	//Converts a unary operator to an identity function
	public static <T> Function<T, T> convertUnaryOperatorToIdentityFunction(final UnaryOperator<T> unaryOperator) {
		if(unaryOperator == null) {
			throw new IllegalArgumentException("The unaryOperator must not be null");
		}
		final Function<T, T> function = input -> {
			unaryOperator.apply(input);
			return input;
		};
		return function;
	}

	//Converts a function to a unary operator
	public static <T> UnaryOperator<T> convertFunctionToUnaryOperator(final Function<T, T> function) {
		if(function == null) {
			throw new IllegalArgumentException("The function must not be null");
		}
		final UnaryOperator<T> unaryOperator = input -> function.apply(input);
		return unaryOperator;
	}

	//Converts a function to an identity unary operator
	public static <T> UnaryOperator<T> convertFunctionToIdentityUnaryOperator(final Function<T, T> function) {
		if(function == null) {
			throw new IllegalArgumentException("The function must not be null");
		}
		final UnaryOperator<T> unaryOperator = input -> {
			function.apply(input);
			return input;
		};
		return unaryOperator;
	}

	//Converts a predicate to a consumer
	public static <T> Consumer<T> convertPredicateToConsumer(final Predicate<T> predicate) {
		if(predicate == null) {
			throw new IllegalArgumentException("The predicate must not be null");
		}
		final Consumer<T> consumer = input -> {
			predicate.test(input);
		};
		return consumer;
	}

	//Converts a predicate to a function
	public static <T> Function<T, Boolean> convertPredicateToFunction(final Predicate<T> predicate) {
		if(predicate == null) {
			throw new IllegalArgumentException("The predicate must not be null");
		}
		final Function<T, Boolean> function = input -> predicate.test(input);
		return function;
	}

	//Converts a predicate to an identity function
	public static <T> Function<T, T> convertPredicateToIdentityFunction(final Predicate<T> predicate) {
		if(predicate == null) {
			throw new IllegalArgumentException("The predicate must not be null");
		}
		final Function<T, T> function = input -> {
			predicate.test(input);
			return input;
		};
		return function;
	}

	//Converts a function to a predicate
	public static <T> Predicate<T> convertFunctionToPredicate(final Function<T, Boolean> function) {
		if(function == null) {
			throw new IllegalArgumentException("The function must not be null");
		}
		final Predicate<T> predicate = input -> function.apply(input);
		return predicate;
	}

	//------------------------------------------------------------------------------------------------------------------
	// Other conversions with no input element
	//------------------------------------------------------------------------------------------------------------------

	//Converts a callable to a runnable
	public static <T> Runnable convertCallableToRunnable(final Callable<T> callable) {
		if(callable == null) {
			throw new IllegalArgumentException("The callable must not be null");
		}
		final Runnable runnable = () -> {
			try {
				callable.call();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		};
		return runnable;
	}

	//Converts a runnable to a callable with Void output
	public static Callable<Void> convertRunnableToCallable(final Runnable runnable) {
		if(runnable == null) {
			throw new IllegalArgumentException("The runnable must not be null");
		}
		final Callable<Void> callable = () -> {
			runnable.run();
			return null;
		};
		return callable;
	}

	//Converts a supplier to a callable
	public static <T> Callable<T> convertSupplierToCallable(final Supplier<T> supplier) {
		if(supplier == null) {
			throw new IllegalArgumentException("The supplier must not be null");
		}
		final Callable<T> callable = () -> supplier.get();
		return callable;
	}

	//Converts a callable to a supplier
	public static <T> Supplier<T> convertCallableToSupplier(final Callable<T> callable) {
		if(callable == null) {
			throw new IllegalArgumentException("The callable must not be null");
		}
		final Supplier<T> supplier = () -> {
			try {
				return callable.call();
			} catch (final Exception e) {
				throw new RuntimeException(e);
			}
		};
		return supplier;
	}

	//Converts a supplier to a runnable
	public static <T> Runnable convertSupplierToRunnable(final Supplier<T> supplier) {
		if(supplier == null) {
			throw new IllegalArgumentException("The supplier must not be null");
		}
		final Runnable runnable = () -> supplier.get();
		return runnable;
	}

	//Converts a runnable to a supplier with Void output
	public static Supplier<Void> convertRunnableToSupplier(final Runnable runnable) {
		if(runnable == null) {
			throw new IllegalArgumentException("The runnable must not be null");
		}
		final Supplier<Void> supplier = () -> {
			runnable.run();
			return null;
		};
		return supplier;
	}
}
