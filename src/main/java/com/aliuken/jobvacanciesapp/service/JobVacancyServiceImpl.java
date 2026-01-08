package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.repository.JobVacancyRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobVacancyServiceImpl extends JobVacancyService {

	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Override
	public @NonNull UpgradedJpaRepository<JobVacancy> getEntityRepository() {
		return jobVacancyRepository;
	}

	@Override
	@ServiceMethod
	public @NonNull List<JobVacancy> findByHighlightedAndStatusOrderByIdDesc(final Boolean highlighted, final JobVacancyStatus status) {
		final List<JobVacancy> jobVacancies = jobVacancyRepository.findByHighlightedAndStatusOrderByIdDesc(highlighted, status);
		return jobVacancies;
	}

	@Override
	@ServiceMethod
	public @NonNull List<JobVacancy> findAllHighlighted() {
		final List<JobVacancy> jobVacancies = jobVacancyRepository.findByHighlightedAndStatusOrderByIdDesc(Boolean.TRUE, JobVacancyStatus.APPROVED);
		return jobVacancies;
	}

	@Override
	public @NonNull JobVacancy getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final JobVacancy jobVacancy = new JobVacancy();
		jobVacancy.setId(id);
		jobVacancy.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		jobVacancy.setLastModificationAuthUser(lastModificationAuthUser);

		return jobVacancy;
	}

	@Override
	public JobVacancy getNewEntityWithJobCompanyName(String jobCompanyName) {
		final JobCompany jobCompany = new JobCompany();
		jobCompany.setName(jobCompanyName);

		final JobVacancy jobVacancy = new JobVacancy();
		jobVacancy.setJobCompany(jobCompany);

		return jobVacancy;
	}
}
