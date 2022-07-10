package com.aliuken.jobvacanciesapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserCurriculumRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserCurriculum> {
	@Query("SELECT auc FROM AuthUserCurriculum auc WHERE auc.authUser = :authUser AND auc.fileName = :fileName")
	AuthUserCurriculum findByAuthUserAndFileName(@Param("authUser") AuthUser authUser, @Param("fileName") String fileName);

	@Override
	default Class<AuthUserCurriculum> getEntityClass() {
		return AuthUserCurriculum.class;
	}
}
