package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.service.superinterface.AbstractEntityExtraServiceInterface;

public interface JobCompanyLogoService extends AbstractEntityExtraServiceInterface<JobCompanyLogo> {
	JobCompanyLogo findByJobCompanyAndFileName(JobCompany jobCompany, String fileName);
}
