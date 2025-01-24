package com.aliuken.jobvacanciesapp.model.entity.factory;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserResetPassword;
import com.aliuken.jobvacanciesapp.model.entity.factory.superclass.AbstractEntityFactory;

public class AuthUserResetPasswordFactory extends AbstractEntityFactory<AuthUserResetPassword> {
	public AuthUserResetPasswordFactory() {
		super();
	}

	@Override
	protected AuthUserResetPassword createInstance() {
		return new AuthUserResetPassword();
	}
}
