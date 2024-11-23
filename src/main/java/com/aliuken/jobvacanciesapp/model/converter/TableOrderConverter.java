package com.aliuken.jobvacanciesapp.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableOrder;

@Converter(autoApply=true)
public class TableOrderConverter implements AttributeConverter<TableOrder, String> {

	@Override
	public String convertToDatabaseColumn(final TableOrder tableOrder) {
		if(tableOrder == null) {
			return null;
		}

		final String tableOrderCode = tableOrder.getCode();
		return tableOrderCode;
	}

	@Override
	public TableOrder convertToEntityAttribute(final String tableOrderCode) {
		final TableOrder tableOrder = TableOrder.findByCode(tableOrderCode);
		return tableOrder;
	}
}