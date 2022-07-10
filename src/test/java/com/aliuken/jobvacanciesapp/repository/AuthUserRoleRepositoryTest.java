package com.aliuken.jobvacanciesapp.repository;

import java.util.List;

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
public class AuthUserRoleRepositoryTest {
	@Autowired
	private AuthUserRoleRepository authUserRoleRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private AuthRoleRepository authRoleRepository;

	@Test
	public void testFindByAuthUserAndAuthRole_Ok() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		AuthRole authRole = authRoleRepository.findByIdNotOptional(2L);

		AuthUserRole authUserRole = authUserRoleRepository.findByAuthUserAndAuthRole(authUser, authRole);

		this.commonTestsAuthUserRole1(authUserRole);
	}

	@Test
	public void testFindByAuthUserAndAuthRole_NullAuthUser() {
		AuthRole authRole = authRoleRepository.findByIdNotOptional(2L);

		AuthUserRole authUserRole = authUserRoleRepository.findByAuthUserAndAuthRole(null, authRole);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testFindByAuthUserAndAuthRole_NullAuthRole() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		AuthUserRole authUserRole = authUserRoleRepository.findByAuthUserAndAuthRole(authUser, null);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testFindByAuthUserAndAuthRole_NotExistsAuthUser() {
		AuthUser authUser = new AuthUser();
		authUser.setId(888L);
		authUser.setEmail("new.user@aliuken.com");

		AuthRole authRole = authRoleRepository.findByIdNotOptional(2L);

		AuthUserRole authUserRole = authUserRoleRepository.findByAuthUserAndAuthRole(authUser, authRole);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testFindByAuthUserAndAuthRole_NotExistsAuthRole() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		AuthRole authRole = new AuthRole();
		authRole.setId(888L);
		authRole.setName("newRole");

		AuthUserRole authUserRole = authUserRoleRepository.findByAuthUserAndAuthRole(authUser, authRole);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testGetEntityClass_Ok() {
		Class<AuthUserRole> authUserRoleClass = authUserRoleRepository.getEntityClass();

		Assertions.assertNotNull(authUserRoleClass);
		Assertions.assertEquals(AuthUserRole.class, authUserRoleClass);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		AuthUserRole authUserRole = authUserRoleRepository.findByIdNotOptional(1L);

		this.commonTestsAuthUserRole1(authUserRole);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		AuthUserRole authUserRole = authUserRoleRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		AuthUserRole authUserRole = authUserRoleRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUserRole);
	}

	@Test
	public void testFindAll_Ok() {
		List<AuthUserRole> authUserRoles = authUserRoleRepository.findAll();

		Assertions.assertNotNull(authUserRoles);
		Assertions.assertEquals(8, authUserRoles.size());

		for(AuthUserRole authUserRole : authUserRoles) {
			Assertions.assertNotNull(authUserRole);

			Long authUserRoleId = authUserRole.getId();

			if(Long.valueOf(1L).equals(authUserRoleId)) {
				this.commonTestsAuthUserRole1(authUserRole);
			} else {
				Assertions.assertNotNull(authUserRoleId);

				AuthUser authUser = authUserRole.getAuthUser();
				Assertions.assertNotNull(authUser);
				Assertions.assertNotNull(authUser.getId());
				Assertions.assertNotNull(authUser.getEmail());

				AuthRole authRole = authUserRole.getAuthRole();
				Assertions.assertNotNull(authRole);
				Assertions.assertNotNull(authRole.getId());
				Assertions.assertNotNull(authRole.getName());

				Assertions.assertNotNull(authUserRole.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = authUserRole.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		Assertions.assertNotNull(authUser);

		AuthRole authRole = authRoleRepository.findByIdNotOptional(1L);
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

		AuthUser firstRegistrationAuthUser = authUserRole.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authUserRole.getLastModificationDateTime());
		Assertions.assertNull(authUserRole.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		AuthRole authRole = authRoleRepository.findByIdNotOptional(1L);
		Assertions.assertNotNull(authRole);

		AuthUserRole authUserRole = authUserRoleRepository.findByIdNotOptional(1L);
		authUserRole.setAuthRole(authRole);

		authUserRole = authUserRoleRepository.saveAndFlush(authUserRole);

		this.commonTestsAuthUserRole1(authUserRole, authRole);
	}

	@Test
	public void testSave_Null() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserRoleRepository.saveAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
				AuthRole authRole = authRoleRepository.findByIdNotOptional(2L);

				AuthUserRole authUserRole = new AuthUserRole();
				authUserRole.setAuthUser(authUser);
				authUserRole.setAuthRole(authRole);

				authUserRoleRepository.saveAndFlush(authUserRole);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry '2-2' for key 'auth_user_role.auth_user_role_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthRole authRole = authRoleRepository.findByIdNotOptional(3L);

				AuthUserRole authUserRole = authUserRoleRepository.findByIdNotOptional(1L);
				authUserRole.setAuthRole(authRole);

				authUserRole = authUserRoleRepository.saveAndFlush(authUserRole);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry '2-3' for key 'auth_user_role.auth_user_role_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		authUserRoleRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserRoleRepository.deleteByIdAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsAuthUserRole1(AuthUserRole authUserRole){
		AuthRole authRole = authRoleRepository.findByIdNotOptional(2L);

		this.commonTestsAuthUserRole1(authUserRole, authRole);
	}

	private void commonTestsAuthUserRole1(AuthUserRole authUserRole, AuthRole authRole) {
		Assertions.assertNotNull(authUserRole);
		Assertions.assertNotNull(authRole);

		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		Assertions.assertEquals(1L, authUserRole.getId());
		Assertions.assertEquals(authUser, authUserRole.getAuthUser());
		Assertions.assertEquals(authRole, authUserRole.getAuthRole());
		Assertions.assertNotNull(authUserRole.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = authUserRole.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if(Long.valueOf(2L).equals(authRole.getId())) {
			Assertions.assertNull(authUserRole.getLastModificationDateTime());
			Assertions.assertNull(authUserRole.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(authUserRole.getLastModificationDateTime());

			AuthUser lastModificationAuthUser = authUserRole.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}
	}
}
