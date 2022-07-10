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
import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.model.dto.TableOrder;
import com.aliuken.jobvacanciesapp.repository.JobCategoryRepository;

@Service
@Transactional
public class JobCategoryServiceImpl implements JobCategoryService {

	@Autowired
	private JobCategoryRepository jobCategoryRepository;

	@Override
	public JobCategory findByName(String name) {
		return jobCategoryRepository.findByName(name);
	}

	@Override
	public Class<JobCategory> getEntityClass() {
		return JobCategory.class;
	}

	@Override
	public JobCategory saveAndFlush(JobCategory jobCategory) {
		return jobCategoryRepository.saveAndFlush(jobCategory);
	}

	@Override
	public void deleteByIdAndFlush(Long jobCategoryId) {
		jobCategoryRepository.deleteByIdAndFlush(jobCategoryId);
	}

	@Override
	public List<JobCategory> findAll() {
		return jobCategoryRepository.findAll();
	}

	@Override
	public JobCategory findByIdNotOptional(Long jobCategoryId) {
		return jobCategoryRepository.findByIdNotOptional(jobCategoryId);
	}

	@Override
	public Page<JobCategory> findAll(Pageable pageable) {
		return jobCategoryRepository.findAll(pageable);
	}

	@Override
	public Page<JobCategory> findAll(Pageable pageable, TableOrder tableOrder) {
		return jobCategoryRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<JobCategory> findAll(Pageable pageable, TableOrder tableOrder, Specification<JobCategory> specification) {
		return jobCategoryRepository.findAll(pageable, tableOrder, specification);
	}

	@Override
	public Page<JobCategory> findAll(Example<JobCategory> example, Pageable pageable) {
		return jobCategoryRepository.findAll(example, pageable);
	}

	@Override
	public Page<JobCategory> findAll(Example<JobCategory> example, Pageable pageable, TableOrder tableOrder) {
		return jobCategoryRepository.findAll(example, pageable, tableOrder);
	}

	@Override
	public JobCategory getNewEntityWithGenericData(Long id, AuthUser firstRegistrationAuthUser, AuthUser lastModificationAuthUser) {
		JobCategory jobCategory = new JobCategory();
		jobCategory.setId(id);
		jobCategory.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		jobCategory.setLastModificationAuthUser(lastModificationAuthUser);

		return jobCategory;
	}

}
