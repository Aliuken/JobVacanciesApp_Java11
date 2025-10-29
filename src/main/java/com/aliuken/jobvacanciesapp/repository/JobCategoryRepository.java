package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.JobCategory;
import com.aliuken.jobvacanciesapp.model.entity.factory.JobCategoryFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface JobCategoryRepository extends UpgradedJpaRepository<JobCategory> {
	public static final AbstractEntityFactory<JobCategory> ENTITY_FACTORY = new JobCategoryFactory();

//	@RepositoryMethod
//	@Query("SELECT jc FROM JobCategory jc WHERE jc.name = :name")
//	public abstract JobCategory findByName(@Param("name") String name);

	@RepositoryMethod
	public default JobCategory findByName(final String name) {
		if(name == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("name", name);

		final JobCategory jobCategory = this.executeQuerySingleResult(
			"SELECT jc FROM JobCategory jc WHERE jc.name = :name", parameterMap);
		return jobCategory;
	}

	@Override
	public default AbstractEntityFactory<JobCategory> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
