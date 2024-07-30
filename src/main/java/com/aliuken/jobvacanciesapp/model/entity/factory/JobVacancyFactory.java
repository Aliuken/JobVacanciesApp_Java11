package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class JobVacancyFactory extends AbstractEntityFactory<JobVacancy> {
	public JobVacancyFactory() {
		super();
	}

	@Override
	protected JobVacancy createInstance() {
		return new JobVacancy();
	}
}
