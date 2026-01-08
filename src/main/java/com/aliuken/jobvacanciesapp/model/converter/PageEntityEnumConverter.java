package com.aliuken.jobvacanciesapp.model.converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

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
		final PageEntityEnum pageEntity = PageEntityEnum.findByValue(pageEntityValue);
		Objects.requireNonNull(pageEntity, "pageEntity cannot be null");
		return pageEntity;
	}
}