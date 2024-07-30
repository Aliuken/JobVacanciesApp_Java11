package com.aliuken.jobvacanciesapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.repository.JobCompanyLogoRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;

@Service
@Transactional
public class JobCompanyLogoServiceImpl extends JobCompanyLogoService {

	@Autowired
	private JobCompanyLogoRepository jobCompanyLogoRepository;

	@Override
	public UpgradedJpaRepository<JobCompanyLogo> getEntityRepository() {
		return jobCompanyLogoRepository;
	}

	@Override
	@ServiceMethod
	public JobCompanyLogo findByJobCompanyAndFileName(final JobCompany jobCompany, final String fileName) {
		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByJobCompanyAndFileName(jobCompany, fileName);
		return jobCompanyLogo;
	}

	@Override
	public JobCompanyLogo getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final JobCompanyLogo jobCompanyLogo = new JobCompanyLogo();
		jobCompanyLogo.setId(id);
		jobCompanyLogo.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		jobCompanyLogo.setLastModificationAuthUser(lastModificationAuthUser);

		return jobCompanyLogo;
	}
}
