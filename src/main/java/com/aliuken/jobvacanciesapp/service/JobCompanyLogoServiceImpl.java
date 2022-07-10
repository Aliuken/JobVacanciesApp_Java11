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
import com.aliuken.jobvacanciesapp.model.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.JobCompanyLogoRepository;

@Service
@Transactional
public class JobCompanyLogoServiceImpl implements JobCompanyLogoService {

	@Autowired
	private JobCompanyLogoRepository jobCompanyLogoRepository;

	@Override
	public JobCompanyLogo findByJobCompanyAndFileName(JobCompany jobCompany, String fileName) {
		return jobCompanyLogoRepository.findByJobCompanyAndFileName(jobCompany, fileName);
	}

	@Override
	public Class<JobCompanyLogo> getEntityClass() {
		return JobCompanyLogo.class;
	}

	@Override
	public JobCompanyLogo saveAndFlush(JobCompanyLogo jobCompanyLogo) {
		return jobCompanyLogoRepository.saveAndFlush(jobCompanyLogo);
	}

	@Override
	public void deleteByIdAndFlush(Long jobCompanyLogoId) {
		jobCompanyLogoRepository.deleteByIdAndFlush(jobCompanyLogoId);
	}

	@Override
	public List<JobCompanyLogo> findAll() {
		return jobCompanyLogoRepository.findAll();
	}

	@Override
	public JobCompanyLogo findByIdNotOptional(Long jobCompanyLogoId) {
		return jobCompanyLogoRepository.findByIdNotOptional(jobCompanyLogoId);
	}

	@Override
	public Page<JobCompanyLogo> findAll(Pageable pageable) {
		return jobCompanyLogoRepository.findAll(pageable);
	}

	@Override
	public Page<JobCompanyLogo> findAll(Pageable pageable, TableOrder tableOrder) {
		return jobCompanyLogoRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<JobCompanyLogo> findAll(Pageable pageable, TableOrder tableOrder, Specification<JobCompanyLogo> specification) {
		return jobCompanyLogoRepository.findAll(pageable, tableOrder, specification);
	}

	@Override
	public Page<JobCompanyLogo> findAll(Example<JobCompanyLogo> example, Pageable pageable) {
		return jobCompanyLogoRepository.findAll(example, pageable);
	}

	@Override
	public Page<JobCompanyLogo> findAll(Example<JobCompanyLogo> example, Pageable pageable, TableOrder tableOrder) {
		return jobCompanyLogoRepository.findAll(example, pageable, tableOrder);
	}

	@Override
	public JobCompanyLogo getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser) {
		JobCompanyLogo jobCompanyLogo = new JobCompanyLogo();
		jobCompanyLogo.setId(id);
		jobCompanyLogo.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		jobCompanyLogo.setLastModificationAuthUser(lastModificationAuthUser);

		return jobCompanyLogo;
	}

}
