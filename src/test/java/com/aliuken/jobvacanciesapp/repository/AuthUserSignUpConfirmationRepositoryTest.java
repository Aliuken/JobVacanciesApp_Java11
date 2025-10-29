package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.MainClass;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserSignUpConfirmation;
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
public class AuthUserSignUpConfirmationRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private AuthUserSignUpConfirmationRepository authUserSignUpConfirmationRepository;

	@Test
	public void testFindByEmail_Ok() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByEmail("antonio@aliuken.com");

		this.commonTestsAuthUserSignUpConfirmation1(authUserSignUpConfirmation);
	}

	@Test
	public void testFindByEmail_Null() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByEmail(null);

		Assertions.assertNull(authUserSignUpConfirmation);
	}

	@Test
	public void testFindByEmail_NotExists() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByEmail("NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserSignUpConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_Ok() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByEmailAndUuid("antonio@aliuken.com", "21d27fe6-4b57-413b-8361-2757c45294e3");

		this.commonTestsAuthUserSignUpConfirmation1(authUserSignUpConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NullEmail() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByEmailAndUuid(null, "21d27fe6-4b57-413b-8361-2757c45294e3");

		Assertions.assertNull(authUserSignUpConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NullUuid() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByEmailAndUuid("antonio@aliuken.com", null);

		Assertions.assertNull(authUserSignUpConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NotExistsEmail() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByEmailAndUuid("NOT_EXISTING_VALUE", "21d27fe6-4b57-413b-8361-2757c45294e3");

		Assertions.assertNull(authUserSignUpConfirmation);
	}

	@Test
	public void testFindByEmailAndUuid_NotExistsUuid() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByEmailAndUuid("antonio@aliuken.com", "NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserSignUpConfirmation);
	}

	@Test
	public void testGetEntityClass_Ok() {
		final Class<AuthUserSignUpConfirmation> authUserSignUpConfirmationClass = authUserSignUpConfirmationRepository.getEntityClass();

		Assertions.assertNotNull(authUserSignUpConfirmationClass);
		Assertions.assertEquals(AuthUserSignUpConfirmation.class, authUserSignUpConfirmationClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.getNewEntityInstance();

		Assertions.assertNotNull(authUserSignUpConfirmation);
		Assertions.assertEquals(new AuthUserSignUpConfirmation(), authUserSignUpConfirmation);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByIdNotOptional(1L);

		this.commonTestsAuthUserSignUpConfirmation1(authUserSignUpConfirmation);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUserSignUpConfirmation);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUserSignUpConfirmation);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByIdOrNewEntity(1L);

		this.commonTestsAuthUserSignUpConfirmation1(authUserSignUpConfirmation);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(authUserSignUpConfirmation);
		Assertions.assertEquals(new AuthUserSignUpConfirmation(), authUserSignUpConfirmation);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(authUserSignUpConfirmation);
	}

	@Test
	public void testFindAll_Ok() {
		final List<AuthUserSignUpConfirmation> authUserSignUpConfirmations = authUserSignUpConfirmationRepository.findAll();

		Assertions.assertNotNull(authUserSignUpConfirmations);
		Assertions.assertEquals(2, authUserSignUpConfirmations.size());

		for(final AuthUserSignUpConfirmation authUserSignUpConfirmation : authUserSignUpConfirmations) {
			Assertions.assertNotNull(authUserSignUpConfirmation);

			final Long authUserSignUpConfirmationId = authUserSignUpConfirmation.getId();

			if(Long.valueOf(1L).equals(authUserSignUpConfirmationId)) {
				this.commonTestsAuthUserSignUpConfirmation1(authUserSignUpConfirmation);
			} else {
				Assertions.assertNotNull(authUserSignUpConfirmationId);
				Assertions.assertNotNull(authUserSignUpConfirmation.getEmail());
				Assertions.assertNotNull(authUserSignUpConfirmation.getUuid());
				Assertions.assertNotNull(authUserSignUpConfirmation.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = authUserSignUpConfirmation.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		AuthUserSignUpConfirmation authUserSignUpConfirmation = new AuthUserSignUpConfirmation();
		authUserSignUpConfirmation.setEmail("new.user@aliuken.com");
		authUserSignUpConfirmation.setUuid("21d27fe6-4b57-413b-8361-2757c45294e3");

		authUserSignUpConfirmation = authUserSignUpConfirmationRepository.saveAndFlush(authUserSignUpConfirmation);

		Assertions.assertNotNull(authUserSignUpConfirmation);
		Assertions.assertNotNull(authUserSignUpConfirmation.getId());
		Assertions.assertEquals("new.user@aliuken.com", authUserSignUpConfirmation.getEmail());
		Assertions.assertEquals("21d27fe6-4b57-413b-8361-2757c45294e3", authUserSignUpConfirmation.getUuid());
		Assertions.assertNotNull(authUserSignUpConfirmation.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserSignUpConfirmation.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authUserSignUpConfirmation.getLastModificationDateTime());
		Assertions.assertNull(authUserSignUpConfirmation.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByIdNotOptional(1L);
		authUserSignUpConfirmation.setEmail("pending.confirmation3@aliuken.com");

		authUserSignUpConfirmation = authUserSignUpConfirmationRepository.saveAndFlush(authUserSignUpConfirmation);

		this.commonTestsAuthUserSignUpConfirmation1(authUserSignUpConfirmation, "pending.confirmation3@aliuken.com");
	}

	@Test
	public void testSave_Null() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserSignUpConfirmationRepository.saveAndFlush(null);
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
				final AuthUserSignUpConfirmation authUserSignUpConfirmation = new AuthUserSignUpConfirmation();
				authUserSignUpConfirmation.setEmail("antonio@aliuken.com");
				authUserSignUpConfirmation.setUuid("953d4c72-2759-4576-a50e-78e37c82ee7a");

				authUserSignUpConfirmationRepository.saveAndFlush(authUserSignUpConfirmation);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'antonio@aliuken.com' for key 'auth_user_signup_confirmation.auth_user_signup_confirmation_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationRepository.findByIdNotOptional(1L);
				authUserSignUpConfirmation.setEmail("pai.mei@aliuken.com");

				authUserSignUpConfirmation = authUserSignUpConfirmationRepository.saveAndFlush(authUserSignUpConfirmation);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'pai.mei@aliuken.com' for key 'auth_user_signup_confirmation.auth_user_signup_confirmation_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		authUserSignUpConfirmationRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserSignUpConfirmationRepository.deleteByIdAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsAuthUserSignUpConfirmation1(final AuthUserSignUpConfirmation authUserSignUpConfirmation) {
		this.commonTestsAuthUserSignUpConfirmation1(authUserSignUpConfirmation, "antonio@aliuken.com");
	}

	private void commonTestsAuthUserSignUpConfirmation1(final AuthUserSignUpConfirmation authUserSignUpConfirmation, final String email) {
		Assertions.assertNotNull(authUserSignUpConfirmation);
		Assertions.assertEquals(1L, authUserSignUpConfirmation.getId());
		Assertions.assertEquals(email, authUserSignUpConfirmation.getEmail());
		Assertions.assertEquals("21d27fe6-4b57-413b-8361-2757c45294e3", authUserSignUpConfirmation.getUuid());
		Assertions.assertNotNull(authUserSignUpConfirmation.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserSignUpConfirmation.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("antonio@aliuken.com".equals(email)) {
			Assertions.assertNull(authUserSignUpConfirmation.getLastModificationDateTime());
			Assertions.assertNull(authUserSignUpConfirmation.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(authUserSignUpConfirmation.getLastModificationDateTime());

			final AuthUser lastModificationAuthUser = authUserSignUpConfirmation.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}
	}
}
