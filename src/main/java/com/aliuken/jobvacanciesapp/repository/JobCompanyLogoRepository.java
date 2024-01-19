package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.model.entity.factory.JobCompanyLogoFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;

@Repository
public interface JobCompanyLogoRepository extends UpgradedJpaRepository<JobCompanyLogo> {
	public static final AbstractEntityFactory<JobCompanyLogo> ENTITY_FACTORY = new JobCompanyLogoFactory();

//	@RepositoryMethod
//	@Query("SELECT jcl FROM JobCompanyLogo jcl WHERE jcl.jobCompany = :jobCompany AND jcl.fileName = :fileName")
//	public abstract JobCompanyLogo findByJobCompanyAndFileName(@Param("jobCompany") JobCompany jobCompany, @Param("fileName") String fileName);

	@RepositoryMethod
	public default JobCompanyLogo findByJobCompanyAndFileName(final JobCompany jobCompany, final String fileName) {
		if(jobCompany == null || fileName == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("jobCompany", jobCompany);
		parameterMap.put("fileName", fileName);

		final JobCompanyLogo jobCompanyLogo = this.executeQuerySingleResult(
			"SELECT jcl FROM JobCompanyLogo jcl WHERE jcl.jobCompany = :jobCompany AND jcl.fileName = :fileName", parameterMap);
		return jobCompanyLogo;
	}

	@Override
	public default AbstractEntityFactory<JobCompanyLogo> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
