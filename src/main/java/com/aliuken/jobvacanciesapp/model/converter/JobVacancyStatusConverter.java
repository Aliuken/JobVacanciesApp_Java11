package com.aliuken.jobvacanciesapp.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;

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
		return jobVacancyStatus;
	}

}