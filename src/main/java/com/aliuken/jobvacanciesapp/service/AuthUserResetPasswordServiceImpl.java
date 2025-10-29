package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserResetPassword;
import com.aliuken.jobvacanciesapp.repository.AuthUserResetPasswordRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthUserResetPasswordServiceImpl extends AuthUserResetPasswordService {

	@Autowired
	private AuthUserResetPasswordRepository authUserResetPasswordRepository;

	@Override
	public UpgradedJpaRepository<AuthUserResetPassword> getEntityRepository() {
		return authUserResetPasswordRepository;
	}

	@Override
	@ServiceMethod
	public AuthUserResetPassword findByEmail(final String email) {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByEmail(email);
		return authUserResetPassword;
	}

	@Override
	@ServiceMethod
	public AuthUserResetPassword findByEmailAndUuid(final String email, final String uuid) {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByEmailAndUuid(email, uuid);
		return authUserResetPassword;
	}

	@Override
	public AuthUserResetPassword getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final AuthUserResetPassword authUserResetPassword = new AuthUserResetPassword();
		authUserResetPassword.setId(id);
		authUserResetPassword.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authUserResetPassword.setLastModificationAuthUser(lastModificationAuthUser);

		return authUserResetPassword;
	}
}
