package com.aliuken.jobvacanciesapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.JobRequestRepository;

@Service
@Transactional
public class JobRequestServiceImpl implements JobRequestService {

	@Autowired
	private JobRequestRepository jobRequestRepository;

	@Override
	public JobRequest findByAuthUserAndJobVacancy(AuthUser authUser, JobVacancy jobVacancy) {
		JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(authUser, jobVacancy);
		return jobRequest;
	}

	@Override
	public List<JobRequest> findByAuthUserAndCurriculumFileName(AuthUser authUser, String curriculumFileName) {
		List<JobRequest> jobRequests = jobRequestRepository.findByAuthUserAndCurriculumFileName(authUser, curriculumFileName);
		return jobRequests;
	}

	@Override
	public Class<JobRequest> getEntityClass() {
		return JobRequest.class;
	}

	@Override
	public JobRequest saveAndFlush(JobRequest jobRequest) {
		return jobRequestRepository.saveAndFlush(jobRequest);
	}

	@Override
	public void deleteByIdAndFlush(Long jobRequestId) {
		jobRequestRepository.deleteByIdAndFlush(jobRequestId);
	}

	@Override
	public List<JobRequest> findAll() {
		return jobRequestRepository.findAll();
	}

	@Override
	public JobRequest findByIdNotOptional(Long jobRequestId) {
		return jobRequestRepository.findByIdNotOptional(jobRequestId);
	}

	@Override
	public Page<JobRequest> findAll(Pageable pageable) {
		return jobRequestRepository.findAll(pageable);
	}

	@Override
	public Page<JobRequest> findAll(Pageable pageable, TableOrder tableOrder) {
		return jobRequestRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<JobRequest> findAll(Pageable pageable, TableOrder tableOrder, Specification<JobRequest> specification) {
		return jobRequestRepository.findAll(pageable, tableOrder, specification);
	}

	@Override
	public Page<JobRequest> findAll(Example<JobRequest> example, Pageable pageable) {
		return jobRequestRepository.findAll(example, pageable);
	}

	@Override
	public Page<JobRequest> findAll(Example<JobRequest> example, Pageable pageable, TableOrder tableOrder) {
		return jobRequestRepository.findAll(example, pageable, tableOrder);
	}

	@Override
	public JobRequest getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser) {
		JobRequest jobRequest = new JobRequest();
		jobRequest.setId(id);
		jobRequest.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		jobRequest.setLastModificationAuthUser(lastModificationAuthUser);

		return jobRequest;
	}

	@Override
	public JobRequest getNewEntityWithAuthUserEmail(String authUserEmail) {
		AuthUser authUser = new AuthUser();
		authUser.setEmail(authUserEmail);

		JobRequest jobRequest = new JobRequest();
		jobRequest.setAuthUser(authUser);

		return jobRequest;
	}

	@Override
	public JobRequest getNewEntityWithAuthUserName(String authUserName) {
		AuthUser authUser = new AuthUser();
		authUser.setName(authUserName);

		JobRequest jobRequest = new JobRequest();
		jobRequest.setAuthUser(authUser);

		return jobRequest;
	}

	@Override
	public JobRequest getNewEntityWithAuthUserSurnames(String authUserSurnames) {
		AuthUser authUser = new AuthUser();
		authUser.setSurnames(authUserSurnames);

		JobRequest jobRequest = new JobRequest();
		jobRequest.setAuthUser(authUser);

		return jobRequest;
	}

}
