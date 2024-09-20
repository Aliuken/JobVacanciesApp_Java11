package com.aliuken.jobvacanciesapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.repository.JobCompanyRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;

@Service
@Transactional
public class JobCompanyServiceImpl extends JobCompanyService {

	@Autowired
	private JobCompanyRepository jobCompanyRepository;

	@Override
	public UpgradedJpaRepository<JobCompany> getEntityRepository() {
		return jobCompanyRepository;
	}

	@Override
	@ServiceMethod
	public JobCompany findByName(final String jobCompanyName) {
		final JobCompany jobCompany = jobCompanyRepository.findByName(jobCompanyName);
		return jobCompany;
	}

	@Override
	public JobCompany getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final JobCompany jobCompany = new JobCompany();
		jobCompany.setId(id);
		jobCompany.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		jobCompany.setLastModificationAuthUser(lastModificationAuthUser);

		return jobCompany;
	}
}
