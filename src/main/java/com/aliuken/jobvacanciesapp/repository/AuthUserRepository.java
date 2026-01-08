package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.factory.AuthUserFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface AuthUserRepository extends UpgradedJpaRepository<AuthUser> {
	public static final AbstractEntityFactory<AuthUser> ENTITY_FACTORY = new AuthUserFactory();

//	@RepositoryMethod
//	@Query("SELECT au FROM AuthUser au WHERE au.email = :email")
//	public abstract AuthUser findByEmail(@Param("email") String email);

//	@RepositoryMethod
//	@Modifying
//	@Query("UPDATE AuthUser au SET au.enabled=0 WHERE au.id = :authUserId")
//	public abstract int lock(@Param("authUserId") long authUserId);

//	@RepositoryMethod
//	@Modifying
//	@Query("UPDATE AuthUser au SET au.enabled=1 WHERE au.id = :authUserId")
//	public abstract int unlock(@Param("authUserId") long authUserId);

	@RepositoryMethod
	public default AuthUser findByEmail(final String email) {
		if(email == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);

		final AuthUser authUser = this.executeQuerySingleResult(
			"SELECT au FROM AuthUser au WHERE au.email = :email", parameterMap);
		return authUser;
	}

	@RepositoryMethod
	public default void lock(final Long authUserId) {
		final AuthUser authUser = this.findByIdNotOptional(authUserId);
		if(authUser != null) {
			authUser.setEnabled(Boolean.FALSE);
		}
	}

//	@RepositoryMethod
//	public default int lock(final Long authUserId) {
//		final Map<String, Object> parameterMap = new HashMap<>();
//		parameterMap.put("authUserId", authUserId);
//
//		final int rows = this.executeUpdate("UPDATE AuthUser au SET au.enabled=0 WHERE au.id = :authUserId", parameterMap);
//		return rows;
//	}

	@RepositoryMethod
	public default void unlock(final Long authUserId) {
		final AuthUser authUser = this.findByIdNotOptional(authUserId);
		if(authUser != null) {
			authUser.setEnabled(Boolean.TRUE);
		}
	}

//	@RepositoryMethod
//	public default int unlock(final Long authUserId) {
//		final Map<String, Object> parameterMap = new HashMap<>();
//		parameterMap.put("authUserId", authUserId);
//
//		final int rows = this.executeUpdate("UPDATE AuthUser au SET au.enabled=1 WHERE au.id = :authUserId", parameterMap);
//		return rows;
//	}

	@Override
	public default @NonNull AbstractEntityFactory<AuthUser> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
