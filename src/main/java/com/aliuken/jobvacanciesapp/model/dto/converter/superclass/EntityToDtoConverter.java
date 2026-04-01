package com.aliuken.jobvacanciesapp.model.dto.converter.superclass;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamUtilsImpl;
import com.aliuken.jobvacanciesapp.util.javase.stream.superinterface.StreamUtils;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

public abstract class EntityToDtoConverter<T extends AbstractEntity<T>,U extends AbstractEntityDTO> {
	private final @NonNull Class<T> inputClass;
	private final @NonNull Class<U> outputClass;
	private final @NonNull IntFunction<U @NonNull []> arrayGenerator;
	private final @NonNull Function<T,U> conversionFunction = entity -> this.convert(entity);

	protected EntityToDtoConverter(final @NonNull Class<T> inputClass, final @NonNull Class<U> outputClass,
			final @NonNull IntFunction<U @NonNull []> arrayGenerator) {
		this.inputClass = inputClass;
		this.outputClass = outputClass;
		this.arrayGenerator = arrayGenerator;
	}

	protected abstract @NonNull U convert(final @NonNull T entity);

	public @NonNull U convertEntityElement(final T entityElement) {
		final U dtoElement = this.convert(entityElement);
		return dtoElement;
	}

	public U @NonNull [] convertEntityArray(final T @NonNull [] entityArray) {
		final Class<?> entityArrayComponentType = entityArray.getClass().getComponentType();
		final Class<T> entityClass = GenericsUtils.cast(entityArrayComponentType);

		final StreamUtils<T> entityStreamUtils = StreamUtilsImpl.getInstance(entityClass);

		final U[] dtoArray = entityStreamUtils.convertArray(entityArray, conversionFunction, inputClass, outputClass, arrayGenerator);
		return dtoArray;
	}

	public @NonNull List<U> convertEntityList(final @NonNull List<T> entityList, final @NonNull Class<T> entityClass) {
		final StreamUtils<T> entityStreamUtils = StreamUtilsImpl.getInstance(entityClass);

		final List<U> dtoList = entityStreamUtils.convertList(entityList, conversionFunction, inputClass, outputClass);
		return dtoList;
	}

	public @NonNull Set<U> convertEntitySet(final @NonNull Set<T> entitySet, final @NonNull Class<T> entityClass) {
		final StreamUtils<T> entityStreamUtils = StreamUtilsImpl.getInstance(entityClass);

		final Set<U> dtoSet = entityStreamUtils.convertSet(entitySet, conversionFunction, inputClass, outputClass);
		return dtoSet;
	}
}