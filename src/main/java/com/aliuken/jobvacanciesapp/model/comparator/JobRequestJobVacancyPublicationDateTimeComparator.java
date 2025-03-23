package com.aliuken.jobvacanciesapp.model.comparator;

import java.time.LocalDateTime;
import java.util.Comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superinterface.AbstractEntityIdComparator;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;

public class JobRequestJobVacancyPublicationDateTimeComparator implements Comparator<JobRequest>, AbstractEntityIdComparator {
	@Override
	public int compare(final JobRequest jobRequest1, final JobRequest jobRequest2) {
		final JobVacancy jobVacancy1;
		final LocalDateTime jobVacancyPublicationDateTime1;
		if(jobRequest1 != null) {
			jobVacancy1 = jobRequest1.getJobVacancy();
			jobVacancyPublicationDateTime1 = jobVacancy1.getPublicationDateTime();
		} else {
			return 1;
		}

		final JobVacancy jobVacancy2;
		final LocalDateTime jobVacancyPublicationDateTime2;
		if(jobRequest2 != null) {
			jobVacancy2 = jobRequest2.getJobVacancy();
			jobVacancyPublicationDateTime2 = jobVacancy2.getPublicationDateTime();
		} else {
			return -1;
		}

		int result = jobVacancyPublicationDateTime2.compareTo(jobVacancyPublicationDateTime1);
		if(result == 0) {
			result = this.compareAbstractEntityIdDesc(jobVacancy1, jobVacancy2);
		}
		return result;
	}
}