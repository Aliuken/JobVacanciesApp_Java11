package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserCurriculumRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserCurriculum> {
//	@Query("SELECT auc FROM AuthUserCurriculum auc WHERE auc.authUser = :authUser AND auc.fileName = :fileName")
//	AuthUserCurriculum findByAuthUserAndFileName(@Param("authUser") AuthUser authUser, @Param("fileName") String fileName);

	default AuthUserCurriculum findByAuthUserAndFileName(AuthUser authUser, String fileName) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("authUser", authUser);
		parameterMap.put("fileName", fileName);

		AuthUserCurriculum authUserCurriculum = this.executeQuerySingleResult(
			"SELECT auc FROM AuthUserCurriculum auc WHERE auc.authUser = :authUser AND auc.fileName = :fileName", parameterMap);
		return authUserCurriculum;
	}

	@Override
	default Class<AuthUserCurriculum> getEntityClass() {
		return AuthUserCurriculum.class;
	}
}
