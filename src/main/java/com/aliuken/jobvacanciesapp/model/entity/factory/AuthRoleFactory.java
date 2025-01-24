package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class AuthRoleFactory extends AbstractEntityFactory<AuthRole> {
	public AuthRoleFactory() {
		super();
	}

	@Override
	protected AuthRole createInstance() {
		return new AuthRole();
	}
}
