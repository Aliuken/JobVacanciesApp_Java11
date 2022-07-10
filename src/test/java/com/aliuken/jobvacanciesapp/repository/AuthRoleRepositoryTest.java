package com.aliuken.jobvacanciesapp.repository;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

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

import com.aliuken.jobvacanciesapp.MainApplication;
import com.aliuken.jobvacanciesapp.model.AuthRole;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserRole;
import com.aliuken.jobvacanciesapp.util.ApplicationContextUtil;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainApplication.class, ApplicationContextUtil.class})
@Sql("classpath:db-dump-for-tests.sql")
public class AuthRoleRepositoryTest {
	@Autowired
	private AuthRoleRepository authRoleRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private AuthUserRoleRepository authUserRoleRepository;

	@Test
	public void testFindByName_Ok() {
		AuthRole authRole = authRoleRepository.findByName("SUPERVISOR");

		this.commonTestsAuthRole1(authRole);
	}

	@Test
	public void testFindByName_Null() {
		AuthRole authRole = authRoleRepository.findByName(null);

		Assertions.assertNull(authRole);
	}

	@Test
	public void testFindByName_NotExists() {
		AuthRole authRole = authRoleRepository.findByName("NOT_EXISTING_VALUE");

		Assertions.assertNull(authRole);
	}

	@Test
	public void testGetEntityClass_Ok() {
		Class<AuthRole> authRoleClass = authRoleRepository.getEntityClass();

		Assertions.assertNotNull(authRoleClass);
		Assertions.assertEquals(AuthRole.class, authRoleClass);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		AuthRole authRole = authRoleRepository.findByIdNotOptional(1L);

		this.commonTestsAuthRole1(authRole);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		AuthRole authRole = authRoleRepository.findByIdNotOptional(null);

		Assertions.assertNull(authRole);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		AuthRole authRole = authRoleRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authRole);
	}

	@Test
	public void testFindAll_Ok() {
		List<AuthRole> authRoles = authRoleRepository.findAll();

		Assertions.assertNotNull(authRoles);
		Assertions.assertEquals(3, authRoles.size());

		for(AuthRole authRole : authRoles) {
			Assertions.assertNotNull(authRole);

			Long authRoleId = authRole.getId();

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

		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

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

		AuthUser firstRegistrationAuthUser = authRole.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authRole.getLastModificationDateTime());
		Assertions.assertNull(authRole.getLastModificationAuthUser());

		authUserRoles = authRole.getAuthUserRoles();
		Assertions.assertNotNull(authUserRoles);
		Assertions.assertEquals(1, authUserRoles.size());

		for(AuthUserRole authUserRole2 : authUserRoles) {
			Assertions.assertNotNull(authUserRole2);
			Assertions.assertNotNull(authUserRole2.getId());
			Assertions.assertNotNull(authUserRole2.getAuthUser());
			Assertions.assertEquals(authRole, authUserRole2.getAuthRole());
			Assertions.assertNotNull(authUserRole2.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = authUserRole.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<Long> authUserRoleIds = authRole.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		Assertions.assertEquals(1, authUserRoleIds.size());

		for(Long authUserRoleId : authUserRoleIds) {
			Assertions.assertNotNull(authUserRoleId);
		}

		Set<AuthUser> authUsers = authRole.getAuthUsers();
		Assertions.assertNotNull(authUsers);
		Assertions.assertEquals(1, authUsers.size());

		for(AuthUser authUser2 : authUsers) {
			Assertions.assertNotNull(authUser2);
			Assertions.assertNotNull(authUser2.getId());
			Assertions.assertNotNull(authUser2.getEmail());
			Assertions.assertNotNull(authUser2.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = authUser.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<Long> authUserIds = authRole.getAuthUserIds();
		Assertions.assertNotNull(authUserIds);
		Assertions.assertEquals(1, authUserIds.size());

		for(Long authUserId : authUserIds) {
			Assertions.assertNotNull(authUserId);
		}

		Set<String> authUserEmails = authRole.getAuthUserEmails();
		Assertions.assertNotNull(authUserEmails);
		Assertions.assertEquals(1, authUserEmails.size());

		for(String authUserEmail : authUserEmails) {
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
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authRoleRepository.saveAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertNameExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthRole authRole = new AuthRole();
				authRole.setName("SUPERVISOR");
				authRole.setMessageName("newMessageName");
				authRole.setPriority((byte) 30);

				authRoleRepository.saveAndFlush(authRole);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'SUPERVISOR' for key 'auth_role.auth_role_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthRole authRole = authRoleRepository.findByIdNotOptional(1L);
				authRole.setName("ADMINISTRATOR");

				authRole = authRoleRepository.saveAndFlush(authRole);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'ADMINISTRATOR' for key 'auth_role.auth_role_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		AuthRole authRole = authRoleRepository.findByIdNotOptional(1L);
		Assertions.assertNotNull(authRole);

		Set<Long> authUserRoleIds = authRole.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		for(Long authUserRoleId : authUserRoleIds) {
			authUserRoleRepository.deleteByIdAndFlush(authUserRoleId);
		}

		authRoleRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authRoleRepository.deleteByIdAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	@Test
	public void testDeleteById_ForeignKeyViolation() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				authRoleRepository.deleteByIdAndFlush(1L);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Cannot delete or update a parent row: a foreign key constraint fails (`job-vacancies-app-db`.`auth_user_role`, CONSTRAINT `auth_user_role_foreign_key_4` FOREIGN KEY (`auth_role_id`) REFERENCES `auth_role` (`id`))", rootCauseMessage);
	}

	private void commonTestsAuthRole1(AuthRole authRole){
		this.commonTestsAuthRole1(authRole, "SUPERVISOR");
	}

	private void commonTestsAuthRole1(AuthRole authRole, String name) {
		Assertions.assertNotNull(authRole);
		Assertions.assertEquals(1L, authRole.getId());
		Assertions.assertEquals(name, authRole.getName());
		Assertions.assertEquals("authRoleValue.supervisor", authRole.getMessageName());
		Assertions.assertEquals((byte) 50, authRole.getPriority());
		Assertions.assertNotNull(authRole.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = authRole.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("SUPERVISOR".equals(name)) {
			Assertions.assertNull(authRole.getLastModificationDateTime());
			Assertions.assertNull(authRole.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(authRole.getLastModificationDateTime());

			AuthUser lastModificationAuthUser = authRole.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}

		Set<AuthUserRole> authUserRoles = authRole.getAuthUserRoles();
		Assertions.assertNotNull(authUserRoles);
		Assertions.assertEquals(2, authUserRoles.size());

		for(AuthUserRole authUserRole : authUserRoles) {
			Assertions.assertNotNull(authUserRole);
			Assertions.assertNotNull(authUserRole.getId());

			AuthUser authUser = authUserRole.getAuthUser();
			Assertions.assertNotNull(authUser);
			Assertions.assertNotNull(authUser.getId());
			Assertions.assertNotNull(authUser.getEmail());

			Assertions.assertEquals(authRole, authUserRole.getAuthRole());
			Assertions.assertNotNull(authUserRole.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = authUserRole.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<Long> authUserRoleIds = authRole.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		Assertions.assertEquals(2, authUserRoleIds.size());

		for(Long authUserRoleId : authUserRoleIds) {
			Assertions.assertNotNull(authUserRoleId);
		}

		Set<AuthUser> authUsers = authRole.getAuthUsers();
		Assertions.assertNotNull(authUsers);
		Assertions.assertEquals(2, authUsers.size());

		for(AuthUser authUser : authUsers) {
			Assertions.assertNotNull(authUser);
			Assertions.assertNotNull(authUser.getId());
			Assertions.assertNotNull(authUser.getEmail());
			Assertions.assertNotNull(authUser.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = authUser.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<Long> authUserIds = authRole.getAuthUserIds();
		Assertions.assertNotNull(authUserIds);
		Assertions.assertEquals(2, authUserIds.size());

		for(Long authUserId : authUserIds) {
			Assertions.assertNotNull(authUserId);
		}

		Set<String> authUserEmails = authRole.getAuthUserEmails();
		Assertions.assertNotNull(authUserEmails);
		Assertions.assertEquals(2, authUserEmails.size());

		for(String authUserEmail : authUserEmails) {
			Assertions.assertNotNull(authUserEmail);
		}
	}
}
