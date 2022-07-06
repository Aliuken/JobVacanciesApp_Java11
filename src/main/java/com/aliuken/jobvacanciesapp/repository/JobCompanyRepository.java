package com.aliuken.jobvacanciesapp.repository;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobCompanyRepository extends JpaRepositoryWithPaginationAndSorting<JobCompany> {
	JobCompany findByName(String name);
	
	@Override
	default Class<JobCompany> getEntityClass() {
		return JobCompany.class;
	}
}
