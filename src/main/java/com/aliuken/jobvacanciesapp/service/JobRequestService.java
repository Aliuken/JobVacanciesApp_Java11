package com.aliuken.jobvacanciesapp.service;

import java.util.List;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.service.superinterface.JobRequestExtraServiceInterface;

public interface JobRequestService extends JobRequestExtraServiceInterface {
	JobRequest findByAuthUserAndJobVacancy(AuthUser authUser, JobVacancy jobVacancy);
	List<JobRequest> findByAuthUserAndCurriculumFileName(AuthUser authUser, String curriculumFileName);
}
