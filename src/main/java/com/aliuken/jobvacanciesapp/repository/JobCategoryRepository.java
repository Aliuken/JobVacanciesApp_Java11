package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobCategoryRepository extends JpaRepositoryWithPaginationAndSorting<JobCategory> {
//	@Query("SELECT jc FROM JobCategory jc WHERE jc.name = :name")
//	JobCategory findByName(@Param("name") String name);

	default JobCategory findByName(String name) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("name", name);

		JobCategory jobCategory = this.executeQuerySingleResult("SELECT jc FROM JobCategory jc WHERE jc.name = :name", parameterMap);
		return jobCategory;
	}

	@Override
	default Class<JobCategory> getEntityClass() {
		return JobCategory.class;
	}
}
