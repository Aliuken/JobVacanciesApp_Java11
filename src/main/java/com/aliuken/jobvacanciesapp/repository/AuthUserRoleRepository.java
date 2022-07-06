package com.aliuken.jobvacanciesapp.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthRole;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserRole;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserRoleRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserRole> {
	AuthUserRole findByAuthUserAndAuthRole(AuthUser authUser, AuthRole authRole);
	List<AuthUserRole> findByAuthUser(AuthUser authUser);
	List<AuthUserRole> findByAuthRole(AuthRole authRole);
	
	@Override
	default Class<AuthUserRole> getEntityClass() {
		return AuthUserRole.class;
	}
}
