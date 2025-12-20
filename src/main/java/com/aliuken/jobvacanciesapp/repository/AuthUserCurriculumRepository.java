package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.entity.factory.AuthUserCurriculumFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface AuthUserCurriculumRepository extends UpgradedJpaRepository<AuthUserCurriculum> {
	public static final AbstractEntityFactory<AuthUserCurriculum> ENTITY_FACTORY = new AuthUserCurriculumFactory();

//	@RepositoryMethod
//	@Query("SELECT auc FROM AuthUserCurriculum auc WHERE auc.authUser = :authUser AND auc.fileName = :fileName")
//	public abstract AuthUserCurriculum findByAuthUserAndFileName(@Param("authUser") AuthUser authUser, @Param("fileName") String fileName);

	@RepositoryMethod
	public default AuthUserCurriculum findByAuthUserAndFileName(final AuthUser authUser, final String fileName) {
		if(authUser == null || fileName == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("authUser", authUser);
		parameterMap.put("fileName", fileName);

		final AuthUserCurriculum authUserCurriculum = this.executeQuerySingleResult(
			"SELECT auc FROM AuthUserCurriculum auc WHERE auc.authUser = :authUser AND auc.fileName = :fileName", parameterMap);
		return authUserCurriculum;
	}

	@Override
	public default AbstractEntityFactory<AuthUserCurriculum> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
