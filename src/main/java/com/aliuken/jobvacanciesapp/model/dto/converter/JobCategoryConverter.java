package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.JobCategoryDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.JobCategory;
import org.jspecify.annotations.NonNull;

public class JobCategoryConverter extends EntityToDtoConverter<JobCategory, JobCategoryDTO> {

	private static final @NonNull JobCategoryConverter SINGLETON_INSTANCE = new JobCategoryConverter();

	private JobCategoryConverter() {
		super(JobCategory.class, JobCategoryDTO.class, JobCategoryDTO[]::new);
	}

	public static @NonNull JobCategoryConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	protected @NonNull JobCategoryDTO convert(final @NonNull JobCategory jobCategory) {
		final JobCategoryDTO jobCategoryDTO = new JobCategoryDTO(
			jobCategory.getId(),
			jobCategory.getName(),
			jobCategory.getDescription(),
			jobCategory.getJobVacancyIds()
		);
		return jobCategoryDTO;
	}
}