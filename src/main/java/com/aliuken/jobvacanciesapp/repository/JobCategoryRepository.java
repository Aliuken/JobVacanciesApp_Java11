package com.aliuken.jobvacanciesapp.repository;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobCategoryRepository extends JpaRepositoryWithPaginationAndSorting<JobCategory> {
	JobCategory findByName(String name);
	
	@Override
	default Class<JobCategory> getEntityClass() {
		return JobCategory.class;
	}
}
