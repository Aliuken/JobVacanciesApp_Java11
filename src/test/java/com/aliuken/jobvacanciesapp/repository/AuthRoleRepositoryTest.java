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
import java.util.Set;
import java.util.TreeSet;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainClass.class, BeanFactoryUtils.class, ConfigPropertiesBean.class})
@Sql("classpath:db_dumps/h2-dump.sql")
public class AuthRoleRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private AuthRoleRepository authRoleRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private AuthUserRoleRepository authUserRoleRepository;

	@Test
	public void testFindByName_Ok() {
		final AuthRole authRole = authRoleRepository.findByName("SUPERVISOR");

		this.commonTestsAuthRole1(authRole);
	}

	@Test
	public void testFindByName_Null() {
		final AuthRole authRole = authRoleRepository.findByName(null);

		Assertions.assertNull(authRole);
	}

	@Test
	public void testFindByName_NotExists() {
		final AuthRole authRole = authRoleRepository.findByName("NOT_EXISTING_VALUE");

		Assertions.assertNull(authRole);
	}

	@Test
	public void testGetEntityClass_Ok() {
		final Class<AuthRole> authRoleClass = authRoleRepository.getEntityClass();

		Assertions.assertNotNull(authRoleClass);
		Assertions.assertEquals(AuthRole.class, authRoleClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final AuthRole authRole = authRoleRepository.getNewEntityInstance();

		Assertions.assertNotNull(authRole);
		Assertions.assertEquals(new AuthRole(), authRole);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final AuthRole authRole = authRoleRepository.findByIdNotOptional(1L);

		this.commonTestsAuthRole1(authRole);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final AuthRole authRole = authRoleRepository.findByIdNotOptional(null);

		Assertions.assertNull(authRole);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final AuthRole authRole = authRoleRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authRole);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final AuthRole authRole = authRoleRepository.findByIdOrNewEntity(1L);

		this.commonTestsAuthRole1(authRole);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final AuthRole authRole = authRoleRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(authRole);
		Assertions.assertEquals(new AuthRole(), authRole);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final AuthRole authRole = authRoleRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(authRole);
	}

	@Test
	public void testFindAll_Ok() {
		final List<AuthRole> authRoles = authRoleRepository.findAll();

		Assertions.assertNotNull(authRoles);
		Assertions.assertEquals(3, authRoles.size());

		for(final AuthRole authRole : authRoles) {
			Assertions.assertNotNull(authRole);

			final Long authRoleId = authRole.getId();

			if(Long.valueOf(1L).equals(authRoleId)) {
				this.commonTestsAuthRole1(authRole);
			} else {
				Assertions.assertNotNull(authRoleId);
				Assertions.assertNotNull(authRole.getName());
				Assertions.assertNotNull(authRole.getMessageName());
				Assertions.assertNotNull(authRole.getPriority());
				Assertions.assertNotNull(authRole.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = authRole.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		AuthRole authRole = new AuthRole();
		authRole.setName("NEW_NAME");
		authRole.setMessageName("authRoleValue.newName");
		authRole.setPriority((byte) 30);

		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		AuthUserRole authUserRole = new AuthUserRole();
		authUserRole.setAuthUser(authUser);
		authUserRole.setAuthRole(authRole);

		Set<AuthUserRole> authUserRoles = new TreeSet<>();
		authUserRoles.add(authUserRole);

		authRole.setAuthUserRoles(authUserRoles);

		authRole = authRoleRepository.saveAndFlush(authRole);
		authUserRole = authUserRoleRepository.saveAndFlush(authUserRole);

		Assertions.assertNotNull(authRole);
		Assertions.assertNotNull(authRole.getId());
		Assertions.assertEquals("NEW_NAME", authRole.getName());
		Assertions.assertEquals("authRoleValue.newName", authRole.getMessageName());
		Assertions.assertEquals((byte) 30, authRole.getPriority());
		Assertions.assertNotNull(authRole.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authRole.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authRole.getLastModificationDateTime());
		Assertions.assertNull(authRole.getLastModificationAuthUser());

		authUserRoles = authRole.getAuthUserRoles();
		Assertions.assertNotNull(authUserRoles);
		Assertions.assertEquals(1, authUserRoles.size());

		for(final AuthUserRole authUserRole2 : authUserRoles) {
			Assertions.assertNotNull(authUserRole2);
			Assertions.assertNotNull(authUserRole2.getId());
			Assertions.assertNotNull(authUserRole2.getAuthUser());
			Assertions.assertEquals(authRole, authUserRole2.getAuthRole());
			Assertions.assertNotNull(authUserRole2.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = authUserRole.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<Long> authUserRoleIds = authRole.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		Assertions.assertEquals(1, authUserRoleIds.size());

		for(final Long authUserRoleId : authUserRoleIds) {
			Assertions.assertNotNull(authUserRoleId);
		}

		final Set<AuthUser> authUsers = authRole.getAuthUsers();
		Assertions.assertNotNull(authUsers);
		Assertions.assertEquals(1, authUsers.size());

		for(final AuthUser authUser2 : authUsers) {
			Assertions.assertNotNull(authUser2);
			Assertions.assertNotNull(authUser2.getId());
			Assertions.assertNotNull(authUser2.getEmail());
			Assertions.assertNotNull(authUser2.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = authUser.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<Long> authUserIds = authRole.getAuthUserIds();
		Assertions.assertNotNull(authUserIds);
		Assertions.assertEquals(1, authUserIds.size());

		for(final Long authUserId : authUserIds) {
			Assertions.assertNotNull(authUserId);
		}

		final Set<String> authUserEmails = authRole.getAuthUserEmails();
		Assertions.assertNotNull(authUserEmails);
		Assertions.assertEquals(1, authUserEmails.size());

		for(final String authUserEmail : authUserEmails) {
			Assertions.assertNotNull(authUserEmail);
		}
	}

	@Test
	public void testSave_UpdateOk() {
		AuthRole authRole = authRoleRepository.findByIdNotOptional(1L);
		authRole.setName("NEW_NAME");

		authRole = authRoleRepository.saveAndFlush(authRole);

		this.commonTestsAuthRole1(authRole, "NEW_NAME");
	}

	@Test
	public void testSave_Null() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authRoleRepository.saveAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertNameExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				final AuthRole authRole = new AuthRole();
				authRole.setName("SUPERVISOR");
				authRole.setMessageName("newMessageName");
				authRole.setPriority((byte) 30);

				authRoleRepository.saveAndFlush(authRole);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'SUPERVISOR' for key 'auth_role.auth_role_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthRole authRole = authRoleRepository.findByIdNotOptional(1L);
				authRole.setName("ADMINISTRATOR");

				authRole = authRoleRepository.saveAndFlush(authRole);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'ADMINISTRATOR' for key 'auth_role.auth_role_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		final AuthRole authRole = authRoleRepository.findByIdNotOptional(1L);
		Assertions.assertNotNull(authRole);

		final Set<Long> authUserRoleIds = authRole.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		for(final Long authUserRoleId : authUserRoleIds) {
			authUserRoleRepository.deleteByIdAndFlush(authUserRoleId);
		}

		authRoleRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authRoleRepository.deleteByIdAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	@Test
	public void testDeleteById_ForeignKeyViolation() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				authRoleRepository.deleteByIdAndFlush(1L);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Cannot delete or update a parent row: a foreign key constraint fails (`job-vacancies-app-db`.`auth_user_role`, CONSTRAINT `auth_user_role_foreign_key_4` FOREIGN KEY (`auth_role_id`) REFERENCES `auth_role` (`id`))", rootCauseMessage);
	}

	private void commonTestsAuthRole1(final AuthRole authRole) {
		this.commonTestsAuthRole1(authRole, "SUPERVISOR");
	}

	private void commonTestsAuthRole1(final AuthRole authRole, final String name) {
		Assertions.assertNotNull(authRole);
		Assertions.assertEquals(1L, authRole.getId());
		Assertions.assertEquals(name, authRole.getName());
		Assertions.assertEquals("authRoleValue.supervisor", authRole.getMessageName());
		Assertions.assertEquals((byte) 50, authRole.getPriority());
		Assertions.assertNotNull(authRole.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authRole.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("SUPERVISOR".equals(name)) {
			Assertions.assertNull(authRole.getLastModificationDateTime());
			Assertions.assertNull(authRole.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(authRole.getLastModificationDateTime());

			final AuthUser lastModificationAuthUser = authRole.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}

		final Set<AuthUserRole> authUserRoles = authRole.getAuthUserRoles();
		Assertions.assertNotNull(authUserRoles);
		Assertions.assertEquals(2, authUserRoles.size());

		for(final AuthUserRole authUserRole : authUserRoles) {
			Assertions.assertNotNull(authUserRole);
			Assertions.assertNotNull(authUserRole.getId());

			final AuthUser authUser = authUserRole.getAuthUser();
			Assertions.assertNotNull(authUser);
			Assertions.assertNotNull(authUser.getId());
			Assertions.assertNotNull(authUser.getEmail());

			Assertions.assertEquals(authRole, authUserRole.getAuthRole());
			Assertions.assertNotNull(authUserRole.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = authUserRole.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<Long> authUserRoleIds = authRole.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		Assertions.assertEquals(2, authUserRoleIds.size());

		for(final Long authUserRoleId : authUserRoleIds) {
			Assertions.assertNotNull(authUserRoleId);
		}

		final Set<AuthUser> authUsers = authRole.getAuthUsers();
		Assertions.assertNotNull(authUsers);
		Assertions.assertEquals(2, authUsers.size());

		for(final AuthUser authUser : authUsers) {
			Assertions.assertNotNull(authUser);
			Assertions.assertNotNull(authUser.getId());
			Assertions.assertNotNull(authUser.getEmail());
			Assertions.assertNotNull(authUser.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = authUser.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<Long> authUserIds = authRole.getAuthUserIds();
		Assertions.assertNotNull(authUserIds);
		Assertions.assertEquals(2, authUserIds.size());

		for(final Long authUserId : authUserIds) {
			Assertions.assertNotNull(authUserId);
		}

		final Set<String> authUserEmails = authRole.getAuthUserEmails();
		Assertions.assertNotNull(authUserEmails);
		Assertions.assertEquals(2, authUserEmails.size());

		for(final String authUserEmail : authUserEmails) {
			Assertions.assertNotNull(authUserEmail);
		}
	}
}
