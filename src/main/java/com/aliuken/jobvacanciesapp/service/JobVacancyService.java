package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.service.superclass.JobVacancyServiceSuperclass;

import java.util.List;

public abstract class JobVacancyService extends JobVacancyServiceSuperclass {

	public abstract List<JobVacancy> findByHighlightedAndStatusOrderByIdDesc(final Boolean highlighted, final JobVacancyStatus status);

	public abstract List<JobVacancy> findAllHighlighted();

}
