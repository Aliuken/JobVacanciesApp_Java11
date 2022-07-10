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
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.util.ApplicationContextUtil;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainApplication.class, ApplicationContextUtil.class})
@Sql("classpath:db-dump-for-tests.sql")
public class AuthUserConfirmationRepositoryTest {
	@Autowired
	private AuthUserConfirmationRepository authUserConfirmationRepository;

	@Test
	public void testFindByEmail_Ok() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmail("antonio@aliuken.com");

		this.commonTestsAuthUserConfirmation1(authUserConfirmation);
	}

	@Test
	public void testFindByEmail_Null() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmail(null);

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByEmail_NotExists() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmail("NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_Ok() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmailAndUuid("antonio@aliuken.com", "cd939918-565d-41f1-a100-992594729dc4");

		this.commonTestsAuthUserConfirmation1(authUserConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NullEmail() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmailAndUuid(null, "cd939918-565d-41f1-a100-992594729dc4");

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NullUuid() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmailAndUuid("antonio@aliuken.com", null);

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NotExistsEmail() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmailAndUuid("NOT_EXISTING_VALUE", "cd939918-565d-41f1-a100-992594729dc4");

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NotExistsUuid() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmailAndUuid("antonio@aliuken.com", "NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testGetEntityClass_Ok() {
		Class<AuthUserConfirmation> authUserConfirmationClass = authUserConfirmationRepository.getEntityClass();

		Assertions.assertNotNull(authUserConfirmationClass);
		Assertions.assertEquals(AuthUserConfirmation.class, authUserConfirmationClass);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdNotOptional(1L);

		this.commonTestsAuthUserConfirmation1(authUserConfirmation);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindAll_Ok() {
		List<AuthUserConfirmation> authUserConfirmations = authUserConfirmationRepository.findAll();

		Assertions.assertNotNull(authUserConfirmations);
		Assertions.assertEquals(2, authUserConfirmations.size());

		for(AuthUserConfirmation authUserConfirmation : authUserConfirmations) {
			Assertions.assertNotNull(authUserConfirmation);

			Long authUserConfirmationId = authUserConfirmation.getId();

			if(Long.valueOf(1L).equals(authUserConfirmationId)) {
				this.commonTestsAuthUserConfirmation1(authUserConfirmation);
			} else {
				Assertions.assertNotNull(authUserConfirmationId);
				Assertions.assertNotNull(authUserConfirmation.getEmail());
				Assertions.assertNotNull(authUserConfirmation.getUuid());
				Assertions.assertNotNull(authUserConfirmation.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = authUserConfirmation.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		AuthUserConfirmation authUserConfirmation = new AuthUserConfirmation();
		authUserConfirmation.setEmail("new.user@aliuken.com");
		authUserConfirmation.setUuid("cd939918-565d-41f1-a100-992594729dc4");

		authUserConfirmation = authUserConfirmationRepository.saveAndFlush(authUserConfirmation);

		Assertions.assertNotNull(authUserConfirmation);
		Assertions.assertNotNull(authUserConfirmation.getId());
		Assertions.assertEquals("new.user@aliuken.com", authUserConfirmation.getEmail());
		Assertions.assertEquals("cd939918-565d-41f1-a100-992594729dc4", authUserConfirmation.getUuid());
		Assertions.assertNotNull(authUserConfirmation.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = authUserConfirmation.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authUserConfirmation.getLastModificationDateTime());
		Assertions.assertNull(authUserConfirmation.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdNotOptional(1L);
		authUserConfirmation.setEmail("pending.confirmation3@aliuken.com");

		authUserConfirmation = authUserConfirmationRepository.saveAndFlush(authUserConfirmation);

		this.commonTestsAuthUserConfirmation1(authUserConfirmation, "pending.confirmation3@aliuken.com");
	}

	@Test
	public void testSave_Null() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserConfirmationRepository.saveAndFlush(null);
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
				AuthUserConfirmation authUserConfirmation = new AuthUserConfirmation();
				authUserConfirmation.setEmail("antonio@aliuken.com");
				authUserConfirmation.setUuid("953d4c72-2759-4576-a50e-78e37c82ee7a");

				authUserConfirmationRepository.saveAndFlush(authUserConfirmation);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'antonio@aliuken.com' for key 'auth_user_confirmation.auth_user_confirmation_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdNotOptional(1L);
				authUserConfirmation.setEmail("pai.mei@aliuken.com");

				authUserConfirmation = authUserConfirmationRepository.saveAndFlush(authUserConfirmation);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'pai.mei@aliuken.com' for key 'auth_user_confirmation.auth_user_confirmation_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		authUserConfirmationRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserConfirmationRepository.deleteByIdAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsAuthUserConfirmation1(AuthUserConfirmation authUserConfirmation){
		this.commonTestsAuthUserConfirmation1(authUserConfirmation, "antonio@aliuken.com");
	}

	private void commonTestsAuthUserConfirmation1(AuthUserConfirmation authUserConfirmation, String email) {
		Assertions.assertNotNull(authUserConfirmation);
		Assertions.assertEquals(1L, authUserConfirmation.getId());
		Assertions.assertEquals(email, authUserConfirmation.getEmail());
		Assertions.assertEquals("cd939918-565d-41f1-a100-992594729dc4", authUserConfirmation.getUuid());
		Assertions.assertNotNull(authUserConfirmation.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = authUserConfirmation.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("antonio@aliuken.com".equals(email)) {
			Assertions.assertNull(authUserConfirmation.getLastModificationDateTime());
			Assertions.assertNull(authUserConfirmation.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(authUserConfirmation.getLastModificationDateTime());

			AuthUser lastModificationAuthUser = authUserConfirmation.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}
	}
}
