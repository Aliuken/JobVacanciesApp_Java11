package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.model.entity.factory.AuthUserConfirmationFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;

@Repository
public interface AuthUserConfirmationRepository extends UpgradedJpaRepository<AuthUserConfirmation> {
	public static final AbstractEntityFactory<AuthUserConfirmation> ENTITY_FACTORY = new AuthUserConfirmationFactory();

//	@RepositoryMethod
//	@Query("SELECT auc FROM AuthUserConfirmation auc WHERE auc.email = :email")
//	public abstract AuthUserConfirmation findByEmail(@Param("email") String email);

//	@RepositoryMethod
//	@Query("SELECT auc FROM AuthUserConfirmation auc WHERE auc.email = :email AND auc.uuid = :uuid")
//	public abstract AuthUserConfirmation findByEmailAndUuid(@Param("email") String email, @Param("uuid") String uuid);

	@RepositoryMethod
	public default AuthUserConfirmation findByEmail(final String email) {
		if(email == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);

		final AuthUserConfirmation authUserConfirmation = this.executeQuerySingleResult(
			"SELECT auc FROM AuthUserConfirmation auc WHERE auc.email = :email", parameterMap);
		return authUserConfirmation;
	}

	@RepositoryMethod
	public default AuthUserConfirmation findByEmailAndUuid(final String email, final String uuid) {
		if(email == null || uuid == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);
		parameterMap.put("uuid", uuid);

		final AuthUserConfirmation authUserConfirmation = this.executeQuerySingleResult(
			"SELECT auc FROM AuthUserConfirmation auc WHERE auc.email = :email AND auc.uuid = :uuid", parameterMap);
		return authUserConfirmation;
	}

	@Override
	public default AbstractEntityFactory<AuthUserConfirmation> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
