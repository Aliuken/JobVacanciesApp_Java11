package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserResetPassword;
import com.aliuken.jobvacanciesapp.model.entity.factory.AuthUserResetPasswordFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface AuthUserResetPasswordRepository extends UpgradedJpaRepository<AuthUserResetPassword> {
	public static final AbstractEntityFactory<AuthUserResetPassword> ENTITY_FACTORY = new AuthUserResetPasswordFactory();

//	@RepositoryMethod
//	@Query("SELECT aurp FROM AuthUserResetPassword aurp WHERE aurp.email = :email")
//	public abstract AuthUserResetPassword findByEmail(@Param("email") String email);

//	@RepositoryMethod
//	@Query("SELECT aurp FROM AuthUserResetPassword aurp WHERE aurp.email = :email AND aurp.uuid = :uuid")
//	public abstract AuthUserResetPassword findByEmailAndUuid(@Param("email") String email, @Param("uuid") String uuid);

	@RepositoryMethod
	public default AuthUserResetPassword findByEmail(final String email) {
		if(email == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);

		final AuthUserResetPassword authUserResetPassword = this.executeQuerySingleResult(
			"SELECT aurp FROM AuthUserResetPassword aurp WHERE aurp.email = :email", parameterMap);
		return authUserResetPassword;
	}

	@RepositoryMethod
	public default AuthUserResetPassword findByEmailAndUuid(final String email, final String uuid) {
		if(email == null || uuid == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);
		parameterMap.put("uuid", uuid);

		final AuthUserResetPassword authUserResetPassword = this.executeQuerySingleResult(
			"SELECT aurp FROM AuthUserResetPassword aurp WHERE aurp.email = :email AND aurp.uuid = :uuid", parameterMap);
		return authUserResetPassword;
	}

	@Override
	public default @NonNull AbstractEntityFactory<AuthUserResetPassword> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
