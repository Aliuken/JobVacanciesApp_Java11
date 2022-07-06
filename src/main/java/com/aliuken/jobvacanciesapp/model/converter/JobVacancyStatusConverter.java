package com.aliuken.jobvacanciesapp.model.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import com.aliuken.jobvacanciesapp.model.JobVacancyStatus;

@Converter(autoApply=true)
public class JobVacancyStatusConverter implements AttributeConverter<JobVacancyStatus, String> {

	@Override
	public String convertToDatabaseColumn(JobVacancyStatus jobVacancyStatus) {
		if (jobVacancyStatus == null) {
			return null;
		}

		return jobVacancyStatus.getCode();
	}

	@Override
	public JobVacancyStatus convertToEntityAttribute(String code) {
		JobVacancyStatus jobVacancyStatus = JobVacancyStatus.findByCode(code);

		return jobVacancyStatus;
	}

}