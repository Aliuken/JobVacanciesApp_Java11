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
import com.aliuken.jobvacanciesapp.model.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.JobCompanyLogoRepository;

@Service
@Transactional
public class JobCompanyLogoServiceImpl implements JobCompanyLogoService {

	@Autowired
	private JobCompanyLogoRepository jobCompanyLogoRepository;

	@Override
	public Class<JobCompanyLogo> getEntityClass() {
		return JobCompanyLogo.class;
	}

	@Override
	public void save(JobCompanyLogo jobCompanyLogo) {
		jobCompanyLogoRepository.save(jobCompanyLogo);
	}

	@Override
	public void deleteById(Long jobCompanyLogoId) {
		jobCompanyLogoRepository.deleteById(jobCompanyLogoId);
	}

	@Override
	public List<JobCompanyLogo> findAll() {
		return jobCompanyLogoRepository.findAll();
	}

	@Override
	public JobCompanyLogo findById(Long jobCompanyLogoId) {
		return jobCompanyLogoRepository.findByIdNotOptional(jobCompanyLogoId);
	}

	@Override
	public Page<JobCompanyLogo> findAll(Pageable pageable) {
		return jobCompanyLogoRepository.findAll(pageable);
	}

	@Override
	public Page<JobCompanyLogo> findAll(Pageable pageable, Example<JobCompanyLogo> example) {
		return jobCompanyLogoRepository.findAll(example, pageable);
	}

	@Override
	public Page<JobCompanyLogo> findAll(Pageable pageable, TableOrder tableOrder) {
		return jobCompanyLogoRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<JobCompanyLogo> findAll(Pageable pageable, TableOrder tableOrder, Example<JobCompanyLogo> example) {
		return jobCompanyLogoRepository.findAll(pageable, tableOrder, example);
	}

	@Override
	public Page<JobCompanyLogo> findAll(Pageable pageable, TableOrder tableOrder, Specification<JobCompanyLogo> specification) {
		return jobCompanyLogoRepository.findAll(pageable, tableOrder, specification);
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
