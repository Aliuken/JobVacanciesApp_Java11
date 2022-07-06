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
	public Class<JobVacancy> getEntityClass() {
		return JobVacancy.class;
	}

	@Override
	public void save(JobVacancy jobVacancy) {
		jobVacancyRepository.save(jobVacancy);
	}

	@Override
	public void deleteById(Long jobVacancyId) {
		jobVacancyRepository.deleteById(jobVacancyId);
	}

	@Override
	public List<JobVacancy> findAll() {
		return jobVacancyRepository.findAll();
	}

	@Override
	public JobVacancy findById(Long jobVacancyId) {
		return jobVacancyRepository.findByIdNotOptional(jobVacancyId);
	}

	@Override
	public List<JobVacancy> findAllHighlighted() {
		return jobVacancyRepository.findByHighlightedAndStatusOrderByIdDesc(true, JobVacancyStatus.APPROVED);
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
	public Page<JobVacancy> findAll(Pageable pageable, Example<JobVacancy> example) {
		return jobVacancyRepository.findAll(example, pageable);
	}

	@Override
	public Page<JobVacancy> findAll(Pageable pageable, TableOrder tableOrder) {
		return jobVacancyRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<JobVacancy> findAll(Pageable pageable, TableOrder tableOrder, Example<JobVacancy> example) {
		return jobVacancyRepository.findAll(pageable, tableOrder, example);
	}

	@Override
	public Page<JobVacancy> findAll(Pageable pageable, TableOrder tableOrder, Specification<JobVacancy> specification) {
		return jobVacancyRepository.findAll(pageable, tableOrder, specification);
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
