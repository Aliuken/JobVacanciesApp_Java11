package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobVacancyRepository extends JpaRepositoryWithPaginationAndSorting<JobVacancy> {
//	@Query("SELECT jv FROM JobVacancy jv WHERE jv.highlighted = :highlighted AND jv.status = :status ORDER BY id desc")
//	List<JobVacancy> findByHighlightedAndStatusOrderByIdDesc(@Param("highlighted") Boolean highlighted, @Param("status") JobVacancyStatus status);

	default List<JobVacancy> findByHighlightedAndStatusOrderByIdDesc(Boolean highlighted, JobVacancyStatus status) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("highlighted", highlighted);
		parameterMap.put("status", status);

		List<JobVacancy> jobVacancies = this.executeQueryResultList("SELECT jv FROM JobVacancy jv WHERE jv.highlighted = :highlighted AND jv.status = :status ORDER BY id desc", parameterMap);
		return jobVacancies;
	}

	@Override
	default Class<JobVacancy> getEntityClass() {
		return JobVacancy.class;
	}
}
