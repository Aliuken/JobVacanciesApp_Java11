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
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.aliuken.jobvacanciesapp.MainApp;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.springcore.di.BeanFactoryUtils;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainApp.class, BeanFactoryUtils.class, ConfigPropertiesBean.class})
@Sql("classpath:db_dumps/h2-dump.sql")
public class AuthUserConfirmationRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private AuthUserConfirmationRepository authUserConfirmationRepository;

	@Test
	public void testFindByEmail_Ok() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmail("antonio@aliuken.com");

		this.commonTestsAuthUserConfirmation1(authUserConfirmation);
	}

	@Test
	public void testFindByEmail_Null() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmail(null);

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByEmail_NotExists() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmail("NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_Ok() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmailAndUuid("antonio@aliuken.com", "cd939918-565d-41f1-a100-992594729dc4");

		this.commonTestsAuthUserConfirmation1(authUserConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NullEmail() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmailAndUuid(null, "cd939918-565d-41f1-a100-992594729dc4");

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NullUuid() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmailAndUuid("antonio@aliuken.com", null);

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NotExistsEmail() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmailAndUuid("NOT_EXISTING_VALUE", "cd939918-565d-41f1-a100-992594729dc4");

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NotExistsUuid() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByEmailAndUuid("antonio@aliuken.com", "NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testGetEntityClass_Ok() {
		final Class<AuthUserConfirmation> authUserConfirmationClass = authUserConfirmationRepository.getEntityClass();

		Assertions.assertNotNull(authUserConfirmationClass);
		Assertions.assertEquals(AuthUserConfirmation.class, authUserConfirmationClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.getNewEntityInstance();

		Assertions.assertNotNull(authUserConfirmation);
		Assertions.assertEquals(new AuthUserConfirmation(), authUserConfirmation);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdNotOptional(1L);

		this.commonTestsAuthUserConfirmation1(authUserConfirmation);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdOrNewEntity(1L);

		this.commonTestsAuthUserConfirmation1(authUserConfirmation);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(authUserConfirmation);
		Assertions.assertEquals(new AuthUserConfirmation(), authUserConfirmation);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(authUserConfirmation);
	}

	@Test
	public void testFindAll_Ok() {
		final List<AuthUserConfirmation> authUserConfirmations = authUserConfirmationRepository.findAll();

		Assertions.assertNotNull(authUserConfirmations);
		Assertions.assertEquals(2, authUserConfirmations.size());

		for(final AuthUserConfirmation authUserConfirmation : authUserConfirmations) {
			Assertions.assertNotNull(authUserConfirmation);

			final Long authUserConfirmationId = authUserConfirmation.getId();

			if(Long.valueOf(1L).equals(authUserConfirmationId)) {
				this.commonTestsAuthUserConfirmation1(authUserConfirmation);
			} else {
				Assertions.assertNotNull(authUserConfirmationId);
				Assertions.assertNotNull(authUserConfirmation.getEmail());
				Assertions.assertNotNull(authUserConfirmation.getUuid());
				Assertions.assertNotNull(authUserConfirmation.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = authUserConfirmation.getFirstRegistrationAuthUser();
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

		final AuthUser firstRegistrationAuthUser = authUserConfirmation.getFirstRegistrationAuthUser();
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
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserConfirmationRepository.saveAndFlush(null);
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
				final AuthUserConfirmation authUserConfirmation = new AuthUserConfirmation();
				authUserConfirmation.setEmail("antonio@aliuken.com");
				authUserConfirmation.setUuid("953d4c72-2759-4576-a50e-78e37c82ee7a");

				authUserConfirmationRepository.saveAndFlush(authUserConfirmation);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'antonio@aliuken.com' for key 'auth_user_confirmation.auth_user_confirmation_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUserConfirmation authUserConfirmation = authUserConfirmationRepository.findByIdNotOptional(1L);
				authUserConfirmation.setEmail("pai.mei@aliuken.com");

				authUserConfirmation = authUserConfirmationRepository.saveAndFlush(authUserConfirmation);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'pai.mei@aliuken.com' for key 'auth_user_confirmation.auth_user_confirmation_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		authUserConfirmationRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserConfirmationRepository.deleteByIdAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsAuthUserConfirmation1(final AuthUserConfirmation authUserConfirmation){
		this.commonTestsAuthUserConfirmation1(authUserConfirmation, "antonio@aliuken.com");
	}

	private void commonTestsAuthUserConfirmation1(final AuthUserConfirmation authUserConfirmation, final String email) {
		Assertions.assertNotNull(authUserConfirmation);
		Assertions.assertEquals(1L, authUserConfirmation.getId());
		Assertions.assertEquals(email, authUserConfirmation.getEmail());
		Assertions.assertEquals("cd939918-565d-41f1-a100-992594729dc4", authUserConfirmation.getUuid());
		Assertions.assertNotNull(authUserConfirmation.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserConfirmation.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("antonio@aliuken.com".equals(email)) {
			Assertions.assertNull(authUserConfirmation.getLastModificationDateTime());
			Assertions.assertNull(authUserConfirmation.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(authUserConfirmation.getLastModificationDateTime());

			final AuthUser lastModificationAuthUser = authUserConfirmation.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}
	}
}
