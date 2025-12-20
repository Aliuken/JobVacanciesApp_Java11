package com.aliuken.jobvacanciesapp.model.converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class TablePageSizeConverter implements AttributeConverter<TablePageSize, Integer> {

	@Override
	public Integer convertToDatabaseColumn(final TablePageSize tablePageSize) {
		if(tablePageSize == null) {
			return TablePageSize.BY_DEFAULT.getValue();
		}

		final Integer tablePageSizeValue = tablePageSize.getValue();
		return tablePageSizeValue;
	}

	@Override
	public TablePageSize convertToEntityAttribute(final Integer tablePageSizeValue) {
		final TablePageSize tablePageSize;
		if(tablePageSizeValue != null) {
			tablePageSize = TablePageSize.findByValue(tablePageSizeValue);
		} else {
			tablePageSize = TablePageSize.BY_DEFAULT;
		}
		return tablePageSize;
	}
}