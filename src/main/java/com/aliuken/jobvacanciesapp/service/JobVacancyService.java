package com.aliuken.jobvacanciesapp.service;

import java.util.List;

import org.springframework.data.domain.Example;

import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.service.superinterface.JobVacancyExtraServiceInterface;

public interface JobVacancyService extends JobVacancyExtraServiceInterface {

	List<JobVacancy> findByHighlightedAndStatusOrderByIdDesc(Boolean highlighted, JobVacancyStatus status);

	List<JobVacancy> findAllHighlighted();

	List<JobVacancy> findAll(Example<JobVacancy> jobVacancyExample);

}
