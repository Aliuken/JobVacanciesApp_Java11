package com.aliuken.jobvacanciesapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobCategoryRepository extends JpaRepositoryWithPaginationAndSorting<JobCategory> {
	@Query("SELECT jc FROM JobCategory jc WHERE jc.name = :name")
	JobCategory findByName(@Param("name") String name);

	@Override
	default Class<JobCategory> getEntityClass() {
		return JobCategory.class;
	}
}
