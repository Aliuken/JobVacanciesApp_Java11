package com.aliuken.jobvacanciesapp.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;

@Converter(autoApply=true)
public class TableFieldConverter implements AttributeConverter<TableField, String> {

	@Override
	public String convertToDatabaseColumn(final TableField tableField) {
		if(tableField == null) {
			return null;
		}

		final String filterName = tableField.getCode();
		return filterName;
	}

	@Override
	public TableField convertToEntityAttribute(final String filterName) {
		final TableField tableField = TableField.findByCode(filterName);
		return tableField;
	}
}