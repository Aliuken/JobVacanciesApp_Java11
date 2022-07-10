package com.aliuken.jobvacanciesapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobCompanyLogoRepository extends JpaRepositoryWithPaginationAndSorting<JobCompanyLogo> {
	@Query("SELECT jcl FROM JobCompanyLogo jcl WHERE jcl.jobCompany = :jobCompany AND jcl.fileName = :fileName")
	JobCompanyLogo findByJobCompanyAndFileName(@Param("jobCompany") JobCompany jobCompany, @Param("fileName") String fileName);

	@Override
	default Class<JobCompanyLogo> getEntityClass() {
		return JobCompanyLogo.class;
	}
}
