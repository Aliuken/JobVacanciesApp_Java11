package com.aliuken.jobvacanciesapp.repository;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

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
import com.aliuken.jobvacanciesapp.model.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.model.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.AuthUserLanguage;
import com.aliuken.jobvacanciesapp.model.AuthUserRole;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.util.ApplicationContextUtil;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainApplication.class, ApplicationContextUtil.class})
@Sql("classpath:db-dump-for-tests.sql")
public class AuthUserRepositoryTest {
	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private AuthUserCredentialsRepository authUserCredentialsRepository;

	@Autowired
	private AuthUserCurriculumRepository authUserCurriculumRepository;

	@Autowired
	private AuthUserConfirmationRepository authUserConfirmationRepository;

	@Autowired
	private AuthRoleRepository authRoleRepository;

	@Autowired
	private AuthUserRoleRepository authUserRoleRepository;

	@Autowired
	private JobRequestRepository jobRequestRepository;

	@Test
	public void testFindByEmail_Ok() {
		AuthUser authUser = authUserRepository.findByEmail("aliuken@aliuken.com");

		this.commonTestsAuthUser2(authUser);
	}

	@Test
	public void testFindByEmail_Null() {
		AuthUser authUser = authUserRepository.findByEmail(null);

		Assertions.assertNull(authUser);
	}

	@Test
	public void testFindByEmail_NotExists() {
		AuthUser authUser = authUserRepository.findByEmail("NOT_EXISTING_VALUE");

		Assertions.assertNull(authUser);
	}

	@Test
	public void testGetEntityClass_Ok() {
		Class<AuthUser> authUserClass = authUserRepository.getEntityClass();

		Assertions.assertNotNull(authUserClass);
		Assertions.assertEquals(AuthUser.class, authUserClass);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		this.commonTestsAuthUser2(authUser);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUser);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUser);
	}

	@Test
	public void testFindAll_Ok() {
		List<AuthUser> authUsers = authUserRepository.findAll();

		Assertions.assertNotNull(authUsers);
		Assertions.assertEquals(8, authUsers.size());

		for(AuthUser authUser : authUsers) {
			Assertions.assertNotNull(authUser);

			Long authUserId = authUser.getId();

			if(Long.valueOf(2L).equals(authUserId)) {
				this.commonTestsAuthUser2(authUser);
			} else {
				Assertions.assertNotNull(authUserId);
				Assertions.assertNotNull(authUser.getEmail());
				Assertions.assertNotNull(authUser.getName());
				Assertions.assertNotNull(authUser.getSurnames());
				Assertions.assertNotNull(authUser.getLanguage());
				Assertions.assertNotNull(authUser.getEnabled());
				Assertions.assertNotNull(authUser.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = authUser.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		AuthUser authUser = new AuthUser();
		authUser.setEmail("new.user@aliuken.com");
		authUser.setName("New");
		authUser.setSurnames("User");
		authUser.setLanguage(AuthUserLanguage.SPANISH);
		authUser.setEnabled(Boolean.FALSE);

		authUser = authUserRepository.saveAndFlush(authUser);

		AuthRole authRole = authRoleRepository.findByIdNotOptional(3L);
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

		final String uuid = UUID.randomUUID().toString();

		AuthUserConfirmation authUserConfirmation = new AuthUserConfirmation();
	    authUserConfirmation.setEmail("new.user@aliuken.com");
        authUserConfirmation.setUuid(uuid);

        authUserConfirmation = authUserConfirmationRepository.saveAndFlush(authUserConfirmation);

		Assertions.assertNotNull(authUser);
		Assertions.assertNotNull(authUser.getId());
		Assertions.assertEquals("new.user@aliuken.com", authUser.getEmail());
		Assertions.assertEquals("New", authUser.getName());
		Assertions.assertEquals("User", authUser.getSurnames());
		Assertions.assertEquals(AuthUserLanguage.SPANISH, authUser.getLanguage());
		Assertions.assertEquals(Boolean.FALSE, authUser.getEnabled());
		Assertions.assertNotNull(authUser.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = authUser.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNotNull(authUser.getLastModificationDateTime());

		AuthUser lastModificationAuthUser = authUser.getLastModificationAuthUser();
		Assertions.assertNotNull(lastModificationAuthUser);
		Assertions.assertEquals(1L, lastModificationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		authUserRoles = authUser.getAuthUserRoles();
		Assertions.assertNotNull(authUserRoles);
		Assertions.assertEquals(1, authUserRoles.size());

		for(AuthUserRole authUserRole2 : authUserRoles) {
			Assertions.assertNotNull(authUserRole2);
			Assertions.assertEquals(authUser, authUserRole2.getAuthUser());
			Assertions.assertEquals(authRole, authUserRole2.getAuthRole());
			Assertions.assertNotNull(authUserRole2.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = authUserRole2.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<Long> authUserRoleIds = authUser.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		Assertions.assertEquals(1, authUserRoleIds.size());

		for(Long authUserRoleId : authUserRoleIds) {
			Assertions.assertNotNull(authUserRoleId);
		}

		Set<AuthRole> authRoles = authUser.getAuthRoles();
		Assertions.assertNotNull(authRoles);
		Assertions.assertEquals(1, authRoles.size());

		for(AuthRole authRole2 : authRoles) {
			Assertions.assertNotNull(authRole2);
			Assertions.assertEquals(authRole, authRole2);
			Assertions.assertNotNull(authRole2.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = authRole2.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<Long> authRoleIds = authUser.getAuthRoleIds();
		Assertions.assertNotNull(authRoleIds);
		Assertions.assertEquals(1, authRoleIds.size());

		for(Long authRoleId : authRoleIds) {
			Assertions.assertEquals(3L, authRoleId);
		}

		Set<String> authRoleNames = authUser.getAuthRoleNames();
		Assertions.assertNotNull(authRoleNames);
		Assertions.assertEquals(1, authRoleNames.size());

		for(String authRoleName : authRoleNames) {
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
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserRepository.saveAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertEmailExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUser authUser = new AuthUser();
				authUser.setEmail("aliuken@aliuken.com");
				authUser.setName("New");
				authUser.setSurnames("User");
				authUser.setLanguage(AuthUserLanguage.SPANISH);
				authUser.setEnabled(Boolean.FALSE);

				authUserRepository.saveAndFlush(authUser);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'aliuken@aliuken.com' for key 'auth_user.auth_user_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateEmailExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUser authUser = authUserRepository.findByIdNotOptional(3L);
				authUser.setEmail("aliuken@aliuken.com");

				authUser = authUserRepository.saveAndFlush(authUser);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'aliuken@aliuken.com' for key 'auth_user.auth_user_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		Assertions.assertNotNull(authUser);
		Assertions.assertNotNull(authUser.getEmail());

		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmail(authUser.getEmail());
		if(authUserConfirmation != null) {
			authUserConfirmationRepository.deleteByIdAndFlush(authUserConfirmation.getId());
		}

		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmail(authUser.getEmail());
		if(authUserCredentials != null) {
			authUserCredentialsRepository.deleteByIdAndFlush(authUserCredentials.getId());
		}

		Set<Long> jobRequestIds = authUser.getJobRequestIds();
		Assertions.assertNotNull(jobRequestIds);
		for(Long jobRequestId : jobRequestIds) {
			Assertions.assertNotNull(jobRequestId);
			jobRequestRepository.deleteByIdAndFlush(jobRequestId);
		}

		Set<Long> authUserCurriculumIds = authUser.getAuthUserCurriculumIds();
		Assertions.assertNotNull(authUserCurriculumIds);
		for(Long authUserCurriculumId : authUserCurriculumIds) {
			authUserCurriculumRepository.deleteByIdAndFlush(authUserCurriculumId);
		}

		Set<Long> authUserRoleIds = authUser.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		for(Long authUserRoleId : authUserRoleIds) {
			Assertions.assertNotNull(authUserRoleId);
			authUserRoleRepository.deleteByIdAndFlush(authUserRoleId);
		}

		authUserRepository.deleteByIdAndFlush(2L);
	}

	@Test
	public void testDeleteById_NullId() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserRepository.deleteByIdAndFlush(null);
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
				authUserRepository.deleteByIdAndFlush(2L);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Cannot delete or update a parent row: a foreign key constraint fails (`job-vacancies-app-db`.`auth_user_curriculum`, CONSTRAINT `auth_user_curriculum_foreign_key_3` FOREIGN KEY (`auth_user_id`) REFERENCES `auth_user` (`id`))", rootCauseMessage);
	}
	
	@Test
	public void testLock_UpdateOk() {
		int rows = authUserRepository.lock(1L);

		Assertions.assertEquals(1, rows);
	}
	
	@Test
	public void testLock_AlreadyLocked() {
		int rows = authUserRepository.lock(7L);

		Assertions.assertEquals(1, rows);
	}
	
	@Test
	public void testUnlock_UpdateOk() {
		int rows = authUserRepository.unlock(7L);

		Assertions.assertEquals(1, rows);
	}
	
	@Test
	public void testUnlock_AlreadyUnlocked() {
		int rows = authUserRepository.unlock(1L);

		Assertions.assertEquals(1, rows);
	}

	private void commonTestsAuthUser2(AuthUser authUser){
		this.commonTestsAuthUser2(authUser, "Aliuken");
	}

	private void commonTestsAuthUser2(AuthUser authUser, String name) {
		Assertions.assertNotNull(authUser);
		Assertions.assertEquals(2L, authUser.getId());
		Assertions.assertEquals("aliuken@aliuken.com", authUser.getEmail());
		Assertions.assertEquals(name, authUser.getName());
		Assertions.assertEquals("Master", authUser.getSurnames());
		Assertions.assertEquals(AuthUserLanguage.ENGLISH, authUser.getLanguage());
		Assertions.assertEquals(Boolean.TRUE, authUser.getEnabled());
		Assertions.assertNotNull(authUser.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = authUser.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("Aliuken".equals(name)) {
			Assertions.assertNull(authUser.getLastModificationDateTime());
			Assertions.assertNull(authUser.getLastModificationAuthUser());

			Assertions.assertEquals("Aliuken Master", authUser.getFullName());
		} else {
			Assertions.assertNotNull(authUser.getLastModificationDateTime());

			AuthUser lastModificationAuthUser = authUser.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());

			Assertions.assertNotNull(authUser.getFullName());
		}

		Set<AuthUserRole> authUserRoles = authUser.getAuthUserRoles();
		Assertions.assertNotNull(authUserRoles);
		Assertions.assertEquals(2, authUserRoles.size());

		for(AuthUserRole authUserRole : authUserRoles) {
			Assertions.assertNotNull(authUserRole);
			Assertions.assertNotNull(authUserRole.getId());
			Assertions.assertEquals(authUser, authUserRole.getAuthUser());

			AuthRole authRole = authUserRole.getAuthRole();
			Assertions.assertNotNull(authRole);
			Assertions.assertNotNull(authRole.getId());
			Assertions.assertNotNull(authRole.getName());

			Assertions.assertNotNull(authUserRole.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = authUserRole.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<JobRequest> jobRequests = authUser.getJobRequests();
		Assertions.assertNotNull(jobRequests);
		Assertions.assertEquals(6, jobRequests.size());

		for(JobRequest jobRequest : jobRequests) {
			Assertions.assertNotNull(jobRequest);
			Assertions.assertNotNull(jobRequest.getId());
			Assertions.assertEquals(authUser, jobRequest.getAuthUser());

			JobVacancy jobVacancy = jobRequest.getJobVacancy();
			Assertions.assertNotNull(jobVacancy);
			Assertions.assertNotNull(jobVacancy.getId());
			Assertions.assertNotNull(jobVacancy.getName());

			Assertions.assertNotNull(jobRequest.getComments());
			Assertions.assertNotNull(jobRequest.getCurriculumFileName());
			Assertions.assertNotNull(jobRequest.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = jobRequest.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<AuthUserCurriculum> authUserCurriculums = authUser.getAuthUserCurriculums();
		Assertions.assertNotNull(authUserCurriculums);
		Assertions.assertEquals(2, authUserCurriculums.size());

		for(AuthUserCurriculum authUserCurriculum : authUserCurriculums) {
			Assertions.assertNotNull(authUserCurriculum);
			Assertions.assertNotNull(authUserCurriculum.getId());
			Assertions.assertEquals(authUser, authUserCurriculum.getAuthUser());
			Assertions.assertNotNull(authUserCurriculum.getFileName());
			Assertions.assertNotNull(authUserCurriculum.getDescription());
			Assertions.assertNotNull(authUserCurriculum.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = authUserCurriculum.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<Long> authUserRoleIds = authUser.getAuthUserRoleIds();
		Assertions.assertNotNull(authUserRoleIds);
		Assertions.assertEquals(2, authUserRoleIds.size());

		for(Long authUserRoleId : authUserRoleIds) {
			Assertions.assertNotNull(authUserRoleId);
		}

		Set<AuthRole> authRoles = authUser.getAuthRoles();
		Assertions.assertNotNull(authRoles);
		Assertions.assertEquals(2, authRoles.size());

		for(AuthRole authRole : authRoles) {
			Assertions.assertNotNull(authRole);
			Assertions.assertNotNull(authRole.getId());
			Assertions.assertNotNull(authRole.getName());
			Assertions.assertNotNull(authRole.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = authRole.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<Long> authRoleIds = authUser.getAuthRoleIds();
		Assertions.assertNotNull(authRoleIds);
		Assertions.assertEquals(2, authRoleIds.size());

		for(Long authRoleId : authRoleIds) {
			Assertions.assertNotNull(authRoleId);
		}

		Set<String> authRoleNames = authUser.getAuthRoleNames();
		Assertions.assertNotNull(authRoleNames);
		Assertions.assertEquals(2, authRoleNames.size());

		for(String authRoleName : authRoleNames) {
			Assertions.assertNotNull(authRoleName);
		}

		Set<Long> jobRequestIds = authUser.getJobRequestIds();
		Assertions.assertNotNull(jobRequestIds);
		Assertions.assertEquals(6, jobRequestIds.size());

		for(Long jobVacancyId : jobRequestIds) {
			Assertions.assertNotNull(jobVacancyId);
		}

		Set<JobVacancy> jobVacancies = authUser.getJobVacancies();
		Assertions.assertNotNull(jobVacancies);
		Assertions.assertEquals(6, jobVacancies.size());

		for(JobVacancy jobVacancy : jobVacancies) {
			Assertions.assertNotNull(jobVacancy);
			Assertions.assertNotNull(jobVacancy.getId());
			Assertions.assertNotNull(jobVacancy.getName());
			Assertions.assertNotNull(jobVacancy.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = jobVacancy.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<Long> jobVacancyIds = authUser.getJobVacancyIds();
		Assertions.assertNotNull(jobVacancyIds);
		Assertions.assertEquals(6, jobVacancyIds.size());

		for(Long jobVacancyId : jobVacancyIds) {
			Assertions.assertNotNull(jobVacancyId);
		}

		Set<String> jobVacancyNames = authUser.getJobVacancyNames();
		Assertions.assertNotNull(jobVacancyNames);
		Assertions.assertEquals(6, jobVacancyNames.size());

		for(String jobVacancyName : jobVacancyNames) {
			Assertions.assertNotNull(jobVacancyName);
		}

		Set<Long> authUserCurriculumIds = authUser.getAuthUserCurriculumIds();
		Assertions.assertNotNull(authUserCurriculumIds);
		Assertions.assertEquals(2, authUserCurriculumIds.size());

		for(Long authUserCurriculumId : authUserCurriculumIds) {
			Assertions.assertNotNull(authUserCurriculumId);
		}

		Set<String> authUserCurriculumSelectionNames = authUser.getAuthUserCurriculumSelectionNames();
		Assertions.assertNotNull(authUserCurriculumSelectionNames);
		Assertions.assertEquals(2, authUserCurriculumSelectionNames.size());

		for(String authUserCurriculumSelectionName : authUserCurriculumSelectionNames) {
			Assertions.assertNotNull(authUserCurriculumSelectionName);
		}

		AuthRole maxPriorityAuthRole = authUser.getMaxPriorityAuthRole();
		Assertions.assertNotNull(maxPriorityAuthRole);
		Assertions.assertNotNull(maxPriorityAuthRole.getId());
		Assertions.assertNotNull(maxPriorityAuthRole.getName());
		Assertions.assertNotNull(maxPriorityAuthRole.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser2 = maxPriorityAuthRole.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser2);
		Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
		Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());

		Long maxPriorityAuthRoleId = authUser.getMaxPriorityAuthRoleId();
		Assertions.assertNotNull(maxPriorityAuthRoleId);

		String maxPriorityAuthRoleName = authUser.getMaxPriorityAuthRoleName();
		Assertions.assertNotNull(maxPriorityAuthRoleName);
	}
}