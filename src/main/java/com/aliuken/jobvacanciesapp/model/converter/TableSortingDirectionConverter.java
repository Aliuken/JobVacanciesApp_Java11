package com.aliuken.jobvacanciesapp.model.converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class TableSortingDirectionConverter implements AttributeConverter<TableSortingDirection, String> {

	@Override
	public String convertToDatabaseColumn(final TableSortingDirection tableSortingDirection) {
		if(tableSortingDirection == null) {
			return TableSortingDirection.BY_DEFAULT.getCode();
		}

		final String tableSortingDirectionCode = tableSortingDirection.getCode();
		return tableSortingDirectionCode;
	}

	@Override
	public TableSortingDirection convertToEntityAttribute(final String tableSortingDirectionCode) {
		final TableSortingDirection tableSortingDirection;
		if(tableSortingDirectionCode != null) {
			tableSortingDirection = TableSortingDirection.findByCode(tableSortingDirectionCode);
		} else {
			tableSortingDirection = TableSortingDirection.BY_DEFAULT;
		}
		return tableSortingDirection;
	}
}