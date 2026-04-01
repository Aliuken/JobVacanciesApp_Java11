package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamStaticUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamUtilsImpl;
import com.aliuken.jobvacanciesapp.util.javase.stream.superinterface.StreamUtils;
import com.aliuken.jobvacanciesapp.util.spring.di.BeanFactoryUtils;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ConfigurableEnumUtils {
	private static final @NonNull ConfigurableEnumUtils SINGLETON_INSTANCE = new ConfigurableEnumUtils();

	private ConfigurableEnumUtils(){}

	public static @NonNull ConfigurableEnumUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public <T extends Enum<T> & ConfigurableEnum<T>> String getConfigurableEnumMessage(final T configurableEnumElement, final @NonNull Class<T> configurableEnumClass, final Language language) {
		final String configurableEnumMessage;
		if(this.isASpecificElement(configurableEnumElement)) {
			configurableEnumMessage = configurableEnumElement.getMessage(language);
		} else {
			final ConfigurableEnum<T> defaultElement = this.getDefaultElement(configurableEnumClass);
			final String defaultElementMessage = defaultElement.getMessage(language);

			final ConfigurableEnum<T> finalElement = this.getCurrentDefaultElement(configurableEnumClass);
			final String finalElementMessage = finalElement.getMessage(language);

			configurableEnumMessage = StringUtils.getStringJoined(finalElementMessage, " (", defaultElementMessage, ")");
		}
		return configurableEnumMessage;
	}

	public <T extends Enum<T> & ConfigurableEnum<T>> String getConfigurableEnumMessage(final T initialElement, final @NonNull T finalElement, final @NonNull Class<T> configurableEnumClass, final Language language) {
		final String configurableEnumMessage;
		if(this.isASpecificElement(initialElement)) {
			configurableEnumMessage = initialElement.getMessage(language);
		} else {
			final ConfigurableEnum<T> defaultElement = this.getDefaultElement(configurableEnumClass);
			final String defaultElementMessage = defaultElement.getMessage(language);

			final String finalElementMessage = finalElement.getMessage(language);

			configurableEnumMessage = StringUtils.getStringJoined(finalElementMessage, " (", defaultElementMessage, ")");
		}
		return configurableEnumMessage;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public <T extends Enum<T> & ConfigurableEnum<T>> @NonNull T getFinalElement(final T configurableEnumElement, final @NonNull Class<T> configurableEnumClass) {
		final T finalEnumElement;
		if(this.isASpecificElement(configurableEnumElement)) {
			finalEnumElement = configurableEnumElement;
		} else {
			finalEnumElement = this.getCurrentDefaultElement(configurableEnumClass);
		}
		return finalEnumElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public <T extends Enum<T> & ConfigurableEnum<T>> @NonNull T getCurrentDefaultElement(final @NonNull Class<T> configurableEnumClass) {
		final ConfigPropertiesBean configPropertiesBean = BeanFactoryUtils.getBean(ConfigPropertiesBean.class);

		final T currentDefaultElement = this.getCurrentDefaultElement(configurableEnumClass, configPropertiesBean);
		return currentDefaultElement;
	}

	public <T extends Enum<T> & ConfigurableEnum<T>> @NonNull T getCurrentDefaultElement(final @NonNull Class<T> configurableEnumClass, final ConfigPropertiesBean configPropertiesBean) {
		final T defaultConfigurableEnumElement = this.getDefaultElement(configurableEnumClass);

		final List<@NonNull Supplier<T>> configurableEnumSuppliers = List.of(
			() -> defaultConfigurableEnumElement.getOverwrittenEnumElement(configPropertiesBean),
			() -> defaultConfigurableEnumElement.getOverwritableEnumElement(configPropertiesBean)
		);

		final Supplier<@NonNull T> finalDefaultEnumElementSupplier = () -> defaultConfigurableEnumElement.getFinalDefaultEnumElement();

		final T currentDefaultElement = getFirstElementThatIsSpecificLazily(configurableEnumSuppliers, finalDefaultEnumElementSupplier, configurableEnumClass);
		return currentDefaultElement;
	}

	public <T extends Enum<T> & ConfigurableEnum<T>> @NonNull ConfigurableEnum<T> getElementOrDefault(final T configurableEnumElement, final @NonNull Class<T> configurableEnumClass) {
		final ConfigurableEnum<T> resultConfigurableEnumElement;
		if(configurableEnumElement != null) {
			resultConfigurableEnumElement = configurableEnumElement;
		} else {
			final ConfigurableEnum<T> defaultElement = this.getDefaultElement(configurableEnumClass);
			resultConfigurableEnumElement = defaultElement.getFinalDefaultEnumElement();
		}
		return resultConfigurableEnumElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public <T extends Enum<T> & ConfigurableEnum<T>> T getCurrentOverwrittenElement(final @NonNull Class<T> configurableEnumClass, final ConfigPropertiesBean configPropertiesBean) {
		final T defaultElement = this.getDefaultElement(configurableEnumClass);

		final T overwrittenEnumElement = defaultElement.getOverwrittenEnumElement(configPropertiesBean);

		final T currentOverwrittenConfigurableEnumElement;
		if(this.isASpecificElement(overwrittenEnumElement)) {
			currentOverwrittenConfigurableEnumElement = overwrittenEnumElement;
		} else {
			currentOverwrittenConfigurableEnumElement = defaultElement;
		}
		return currentOverwrittenConfigurableEnumElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public <T extends Enum<T> & ConfigurableEnum<T>> @NonNull T getFirstElementThatIsSpecificLazily(final @NonNull Collection<@NonNull Supplier<T>> initialElementSuppliers, final @NonNull Supplier<@NonNull T> defaultElementSupplier, final @NonNull Class<T> elementClass) {
		final StreamUtils<T> sequentialStreamUtils = StreamUtilsImpl.getInstance(elementClass, false);
		final Predicate<@NonNull T> elementCondition = ConfigurableEnum.isASpecificElementPredicate();

		final T finalElement = sequentialStreamUtils.getFirstElementFilteredByConditionLazily(initialElementSuppliers, elementCondition, defaultElementSupplier);
		return finalElement;
	}

	public <T extends Enum<T> & ConfigurableEnum<T>> @NonNull T getFirstElementThatIsSpecific(final @NonNull Collection<T> initialElements, final @NonNull T defaultElement, final @NonNull Class<T> elementClass) {
		final StreamUtils<T> sequentialStreamUtils = StreamUtilsImpl.getInstance(elementClass, false);
		final Predicate<@NonNull T> elementCondition = ConfigurableEnum.isASpecificElementPredicate();

		final T finalElement = sequentialStreamUtils.getFirstElementFilteredByCondition(initialElements, elementCondition, defaultElement);
		return finalElement;
	}

	public <T extends Enum<T> & ConfigurableEnum<T>> @NonNull T getDefaultElement(final @NonNull Class<T> configurableEnumClass) {
		final T defaultElement = Enum.valueOf(configurableEnumClass, ConfigurableEnum.BY_DEFAULT_ELEMENT_NAME);
		return defaultElement;
	}

	public <T extends Enum<T> & ConfigurableEnum<T>> @NonNull T[] getSpecificElements(final @NonNull Class<T> configurableEnumClass) {
		final T[] elements = this.getElements(configurableEnumClass);

		final List<T> elementsWithoutByDefaultList = new ArrayList<>();
		for(final T element : elements) {
			if(this.isASpecificElement(element)) {
				elementsWithoutByDefaultList.add(element);
			}
		}

		final T[] elementsWithoutByDefault = GenericsUtils.getCollectionAsArray(configurableEnumClass, elementsWithoutByDefaultList);
		return elementsWithoutByDefault;
	}

	public <T extends Enum<T> & ConfigurableEnum<T>> @NonNull T[] getElements(final @NonNull Class<T> configurableEnumClass) {
		final List<T> elementList = StreamStaticUtils.ofEnum(configurableEnumClass, false)
			.collect(Collectors.toList());

		final T[] elements = GenericsUtils.getCollectionAsArray(configurableEnumClass, elementList);
		return elements;
	}

	public <T extends Enum<T> & ConfigurableEnum<T>> boolean isASpecificElement(final T element) {
		final boolean result = (element != null && element.isASpecificElement());
		return result;
	}
}
