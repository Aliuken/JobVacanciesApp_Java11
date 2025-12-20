package com.aliuken.jobvacanciesapp.model.comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superclass.AbstractEntitySpecificComparator;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;

import java.time.chrono.ChronoLocalDateTime;
import java.util.function.Function;

public class JobRequestJobVacancyPublicationDateTimeComparator extends AbstractEntitySpecificComparator<JobRequest, ChronoLocalDateTime<?>> {
	@Override
	public final Function<JobRequest, ChronoLocalDateTime<?>> getFirstCompareFieldFunction() {
		return jobRequest -> jobRequest.getJobVacancy().getPublicationDateTime();
	}

	@Override
	public boolean getIsDescendingOrder() {
		return false;
	}
}