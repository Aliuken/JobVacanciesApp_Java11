package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserSignUpConfirmation;
import com.aliuken.jobvacanciesapp.model.entity.factory.AuthUserSignUpConfirmationFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface AuthUserSignUpConfirmationRepository extends UpgradedJpaRepository<AuthUserSignUpConfirmation> {
	public static final AbstractEntityFactory<AuthUserSignUpConfirmation> ENTITY_FACTORY = new AuthUserSignUpConfirmationFactory();

//	@RepositoryMethod
//	@Query("SELECT ausc FROM AuthUserSignUpConfirmation ausc WHERE ausc.email = :email")
//	public abstract AuthUserSignUpConfirmation findByEmail(@Param("email") String email);

//	@RepositoryMethod
//	@Query("SELECT ausc FROM AuthUserSignUpConfirmation ausc WHERE ausc.email = :email AND ausc.uuid = :uuid")
//	public abstract AuthUserSignUpConfirmation findByEmailAndUuid(@Param("email") String email, @Param("uuid") String uuid);

	@RepositoryMethod
	public default AuthUserSignUpConfirmation findByEmail(final String email) {
		if(email == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);

		final AuthUserSignUpConfirmation authUserSignUpConfirmation = this.executeQuerySingleResult(
			"SELECT ausc FROM AuthUserSignUpConfirmation ausc WHERE ausc.email = :email", parameterMap);
		return authUserSignUpConfirmation;
	}

	@RepositoryMethod
	public default AuthUserSignUpConfirmation findByEmailAndUuid(final String email, final String uuid) {
		if(email == null || uuid == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);
		parameterMap.put("uuid", uuid);

		final AuthUserSignUpConfirmation authUserSignUpConfirmation = this.executeQuerySingleResult(
			"SELECT ausc FROM AuthUserSignUpConfirmation ausc WHERE ausc.email = :email AND ausc.uuid = :uuid", parameterMap);
		return authUserSignUpConfirmation;
	}

	@Override
	public default AbstractEntityFactory<AuthUserSignUpConfirmation> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
