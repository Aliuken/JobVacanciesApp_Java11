package com.aliuken.jobvacanciesapp.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSorting;

@Converter(autoApply=true)
public class TableSortingConverter implements AttributeConverter<TableSorting, String> {

	@Override
	public String convertToDatabaseColumn(final TableSorting tableSorting) {
		if(tableSorting == null) {
			return null;
		}

		final String tableSortingCode = tableSorting.getCode();
		return tableSortingCode;
	}

	@Override
	public TableSorting convertToEntityAttribute(final String tableSortingCode) {
		final TableSorting tableSorting = TableSorting.findByCode(tableSortingCode);
		return tableSorting;
	}
}