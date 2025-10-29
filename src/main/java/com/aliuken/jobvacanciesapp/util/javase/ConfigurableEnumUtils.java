package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.spring.di.BeanFactoryUtils;

import java.util.ArrayList;
import java.util.List;

public class ConfigurableEnumUtils {
	private static final ConfigurableEnumUtils SINGLETON_INSTANCE = new ConfigurableEnumUtils();

	private ConfigurableEnumUtils(){}

	public static ConfigurableEnumUtils getInstance() {
		return SINGLETON_INSTANCE;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> String getConfigurableEnumMessage(final ConfigurableEnum<T> configurableEnumElement, final Class<U> configurableEnumClass, final Language language) {
		final String configurableEnumMessage;
		if(this.hasASpecificValue(configurableEnumElement)) {
			configurableEnumMessage = configurableEnumElement.getMessage(language);
		} else {
			final ConfigurableEnum<T> defaultConfigurableEnumElement = this.getDefaultConfigurableEnumElement(configurableEnumClass);
			final String initialConfigurableEnumMessage = defaultConfigurableEnumElement.getMessage(language);

			final ConfigurableEnum<T> finalConfigurableEnumElement = this.getCurrentDefaultConfigurableEnumElement(configurableEnumClass);
			final String finalConfigurableEnumMessage = finalConfigurableEnumElement.getMessage(language);

			configurableEnumMessage = StringUtils.getStringJoined(finalConfigurableEnumMessage, " (", initialConfigurableEnumMessage, ")");
		}
		return configurableEnumMessage;
	}

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> String getConfigurableEnumMessage(final ConfigurableEnum<T> initialConfigurableEnumElement, final ConfigurableEnum<T> finalConfigurableEnumElement, final Class<U> configurableEnumClass, final Language language) {
		final String configurableEnumMessage;
		if(this.hasASpecificValue(initialConfigurableEnumElement)) {
			configurableEnumMessage = initialConfigurableEnumElement.getMessage(language);
		} else {
			final ConfigurableEnum<T> defaultConfigurableEnumElement = this.getDefaultConfigurableEnumElement(configurableEnumClass);
			final String initialConfigurableEnumMessage = defaultConfigurableEnumElement.getMessage(language);

			final String finalConfigurableEnumMessage = finalConfigurableEnumElement.getMessage(language);

			configurableEnumMessage = StringUtils.getStringJoined(finalConfigurableEnumMessage, " (", initialConfigurableEnumMessage, ")");
		}
		return configurableEnumMessage;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> ConfigurableEnum<T> getFinalConfigurableEnumElement(final ConfigurableEnum<T> configurableEnumElement, final Class<U> configurableEnumClass) {
		final ConfigurableEnum<T> finalEnumElement;
		if(this.hasASpecificValue(configurableEnumElement)) {
			finalEnumElement = configurableEnumElement;
		} else {
			finalEnumElement = this.getCurrentDefaultConfigurableEnumElement(configurableEnumClass);
		}
		return finalEnumElement;
	}

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> T getFinalEnumElement(final ConfigurableEnum<T> configurableEnumElement, final Class<U> configurableEnumClass) {
		final T finalEnumElement;
		if(this.hasASpecificValue(configurableEnumElement)) {
			finalEnumElement = GenericsUtils.cast(configurableEnumElement);
		} else {
			finalEnumElement = this.getCurrentDefaultEnumElement(configurableEnumClass);
		}
		return finalEnumElement;
	}

	public <T extends Enum<T>> ConfigurableEnum<T> getFinalConfigurableEnumElement(final ConfigurableEnum<T> configurableEnumElement, final ConfigurableEnum<T> currentDefaultConfigurableEnumElement) {
		final ConfigurableEnum<T> finalEnumElement;
		if(this.hasASpecificValue(configurableEnumElement)) {
			finalEnumElement = configurableEnumElement;
		} else {
			finalEnumElement = currentDefaultConfigurableEnumElement;
		}
		return finalEnumElement;
	}

	public <T extends Enum<T>> T getFinalEnumElement(final ConfigurableEnum<T> configurableEnumElement, final ConfigurableEnum<T> currentDefaultConfigurableEnumElement) {
		final T finalEnumElement;
		if(this.hasASpecificValue(configurableEnumElement)) {
			finalEnumElement = GenericsUtils.cast(configurableEnumElement);
		} else {
			finalEnumElement = GenericsUtils.cast(currentDefaultConfigurableEnumElement);
		}
		return finalEnumElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> T getCurrentDefaultEnumElement(final Class<U> configurableEnumClass) {
		final ConfigPropertiesBean configPropertiesBean = BeanFactoryUtils.getBean(ConfigPropertiesBean.class);

		final T currentDefaultEnumElement = this.getCurrentDefaultEnumElement(configurableEnumClass, configPropertiesBean);
		return currentDefaultEnumElement;
	}

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> ConfigurableEnum<T> getCurrentDefaultConfigurableEnumElement(final Class<U> configurableEnumClass) {
		final ConfigPropertiesBean configPropertiesBean = BeanFactoryUtils.getBean(ConfigPropertiesBean.class);

		final ConfigurableEnum<T> currentDefaultConfigurableEnumElement = this.getCurrentDefaultConfigurableEnumElement(configurableEnumClass, configPropertiesBean);
		return currentDefaultConfigurableEnumElement;
	}

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> T getCurrentDefaultEnumElement(final Class<U> configurableEnumClass, final ConfigPropertiesBean configPropertiesBean) {
		final ConfigurableEnum<T> currentDefaultConfigurableEnumElement = this.getCurrentDefaultConfigurableEnumElement(configurableEnumClass, configPropertiesBean);

		final T currentDefaultEnumElement = GenericsUtils.cast(currentDefaultConfigurableEnumElement);
		return currentDefaultEnumElement;
	}

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> ConfigurableEnum<T> getCurrentDefaultConfigurableEnumElement(final Class<U> configurableEnumClass, final ConfigPropertiesBean configPropertiesBean) {
		final ConfigurableEnum<T> defaultConfigurableEnumElement = this.getDefaultConfigurableEnumElement(configurableEnumClass);

		final ConfigurableEnum<T> overwrittenEnumElement = defaultConfigurableEnumElement.getOverwrittenEnumElement(configPropertiesBean);

		final ConfigurableEnum<T> currentDefaultConfigurableEnumElement;
		if(this.hasASpecificValue(overwrittenEnumElement)) {
			currentDefaultConfigurableEnumElement = overwrittenEnumElement;
		} else {
			final ConfigurableEnum<T> overwritableEnumElement = defaultConfigurableEnumElement.getOverwritableEnumElement(configPropertiesBean);
			if(this.hasASpecificValue(overwritableEnumElement)) {
				currentDefaultConfigurableEnumElement = overwritableEnumElement;
			} else {
				currentDefaultConfigurableEnumElement = defaultConfigurableEnumElement.getFinalDefaultEnumElement();
			}
		}
		return currentDefaultConfigurableEnumElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> T getCurrentOverwrittenEnumElement(final Class<U> configurableEnumClass) {
		final ConfigPropertiesBean configPropertiesBean = BeanFactoryUtils.getBean(ConfigPropertiesBean.class);

		final T currentOverwrittenEnumElement = this.getCurrentOverwrittenEnumElement(configurableEnumClass, configPropertiesBean);
		return currentOverwrittenEnumElement;
	}

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> ConfigurableEnum<T> getCurrentOverwrittenConfigurableEnumElement(final Class<U> configurableEnumClass) {
		final ConfigPropertiesBean configPropertiesBean = BeanFactoryUtils.getBean(ConfigPropertiesBean.class);

		final ConfigurableEnum<T> currentOverwrittenConfigurableEnumElement = this.getCurrentOverwrittenConfigurableEnumElement(configurableEnumClass, configPropertiesBean);
		return currentOverwrittenConfigurableEnumElement;
	}

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> T getCurrentOverwrittenEnumElement(final Class<U> configurableEnumClass, final ConfigPropertiesBean configPropertiesBean) {
		final ConfigurableEnum<T> currentOverwrittenConfigurableEnumElement = this.getCurrentOverwrittenConfigurableEnumElement(configurableEnumClass, configPropertiesBean);

		final T currentOverwrittenEnumElement = GenericsUtils.cast(currentOverwrittenConfigurableEnumElement);
		return currentOverwrittenEnumElement;
	}

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> ConfigurableEnum<T> getCurrentOverwrittenConfigurableEnumElement(final Class<U> configurableEnumClass, final ConfigPropertiesBean configPropertiesBean) {
		final ConfigurableEnum<T> defaultConfigurableEnumElement = this.getDefaultConfigurableEnumElement(configurableEnumClass);

		final ConfigurableEnum<T> overwrittenEnumElement = defaultConfigurableEnumElement.getOverwrittenEnumElement(configPropertiesBean);

		final ConfigurableEnum<T> currentOverwrittenConfigurableEnumElement;
		if(this.hasASpecificValue(overwrittenEnumElement)) {
			currentOverwrittenConfigurableEnumElement = overwrittenEnumElement;
		} else {
			currentOverwrittenConfigurableEnumElement = defaultConfigurableEnumElement;
		}
		return currentOverwrittenConfigurableEnumElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> List<T> getSpecificEnumElements(final Class<U> configurableEnumClass) {
		final List<ConfigurableEnum<T>> configurableEnumList = this.getSpecificConfigurableEnumElements(configurableEnumClass);

		final List<T> enumList = GenericsUtils.cast(configurableEnumList);
		return enumList;
	}

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> List<ConfigurableEnum<T>> getSpecificConfigurableEnumElements(final Class<U> configurableEnumClass) {
		final ConfigurableEnum<T> defaultConfigurableEnumElement = this.getDefaultConfigurableEnumElement(configurableEnumClass);
		final ConfigurableEnum<T>[] configurableEnumElements = defaultConfigurableEnumElement.getEnumElements(configurableEnumClass);

		final List<ConfigurableEnum<T>> configurableEnumList = new ArrayList<>();
		for(ConfigurableEnum<T> configurableEnumElement : configurableEnumElements) {
			if(this.hasASpecificValue(configurableEnumElement)) {
				configurableEnumList.add(configurableEnumElement);
			}
		}

		return configurableEnumList;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public <T extends Enum<T>> boolean hasASpecificValue(final ConfigurableEnum<T> configurableEnumElement) {
		final T enumElement = GenericsUtils.cast(configurableEnumElement);
		final boolean result = (enumElement != null && !ConfigurableEnum.BY_DEFAULT_ELEMENT_NAME.equals(enumElement.name()));
		return result;
	}

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> ConfigurableEnum<T> getDefaultConfigurableEnumElement(final Class<U> configurableEnumClass) {
		final T defaultEnumElement = this.getDefaultEnumElement(configurableEnumClass);
		final ConfigurableEnum<T> defaultConfigurableEnumElement = GenericsUtils.cast(defaultEnumElement);
		return defaultConfigurableEnumElement;
	}

	public <T extends Enum<T>, U extends ConfigurableEnum<T>> T getDefaultEnumElement(final Class<U> configurableEnumClass) {
		final Class<T> enumClass = GenericsUtils.cast(configurableEnumClass);
		final T defaultEnumElement = Enum.valueOf(enumClass, ConfigurableEnum.BY_DEFAULT_ELEMENT_NAME);
		return defaultEnumElement;
	}
}
