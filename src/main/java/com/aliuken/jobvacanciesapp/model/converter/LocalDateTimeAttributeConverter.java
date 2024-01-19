package com.aliuken.jobvacanciesapp.model.converter;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class LocalDateTimeAttributeConverter implements AttributeConverter<LocalDateTime, Timestamp> {

	@Override
	public Timestamp convertToDatabaseColumn(final LocalDateTime localDateTime) {
		if(localDateTime == null) {
			return null;
		}

		final Timestamp sqlTimestamp = Timestamp.valueOf(localDateTime);
		return sqlTimestamp;
	}

	@Override
	public LocalDateTime convertToEntityAttribute(final Timestamp sqlTimestamp) {
		if(sqlTimestamp == null) {
			return null;
		}

		final LocalDateTime localDateTime = sqlTimestamp.toLocalDateTime();
		return localDateTime;
	}

}