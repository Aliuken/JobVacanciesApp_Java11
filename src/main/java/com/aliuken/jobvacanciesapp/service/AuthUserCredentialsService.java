package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.service.superinterface.AbstractEntityExtraServiceInterface;

public interface AuthUserCredentialsService extends AbstractEntityExtraServiceInterface<AuthUserCredentials> {

	AuthUserCredentials findByEmail(String email);
	AuthUserCredentials findByEmailAndEncryptedPassword(String email, String encryptedPassword);

}