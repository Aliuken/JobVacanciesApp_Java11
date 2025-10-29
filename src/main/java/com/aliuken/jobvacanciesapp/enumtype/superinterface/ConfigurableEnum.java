package com.aliuken.jobvacanciesapp.enumtype.superinterface;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.superinterface.Internationalizable;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;

import java.io.Serializable;

public interface ConfigurableEnum<T extends Enum<T>> extends Serializable, Internationalizable {
	public static final String BY_DEFAULT_ELEMENT_NAME = "BY_DEFAULT";

	public default boolean hasASpecificValue() {
		final T enumElement = GenericsUtils.cast(this);
		final boolean result = !ConfigurableEnum.BY_DEFAULT_ELEMENT_NAME.equals(enumElement.name());
		return result;
	}

	public default ConfigurableEnum<T> getDefaultConfigurableEnumElement(final Class<T> enumClass) {
		final T defaultEnumElement = this.getDefaultEnumElement(enumClass);
		final ConfigurableEnum<T> defaultConfigurableEnumElement = GenericsUtils.cast(defaultEnumElement);
		return defaultConfigurableEnumElement;
	}

	public default T getDefaultEnumElement(final Class<T> enumClass) {
		final T defaultEnumElement = Enum.valueOf(enumClass, ConfigurableEnum.BY_DEFAULT_ELEMENT_NAME);
		return defaultEnumElement;
	}

	public abstract ConfigurableEnum<T> getOverwrittenEnumElement(final ConfigPropertiesBean configPropertiesBean);
	public abstract ConfigurableEnum<T> getOverwritableEnumElement(final ConfigPropertiesBean configPropertiesBean);
	public abstract ConfigurableEnum<T> getFinalDefaultEnumElement();

	public default <U extends ConfigurableEnum<T>> ConfigurableEnum<T>[] getEnumElements(final Class<U> enumClass) {
		final ConfigurableEnum<T>[] values = enumClass.getEnumConstants();
		return values;
	}
}
