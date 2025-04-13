package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserEntityQuery;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class AuthUserEntityQueryFactory extends AbstractEntityFactory<AuthUserEntityQuery> {
	public AuthUserEntityQueryFactory() {
		super();
	}

	@Override
	protected AuthUserEntityQuery createInstance() {
		return new AuthUserEntityQuery();
	}
}
