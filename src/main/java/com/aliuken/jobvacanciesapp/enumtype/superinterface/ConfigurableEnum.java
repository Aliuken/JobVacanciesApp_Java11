package com.aliuken.jobvacanciesapp.enumtype.superinterface;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.superinterface.Internationalizable;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;

public interface ConfigurableEnum<T extends Enum<T>> extends Serializable, Internationalizable {
	public static final String BY_DEFAULT_ELEMENT_NAME = "BY_DEFAULT";

	public default boolean hasASpecificValue() {
		final T enumElement = GenericsUtils.cast(this);
		final boolean result = !ConfigurableEnum.BY_DEFAULT_ELEMENT_NAME.equals(enumElement.name());
		return result;
	}

	public abstract @NonNull Class<T> getEnumClass();
	public abstract ConfigurableEnum<T> getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean);
	public abstract ConfigurableEnum<T> getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean);
	public abstract @NonNull ConfigurableEnum<T> getFinalDefaultEnumElement();
}
