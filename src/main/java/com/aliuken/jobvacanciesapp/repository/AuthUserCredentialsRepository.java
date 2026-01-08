package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.entity.factory.AuthUserCredentialsFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface AuthUserCredentialsRepository extends UpgradedJpaRepository<AuthUserCredentials> {
	public static final AbstractEntityFactory<AuthUserCredentials> ENTITY_FACTORY = new AuthUserCredentialsFactory();

//	@RepositoryMethod
//	@Query("SELECT auc FROM AuthUserCredentials auc WHERE auc.email = :email")
//	public abstract AuthUserCredentials findByEmail(@Param("email") String email);

//	@RepositoryMethod
//	@Query("SELECT auc FROM AuthUserCredentials auc WHERE auc.email = :email AND auc.encryptedPassword = :encryptedPassword")
//	public abstract AuthUserCredentials findByEmailAndEncryptedPassword(@Param("email") String email, @Param("encryptedPassword") String encryptedPassword);

	@RepositoryMethod
	public default AuthUserCredentials findByEmail(final String email) {
		if(email == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);

		final AuthUserCredentials authUserCredentials = this.executeQuerySingleResult(
			"SELECT auc FROM AuthUserCredentials auc WHERE auc.email = :email", parameterMap);
		return authUserCredentials;
	}

	@RepositoryMethod
	public default AuthUserCredentials findByEmailAndEncryptedPassword(final String email, final String encryptedPassword) {
		if(email == null || encryptedPassword == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);
		parameterMap.put("encryptedPassword", encryptedPassword);

		final AuthUserCredentials authUserCredentials = this.executeQuerySingleResult(
			"SELECT auc FROM AuthUserCredentials auc WHERE auc.email = :email AND auc.encryptedPassword = :encryptedPassword", parameterMap);
		return authUserCredentials;
	}

	@Override
	public default @NonNull AbstractEntityFactory<AuthUserCredentials> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}