package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.factory.JobRequestFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface JobRequestRepository extends UpgradedJpaRepository<JobRequest> {
	public static final AbstractEntityFactory<JobRequest> ENTITY_FACTORY = new JobRequestFactory();

//	@RepositoryMethod
//	@Query("SELECT jr FROM JobRequest jr WHERE jr.authUser = :authUser AND jr.jobVacancy = :jobVacancy")
//	public abstract JobRequest findByAuthUserAndJobVacancy(@Param("authUser") AuthUser authUser, @Param("jobVacancy") JobVacancy jobVacancy);

//	@RepositoryMethod
//	@Query("SELECT jr FROM JobRequest jr WHERE jr.authUser = :authUser AND jr.curriculumFileName = :curriculumFileName")
//	public abstract List<JobRequest> findByAuthUserAndCurriculumFileName(@Param("authUser") AuthUser authUser, @Param("curriculumFileName") String curriculumFileName);

	@RepositoryMethod
	public default JobRequest findByAuthUserAndJobVacancy(final AuthUser authUser, final JobVacancy jobVacancy) {
		if(authUser == null || jobVacancy == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("authUser", authUser);
		parameterMap.put("jobVacancy", jobVacancy);

		final JobRequest jobRequest = this.executeQuerySingleResult(
			"SELECT jr FROM JobRequest jr WHERE jr.authUser = :authUser AND jr.jobVacancy = :jobVacancy", parameterMap);
		return jobRequest;
	}

	@RepositoryMethod
	public default @NonNull List<JobRequest> findByAuthUserAndCurriculumFileName(final AuthUser authUser, final String curriculumFileName) {
		if(authUser == null || curriculumFileName == null) {
			final List<JobRequest> jobRequests = new ArrayList<>();
			return jobRequests;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("authUser", authUser);
		parameterMap.put("curriculumFileName", curriculumFileName);

		final List<JobRequest> jobRequests = this.executeQueryResultList(
			"SELECT jr FROM JobRequest jr WHERE jr.authUser = :authUser AND jr.curriculumFileName = :curriculumFileName", parameterMap);
		return jobRequests;
	}

	@Override
	public default @NonNull AbstractEntityFactory<JobRequest> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
