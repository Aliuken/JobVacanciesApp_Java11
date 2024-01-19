package com.aliuken.jobvacanciesapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.repository.AuthUserConfirmationRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;

@Service
@Transactional
public class AuthUserConfirmationServiceImpl extends AuthUserConfirmationService {

	@Autowired
	private AuthUserConfirmationRepository authUserConfirmationRepository;

	@Override
	public UpgradedJpaRepository<AuthUserConfirmation> getEntityRepository() {
		return authUserConfirmationRepository;
	}

	@Override
	@ServiceMethod
	public AuthUserConfirmation findByEmail(final String email) {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmail(email);
		return authUserConfirmation;
	}

	@Override
	@ServiceMethod
	public AuthUserConfirmation findByEmailAndUuid(final String email, final String uuid) {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmailAndUuid(email, uuid);
		return authUserConfirmation;
	}

	@Override
	public AuthUserConfirmation getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final AuthUserConfirmation authUserConfirmation = new AuthUserConfirmation();
		authUserConfirmation.setId(id);
		authUserConfirmation.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authUserConfirmation.setLastModificationAuthUser(lastModificationAuthUser);

		return authUserConfirmation;
	}

}
