package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.annotation.RepositoryMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.factory.AuthRoleFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface AuthRoleRepository extends UpgradedJpaRepository<AuthRole> {
	public static final AbstractEntityFactory<AuthRole> ENTITY_FACTORY = new AuthRoleFactory();

//	@RepositoryMethod
//	@Query("SELECT ar FROM AuthRole ar WHERE ar.name = :name")
//	public abstract AuthRole findByName(@Param("name") String name);

	@RepositoryMethod
	public default AuthRole findByName(final String name) {
		if(name == null) {
			return null;
		}

		final Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("name", name);

		final AuthRole authRole = this.executeQuerySingleResult(
			"SELECT ar FROM AuthRole ar WHERE ar.name = :name", parameterMap);
		return authRole;
	}

	@Override
	public default AbstractEntityFactory<AuthRole> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
