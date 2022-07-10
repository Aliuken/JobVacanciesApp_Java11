package com.aliuken.jobvacanciesapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobCompanyRepository extends JpaRepositoryWithPaginationAndSorting<JobCompany> {
	@Query("SELECT jc FROM JobCompany jc WHERE jc.name = :name")
	JobCompany findByName(@Param("name") String name);

	@Override
	default Class<JobCompany> getEntityClass() {
		return JobCompany.class;
	}
}
