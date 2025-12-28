package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserEntityQuery;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import org.jspecify.annotations.NonNull;

public class AuthUserEntityQueryFactory extends AbstractEntityFactory<AuthUserEntityQuery> {
	public AuthUserEntityQueryFactory() {
		super();
	}

	@Override
	protected @NonNull AuthUserEntityQuery createInstance() {
		return new AuthUserEntityQuery();
	}
}
