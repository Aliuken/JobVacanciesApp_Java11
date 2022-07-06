package com.aliuken.jobvacanciesapp.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserCurriculumRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserCurriculum> {
	List<AuthUserCurriculum> findByAuthUser(AuthUser authUser);
	
	@Override
	default Class<AuthUserCurriculum> getEntityClass() {
		return AuthUserCurriculum.class;
	}
}
