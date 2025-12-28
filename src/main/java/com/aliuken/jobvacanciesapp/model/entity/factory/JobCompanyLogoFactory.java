package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import org.jspecify.annotations.NonNull;

public class JobCompanyLogoFactory extends AbstractEntityFactory<JobCompanyLogo> {
	public JobCompanyLogoFactory() {
		super();
	}

	@Override
	protected @NonNull JobCompanyLogo createInstance() {
		return new JobCompanyLogo();
	}
}
