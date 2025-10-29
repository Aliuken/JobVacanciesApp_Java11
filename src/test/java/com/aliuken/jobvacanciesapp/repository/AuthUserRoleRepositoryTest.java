package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.MainClass;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.spring.di.BeanFactoryUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainClass.class, BeanFactoryUtils.class, ConfigPropertiesBean.class})
@Sql("classpath:db_dumps/h2-dump.sql")
public class AuthUserRoleRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private AuthUserRoleRepository authUserRoleRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private AuthRoleRepository authRoleRepository;

	@Test
	public void testFindByAuthUserAndAuthRole_Ok() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		final AuthRole authRole = authRoleRepository.findByIdNotOptional(2L);

		final AuthUserRole authUserRole = authUserRoleRepository.findByAuthUserAndAuthRole(authUser, authRole);

		this.commonTestsAuthUserRole1(authUserRole);
	}

	@Test
	public void testFindByAuthUserAndAuthRole_NullAuthUser() {
		final AuthRole authRole = authRoleRepository.findByIdNotOptional(2L);

		final AuthUserRole authUserRole = authUserRoleRepository.findByAuthUserAndAuthRole(null, authRole);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testFindByAuthUserAndAuthRole_NullAuthRole() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		final AuthUserRole authUserRole = authUserRoleRepository.findByAuthUserAndAuthRole(authUser, null);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testFindByAuthUserAndAuthRole_NotExistsAuthUser() {
		final AuthUser authUser = new AuthUser();
		authUser.setId(888L);
		authUser.setEmail("new.user@aliuken.com");

		final AuthRole authRole = authRoleRepository.findByIdNotOptional(2L);

		final AuthUserRole authUserRole = authUserRoleRepository.findByAuthUserAndAuthRole(authUser, authRole);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testFindByAuthUserAndAuthRole_NotExistsAuthRole() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		final AuthRole authRole = new AuthRole();
		authRole.setId(888L);
		authRole.setName("newRole");

		final AuthUserRole authUserRole = authUserRoleRepository.findByAuthUserAndAuthRole(authUser, authRole);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testGetEntityClass_Ok() {
		final Class<AuthUserRole> authUserRoleClass = authUserRoleRepository.getEntityClass();

		Assertions.assertNotNull(authUserRoleClass);
		Assertions.assertEquals(AuthUserRole.class, authUserRoleClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final AuthUserRole authUserRole = authUserRoleRepository.getNewEntityInstance();

		Assertions.assertNotNull(authUserRole);
		Assertions.assertEquals(new AuthUserRole(), authUserRole);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final AuthUserRole authUserRole = authUserRoleRepository.findByIdNotOptional(1L);

		this.commonTestsAuthUserRole1(authUserRole);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final AuthUserRole authUserRole = authUserRoleRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final AuthUserRole authUserRole = authUserRoleRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final AuthUserRole authUserRole = authUserRoleRepository.findByIdOrNewEntity(1L);

		this.commonTestsAuthUserRole1(authUserRole);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final AuthUserRole authUserRole = authUserRoleRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(authUserRole);
		Assertions.assertEquals(new AuthUserRole(), authUserRole);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final AuthUserRole authUserRole = authUserRoleRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testFindAll_Ok() {
		final List<AuthUserRole> authUserRoles = authUserRoleRepository.findAll();

		Assertions.assertNotNull(authUserRoles);
		Assertions.assertEquals(9, authUserRoles.size());

		for(final AuthUserRole authUserRole : authUserRoles) {
			Assertions.assertNotNull(authUserRole);

			final Long authUserRoleId = authUserRole.getId();

			if(Long.valueOf(1L).equals(authUserRoleId)) {
				this.commonTestsAuthUserRole1(authUserRole);
			} else {
				Assertions.assertNotNull(authUserRoleId);

				final AuthUser authUser = authUserRole.getAuthUser();
				Assertions.assertNotNull(authUser);
				Assertions.assertNotNull(authUser.getId());
				Assertions.assertNotNull(authUser.getEmail());

				final AuthRole authRole = authUserRole.getAuthRole();
				Assertions.assertNotNull(authRole);
				Assertions.assertNotNull(authRole.getId());
				Assertions.assertNotNull(authRole.getName());

				Assertions.assertNotNull(authUserRole.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = authUserRole.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		Assertions.assertNotNull(authUser);

		final AuthRole authRole = authRoleRepository.findByIdNotOptional(1L);
		Assertions.assertNotNull(authRole);

		AuthUserRole authUserRole = new AuthUserRole();
		authUserRole.setAuthUser(authUser);
		authUserRole.setAuthRole(authRole);

		authUserRole = authUserRoleRepository.saveAndFlush(authUserRole);

		Assertions.assertNotNull(authUserRole);
		Assertions.assertNotNull(authUserRole.getId());
		Assertions.assertEquals(authUser, authUserRole.getAuthUser());
		Assertions.assertEquals(authRole, authUserRole.getAuthRole());
		Assertions.assertNotNull(authUserRole.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserRole.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authUserRole.getLastModificationDateTime());
		Assertions.assertNull(authUserRole.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		final AuthRole authRole = authRoleRepository.findByIdNotOptional(1L);
		Assertions.assertNotNull(authRole);

		AuthUserRole authUserRole = authUserRoleRepository.findByIdNotOptional(1L);
		authUserRole.setAuthRole(authRole);

		authUserRole = authUserRoleRepository.saveAndFlush(authUserRole);

		this.commonTestsAuthUserRole1(authUserRole, authRole);
	}

	@Test
	public void testSave_Null() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserRoleRepository.saveAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
				final AuthRole authRole = authRoleRepository.findByIdNotOptional(2L);

				final AuthUserRole authUserRole = new AuthUserRole();
				authUserRole.setAuthUser(authUser);
				authUserRole.setAuthRole(authRole);

				authUserRoleRepository.saveAndFlush(authUserRole);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry '2-2' for key 'auth_user_role.auth_user_role_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				final AuthRole authRole = authRoleRepository.findByIdNotOptional(3L);

				AuthUserRole authUserRole = authUserRoleRepository.findByIdNotOptional(1L);
				authUserRole.setAuthRole(authRole);

				authUserRole = authUserRoleRepository.saveAndFlush(authUserRole);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry '2-3' for key 'auth_user_role.auth_user_role_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		authUserRoleRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserRoleRepository.deleteByIdAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsAuthUserRole1(final AuthUserRole authUserRole) {
		final AuthRole authRole = authRoleRepository.findByIdNotOptional(2L);

		this.commonTestsAuthUserRole1(authUserRole, authRole);
	}

	private void commonTestsAuthUserRole1(final AuthUserRole authUserRole, final AuthRole authRole) {
		Assertions.assertNotNull(authUserRole);
		Assertions.assertNotNull(authRole);

		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		Assertions.assertEquals(1L, authUserRole.getId());
		Assertions.assertEquals(authUser, authUserRole.getAuthUser());
		Assertions.assertEquals(authRole, authUserRole.getAuthRole());
		Assertions.assertNotNull(authUserRole.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserRole.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if(Long.valueOf(2L).equals(authRole.getId())) {
			Assertions.assertNull(authUserRole.getLastModificationDateTime());
			Assertions.assertNull(authUserRole.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(authUserRole.getLastModificationDateTime());

			final AuthUser lastModificationAuthUser = authUserRole.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}
	}
}
