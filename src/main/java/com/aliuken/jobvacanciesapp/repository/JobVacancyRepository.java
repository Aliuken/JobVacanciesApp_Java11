package com.aliuken.jobvacanciesapp.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobVacancyRepository extends JpaRepositoryWithPaginationAndSorting<JobVacancy> {

	List<JobVacancy> findByStatus(JobVacancyStatus status);

	List<JobVacancy> findByJobCategory(JobCategory jobCategory);

	List<JobVacancy> findByHighlightedAndStatusOrderByIdDesc(boolean highlighted, JobVacancyStatus status);

	List<JobVacancy> findBySalaryBetweenOrderBySalaryDesc(BigDecimal salary1, BigDecimal salary2);

	List<JobVacancy> findByStatusIn(JobVacancyStatus[] statusArray);
	
	@Override
	default Class<JobVacancy> getEntityClass() {
		return JobVacancy.class;
	}
}
