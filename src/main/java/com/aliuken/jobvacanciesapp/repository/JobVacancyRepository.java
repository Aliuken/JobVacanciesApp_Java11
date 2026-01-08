package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.model.entity.factory.JobVacancyFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface JobVacancyRepository extends UpgradedJpaRepository<JobVacancy> {
	public static final AbstractEntityFactory<JobVacancy> ENTITY_FACTORY = new JobVacancyFactory();

//	@RepositoryMethod
//	@Query("SELECT jv FROM JobVacancy jv WHERE jv.highlighted = :highlighted AND jv.status = :status ORDER BY id desc")
//	public abstract List<JobVacancy> findByHighlightedAndStatusOrderByIdDesc(@Param("highlighted") Boolean highlighted, @Param("status") JobVacancyStatus status);

	@RepositoryMethod
	public default @NonNull List<JobVacancy> findByHighlightedAndStatusOrderByIdDesc(final Boolean highlighted, final JobVacancyStatus status) {
		if(highlighted == null || status == null) {
			final List<JobVacancy> jobVacancies = new ArrayList<>();
			return jobVacancies;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("highlighted", highlighted);
		parameterMap.put("status", status);

		final List<JobVacancy> jobVacancies = this.executeQueryResultList(
			"SELECT jv FROM JobVacancy jv WHERE jv.highlighted = :highlighted AND jv.status = :status ORDER BY id desc", parameterMap);
		return jobVacancies;
	}

	@Override
	public default @NonNull AbstractEntityFactory<JobVacancy> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
