package com.aliuken.jobvacanciesapp.service;

import com.aliuken.jobvacanciesapp.annotation.ServiceMethod;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.repository.AuthUserRepository;
import com.aliuken.jobvacanciesapp.repository.superinterface.UpgradedJpaRepository;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.database.DatabaseUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("authUserService")
@Transactional
public class AuthUserServiceImpl extends AuthUserService {
	private static final @NonNull ExampleMatcher EMAIL_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("email");
	private static final @NonNull ExampleMatcher NAME_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("name");
	private static final @NonNull ExampleMatcher SURNAMES_EXAMPLE_MATCHER = DatabaseUtils.getExampleMatcherWithContainsOneField("surnames");

	@Autowired
	private AuthUserRepository authUserRepository;

	@Override
	public @NonNull UpgradedJpaRepository<AuthUser> getEntityRepository() {
		return authUserRepository;
	}

	@Override
	protected @NonNull Example<AuthUser> getDefaultEntityPageExample(final @NonNull TableField filterTableField, final String filterValue) {
		final Example<AuthUser> example;
		switch(filterTableField) {
			case AUTH_USER_EMAIL: {
				final AuthUser authUserSearch = new AuthUser();
				authUserSearch.setEmail(filterValue);
				example = Example.of(authUserSearch, EMAIL_EXAMPLE_MATCHER);
				break;
			}
			case AUTH_USER_NAME: {
				final AuthUser authUserSearch = new AuthUser();
				authUserSearch.setName(filterValue);
				example = Example.of(authUserSearch, NAME_EXAMPLE_MATCHER);
				break;
			}
			case AUTH_USER_SURNAMES: {
				final AuthUser authUserSearch = new AuthUser();
				authUserSearch.setSurnames(filterValue);
				example = Example.of(authUserSearch, SURNAMES_EXAMPLE_MATCHER);
				break;
			}
			default: {
				throw new IllegalArgumentException(StringUtils.getStringJoined("TableField '", filterTableField.name(), "' not supported"));
			}
		}

		return example;
	}

	@Override
	@ServiceMethod
	public AuthUser findByEmail(final String email) {
		final AuthUser authUser = authUserRepository.findByEmail(email);
		return authUser;
	}

	@Override
	@ServiceMethod
	public void lock(final Long authUserId) {
		authUserRepository.lock(authUserId);
	}

	@Override
	@ServiceMethod
	public void unlock(final Long authUserId) {
		authUserRepository.unlock(authUserId);
	}

	@Override
	public @NonNull AuthUser getNewEntityForSearchByExample(final Long id, final AuthUser firstRegistrationAuthUser, final AuthUser lastModificationAuthUser) {
		final AuthUser authUser = new AuthUser();
		authUser.setId(id);
		authUser.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		authUser.setLastModificationAuthUser(lastModificationAuthUser);

		return authUser;
	}
}
