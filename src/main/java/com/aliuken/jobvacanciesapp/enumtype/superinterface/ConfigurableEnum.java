package com.aliuken.jobvacanciesapp.enumtype.superinterface;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.UserInterfaceFramework;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.superinterface.Internationalizable;
import com.aliuken.jobvacanciesapp.util.javase.ConfigurableEnumUtils;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamStaticUtils;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.util.function.Predicate;

public interface ConfigurableEnum<C, E extends Enum<E> & ConfigurableEnum<C,E>> extends Serializable, Internationalizable {
	public static final @NonNull String BY_DEFAULT_ELEMENT_NAME = "BY_DEFAULT";

	public default boolean isASpecificElement() {
		final E element = GenericsUtils.cast(this);
		final boolean result = !ConfigurableEnum.BY_DEFAULT_ELEMENT_NAME.equals(element.name());
		return result;
	}

	public default @NonNull E getElement() {
		final E element = GenericsUtils.cast(this);
		return element;
	}

	@SuppressWarnings("rawtypes")
	public default @NonNull Class<E> getElementClass() {
		final Class<? extends ConfigurableEnum> thisClass = this.getClass();
		final Class<E> elementClass = GenericsUtils.cast(thisClass);
		return elementClass;
	}

	public default Class<C> getCodeClass() {
		final C code = this.getCode();
		final Class<?> valueClass = code.getClass();
		final Class<C> finalValueClass = GenericsUtils.cast(valueClass);
		return finalValueClass;
	}

	public default @NonNull E getDefaultElement() {
		final Class<E> elementClass = getElementClass();
		final E defaultElement = Enum.valueOf(elementClass, ConfigurableEnum.BY_DEFAULT_ELEMENT_NAME);
		return defaultElement;
	}

	public default @NonNull ConfigurableEnumUtils<C, E> getConfigurableEnumUtils() {
		final Class<E> elementClass = getElementClass();
		final ConfigurableEnumUtils<?, ?> configurableEnumUtils = Constants.CONFIGURABLE_ENUM_UTILS_BY_ENUM_MAP.get(elementClass);
		final ConfigurableEnumUtils<C, E> configurableEnumUtilsCasted = GenericsUtils.cast(configurableEnumUtils);
		return configurableEnumUtilsCasted;
	}

	public abstract @NonNull C getCode();
	public abstract E getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean);
	public abstract E getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean);
	public abstract @NonNull E getFinalDefaultEnumElement();
}