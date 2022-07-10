package com.aliuken.jobvacanciesapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthRole;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthRoleRepository extends JpaRepositoryWithPaginationAndSorting<AuthRole> {
	@Query("SELECT ar FROM AuthRole ar WHERE ar.name = :name")
	AuthRole findByName(@Param("name") String name);

	@Override
	default Class<AuthRole> getEntityClass() {
		return AuthRole.class;
	}
}
