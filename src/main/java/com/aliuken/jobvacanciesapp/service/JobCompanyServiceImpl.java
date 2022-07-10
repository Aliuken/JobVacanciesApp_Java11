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
import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.JobCompanyRepository;

@Service
@Transactional
public class JobCompanyServiceImpl implements JobCompanyService {

	@Autowired
	private JobCompanyRepository jobCompanyRepository;

	@Override
	public Class<JobCompany> getEntityClass() {
		return JobCompany.class;
	}

	@Override
	public JobCompany saveAndFlush(JobCompany jobCompany) {
		return jobCompanyRepository.saveAndFlush(jobCompany);
	}

	@Override
	public void deleteByIdAndFlush(Long jobCompanyId) {
		jobCompanyRepository.deleteByIdAndFlush(jobCompanyId);
	}

	@Override
	public List<JobCompany> findAll() {
		return jobCompanyRepository.findAll();
	}

	@Override
	public JobCompany findByIdNotOptional(Long jobCompanyId) {
		return jobCompanyRepository.findByIdNotOptional(jobCompanyId);
	}

	@Override
	public JobCompany findByName(String jobCompanyName) {
		return jobCompanyRepository.findByName(jobCompanyName);
	}

	@Override
	public Page<JobCompany> findAll(Pageable pageable) {
		return jobCompanyRepository.findAll(pageable);
	}

	@Override
	public Page<JobCompany> findAll(Pageable pageable, TableOrder tableOrder) {
		return jobCompanyRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<JobCompany> findAll(Pageable pageable, TableOrder tableOrder, Specification<JobCompany> specification) {
		return jobCompanyRepository.findAll(pageable, tableOrder, specification);
	}

	@Override
	public Page<JobCompany> findAll(Example<JobCompany> example, Pageable pageable) {
		return jobCompanyRepository.findAll(example, pageable);
	}

	@Override
	public Page<JobCompany> findAll(Example<JobCompany> example, Pageable pageable, TableOrder tableOrder) {
		return jobCompanyRepository.findAll(example, pageable, tableOrder);
	}

	@Override
	public JobCompany getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser) {
		JobCompany jobCompany = new JobCompany();
		jobCompany.setId(id);
		jobCompany.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		jobCompany.setLastModificationAuthUser(lastModificationAuthUser);

		return jobCompany;
	}

}
