package com.aliuken.jobvacanciesapp.model.comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superclass.AbstractEntitySpecificComparator;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;

import java.util.function.Function;

public class JobRequestAuthUserFullNameComparator extends AbstractEntitySpecificComparator<JobRequest, String> {
	@Override
	public final Function<JobRequest, String> getFirstCompareFieldFunction() {
		return jobRequest -> jobRequest.getAuthUser().getFullName();
	}

	@Override
	public boolean getIsDescendingOrder() {
		return false;
	}
}