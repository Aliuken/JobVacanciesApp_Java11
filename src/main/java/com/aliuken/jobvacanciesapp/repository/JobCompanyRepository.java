package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobCompanyRepository extends JpaRepositoryWithPaginationAndSorting<JobCompany> {
//	@Query("SELECT jc FROM JobCompany jc WHERE jc.name = :name")
//	JobCompany findByName(@Param("name") String name);

	default JobCompany findByName(String name) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("name", name);

		JobCompany jobCompany = this.executeQuerySingleResult("SELECT jc FROM JobCompany jc WHERE jc.name = :name", parameterMap);
		return jobCompany;
	}

	@Override
	default Class<JobCompany> getEntityClass() {
		return JobCompany.class;
	}
}
