package com.aliuken.jobvacanciesapp.util.javase;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamStaticUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamUtilsImpl;
import com.aliuken.jobvacanciesapp.util.javase.stream.superinterface.StreamUtils;
import com.aliuken.jobvacanciesapp.util.spring.di.BeanFactoryUtils;
import lombok.Data;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Data
public class ConfigurableEnumUtils<V, E extends Enum<E> & ConfigurableEnum<V,E>> {
	private static final @NonNull Map<Class<? extends ConfigurableEnum<?,?>>, ConfigurableEnumUtils<?,?>> CACHE = new ConcurrentHashMap<>();

	private final @NonNull Class<V> valueClass;
	private final @NonNull Class<E> elementClass;

	private final @NonNull E defaultElement;
	private final @NonNull E finalDefaultElement;

	private final @NonNull Supplier<@NonNull E> defaultElementSupplier;
	private final @NonNull Supplier<@NonNull E> finalDefaultElementSupplier;

	private final @NonNull Predicate<E> specificElementPredicate;
	private final @NonNull IntFunction<E[]> arrayGenerator;

	private final E @NonNull [] elements;
	private final E @NonNull [] specificElements;

	private final @NonNull StreamUtils<E> elementSequentialStreamUtils;

	private ConfigurableEnumUtils(final @NonNull Class<V> valueClass, final @NonNull Class<E> elementClass) {
		this.valueClass = valueClass;
		this.elementClass = elementClass;

		this.defaultElement = Enum.valueOf(elementClass, ConfigurableEnum.BY_DEFAULT_ELEMENT_NAME);
		this.finalDefaultElement = this.defaultElement.getFinalDefaultEnumElement();

		this.defaultElementSupplier = () -> defaultElement;
		this.finalDefaultElementSupplier = () -> finalDefaultElement;

		this.specificElementPredicate = element -> this.isASpecificElement(element);
		this.arrayGenerator = GenericsUtils.getArrayGenerator(elementClass);

		this.elements = elementClass.getEnumConstants();
		this.specificElements = Arrays.stream(this.elements)
			.filter(this.specificElementPredicate)
			.toArray(this.arrayGenerator);

		this.elementSequentialStreamUtils = StreamUtilsImpl.getInstance(elementClass, false);
	}

	// ==========================================================
	// ConfigurableEnumUtils factory methods
	// ==========================================================

	public static <T, U extends Enum<U> & ConfigurableEnum<T,U>> @NonNull ConfigurableEnumUtils<T,U> getInstance(final @NonNull Class<T> valueClass, final @NonNull Class<U> elementClass) {
		final ConfigurableEnumUtils<?,?> configurableEnumUtils = CACHE.computeIfAbsent(elementClass, elementClassAux -> new ConfigurableEnumUtils<>(valueClass, elementClass));
		final ConfigurableEnumUtils<T,U> finalConfigurableEnumUtils = GenericsUtils.cast(configurableEnumUtils);

		return finalConfigurableEnumUtils;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public String getConfigurableEnumMessage(final E configurableEnumElement, final Language language) {
		final String configurableEnumMessage;
		if(this.isASpecificElement(configurableEnumElement)) {
			configurableEnumMessage = configurableEnumElement.getMessage(language);
		} else {
			final String defaultElementMessage = defaultElement.getMessage(language);

			final ConfigurableEnum<V,E> finalElement = this.getCurrentDefaultElement();
			final String finalElementMessage = finalElement.getMessage(language);

			configurableEnumMessage = StringUtils.getStringJoined(finalElementMessage, " (", defaultElementMessage, ")");
		}
		return configurableEnumMessage;
	}

	public String getConfigurableEnumMessage(final E initialElement, final @NonNull E finalElement, final Language language) {
		final String configurableEnumMessage;
		if(this.isASpecificElement(initialElement)) {
			configurableEnumMessage = initialElement.getMessage(language);
		} else {
			final String defaultElementMessage = defaultElement.getMessage(language);

			final String finalElementMessage = finalElement.getMessage(language);

			configurableEnumMessage = StringUtils.getStringJoined(finalElementMessage, " (", defaultElementMessage, ")");
		}
		return configurableEnumMessage;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public @NonNull E getFinalElement(final E configurableEnumElement) {
		final E finalEnumElement;
		if(this.isASpecificElement(configurableEnumElement)) {
			Objects.requireNonNull(configurableEnumElement, "configurableEnumElement cannot be null");
			finalEnumElement = configurableEnumElement;
		} else {
			finalEnumElement = this.getCurrentDefaultElement();
		}
		return finalEnumElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public @NonNull E getCurrentDefaultElement() {
		final ConfigPropertiesBean configPropertiesBean = BeanFactoryUtils.getBean(ConfigPropertiesBean.class);

		final E currentDefaultElement = this.getCurrentDefaultElement(configPropertiesBean);
		return currentDefaultElement;
	}

	public @NonNull E getCurrentDefaultElement(final ConfigPropertiesBean configPropertiesBean) {
		final List<@NonNull Supplier<E>> configurableEnumSuppliers = List.of(
			() -> defaultElement.getOverwrittenEnumElement(configPropertiesBean),
			() -> defaultElement.getOverwritableEnumElement(configPropertiesBean)
		);

		final Supplier<@NonNull E> finalDefaultEnumElementSupplier = () -> finalDefaultElement;

		final E currentDefaultElement = getFirstElementThatIsSpecificLazily(configurableEnumSuppliers, finalDefaultEnumElementSupplier);
		return currentDefaultElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public @NonNull E getCurrentOverwrittenElement(final ConfigPropertiesBean configPropertiesBean) {
		final E overwrittenEnumElement = defaultElement.getOverwrittenEnumElement(configPropertiesBean);

		final E currentOverwrittenElement;
		if(this.isASpecificElement(overwrittenEnumElement)) {
			Objects.requireNonNull(overwrittenEnumElement, "overwrittenEnumElement cannot be null");
			currentOverwrittenElement = overwrittenEnumElement;
		} else {
			currentOverwrittenElement = defaultElement;
		}
		return currentOverwrittenElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public @NonNull E getFirstElementThatIsSpecificLazily(final @NonNull Collection<@NonNull Supplier<E>> initialElementSuppliers, final @NonNull Supplier<@NonNull E> defaultElementSupplier) {
		final E finalElement = elementSequentialStreamUtils.getFirstElementFilteredByConditionLazily(initialElementSuppliers, specificElementPredicate, defaultElementSupplier);
		return finalElement;
	}

	public @NonNull E getFirstElementThatIsSpecific(final @NonNull Collection<E> initialElements, final @NonNull E defaultElement) {
		final E finalElement = elementSequentialStreamUtils.getFirstElementFilteredByCondition(initialElements, specificElementPredicate, defaultElement);
		return finalElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public @NonNull E getSpecificElementOrDefault(final E initialElement) {
		final E element = elementSequentialStreamUtils.resolveElement(initialElement, specificElementPredicate, defaultElementSupplier);
		return element;
	}

	public @NonNull E getSpecificElementOrFinalDefault(final E initialElement) {
		final E element = elementSequentialStreamUtils.resolveElement(initialElement, specificElementPredicate, finalDefaultElementSupplier);
		return element;
	}

	public E getSpecificElementOrNull(final E initialElement) {
		final E element = elementSequentialStreamUtils.resolveElement(initialElement, specificElementPredicate);
		return element;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public @NonNull E findSpecificElementByCodeOrDefault(final @NonNull V code) {
		final E initialElement = this.findElementByCodeOrNull(code);
		final E finalElement = elementSequentialStreamUtils.resolveElement(initialElement, specificElementPredicate, defaultElementSupplier);
		return finalElement;
	}

	public @NonNull E findSpecificElementByCodeOrFinalDefault(final @NonNull V code) {
		final E initialElement = this.findElementByCodeOrNull(code);
		final E finalElement = elementSequentialStreamUtils.resolveElement(initialElement, specificElementPredicate, finalDefaultElementSupplier);
		return finalElement;
	}

	public E findSpecificElementByCodeOrNull(final @NonNull V code) {
		final E initialElement = this.findElementByCodeOrNull(code);
		final E finalElement = elementSequentialStreamUtils.resolveElement(initialElement, specificElementPredicate);
		return finalElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public @NonNull E getElementOrDefault(final E initialElement) {
		final E element = elementSequentialStreamUtils.resolveElement(initialElement, defaultElementSupplier);
		return element;
	}

	public @NonNull E getElementOrFinalDefault(final E initialElement) {
		final E element = elementSequentialStreamUtils.resolveElement(initialElement, finalDefaultElementSupplier);
		return element;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public @NonNull E findElementByCodeOrDefault(final @NonNull V code) {
		final E initialElement = this.findElementByCodeOrNull(code);
		final E finalElement = elementSequentialStreamUtils.resolveElement(initialElement, defaultElementSupplier);
		return finalElement;
	}

	public @NonNull E findElementByCodeOrFinalDefault(final @NonNull V code) {
		final E initialElement = this.findElementByCodeOrNull(code);
		final E finalElement = elementSequentialStreamUtils.resolveElement(initialElement, finalDefaultElementSupplier);
		return finalElement;
	}

//--------------------------------------------------------------------------------------------------------------------------------------------------------------

	public E findElementByCodeOrNull(final V code) {
		return StreamStaticUtils.ofEnum(elementClass, true)
			.filter(e -> code.equals(e.getCode()))
			.findFirst()
			.orElse(null);
	}

	public boolean isASpecificElement(final E element) {
		final boolean result = (element != null && element.isASpecificElement());
		return result;
	}
}
