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
	public Class<JobCategory> getEntityClass() {
		return JobCategory.class;
	}

	@Override
	public void save(JobCategory jobCategory) {
		jobCategoryRepository.save(jobCategory);
	}

	@Override
	public void deleteById(Long jobCategoryId) {
		jobCategoryRepository.deleteById(jobCategoryId);
	}

	@Override
	public List<JobCategory> findAll() {
		return jobCategoryRepository.findAll();
	}

	@Override
	public JobCategory findById(Long jobCategoryId) {
		return jobCategoryRepository.findByIdNotOptional(jobCategoryId);
	}

	@Override
	public Page<JobCategory> findAll(Pageable pageable) {
		return jobCategoryRepository.findAll(pageable);
	}

	@Override
	public Page<JobCategory> findAll(Pageable pageable, Example<JobCategory> example) {
		return jobCategoryRepository.findAll(example, pageable);
	}

	@Override
	public Page<JobCategory> findAll(Pageable pageable, TableOrder tableOrder) {
		return jobCategoryRepository.findAll(pageable, tableOrder);
	}

	@Override
	public Page<JobCategory> findAll(Pageable pageable, TableOrder tableOrder, Example<JobCategory> example) {
		return jobCategoryRepository.findAll(pageable, tableOrder, example);
	}

	@Override
	public Page<JobCategory> findAll(Pageable pageable, TableOrder tableOrder, Specification<JobCategory> specification) {
		return jobCategoryRepository.findAll(pageable, tableOrder, specification);
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
