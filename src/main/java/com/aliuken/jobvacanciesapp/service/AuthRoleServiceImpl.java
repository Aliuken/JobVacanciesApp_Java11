package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.repository.AuthRoleRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthRoleServiceImpl extends AuthRoleService {

	@Autowired
	private AuthRoleRepository authRoleRepository;

	@Override
	public @NonNull UpgradedJpaRepository<AuthRole> getEntityRepository() {
		return authRoleRepository;
	}

	@Override
	@ServiceMethod
	public AuthRole findByName(final String name) {
		final AuthRole authRole = authRoleRepository.findByName(name);
		return authRole;
	}

	@Override
	public @NonNull AuthRole getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final AuthRole authRole = new AuthRole();
		authRole.setId(id);
		authRole.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authRole.setLastModificationAuthUser(lastModificationAuthUser);

		return authRole;
	}
}
