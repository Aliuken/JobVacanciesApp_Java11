package com.aliuken.jobvacanciesapp.repository;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthRole;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthRoleRepository extends JpaRepositoryWithPaginationAndSorting<AuthRole> {
	AuthRole findByName(String name);
	
	@Override
	default Class<AuthRole> getEntityClass() {
		return AuthRole.class;
	}
}
