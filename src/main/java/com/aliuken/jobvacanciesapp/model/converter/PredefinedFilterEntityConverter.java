package com.aliuken.jobvacanciesapp.model.converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.PredefinedFilterEntity;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class PredefinedFilterEntityConverter implements AttributeConverter<PredefinedFilterEntity, String> {

	@Override
	public String convertToDatabaseColumn(final PredefinedFilterEntity predefinedFilterEntity) {
		if(predefinedFilterEntity == null) {
			return null;
		}

		final String predefinedFilterEntityName = predefinedFilterEntity.getUpperCasedEntityName();
		return predefinedFilterEntityName;
	}

	@Override
	public PredefinedFilterEntity convertToEntityAttribute(final String predefinedFilterEntityName) {
		final PredefinedFilterEntity predefinedFilterEntity = PredefinedFilterEntity.findByEntityName(predefinedFilterEntityName);
		return predefinedFilterEntity;
	}
}