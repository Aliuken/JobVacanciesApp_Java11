package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserSignUpConfirmation;
import com.aliuken.jobvacanciesapp.repository.AuthUserSignUpConfirmationRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthUserSignUpConfirmationServiceImpl extends AuthUserSignUpConfirmationService {

	@Autowired
	private AuthUserSignUpConfirmationRepository authUserSignUpConfirmationRepository;

	@Override
	public UpgradedJpaRepository<AuthUserSignUpConfirmation> getEntityRepository() {
		return authUserSignUpConfirmationRepository;
	}

	@Override
	@ServiceMethod
	public AuthUserSignUpConfirmation findByEmail(final String email) {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByEmail(email);
		return authUserSignUpConfirmation;
	}

	@Override
	@ServiceMethod
	public AuthUserSignUpConfirmation findByEmailAndUuid(final String email, final String uuid) {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByEmailAndUuid(email, uuid);
		return authUserSignUpConfirmation;
	}

	@Override
	public AuthUserSignUpConfirmation getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = new AuthUserSignUpConfirmation();
		authUserSignUpConfirmation.setId(id);
		authUserSignUpConfirmation.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authUserSignUpConfirmation.setLastModificationAuthUser(lastModificationAuthUser);

		return authUserSignUpConfirmation;
	}
}
