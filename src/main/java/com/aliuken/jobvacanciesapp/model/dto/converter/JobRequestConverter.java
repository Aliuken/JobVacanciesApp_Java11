package com.aliuken.jobvacanciesapp.model.dto.converter;

import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobRequestDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobVacancyDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.superclass.EntityToDtoConverter;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;

public class JobRequestConverter extends EntityToDtoConverter<JobRequest, JobRequestDTO> {

	private static final JobRequestConverter SINGLETON_INSTANCE = new JobRequestConverter();

	private JobRequestConverter() {
		super(JobRequestConverter::conversionFunction, JobRequest.class, JobRequestDTO.class, JobRequestDTO[]::new);
	}

	public static JobRequestConverter getInstance() {
		return SINGLETON_INSTANCE;
	}

	private static JobRequestDTO conversionFunction(final JobRequest jobRequest) {
		final JobRequestDTO jobRequestDTO;
		if(jobRequest != null) {
			final AuthUserDTO authUserDTO = AuthUserConverter.getInstance().convertEntityElement(jobRequest.getAuthUser());
			final JobVacancyDTO jobVacancyDTO = JobVacancyConverter.getInstance().convertEntityElement(jobRequest.getJobVacancy());

			jobRequestDTO = new JobRequestDTO(
				jobRequest.getId(),
				authUserDTO,
				jobVacancyDTO,
				(jobVacancyDTO != null) ? jobVacancyDTO.getId() : null,
				jobRequest.getComments(),
				jobRequest.getCurriculumFileName()
			);
		} else {
			jobRequestDTO = JobRequestDTO.getNewInstance();
		}
		return jobRequestDTO;
	}
}