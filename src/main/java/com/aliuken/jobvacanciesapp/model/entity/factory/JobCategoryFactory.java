package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.JobCategory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import org.jspecify.annotations.NonNull;

public class JobCategoryFactory extends AbstractEntityFactory<JobCategory> {
	public JobCategoryFactory() {
		super();
	}

	@Override
	protected @NonNull JobCategory createInstance() {
		return new JobCategory();
	}
}
