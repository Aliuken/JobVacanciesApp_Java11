package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.service.superclass.JobVacancyServiceSuperclass;
import org.jspecify.annotations.NonNull;

import java.util.List;

public abstract class JobVacancyService extends JobVacancyServiceSuperclass {

	public abstract @NonNull List<JobVacancy> findByHighlightedAndStatusOrderByIdDesc(final Boolean highlighted, final JobVacancyStatus status);

	public abstract @NonNull List<JobVacancy> findAllHighlighted();

}
