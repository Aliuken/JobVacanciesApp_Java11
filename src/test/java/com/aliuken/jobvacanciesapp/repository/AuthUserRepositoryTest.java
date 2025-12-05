package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.MainClass;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserForSignupDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.AuthUserConverter;
import com.aliuken.jobvacanciesapp.model.dto.converter.AuthUserForSignupConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserSignUpConfirmation;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Currency;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.security.RandomUtils;
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

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainClass.class, BeanFactoryUtils.class, ConfigPropertiesBean.class})
@Sql("classpath:db_dumps/h2-dump.sql")
public class AuthUserRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private AuthUserCredentialsRepository authUserCredentialsRepository;

	@Autowired
	private AuthUserCurriculumRepository authUserCurriculumRepository;

	@Autowired
	private AuthUserEntityQueryRepository authUserEntityQueryRepository;

	@Autowired
	private AuthUserSignUpConfirmationRepository authUserSignUpConfirmationRepository;

	@Autowired
	private AuthRoleRepository authRoleRepository;

	@Autowired
	private AuthUserRoleRepository authUserRoleRepository;

	@Autowired
	private JobRequestRepository jobRequestRepository;

	@Test
	public void testFindByEmail_Ok() {
		final AuthUser authUser = authUserRepository.findByEmail("aliuken@aliuken.com");

		this.commonTestsAuthUser2(authUser);
	}

	@Test
	public void testFindByEmail_Null() {
		final AuthUser authUser = authUserRepository.findByEmail(null);

		Assertions.assertNull(authUser);
	}

	@Test
	public void testFindByEmail_NotExists() {
		final AuthUser authUser = authUserRepository.findByEmail("NOT_EXISTING_VALUE");

		Assertions.assertNull(authUser);
	}

	@Test
	public void testGetEntityClass_Ok() {
		final Class<AuthUser> authUserClass = authUserRepository.getEntityClass();

		Assertions.assertNotNull(authUserClass);
		Assertions.assertEquals(AuthUser.class, authUserClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final AuthUser authUser = authUserRepository.getNewEntityInstance();

		Assertions.assertNotNull(authUser);
		Assertions.assertEquals(new AuthUser(), authUser);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		this.commonTestsAuthUser2(authUser);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUser);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUser);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final AuthUser authUser = authUserRepository.findByIdOrNewEntity(2L);

		this.commonTestsAuthUser2(authUser);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final AuthUser authUser = authUserRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(authUser);
		Assertions.assertEquals(new AuthUser(), authUser);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final AuthUser authUser = authUserRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(authUser);
	}

	@Test
	public void testFindAll_Ok() {
		final List<AuthUser> authUsers = authUserRepository.findAll();

		Assertions.assertNotNull(authUsers);
		Assertions.assertEquals(9, authUsers.size());

		for(final AuthUser authUser : authUsers) {
			Assertions.assertNotNull(authUser);

			final Long authUserId = authUser.getId();

			if(Long.valueOf(2L).equals(authUserId)) {
				this.commonTestsAuthUser2(authUser);
			} else {
				Assertions.assertNotNull(authUserId);
				Assertions.assertNotNull(authUser.getEmail());
				Assertions.assertNotNull(authUser.getName());
				Assertions.assertNotNull(authUser.getSurnames());
				Assertions.assertNotNull(authUser.getLanguage());
				Assertions.assertNotNull(authUser.getEnabled());
				Assertions.assertNotNull(authUser.getColorMode());
				Assertions.assertNotNull(authUser.getInitialCurrency());
				Assertions.assertNotNull(authUser.getInitialTableSortingDirection());
				Assertions.assertNotNull(authUser.getInitialTablePageSize());
				Assertions.assertNotNull(authUser.getPdfDocumentPageFormat());
				Assertions.assertNotNull(authUser.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = authUser.getFirstRegistrationAuthUser();
				if(Long.valueOf(1L).equals(authUserId)) {
					Assertions.assertNull(firstRegistrationAuthUser);
				} else {
					Assertions.assertNotNull(firstRegistrationAuthUser);
					Assertions.assertNotNull(firstRegistrationAuthUser.getId());
					Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
				}
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		AuthUser authUser = new AuthUser();
		authUser.setEmail("new.user@aliuken.com");
		authUser.setName("New");
		authUser.setSurnames("User");
		authUser.setLanguage(Language.SPANISH);
		authUser.setEnabled(Boolean.FALSE);
		authUser.setColorMode(ColorMode.BY_DEFAULT);
		authUser.setInitialCurrency(Currency.BY_DEFAULT);
		authUser.setInitialTableSortingDirection(TableSortingDirection.BY_DEFAULT);
		authUser.setInitialTablePageSize(TablePageSize.BY_DEFAULT);
		authUser.setPdfDocumentPageFormat(PdfDocumentPageFormat.BY_DEFAULT);

		authUser = authUserRepository.saveAndFlush(authUser);

		final AuthRole authRole = authRoleRepository.findByIdNotOptional(3L);
		Assertions.assertNotNull(authRole);

		AuthUserRole authUserRole = new AuthUserRole();
		authUserRole.setAuthUser(authUser);
		authUserRole.setAuthRole(authRole);

		authUserRole = authUserRoleRepository.saveAndFlush(authUserRole);

		Set<AuthUserRole> authUserRoles = new TreeSet<>();
		authUserRoles.add(authUserRole);

		authUser.setAuthUserRoles(authUserRoles);

		authUser = authUserRepository.saveAndFlush(authUser);

		AuthUserCredentials authUserCredentials = new AuthUserCredentials();
		authUserCredentials.setEmail("new.user@aliuken.com");
		authUserCredentials.setEncryptedPassword("$2a$10$emZdB6.UCgKUSSP5XbtUUu3EJC90ORzvBGgmWLEcHxzd.rNwTTzWq");

		authUserCredentials = authUserCredentialsRepository.saveAndFlush(authUserCredentials);

		final String uuid = RandomUtils.UUID_GENERATOR.get();

		AuthUserSignUpConfirmation authUserSignUpConfirmation = new AuthUserSignUpConfirmation();
		authUserSignUpConfirmation.setEmail("new.user@aliuken.com");
		authUserSignUpConfirmation.setUuid(uuid);

		authUserSignUpConfirmation = authUserSignUpConfirmationRepository.saveAndFlush(authUserSignUpConfirmation);

		Assertions.assertNotNull(authUser);
		Assertions.assertNotNull(authUser.getId());
		Assertions.assertEquals("new.user@aliuken.com", authUser.getEmail());
		Assertions.assertEquals("New", authUser.getName());
		Assertions.assertEquals("User", authUser.getSurnames());
		Assertions.assertEquals(Language.SPANISH, authUser.getLanguage());
		Assertions.assertEquals(Boolean.FALSE, authUser.getEnabled());
		Assertions.assertEquals(ColorMode.BY_DEFAULT, authUser.getColorMode());
		Assertions.assertEquals(Currency.BY_DEFAULT, authUser.getInitialCurrency());
		Assertions.assertEquals(TableSortingDirection.BY_DEFAULT, authUser.getInitialTableSortingDirection());
		Assertions.assertEquals(TablePageSize.BY_DEFAULT, authUser.getInitialTablePageSize());

		Assertions.assertNotNull(authUser.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUser.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNotNull(authUser.getLastModificationDateTime());

		final AuthUser lastModificationAuthUser = authUser.getLastModificationAuthUser();
		Assertions.assertNotNull(lastModificationAuthUser);
		Assertions.assertEquals(1L, lastModificationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		authUserRoles = authUser.getAuthUserRoles();
		Assertions.assertNotNull(authUserRoles);
		Assertions.assertEquals(1, authUserRoles.size());

		for(final AuthUserRole authUserRole2 : authUserRoles) {
			Assertions.assertNotNull(authUserRole2);
			Assertions.assertEquals(authUser, authUserRole2.getAuthUser());
			Assertions.assertEquals(authRole, authUserRole2.getAuthRole());
			Assertions.assertNotNull(authUserRole2.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = authUserRole2.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<Long> authUserRoleIds = authUser.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		Assertions.assertEquals(1, authUserRoleIds.size());

		for(final Long authUserRoleId : authUserRoleIds) {
			Assertions.assertNotNull(authUserRoleId);
		}

		final Set<AuthRole> authRoles = authUser.getAuthRoles();
		Assertions.assertNotNull(authRoles);
		Assertions.assertEquals(1, authRoles.size());

		for(final AuthRole authRole2 : authRoles) {
			Assertions.assertNotNull(authRole2);
			Assertions.assertEquals(authRole, authRole2);
			Assertions.assertNotNull(authRole2.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = authRole2.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<Long> authRoleIds = authUser.getAuthRoleIds();
		Assertions.assertNotNull(authRoleIds);
		Assertions.assertEquals(1, authRoleIds.size());

		for(final Long authRoleId : authRoleIds) {
			Assertions.assertEquals(3L, authRoleId);
		}

		final Set<String> authRoleNames = authUser.getAuthRoleNames();
		Assertions.assertNotNull(authRoleNames);
		Assertions.assertEquals(1, authRoleNames.size());

		for(final String authRoleName : authRoleNames) {
			Assertions.assertEquals("USER", authRoleName);
		}

		Assertions.assertEquals(authRole, authUser.getMaxPriorityAuthRole());
		Assertions.assertEquals(3L, authUser.getMaxPriorityAuthRoleId());
		Assertions.assertEquals("USER", authUser.getMaxPriorityAuthRoleName());

		Assertions.assertNull(authUser.getJobRequests());
		Assertions.assertTrue(authUser.getJobRequestIds().isEmpty());
		Assertions.assertTrue(authUser.getJobVacancies().isEmpty());
		Assertions.assertTrue(authUser.getJobVacancyIds().isEmpty());
		Assertions.assertTrue(authUser.getJobVacancyNames().isEmpty());

		Assertions.assertNull(authUser.getAuthUserCurriculums());
		Assertions.assertTrue(authUser.getAuthUserCurriculumIds().isEmpty());
		Assertions.assertTrue(authUser.getAuthUserCurriculumSelectionNames().isEmpty());
	}

	@Test
	public void testSave_UpdateOk() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		authUser.setName("NEW_NAME");

		authUser = authUserRepository.saveAndFlush(authUser);

		this.commonTestsAuthUser2(authUser, "NEW_NAME");
	}

	@Test
	public void testSave_Null() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserRepository.saveAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertEmailExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				final AuthUser authUser = new AuthUser();
				authUser.setEmail("aliuken@aliuken.com");
				authUser.setName("New");
				authUser.setSurnames("User");
				authUser.setLanguage(Language.SPANISH);
				authUser.setEnabled(Boolean.FALSE);
				authUser.setColorMode(ColorMode.BY_DEFAULT);
				authUser.setInitialCurrency(Currency.BY_DEFAULT);
				authUser.setInitialTableSortingDirection(TableSortingDirection.BY_DEFAULT);
				authUser.setInitialTablePageSize(TablePageSize.BY_DEFAULT);
				authUser.setPdfDocumentPageFormat(PdfDocumentPageFormat.BY_DEFAULT);

				authUserRepository.saveAndFlush(authUser);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'aliuken@aliuken.com' for key 'auth_user.auth_user_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_InsertColorModeNull() {
		final ConstraintViolationException exception = Assertions.assertThrows(
			ConstraintViolationException.class, () -> {
				final AuthUser authUser = new AuthUser();
				authUser.setEmail("aliuken@aliuken.com");
				authUser.setName("New");
				authUser.setSurnames("User");
				authUser.setLanguage(Language.SPANISH);
				authUser.setEnabled(Boolean.FALSE);
				authUser.setInitialCurrency(Currency.BY_DEFAULT);
				authUser.setInitialTableSortingDirection(TableSortingDirection.BY_DEFAULT);
				authUser.setInitialTablePageSize(TablePageSize.BY_DEFAULT);
				authUser.setColorMode(null);

				authUserRepository.saveAndFlush(authUser);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
	}

	@Test
	public void testSave_InsertInitialCurrencyNull() {
		final ConstraintViolationException exception = Assertions.assertThrows(
			ConstraintViolationException.class, () -> {
				final AuthUser authUser = new AuthUser();
				authUser.setEmail("aliuken@aliuken.com");
				authUser.setName("New");
				authUser.setSurnames("User");
				authUser.setLanguage(Language.SPANISH);
				authUser.setEnabled(Boolean.FALSE);
				authUser.setInitialCurrency(null);
				authUser.setInitialTableSortingDirection(TableSortingDirection.BY_DEFAULT);
				authUser.setInitialTablePageSize(TablePageSize.BY_DEFAULT);
				authUser.setColorMode(ColorMode.BY_DEFAULT);

				authUserRepository.saveAndFlush(authUser);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
	}

	@Test
	public void testSave_InsertInitialTableSortingDirectionNull() {
		final ConstraintViolationException exception = Assertions.assertThrows(
				ConstraintViolationException.class, () -> {
					final AuthUser authUser = new AuthUser();
					authUser.setEmail("aliuken@aliuken.com");
					authUser.setName("New");
					authUser.setSurnames("User");
					authUser.setLanguage(Language.SPANISH);
					authUser.setEnabled(Boolean.FALSE);
					authUser.setInitialCurrency(Currency.BY_DEFAULT);
					authUser.setInitialTableSortingDirection(null);
					authUser.setInitialTablePageSize(TablePageSize.BY_DEFAULT);
					authUser.setColorMode(ColorMode.BY_DEFAULT);

					authUserRepository.saveAndFlush(authUser);
				}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
	}

	@Test
	public void testSave_InsertInitialTablePageSizeNull() {
		final ConstraintViolationException exception = Assertions.assertThrows(
			ConstraintViolationException.class, () -> {
				final AuthUser authUser = new AuthUser();
				authUser.setEmail("aliuken@aliuken.com");
				authUser.setName("New");
				authUser.setSurnames("User");
				authUser.setLanguage(Language.SPANISH);
				authUser.setEnabled(Boolean.FALSE);
				authUser.setInitialCurrency(Currency.BY_DEFAULT);
				authUser.setInitialTableSortingDirection(TableSortingDirection.BY_DEFAULT);
				authUser.setInitialTablePageSize(null);
				authUser.setColorMode(ColorMode.BY_DEFAULT);

				authUserRepository.saveAndFlush(authUser);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
	}

	@Test
	public void testSave_UpdateEmailExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUser authUser = authUserRepository.findByIdNotOptional(3L);
				authUser.setEmail("aliuken@aliuken.com");

				authUser = authUserRepository.saveAndFlush(authUser);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'aliuken@aliuken.com' for key 'auth_user.auth_user_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		Assertions.assertNotNull(authUser);
		Assertions.assertNotNull(authUser.getEmail());

		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByEmail(authUser.getEmail());
		if(authUserSignUpConfirmation != null) {
			authUserSignUpConfirmationRepository.deleteByIdAndFlush(authUserSignUpConfirmation.getId());
		}

		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmail(authUser.getEmail());
		if(authUserCredentials != null) {
			authUserCredentialsRepository.deleteByIdAndFlush(authUserCredentials.getId());
		}

		final Set<Long> jobRequestIds = authUser.getJobRequestIds();
		Assertions.assertNotNull(jobRequestIds);
		for(final Long jobRequestId : jobRequestIds) {
			Assertions.assertNotNull(jobRequestId);
			jobRequestRepository.deleteByIdAndFlush(jobRequestId);
		}

		final Set<Long> authUserCurriculumIds = authUser.getAuthUserCurriculumIds();
		Assertions.assertNotNull(authUserCurriculumIds);
		for(final Long authUserCurriculumId : authUserCurriculumIds) {
			authUserCurriculumRepository.deleteByIdAndFlush(authUserCurriculumId);
		}

		final Set<Long> authUserEntityQueryIds = authUser.getAuthUserEntityQueryIds();
		Assertions.assertNotNull(authUserEntityQueryIds);
		for(final Long authUserEntityQueryId : authUserEntityQueryIds) {
			authUserEntityQueryRepository.deleteByIdAndFlush(authUserEntityQueryId);
		}

		final Set<Long> authUserRoleIds = authUser.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		for(final Long authUserRoleId : authUserRoleIds) {
			Assertions.assertNotNull(authUserRoleId);
			authUserRoleRepository.deleteByIdAndFlush(authUserRoleId);
		}

		authUserRepository.deleteByIdAndFlush(2L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserRepository.deleteByIdAndFlush(null);
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
				authUserRepository.deleteByIdAndFlush(2L);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Cannot delete or update a parent row: a foreign key constraint fails (`job-vacancies-app-db`.`auth_user_curriculum`, CONSTRAINT `auth_user_curriculum_foreign_key_3` FOREIGN KEY (`auth_user_id`) REFERENCES `auth_user` (`id`))", rootCauseMessage);
	}

	@Test
	public void testLock_UpdateOk() {
		authUserRepository.lock(1L);
	}

	@Test
	public void testLock_AlreadyLocked() {
		authUserRepository.lock(7L);
	}

	@Test
	public void testUnlock_UpdateOk() {
		authUserRepository.unlock(7L);
	}

	@Test
	public void testUnlock_AlreadyUnlocked() {
		authUserRepository.unlock(1L);
	}

	@Test
	public void testRefreshEntity_Ok() {
		AuthUser authUser = new AuthUser();
		authUser.setId(2L);

		authUser = authUserRepository.refreshEntity(authUser);

		Assertions.assertNotNull(authUser);
		Assertions.assertEquals(2L, authUser.getId());
		Assertions.assertEquals("aliuken@aliuken.com", authUser.getEmail());
		Assertions.assertEquals("Aliuken", authUser.getName());
		Assertions.assertEquals("Master", authUser.getSurnames());
		Assertions.assertEquals(Language.SPANISH, authUser.getLanguage());
		Assertions.assertEquals(Boolean.TRUE, authUser.getEnabled());
		Assertions.assertNotNull(authUser.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUser.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authUser.getLastModificationDateTime());
		Assertions.assertNull(authUser.getLastModificationAuthUser());
	}

	@Test
	public void testRefreshEntity_Null() {
		final AuthUser authUser = authUserRepository.refreshEntity(null);

		Assertions.assertNull(authUser);
	}

	@Test
	public void testRefreshEntity_IdNotExists() {
		AuthUser authUser = new AuthUser();
		authUser.setId(888L);

		authUser = authUserRepository.refreshEntity(authUser);

		Assertions.assertNull(authUser);
	}

	@Test
	public void testRefreshEntity_NoId() {
		AuthUser authUser = new AuthUser();
		authUser.setEmail("aliuken@aliuken.com");

		authUser = authUserRepository.refreshEntity(authUser);

		Assertions.assertNull(authUser);
	}

	private void commonTestsAuthUser2(final AuthUser authUser) {
		this.commonTestsAuthUser2(authUser, "Aliuken");
	}

	private void commonTestsAuthUser2(final AuthUser authUser, final String name) {
		Assertions.assertNotNull(authUser);
		Assertions.assertEquals(2L, authUser.getId());
		Assertions.assertEquals("aliuken@aliuken.com", authUser.getEmail());
		Assertions.assertEquals(name, authUser.getName());
		Assertions.assertEquals("Master", authUser.getSurnames());
		Assertions.assertEquals(Language.SPANISH, authUser.getLanguage());
		Assertions.assertEquals(Boolean.TRUE, authUser.getEnabled());
		Assertions.assertEquals(ColorMode.DARK, authUser.getColorMode());
		Assertions.assertEquals(Currency.US_DOLLAR, authUser.getInitialCurrency());
		Assertions.assertEquals(TableSortingDirection.BY_DEFAULT, authUser.getInitialTableSortingDirection());
		Assertions.assertEquals(TablePageSize.BY_DEFAULT, authUser.getInitialTablePageSize());
		Assertions.assertEquals(PdfDocumentPageFormat.A3_HORIZONTAL, authUser.getPdfDocumentPageFormat());
		Assertions.assertNotNull(authUser.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUser.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("Aliuken".equals(name)) {
			Assertions.assertNull(authUser.getLastModificationDateTime());
			Assertions.assertNull(authUser.getLastModificationAuthUser());

			Assertions.assertEquals("Aliuken Master", authUser.getFullName());
		} else {
			Assertions.assertNotNull(authUser.getLastModificationDateTime());

			final AuthUser lastModificationAuthUser = authUser.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());

			Assertions.assertNotNull(authUser.getFullName());
		}

		final Set<AuthUserRole> authUserRoles = authUser.getAuthUserRoles();
		Assertions.assertNotNull(authUserRoles);
		Assertions.assertEquals(2, authUserRoles.size());

		for(final AuthUserRole authUserRole : authUserRoles) {
			Assertions.assertNotNull(authUserRole);
			Assertions.assertNotNull(authUserRole.getId());
			Assertions.assertEquals(authUser, authUserRole.getAuthUser());

			final AuthRole authRole = authUserRole.getAuthRole();
			Assertions.assertNotNull(authRole);
			Assertions.assertNotNull(authRole.getId());
			Assertions.assertNotNull(authRole.getName());

			Assertions.assertNotNull(authUserRole.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = authUserRole.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<JobRequest> jobRequests = authUser.getJobRequests();
		Assertions.assertNotNull(jobRequests);
		Assertions.assertEquals(6, jobRequests.size());

		for(final JobRequest jobRequest : jobRequests) {
			Assertions.assertNotNull(jobRequest);
			Assertions.assertNotNull(jobRequest.getId());
			Assertions.assertEquals(authUser, jobRequest.getAuthUser());

			final JobVacancy jobVacancy = jobRequest.getJobVacancy();
			Assertions.assertNotNull(jobVacancy);
			Assertions.assertNotNull(jobVacancy.getId());
			Assertions.assertNotNull(jobVacancy.getName());

			Assertions.assertNotNull(jobRequest.getComments());
			Assertions.assertNotNull(jobRequest.getCurriculumFileName());
			Assertions.assertNotNull(jobRequest.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = jobRequest.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<AuthUserCurriculum> authUserCurriculums = authUser.getAuthUserCurriculums();
		Assertions.assertNotNull(authUserCurriculums);
		Assertions.assertEquals(2, authUserCurriculums.size());

		for(final AuthUserCurriculum authUserCurriculum : authUserCurriculums) {
			Assertions.assertNotNull(authUserCurriculum);
			Assertions.assertNotNull(authUserCurriculum.getId());
			Assertions.assertEquals(authUser, authUserCurriculum.getAuthUser());
			Assertions.assertNotNull(authUserCurriculum.getFileName());
			Assertions.assertNotNull(authUserCurriculum.getDescription());
			Assertions.assertNotNull(authUserCurriculum.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = authUserCurriculum.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<Long> authUserRoleIds = authUser.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		Assertions.assertEquals(2, authUserRoleIds.size());

		for(final Long authUserRoleId : authUserRoleIds) {
			Assertions.assertNotNull(authUserRoleId);
		}

		final Set<AuthRole> authRoles = authUser.getAuthRoles();
		Assertions.assertNotNull(authRoles);
		Assertions.assertEquals(2, authRoles.size());

		for(final AuthRole authRole : authRoles) {
			Assertions.assertNotNull(authRole);
			Assertions.assertNotNull(authRole.getId());
			Assertions.assertNotNull(authRole.getName());
			Assertions.assertNotNull(authRole.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = authRole.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<Long> authRoleIds = authUser.getAuthRoleIds();
		Assertions.assertNotNull(authRoleIds);
		Assertions.assertEquals(2, authRoleIds.size());

		for(final Long authRoleId : authRoleIds) {
			Assertions.assertNotNull(authRoleId);
		}

		final Set<String> authRoleNames = authUser.getAuthRoleNames();
		Assertions.assertNotNull(authRoleNames);
		Assertions.assertEquals(2, authRoleNames.size());

		for(final String authRoleName : authRoleNames) {
			Assertions.assertNotNull(authRoleName);
		}

		final Set<Long> jobRequestIds = authUser.getJobRequestIds();
		Assertions.assertNotNull(jobRequestIds);
		Assertions.assertEquals(6, jobRequestIds.size());

		for(final Long jobVacancyId : jobRequestIds) {
			Assertions.assertNotNull(jobVacancyId);
		}

		final Set<JobVacancy> jobVacancies = authUser.getJobVacancies();
		Assertions.assertNotNull(jobVacancies);
		Assertions.assertEquals(6, jobVacancies.size());

		for(final JobVacancy jobVacancy : jobVacancies) {
			Assertions.assertNotNull(jobVacancy);
			Assertions.assertNotNull(jobVacancy.getId());
			Assertions.assertNotNull(jobVacancy.getName());
			Assertions.assertNotNull(jobVacancy.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = jobVacancy.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<Long> jobVacancyIds = authUser.getJobVacancyIds();
		Assertions.assertNotNull(jobVacancyIds);
		Assertions.assertEquals(6, jobVacancyIds.size());

		for(final Long jobVacancyId : jobVacancyIds) {
			Assertions.assertNotNull(jobVacancyId);
		}

		final Set<String> jobVacancyNames = authUser.getJobVacancyNames();
		Assertions.assertNotNull(jobVacancyNames);
		Assertions.assertEquals(6, jobVacancyNames.size());

		for(final String jobVacancyName : jobVacancyNames) {
			Assertions.assertNotNull(jobVacancyName);
		}

		final Set<Long> authUserCurriculumIds = authUser.getAuthUserCurriculumIds();
		Assertions.assertNotNull(authUserCurriculumIds);
		Assertions.assertEquals(2, authUserCurriculumIds.size());

		for(final Long authUserCurriculumId : authUserCurriculumIds) {
			Assertions.assertNotNull(authUserCurriculumId);
		}

		final Set<String> authUserCurriculumSelectionNames = authUser.getAuthUserCurriculumSelectionNames();
		Assertions.assertNotNull(authUserCurriculumSelectionNames);
		Assertions.assertEquals(2, authUserCurriculumSelectionNames.size());

		for(final String authUserCurriculumSelectionName : authUserCurriculumSelectionNames) {
			Assertions.assertNotNull(authUserCurriculumSelectionName);
		}

		final AuthRole maxPriorityAuthRole = authUser.getMaxPriorityAuthRole();
		Assertions.assertNotNull(maxPriorityAuthRole);
		Assertions.assertNotNull(maxPriorityAuthRole.getId());
		Assertions.assertNotNull(maxPriorityAuthRole.getName());
		Assertions.assertNotNull(maxPriorityAuthRole.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser2 = maxPriorityAuthRole.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser2);
		Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
		Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());

		final Long maxPriorityAuthRoleId = authUser.getMaxPriorityAuthRoleId();
		Assertions.assertNotNull(maxPriorityAuthRoleId);

		final String maxPriorityAuthRoleName = authUser.getMaxPriorityAuthRoleName();
		Assertions.assertNotNull(maxPriorityAuthRoleName);

		final AuthUserDTO authUserDTO = AuthUserConverter.getInstance().convertEntityElement(authUser);
		this.commonTestsAuthUserDTO2(authUserDTO, name);

		final AuthUserForSignupDTO authUserForSignupDTO = AuthUserForSignupConverter.getInstance().convertEntityElement(authUser);
		this.commonTestsAuthUserForSignupDTO2(authUserForSignupDTO, name);
	}

	private void commonTestsAuthUserDTO2(final AuthUserDTO authUserDTO, final String name) {
		Assertions.assertNotNull(authUserDTO);
		Assertions.assertEquals(2L, authUserDTO.getId());
		Assertions.assertEquals("aliuken@aliuken.com", authUserDTO.getEmail());
		Assertions.assertEquals(name, authUserDTO.getName());
		Assertions.assertEquals("Master", authUserDTO.getSurnames());
		Assertions.assertEquals(Language.SPANISH.getCode(), authUserDTO.getLanguageCode());
		Assertions.assertEquals(Boolean.TRUE, authUserDTO.getEnabled());
		Assertions.assertEquals(ColorMode.DARK.getCode(), authUserDTO.getColorModeCode());
		Assertions.assertEquals(Currency.US_DOLLAR.getSymbol(), authUserDTO.getInitialCurrencySymbol());
		Assertions.assertEquals(TableSortingDirection.BY_DEFAULT.getCode(), authUserDTO.getInitialTableSortingDirectionCode());
		Assertions.assertEquals(TablePageSize.BY_DEFAULT.getValue(), authUserDTO.getInitialTablePageSizeValue());
		Assertions.assertEquals(PdfDocumentPageFormat.A3_HORIZONTAL.getCode(), authUserDTO.getPdfDocumentPageFormatCode());

		final Set<String> authRoleNames = authUserDTO.getAuthRoleNames();
		Assertions.assertNotNull(authRoleNames);
		Assertions.assertEquals(2, authRoleNames.size());

		for(final String authRoleName : authRoleNames) {
			Assertions.assertNotNull(authRoleName);
		}
	}

	private void commonTestsAuthUserForSignupDTO2(final AuthUserForSignupDTO authUserForSignupDTO, final String name) {
		Assertions.assertNotNull(authUserForSignupDTO);
		Assertions.assertEquals("aliuken@aliuken.com", authUserForSignupDTO.getEmail());
		Assertions.assertNull(authUserForSignupDTO.getPassword1());
		Assertions.assertNull(authUserForSignupDTO.getPassword2());
		Assertions.assertEquals(name, authUserForSignupDTO.getName());
		Assertions.assertEquals("Master", authUserForSignupDTO.getSurnames());
		Assertions.assertEquals(Language.SPANISH.getCode(), authUserForSignupDTO.getLanguageCode());
	}
}