package com.aliuken.jobvacanciesapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface JobVacancyRepository extends JpaRepositoryWithPaginationAndSorting<JobVacancy> {
	@Query("SELECT jv FROM JobVacancy jv WHERE jv.highlighted = :highlighted AND jv.status = :status ORDER BY id desc")
	List<JobVacancy> findByHighlightedAndStatusOrderByIdDesc(@Param("highlighted") boolean highlighted, @Param("status") JobVacancyStatus status);

	@Override
	default Class<JobVacancy> getEntityClass() {
		return JobVacancy.class;
	}
}
