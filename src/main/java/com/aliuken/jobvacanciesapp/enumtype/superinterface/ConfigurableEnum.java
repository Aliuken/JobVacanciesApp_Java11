package com.aliuken.jobvacanciesapp.enumtype.superinterface;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.superinterface.Internationalizable;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.util.function.Predicate;

public interface ConfigurableEnum<T extends Enum<T> & ConfigurableEnum<T>> extends Serializable, Internationalizable {
	public static final @NonNull String BY_DEFAULT_ELEMENT_NAME = "BY_DEFAULT";

	public static <U extends Enum<U> & ConfigurableEnum<U>> @NonNull Predicate<@NonNull U> isASpecificElementPredicate() {
		return element -> element.isASpecificElement();
	}

	public default boolean isASpecificElement() {
		final T element = GenericsUtils.cast(this);
		final boolean result = !ConfigurableEnum.BY_DEFAULT_ELEMENT_NAME.equals(element.name());
		return result;
	}

	public abstract @NonNull Class<T> getEnumClass();
	public abstract T getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean);
	public abstract T getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean);
	public abstract @NonNull T getFinalDefaultEnumElement();
}