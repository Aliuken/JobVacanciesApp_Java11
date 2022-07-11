package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobCompanyLogoRepository extends JpaRepositoryWithPaginationAndSorting<JobCompanyLogo> {
//	@Query("SELECT jcl FROM JobCompanyLogo jcl WHERE jcl.jobCompany = :jobCompany AND jcl.fileName = :fileName")
//	JobCompanyLogo findByJobCompanyAndFileName(@Param("jobCompany") JobCompany jobCompany, @Param("fileName") String fileName);

	default JobCompanyLogo findByJobCompanyAndFileName(JobCompany jobCompany, String fileName) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("jobCompany", jobCompany);
		parameterMap.put("fileName", fileName);

		JobCompanyLogo jobCompanyLogo = this.executeQuerySingleResult("SELECT jcl FROM JobCompanyLogo jcl WHERE jcl.jobCompany = :jobCompany AND jcl.fileName = :fileName", parameterMap);
		return jobCompanyLogo;
	}

	@Override
	default Class<JobCompanyLogo> getEntityClass() {
		return JobCompanyLogo.class;
	}
}
