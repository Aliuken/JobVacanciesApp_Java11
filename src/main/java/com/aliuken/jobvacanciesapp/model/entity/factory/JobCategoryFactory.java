package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.JobCategory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class JobCategoryFactory extends AbstractEntityFactory<JobCategory> {
	public JobCategoryFactory() {
		super();
	}

	@Override
	protected JobCategory createInstance() {
		return new JobCategory();
	}
}
