package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthRole;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthRoleRepository extends JpaRepositoryWithPaginationAndSorting<AuthRole> {
//	@Query("SELECT ar FROM AuthRole ar WHERE ar.name = :name")
//	AuthRole findByName(@Param("name") String name);

	default AuthRole findByName(String name) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("name", name);

		AuthRole authRole = this.executeQuerySingleResult("SELECT ar FROM AuthRole ar WHERE ar.name = :name", parameterMap);
		return authRole;
	}

	@Override
	default Class<AuthRole> getEntityClass() {
		return AuthRole.class;
	}
}
