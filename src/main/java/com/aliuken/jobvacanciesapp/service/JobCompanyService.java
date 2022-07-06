package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.service.superinterface.AbstractEntityExtraServiceInterface;

public interface JobCompanyService extends AbstractEntityExtraServiceInterface<JobCompany> {

	JobCompany findByName(String jobCompanyName);

}
