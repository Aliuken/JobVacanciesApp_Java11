package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserEntityQuery;
import com.aliuken.jobvacanciesapp.model.entity.factory.AuthUserEntityQueryFactory;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthUserEntityQueryRepository extends UpgradedJpaRepository<AuthUserEntityQuery> {
	public static final AbstractEntityFactory<AuthUserEntityQuery> ENTITY_FACTORY = new AuthUserEntityQueryFactory();

	@Override
	public default AbstractEntityFactory<AuthUserEntityQuery> getEntityFactory() {
		return ENTITY_FACTORY;
	}
}
