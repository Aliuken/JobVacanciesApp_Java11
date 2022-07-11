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
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.JobVacancyRepository;

@Service
@Transactional
public class JobVacancyServiceImpl implements JobVacancyService {

	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Override
	public List<JobVacancy> findByHighlightedAndStatusOrderByIdDesc(Boolean highlighted, JobVacancyStatus status) {
		return jobVacancyRepository.findByHighlightedAndStatusOrderByIdDesc(highlighted, status);
	}

	@Override
	public Class<JobVacancy> getEntityClass() {
		return JobVacancy.class;
	}

	@Override
	public JobVacancy saveAndFlush(JobVacancy jobVacancy) {
		return jobVacancyRepository.saveAndFlush(jobVacancy);
	}

	@Override
	public void deleteByIdAndFlush(Long jobVacancyId) {
		jobVacancyRepository.deleteByIdAndFlush(jobVacancyId);
	}

	@Override
	public List<JobVacancy> findAll() {
		return jobVacancyRepository.findAll();
	}

	@Override
	public JobVacancy findByIdNotOptional(Long jobVacancyId) {
		return jobVacancyRepository.findByIdNotOptional(jobVacancyId);
	}

	@Override
	public List<JobVacancy> findAllHighlighted() {
		return jobVacancyRepository.findByHighlightedAndStatusOrderByIdDesc(Boolean.TRUE, JobVacancyStatus.APPROVED);
	}

	@Override
	public List<JobVacancy> findAll(Example<JobVacancy> jobVacancyExample) {
		return jobVacancyRepository.findAll(jobVacancyExample);
	}

	@Override
	public Page<JobVacancy> findAll(Pageable pageable) {
		return jobVacancyRepository.findAll(pageable);
	}

	@Override
	public Page<JobVacancy> findAll(Pageable pageable, TableOrder tableOrder) {
		return jobVacancyRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<JobVacancy> findAll(Pageable pageable, TableOrder tableOrder, Specification<JobVacancy> specification) {
		return jobVacancyRepository.findAll(pageable, tableOrder, specification);
	}

	@Override
	public Page<JobVacancy> findAll(Example<JobVacancy> example, Pageable pageable) {
		return jobVacancyRepository.findAll(example, pageable);
	}

	@Override
	public Page<JobVacancy> findAll(Example<JobVacancy> example, Pageable pageable, TableOrder tableOrder) {
		return jobVacancyRepository.findAll(example, pageable, tableOrder);
	}

	@Override
	public JobVacancy getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser) {
		JobVacancy jobVacancy = new JobVacancy();
		jobVacancy.setId(id);
		jobVacancy.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		jobVacancy.setLastModificationAuthUser(lastModificationAuthUser);

		return jobVacancy;
	}

}
