package com.aliuken.jobvacanciesapp.model.converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class PageEntityEnumConverter implements AttributeConverter<PageEntityEnum, String> {

	@Override
	public String convertToDatabaseColumn(final PageEntityEnum pageEntity) {
		if(pageEntity == null) {
			return null;
		}

		final String pageEntityValue = pageEntity.getValue();
		return pageEntityValue;
	}

	@Override
	public PageEntityEnum convertToEntityAttribute(final String pageEntityValue) {
		final PageEntityEnum pageEntity;
		if(pageEntityValue != null) {
			pageEntity = PageEntityEnum.findByValue(pageEntityValue);
		} else {
			pageEntity = null;
		}

		return pageEntity;
	}
}