package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.JobCategoryDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.JobCategory;

public class JobCategoryConverter extends EntityToDtoConverter<JobCategory, JobCategoryDTO> {

	private static final JobCategoryConverter SINGLETON_INSTANCE = new JobCategoryConverter();

	private JobCategoryConverter() {
		super(JobCategoryConverter::conversionFunction, JobCategory.class, JobCategoryDTO.class, JobCategoryDTO[]::new);
	}

	public static JobCategoryConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	private static JobCategoryDTO conversionFunction(final JobCategory jobCategory) {
		final JobCategoryDTO jobCategoryDTO;
		if(jobCategory != null) {
			jobCategoryDTO = new JobCategoryDTO(
				jobCategory.getId(),
				jobCategory.getName(),
				jobCategory.getDescription(),
				jobCategory.getJobVacancyIds()
			);
		} else {
			jobCategoryDTO = JobCategoryDTO.getNewInstance();
		}
		return jobCategoryDTO;
	}
}