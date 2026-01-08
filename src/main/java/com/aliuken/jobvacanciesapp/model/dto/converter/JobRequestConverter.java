package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobRequestDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobVacancyDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import org.jspecify.annotations.NonNull;

public class JobRequestConverter extends EntityToDtoConverter<JobRequest, JobRequestDTO> {

	private static final @NonNull JobRequestConverter SINGLETON_INSTANCE = new JobRequestConverter();

	private JobRequestConverter() {
		super(JobRequest.class, JobRequestDTO.class, JobRequestDTO[]::new);
	}

	public static @NonNull JobRequestConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	@Override
	protected @NonNull JobRequestDTO convert(final @NonNull JobRequest jobRequest) {
		final AuthUserDTO authUserDTO = AuthUserConverter.getInstance().convertEntityElement(jobRequest.getAuthUser());
		final JobVacancyDTO jobVacancyDTO = JobVacancyConverter.getInstance().convertEntityElement(jobRequest.getJobVacancy());

		final JobRequestDTO jobRequestDTO = new JobRequestDTO(
			jobRequest.getId(),
			authUserDTO,
			jobVacancyDTO,
			jobVacancyDTO.getId(),
			jobRequest.getComments(),
			jobRequest.getCurriculumFileName()
		);
		return jobRequestDTO;
	}
}