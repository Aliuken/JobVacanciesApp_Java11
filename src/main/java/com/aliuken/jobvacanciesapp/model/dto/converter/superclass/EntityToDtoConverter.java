package com.aliuken.jobvacanciesapp.model.dto.converter.superclass;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
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

	public @NonNull U[] convertEntityArray(final T @NonNull [] entityArray) {
		final U[] dtoArray = Constants.PARALLEL_STREAM_UTILS.convertArray(entityArray, conversionFunction, inputClass, outputClass, arrayGenerator);
		return dtoArray;
	}

	public @NonNull List<U> convertEntityList(final @NonNull List<T> entityList) {
		final List<U> dtoList = Constants.PARALLEL_STREAM_UTILS.convertList(entityList, conversionFunction, inputClass, outputClass);
		return dtoList;
	}

	public @NonNull Set<U> convertEntitySet(final @NonNull Set<T> entitySet) {
		final Set<U> dtoSet = Constants.PARALLEL_STREAM_UTILS.convertSet(entitySet, conversionFunction, inputClass, outputClass);
		return dtoSet;
	}
}