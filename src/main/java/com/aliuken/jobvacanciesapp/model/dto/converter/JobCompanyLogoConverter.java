package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.JobCompanyLogoDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;

public class JobCompanyLogoConverter extends EntityToDtoConverter<JobCompanyLogo, JobCompanyLogoDTO> {

	private static final JobCompanyLogoConverter SINGLETON_INSTANCE = new JobCompanyLogoConverter();

	private JobCompanyLogoConverter() {
		super(JobCompanyLogoConverter::conversionFunction, JobCompanyLogo.class, JobCompanyLogoDTO.class, JobCompanyLogoDTO[]::new);
	}

	public static JobCompanyLogoConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	private static JobCompanyLogoDTO conversionFunction(final JobCompanyLogo jobCompanyLogo) {
		final JobCompanyLogoDTO jobCompanyLogoDTO;
		if(jobCompanyLogo != null) {
			jobCompanyLogoDTO = new JobCompanyLogoDTO(
				jobCompanyLogo.getId(),
				jobCompanyLogo.getFileName(),
				jobCompanyLogo.getFilePath(),
				jobCompanyLogo.getSelectionName()
			);
		} else {
			jobCompanyLogoDTO = JobCompanyLogoDTO.getNewInstance();
		}
		return jobCompanyLogoDTO;
	}
}