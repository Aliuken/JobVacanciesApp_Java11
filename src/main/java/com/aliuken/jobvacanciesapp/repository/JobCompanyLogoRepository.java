package com.aliuken.jobvacanciesapp.repository;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobCompanyLogoRepository extends JpaRepositoryWithPaginationAndSorting<JobCompanyLogo> {
	JobCompanyLogo findByJobCompanyAndFileName(JobCompany jobCompany, String fileName);
	
	@Override
	default Class<JobCompanyLogo> getEntityClass() {
		return JobCompanyLogo.class;
	}
}
