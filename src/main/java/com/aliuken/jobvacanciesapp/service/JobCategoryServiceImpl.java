package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobCategory;
import com.aliuken.jobvacanciesapp.repository.JobCategoryRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class JobCategoryServiceImpl extends JobCategoryService {

	@Autowired
	private JobCategoryRepository jobCategoryRepository;

	@Override
	public @NonNull UpgradedJpaRepository<JobCategory> getEntityRepository() {
		return jobCategoryRepository;
	}

	@Override
	@ServiceMethod
	public JobCategory findByName(final String name) {
		final JobCategory jobCategory = jobCategoryRepository.findByName(name);
		return jobCategory;
	}

	@Override
	public @NonNull JobCategory getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final JobCategory jobCategory = new JobCategory();
		jobCategory.setId(id);
		jobCategory.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		jobCategory.setLastModificationAuthUser(lastModificationAuthUser);

		return jobCategory;
	}
}
