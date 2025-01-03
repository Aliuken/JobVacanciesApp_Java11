package com.aliuken.jobvacanciesapp.service;

import java.util.List;

import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.service.superclass.JobRequestServiceSuperclass;

public abstract class JobRequestService extends JobRequestServiceSuperclass {

	public abstract JobRequest findByAuthUserAndJobVacancy(final AuthUser authUser, final JobVacancy jobVacancy);

	public abstract List<JobRequest> findByAuthUserAndCurriculumFileName(final AuthUser authUser, final String curriculumFileName);

}
