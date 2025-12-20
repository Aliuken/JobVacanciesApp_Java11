package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.factory.JobCompanyFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface JobCompanyRepository extends UpgradedJpaRepository<JobCompany> {
	public static final AbstractEntityFactory<JobCompany> ENTITY_FACTORY = new JobCompanyFactory();

//	@RepositoryMethod
//	@Query("SELECT jc FROM JobCompany jc WHERE jc.name = :name")
//	public abstract JobCompany findByName(@Param("name") String name);

	@RepositoryMethod
	public default JobCompany findByName(final String name) {
		if(name == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("name", name);

		final JobCompany jobCompany = this.executeQuerySingleResult(
			"SELECT jc FROM JobCompany jc WHERE jc.name = :name", parameterMap);
		return jobCompany;
	}

	@Override
	public default AbstractEntityFactory<JobCompany> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
