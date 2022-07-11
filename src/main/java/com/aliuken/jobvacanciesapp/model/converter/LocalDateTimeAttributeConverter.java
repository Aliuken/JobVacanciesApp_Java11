package com.aliuken.jobvacanciesapp.model.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(LocalDateTime localDateTime) {
		final Timestamp sqlTimestamp;
		if (localDateTime != null) {
			sqlTimestamp = Timestamp.valueOf(localDateTime);
		} else {
			sqlTimestamp = null;
		}

		return sqlTimestamp;
	}

	@Override
	public LocalDateTime convertToEntityAttribute(Timestamp sqlTimestamp) {
		final LocalDateTime localDateTime;
		if (sqlTimestamp != null) {
			localDateTime = sqlTimestamp.toLocalDateTime();
		} else {
			localDateTime = null;
		}

		return localDateTime;
	}

}