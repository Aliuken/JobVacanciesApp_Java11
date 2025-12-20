package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class JobRequestFactory extends AbstractEntityFactory<JobRequest> {
	public JobRequestFactory() {
		super();
	}

	@Override
	protected JobRequest createInstance() {
		return new JobRequest();
	}
}
