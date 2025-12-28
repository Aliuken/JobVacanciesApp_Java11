package com.aliuken.jobvacanciesapp.model.comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superclass.AbstractEntitySpecificComparator;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class JobRequestAuthUserFullNameComparator extends AbstractEntitySpecificComparator<JobRequest, String> {
	@Override
	public final @NonNull Function<JobRequest, String> getFirstCompareFieldFunction() {
		final Function<JobRequest, String> function = jobRequest -> jobRequest.getAuthUser().getFullName();
		return function;
	}

	@Override
	public boolean getIsDescendingOrder() {
		return false;
	}
}