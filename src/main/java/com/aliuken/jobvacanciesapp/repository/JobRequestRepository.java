package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobRequestRepository extends JpaRepositoryWithPaginationAndSorting<JobRequest> {
//	@Query("SELECT jr FROM JobRequest jr WHERE jr.authUser = :authUser AND jr.jobVacancy = :jobVacancy")
//	JobRequest findByAuthUserAndJobVacancy(@Param("authUser") AuthUser authUser, @Param("jobVacancy") JobVacancy jobVacancy);

//	@Query("SELECT jr FROM JobRequest jr WHERE jr.authUser = :authUser AND jr.curriculumFileName = :curriculumFileName")
//	List<JobRequest> findByAuthUserAndCurriculumFileName(@Param("authUser") AuthUser authUser, @Param("curriculumFileName") String curriculumFileName);

	default JobRequest findByAuthUserAndJobVacancy(AuthUser authUser, JobVacancy jobVacancy) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("authUser", authUser);
		parameterMap.put("jobVacancy", jobVacancy);

		JobRequest jobRequest = this.executeQuerySingleResult("SELECT jr FROM JobRequest jr WHERE jr.authUser = :authUser AND jr.jobVacancy = :jobVacancy", parameterMap);
		return jobRequest;
	}

	default List<JobRequest> findByAuthUserAndCurriculumFileName(AuthUser authUser, String curriculumFileName) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("authUser", authUser);
		parameterMap.put("curriculumFileName", curriculumFileName);

		List<JobRequest> jobRequests = this.executeQueryResultList("SELECT jr FROM JobRequest jr WHERE jr.authUser = :authUser AND jr.curriculumFileName = :curriculumFileName", parameterMap);
		return jobRequests;
	}

	@Override
	default Class<JobRequest> getEntityClass() {
		return JobRequest.class;
	}
}
