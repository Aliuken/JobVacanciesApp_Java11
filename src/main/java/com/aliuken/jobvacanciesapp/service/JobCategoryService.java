package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.JobCategory;
import com.aliuken.jobvacanciesapp.service.superclass.AbstractEntityServiceSuperclass;

public abstract class JobCategoryService extends AbstractEntityServiceSuperclass<JobCategory> {

	public abstract JobCategory findByName(final String name);

}
