package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserEntityQuery;
import com.aliuken.jobvacanciesapp.repository.AuthUserEntityQueryRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authUserEntityQueryService")
@Transactional
public class AuthUserEntityQueryServiceImpl extends AuthUserEntityQueryService {
	@Autowired
	private AuthUserEntityQueryRepository authUserEntityQueryRepository;

	@Override
	public UpgradedJpaRepository<AuthUserEntityQuery> getEntityRepository() {
		return authUserEntityQueryRepository;
	}

	@Override
	public AuthUserEntityQuery getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final AuthUserEntityQuery authUserEntityQuery = new AuthUserEntityQuery();
		authUserEntityQuery.setId(id);
		authUserEntityQuery.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authUserEntityQuery.setLastModificationAuthUser(lastModificationAuthUser);

		return authUserEntityQuery;
	}

	@Override
	public AuthUserEntityQuery getNewEntityWithAuthUserEmail(final String authUserEmail) {
		final AuthUser authUser = new AuthUser();
		authUser.setEmail(authUserEmail);

		final AuthUserEntityQuery authUserEntityQuery = new AuthUserEntityQuery();
		authUserEntityQuery.setAuthUser(authUser);

		return authUserEntityQuery;
	}

	@Override
	public AuthUserEntityQuery getNewEntityWithAuthUserName(final String authUserName) {
		final AuthUser authUser = new AuthUser();
		authUser.setName(authUserName);

		final AuthUserEntityQuery authUserEntityQuery = new AuthUserEntityQuery();
		authUserEntityQuery.setAuthUser(authUser);

		return authUserEntityQuery;
	}

	@Override
	public AuthUserEntityQuery getNewEntityWithAuthUserSurnames(String authUserSurnames) {
		final AuthUser authUser = new AuthUser();
		authUser.setSurnames(authUserSurnames);

		final AuthUserEntityQuery authUserEntityQuery = new AuthUserEntityQuery();
		authUserEntityQuery.setAuthUser(authUser);

		return authUserEntityQuery;
	}
}
