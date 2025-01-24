package com.aliuken.jobvacanciesapp.model.comparator;

import java.util.Comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superinterface.AbstractEntityIdComparator;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;

public class JobRequestAuthUserFullNameComparator implements Comparator<JobRequest>, AbstractEntityIdComparator {
	@Override
	public int compare(final JobRequest jobRequest1, final JobRequest jobRequest2) {
		final AuthUser authUser1;
		final String authUserFullName1;
		if(jobRequest1 != null) {
			authUser1 = jobRequest1.getAuthUser();
			authUserFullName1 = authUser1.getFullName();
		} else {
			return -1;
		}

		final AuthUser authUser2;
		final String authUserFullName2;
		if(jobRequest2 != null) {
			authUser2 = jobRequest2.getAuthUser();
			authUserFullName2 = authUser2.getFullName();
		} else {
			return 1;
		}

		int result = authUserFullName1.compareTo(authUserFullName2);
		if(result == 0) {
			result = this.compareAbstractEntityIdDesc(authUser1, authUser2);
		}
		return result;
	}
}