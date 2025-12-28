package com.aliuken.jobvacanciesapp.model.comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superclass.AbstractEntitySpecificComparator;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import org.jspecify.annotations.NonNull;

import java.time.chrono.ChronoLocalDateTime;
import java.util.function.Function;

public class JobRequestJobVacancyPublicationDateTimeComparator extends AbstractEntitySpecificComparator<JobRequest, ChronoLocalDateTime<?>> {
	@Override
	public final @NonNull Function<JobRequest, ChronoLocalDateTime<?>> getFirstCompareFieldFunction() {
		final Function<JobRequest, ChronoLocalDateTime<?>> function = jobRequest -> jobRequest.getJobVacancy().getPublicationDateTime();
		return function;
	}

	@Override
	public boolean getIsDescendingOrder() {
		return false;
	}
}