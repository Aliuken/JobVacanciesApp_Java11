package com.aliuken.jobvacanciesapp.model.converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Objects;

@Converter(autoApply=true)
public class JobVacancyStatusConverter implements AttributeConverter<JobVacancyStatus, String> {

	@Override
	public String convertToDatabaseColumn(final JobVacancyStatus jobVacancyStatus) {
		if(jobVacancyStatus == null) {
			return null;
		}

		final String jobVacancyStatusCode = jobVacancyStatus.getCode();
		return jobVacancyStatusCode;
	}

	@Override
	public JobVacancyStatus convertToEntityAttribute(final String jobVacancyStatusCode) {
		final JobVacancyStatus jobVacancyStatus = JobVacancyStatus.findByCode(jobVacancyStatusCode);
		Objects.requireNonNull(jobVacancyStatus, "jobVacancyStatus cannot be null");
		return jobVacancyStatus;
	}
}