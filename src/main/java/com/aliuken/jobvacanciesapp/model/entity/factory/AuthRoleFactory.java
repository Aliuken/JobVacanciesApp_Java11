package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;
import org.jspecify.annotations.NonNull;

public class AuthRoleFactory extends AbstractEntityFactory<AuthRole> {
	public AuthRoleFactory() {
		super();
	}

	@Override
	protected @NonNull AuthRole createInstance() {
		return new AuthRole();
	}
}
