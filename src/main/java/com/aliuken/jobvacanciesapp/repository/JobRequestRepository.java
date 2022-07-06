package com.aliuken.jobvacanciesapp.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobRequestRepository extends JpaRepositoryWithPaginationAndSorting<JobRequest> {
	JobRequest findByAuthUserAndJobVacancy(AuthUser authUser, JobVacancy jobVacancy);
	List<JobRequest> findByAuthUser(AuthUser authUser);
	List<JobRequest> findByAuthUserAndCurriculumFileName(AuthUser authUser, String curriculumFileName);
	List<JobRequest> findByJobVacancy(JobVacancy jobVacancy);
	
	@Override
	default Class<JobRequest> getEntityClass() {
		return JobRequest.class;
	}
}
