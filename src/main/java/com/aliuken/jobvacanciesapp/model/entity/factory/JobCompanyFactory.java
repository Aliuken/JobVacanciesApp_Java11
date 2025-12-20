package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class JobCompanyFactory extends AbstractEntityFactory<JobCompany> {
	public JobCompanyFactory() {
		super();
	}

	@Override
	protected JobCompany createInstance() {
		return new JobCompany();
	}
}
