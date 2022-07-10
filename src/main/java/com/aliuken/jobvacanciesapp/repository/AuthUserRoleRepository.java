package com.aliuken.jobvacanciesapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthRole;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserRole;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserRoleRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserRole> {
	@Query("SELECT aur FROM AuthUserRole aur WHERE aur.authUser = :authUser AND aur.authRole = :authRole")
	AuthUserRole findByAuthUserAndAuthRole(@Param("authUser") AuthUser authUser, @Param("authRole") AuthRole authRole);

	@Override
	default Class<AuthUserRole> getEntityClass() {
		return AuthUserRole.class;
	}
}
