package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.BigDecimalFromStringConversionResult;
import com.aliuken.jobvacanciesapp.model.dto.JobCategoryDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobVacancyDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import org.jspecify.annotations.NonNull;

public class JobVacancyConverter extends EntityToDtoConverter<JobVacancy, JobVacancyDTO> {

	private static final @NonNull JobVacancyConverter SINGLETON_INSTANCE = new JobVacancyConverter();

	private JobVacancyConverter() {
		super(JobVacancy.class, JobVacancyDTO.class, JobVacancyDTO[]::new);
	}

	public static @NonNull JobVacancyConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	protected @NonNull JobVacancyDTO convert(final @NonNull JobVacancy jobVacancy) {
		final JobCategoryDTO jobCategoryDTO = JobCategoryConverter.getInstance().convertEntityElement(jobVacancy.getJobCategory());
		final JobCompanyDTO jobCompanyDTO = JobCompanyConverter.getInstance().convertEntityElement(jobVacancy.getJobCompany());
		final String salaryString = String.valueOf(jobVacancy.getSalary());

		final JobVacancyDTO jobVacancyDTO = new JobVacancyDTO(
			jobVacancy.getId(),
			jobVacancy.getName(),
			jobVacancy.getDescription(),
			jobCategoryDTO,
			jobCategoryDTO.getId(),
			jobCompanyDTO,
			jobCompanyDTO.getId(),
			jobVacancy.getStatus().getCode(),
			jobVacancy.getPublicationDateTime(),
			salaryString,
			jobVacancy.getCurrency().getSymbol(),
			jobVacancy.getHighlighted(),
			jobVacancy.getDetails()
		);
		return jobVacancyDTO;
	}
}