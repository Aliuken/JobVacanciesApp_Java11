package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.JobCompanyLogoDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;
import org.jspecify.annotations.NonNull;

public class JobCompanyLogoConverter extends EntityToDtoConverter<JobCompanyLogo, JobCompanyLogoDTO> {

	private static final @NonNull JobCompanyLogoConverter SINGLETON_INSTANCE = new JobCompanyLogoConverter();

	private JobCompanyLogoConverter() {
		super(JobCompanyLogo.class, JobCompanyLogoDTO.class, JobCompanyLogoDTO[]::new);
	}

	public static @NonNull JobCompanyLogoConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	protected @NonNull JobCompanyLogoDTO convert(final @NonNull JobCompanyLogo jobCompanyLogo) {
		final JobCompanyLogoDTO jobCompanyLogoDTO = new JobCompanyLogoDTO(
			jobCompanyLogo.getId(),
			jobCompanyLogo.getFileName(),
			jobCompanyLogo.getFilePath(),
			jobCompanyLogo.getSelectionName()
		);
		return jobCompanyLogoDTO;
	}
}