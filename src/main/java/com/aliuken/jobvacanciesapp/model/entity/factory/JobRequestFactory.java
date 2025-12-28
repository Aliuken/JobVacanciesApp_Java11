package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import org.jspecify.annotations.NonNull;

public class JobRequestFactory extends AbstractEntityFactory<JobRequest> {
	public JobRequestFactory() {
		super();
	}

	@Override
	protected @NonNull JobRequest createInstance() {
		return new JobRequest();
	}
}
