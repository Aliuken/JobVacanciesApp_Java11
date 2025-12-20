package com.aliuken.jobvacanciesapp.model.dto.converter.superclass;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.dto.superinterface.AbstractEntityDTO;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.IntFunction;

public abstract class EntityToDtoConverter<T extends AbstractEntity,U extends AbstractEntityDTO> {

	private final Function<T,U> conversionFunction;
	private final Class<T> inputClass;
	private final Class<U> outputClass;
	private final IntFunction<U[]> arrayGenerator;

	protected EntityToDtoConverter(Function<T,U> conversionFunction, Class<T> inputClass, Class<U> outputClass, IntFunction<U[]> arrayGenerator) {
		this.conversionFunction = conversionFunction;
		this.inputClass = inputClass;
		this.outputClass = outputClass;
		this.arrayGenerator = arrayGenerator;
	}

	public U convertEntityElement(T entityElement) {
		final U dtoElement = conversionFunction.apply(entityElement);
		return dtoElement;
	}

	public U[] convertEntityArray(final T[] entityArray) {
		final U[] dtoArray = Constants.PARALLEL_STREAM_UTILS.convertArray(entityArray, conversionFunction, inputClass, outputClass, arrayGenerator);
		return dtoArray;
	}

	public List<U> convertEntityList(final List<T> entityList) {
		final List<U> dtoList = Constants.PARALLEL_STREAM_UTILS.convertList(entityList, conversionFunction, inputClass, outputClass);
		return dtoList;
	}

	public Set<U> convertEntitySet(final Set<T> entitySet) {
		final Set<U> dtoSet = Constants.PARALLEL_STREAM_UTILS.convertSet(entitySet, conversionFunction, inputClass, outputClass);
		return dtoSet;
	}
}