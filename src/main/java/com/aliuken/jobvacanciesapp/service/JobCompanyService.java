package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.service.superclass.AbstractEntityServiceSuperclass;

public abstract class JobCompanyService extends AbstractEntityServiceSuperclass<JobCompany> {

	public abstract JobCompany findByName(final String jobCompanyName);

}
