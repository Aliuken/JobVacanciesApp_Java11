package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthRole;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserRole;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserRoleRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserRole> {
//	@Query("SELECT aur FROM AuthUserRole aur WHERE aur.authUser = :authUser AND aur.authRole = :authRole")
//	AuthUserRole findByAuthUserAndAuthRole(@Param("authUser") AuthUser authUser, @Param("authRole") AuthRole authRole);

	default AuthUserRole findByAuthUserAndAuthRole(AuthUser authUser, AuthRole authRole) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("authUser", authUser);
		parameterMap.put("authRole", authRole);

		AuthUserRole authUserRole = this.executeQuerySingleResult(
			"SELECT aur FROM AuthUserRole aur WHERE aur.authUser = :authUser AND aur.authRole = :authRole", parameterMap);
		return authUserRole;
	}

	@Override
	default Class<AuthUserRole> getEntityClass() {
		return AuthUserRole.class;
	}
}
