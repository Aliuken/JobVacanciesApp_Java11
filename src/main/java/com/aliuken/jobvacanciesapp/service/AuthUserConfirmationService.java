package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.service.superinterface.AbstractEntityExtraServiceInterface;

public interface AuthUserConfirmationService extends AbstractEntityExtraServiceInterface<AuthUserConfirmation> {

	AuthUserConfirmation findByEmail(String email);

	AuthUserConfirmation findByEmailAndUuid(String email, String uuid);

}