package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.service.superclass.JobCompanyLogoServiceSuperclass;

public abstract class JobCompanyLogoService extends JobCompanyLogoServiceSuperclass {

	public abstract JobCompanyLogo findByJobCompanyAndFileName(final JobCompany jobCompany, final String fileName);

}
