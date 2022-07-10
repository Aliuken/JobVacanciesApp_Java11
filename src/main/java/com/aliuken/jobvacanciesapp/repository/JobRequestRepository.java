package com.aliuken.jobvacanciesapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobRequestRepository extends JpaRepositoryWithPaginationAndSorting<JobRequest> {
	@Query("SELECT jr FROM JobRequest jr WHERE jr.authUser = :authUser AND jr.jobVacancy = :jobVacancy")
	JobRequest findByAuthUserAndJobVacancy(@Param("authUser") AuthUser authUser, @Param("jobVacancy") JobVacancy jobVacancy);

	@Query("SELECT jr FROM JobRequest jr WHERE jr.authUser = :authUser AND jr.curriculumFileName = :curriculumFileName")
	List<JobRequest> findByAuthUserAndCurriculumFileName(@Param("authUser") AuthUser authUser, @Param("curriculumFileName") String curriculumFileName);

	@Override
	default Class<JobRequest> getEntityClass() {
		return JobRequest.class;
	}
}
