package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;
import com.aliuken.jobvacanciesapp.model.entity.factory.AuthUserRoleFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface AuthUserRoleRepository extends UpgradedJpaRepository<AuthUserRole> {
	public static final AbstractEntityFactory<AuthUserRole> ENTITY_FACTORY = new AuthUserRoleFactory();

//	@RepositoryMethod
//	@Query("SELECT aur FROM AuthUserRole aur WHERE aur.authUser = :authUser AND aur.authRole = :authRole")
//	public abstract AuthUserRole findByAuthUserAndAuthRole(@Param("authUser") AuthUser authUser, @Param("authRole") AuthRole authRole);

	@RepositoryMethod
	public default AuthUserRole findByAuthUserAndAuthRole(final AuthUser authUser, final AuthRole authRole) {
		if(authUser == null || authRole == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("authUser", authUser);
		parameterMap.put("authRole", authRole);

		final AuthUserRole authUserRole = this.executeQuerySingleResult(
			"SELECT aur FROM AuthUserRole aur WHERE aur.authUser = :authUser AND aur.authRole = :authRole", parameterMap);
		return authUserRole;
	}

	@Override
	public default @NonNull AbstractEntityFactory<AuthUserRole> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
