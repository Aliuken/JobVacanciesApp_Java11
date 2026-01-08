package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.repository.JobRequestRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobRequestServiceImpl extends JobRequestService {

	@Autowired
	private JobRequestRepository jobRequestRepository;

	@Override
	public @NonNull UpgradedJpaRepository<JobRequest> getEntityRepository() {
		return jobRequestRepository;
	}

	@Override
	@ServiceMethod
	public JobRequest findByAuthUserAndJobVacancy(final AuthUser authUser, final JobVacancy jobVacancy) {
		final JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(authUser, jobVacancy);
		return jobRequest;
	}

	@Override
	@ServiceMethod
	public @NonNull List<JobRequest> findByAuthUserAndCurriculumFileName(final AuthUser authUser, final String curriculumFileName) {
		final List<JobRequest> jobRequests = jobRequestRepository.findByAuthUserAndCurriculumFileName(authUser, curriculumFileName);
		return jobRequests;
	}

	@Override
	public @NonNull JobRequest getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final JobRequest jobRequest = new JobRequest();
		jobRequest.setId(id);
		jobRequest.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		jobRequest.setLastModificationAuthUser(lastModificationAuthUser);

		return jobRequest;
	}

	@Override
	public JobRequest getNewEntityWithAuthUserEmail(String authUserEmail) {
		final AuthUser authUser = new AuthUser();
		authUser.setEmail(authUserEmail);

		final JobRequest jobRequest = new JobRequest();
		jobRequest.setAuthUser(authUser);

		return jobRequest;
	}

	@Override
	public JobRequest getNewEntityWithAuthUserName(String authUserName) {
		final AuthUser authUser = new AuthUser();
		authUser.setName(authUserName);

		final JobRequest jobRequest = new JobRequest();
		jobRequest.setAuthUser(authUser);

		return jobRequest;
	}

	@Override
	public JobRequest getNewEntityWithAuthUserSurnames(String authUserSurnames) {
		final AuthUser authUser = new AuthUser();
		authUser.setSurnames(authUserSurnames);

		final JobRequest jobRequest = new JobRequest();
		jobRequest.setAuthUser(authUser);

		return jobRequest;
	}

	@Override
	public JobRequest getNewEntityWithJobCompanyName(String jobCompanyName) {
		final JobCompany jobCompany = new JobCompany();
		jobCompany.setName(jobCompanyName);

		final JobVacancy jobVacancy = new JobVacancy();
		jobVacancy.setJobCompany(jobCompany);

		final JobRequest jobRequest = new JobRequest();
		jobRequest.setJobVacancy(jobVacancy);

		return jobRequest;
	}
}
