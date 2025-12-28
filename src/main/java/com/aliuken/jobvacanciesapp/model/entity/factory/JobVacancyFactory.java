package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import org.jspecify.annotations.NonNull;

public class JobVacancyFactory extends AbstractEntityFactory<JobVacancy> {
	public JobVacancyFactory() {
		super();
	}

	@Override
	protected @NonNull JobVacancy createInstance() {
		return new JobVacancy();
	}
}
