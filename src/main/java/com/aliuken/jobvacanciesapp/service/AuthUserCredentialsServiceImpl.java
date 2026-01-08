package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.repository.AuthUserCredentialsRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthUserCredentialsServiceImpl extends AuthUserCredentialsService {

	@Autowired
	private AuthUserCredentialsRepository authUserCredentialsRepository;

	@Override
	public @NonNull UpgradedJpaRepository<AuthUserCredentials> getEntityRepository() {
		return authUserCredentialsRepository;
	}

	@Override
	@ServiceMethod
	public AuthUserCredentials findByEmail(final String email) {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmail(email);
		return authUserCredentials;
	}

	@Override
	@ServiceMethod
	public AuthUserCredentials findByEmailAndEncryptedPassword(final String email, final String encryptedPassword) {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmailAndEncryptedPassword(email, encryptedPassword);
		return authUserCredentials;
	}

	@Override
	public @NonNull AuthUserCredentials getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final AuthUserCredentials authUserCredentials = new AuthUserCredentials();
		authUserCredentials.setId(id);
		authUserCredentials.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authUserCredentials.setLastModificationAuthUser(lastModificationAuthUser);

		return authUserCredentials;
	}
}
