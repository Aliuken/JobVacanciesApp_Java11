package com.aliuken.jobvacanciesapp.model.converter;

import java.sql.Date;
import java.time.LocalDate;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class LocalDateAttributeConverter implements AttributeConverter<LocalDate, Date> {

	@Override
	public Date convertToDatabaseColumn(LocalDate localDate) {
		final Date sqlDate;
		if (localDate != null) {
			sqlDate = Date.valueOf(localDate);
		} else {
			sqlDate = null;
		}

		return sqlDate;
	}

	@Override
	public LocalDate convertToEntityAttribute(Date sqlDate) {
		final LocalDate localDate;
		if (sqlDate != null) {
			localDate = sqlDate.toLocalDate();
		} else {
			localDate = null;
		}

		return localDate;
	}

}
