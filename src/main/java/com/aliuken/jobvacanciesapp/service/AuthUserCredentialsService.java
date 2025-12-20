package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.service.superclass.AbstractEntityServiceSuperclass;

public abstract class AuthUserCredentialsService extends AbstractEntityServiceSuperclass<AuthUserCredentials> {

	public abstract AuthUserCredentials findByEmail(final String email);

	public abstract AuthUserCredentials findByEmailAndEncryptedPassword(final String email, final String encryptedPassword);

}