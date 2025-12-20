package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.MainClass;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserResetPassword;
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
public class AuthUserResetPasswordRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private AuthUserResetPasswordRepository authUserResetPasswordRepository;

	@Test
	public void testFindByEmail_Ok() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByEmail("guti@aliuken.com");

		this.commonTestsAuthUserResetPassword1(authUserResetPassword);
	}

	@Test
	public void testFindByEmail_Null() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByEmail(null);

		Assertions.assertNull(authUserResetPassword);
	}

	@Test
	public void testFindByEmail_NotExists() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByEmail("NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserResetPassword);
	}

	@Test
	public void testFindByEmailAndUuid_Ok() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByEmailAndUuid("guti@aliuken.com", "a0396f47-50e8-470d-94ba-16f981cdfad6");

		this.commonTestsAuthUserResetPassword1(authUserResetPassword);
	}

	@Test
	public void testFindByEmailAndUuid_NullEmail() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByEmailAndUuid(null, "a0396f47-50e8-470d-94ba-16f981cdfad6");

		Assertions.assertNull(authUserResetPassword);
	}

	@Test
	public void testFindByEmailAndUuid_NullUuid() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByEmailAndUuid("guti@aliuken.com", null);

		Assertions.assertNull(authUserResetPassword);
	}

	@Test
	public void testFindByEmailAndUuid_NotExistsEmail() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByEmailAndUuid("NOT_EXISTING_VALUE", "a0396f47-50e8-470d-94ba-16f981cdfad6");

		Assertions.assertNull(authUserResetPassword);
	}

	@Test
	public void testFindByEmailAndUuid_NotExistsUuid() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByEmailAndUuid("guti@aliuken.com", "NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserResetPassword);
	}

	@Test
	public void testGetEntityClass_Ok() {
		final Class<AuthUserResetPassword> authUserResetPasswordClass = authUserResetPasswordRepository.getEntityClass();

		Assertions.assertNotNull(authUserResetPasswordClass);
		Assertions.assertEquals(AuthUserResetPassword.class, authUserResetPasswordClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.getNewEntityInstance();

		Assertions.assertNotNull(authUserResetPassword);
		Assertions.assertEquals(new AuthUserResetPassword(), authUserResetPassword);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByIdNotOptional(1L);

		this.commonTestsAuthUserResetPassword1(authUserResetPassword);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUserResetPassword);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUserResetPassword);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByIdOrNewEntity(1L);

		this.commonTestsAuthUserResetPassword1(authUserResetPassword);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(authUserResetPassword);
		Assertions.assertEquals(new AuthUserResetPassword(), authUserResetPassword);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(authUserResetPassword);
	}

	@Test
	public void testFindAll_Ok() {
		final List<AuthUserResetPassword> authUserResetPasswords = authUserResetPasswordRepository.findAll();

		Assertions.assertNotNull(authUserResetPasswords);
		Assertions.assertEquals(2, authUserResetPasswords.size());

		for(final AuthUserResetPassword authUserResetPassword : authUserResetPasswords) {
			Assertions.assertNotNull(authUserResetPassword);

			final Long authUserResetPasswordId = authUserResetPassword.getId();

			if(Long.valueOf(1L).equals(authUserResetPasswordId)) {
				this.commonTestsAuthUserResetPassword1(authUserResetPassword);
			} else {
				Assertions.assertNotNull(authUserResetPasswordId);
				Assertions.assertNotNull(authUserResetPassword.getEmail());
				Assertions.assertNotNull(authUserResetPassword.getUuid());
				Assertions.assertNotNull(authUserResetPassword.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = authUserResetPassword.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		AuthUserResetPassword authUserResetPassword = new AuthUserResetPassword();
		authUserResetPassword.setEmail("new.user@aliuken.com");
		authUserResetPassword.setUuid("a0396f47-50e8-470d-94ba-16f981cdfad6");

		authUserResetPassword = authUserResetPasswordRepository.saveAndFlush(authUserResetPassword);

		Assertions.assertNotNull(authUserResetPassword);
		Assertions.assertNotNull(authUserResetPassword.getId());
		Assertions.assertEquals("new.user@aliuken.com", authUserResetPassword.getEmail());
		Assertions.assertEquals("a0396f47-50e8-470d-94ba-16f981cdfad6", authUserResetPassword.getUuid());
		Assertions.assertNotNull(authUserResetPassword.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserResetPassword.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authUserResetPassword.getLastModificationDateTime());
		Assertions.assertNull(authUserResetPassword.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByIdNotOptional(1L);
		authUserResetPassword.setEmail("already.confirmed3@aliuken.com");

		authUserResetPassword = authUserResetPasswordRepository.saveAndFlush(authUserResetPassword);

		this.commonTestsAuthUserResetPassword1(authUserResetPassword, "already.confirmed3@aliuken.com");
	}

	@Test
	public void testSave_Null() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserResetPasswordRepository.saveAndFlush(null);
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
				final AuthUserResetPassword authUserResetPassword = new AuthUserResetPassword();
				authUserResetPassword.setEmail("guti@aliuken.com");
				authUserResetPassword.setUuid("953d4c72-2759-4576-a50e-78e37c82ee7b");

				authUserResetPasswordRepository.saveAndFlush(authUserResetPassword);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'guti@aliuken.com' for key 'auth_user_reset_password.auth_user_reset_password_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUserResetPassword authUserResetPassword = authUserResetPasswordRepository.findByIdNotOptional(1L);
				authUserResetPassword.setEmail("raul@aliuken.com");

				authUserResetPassword = authUserResetPasswordRepository.saveAndFlush(authUserResetPassword);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'raul@aliuken.com' for key 'auth_user_reset_password.auth_user_reset_password_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		authUserResetPasswordRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserResetPasswordRepository.deleteByIdAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsAuthUserResetPassword1(final AuthUserResetPassword authUserResetPassword) {
		this.commonTestsAuthUserResetPassword1(authUserResetPassword, "guti@aliuken.com");
	}

	private void commonTestsAuthUserResetPassword1(final AuthUserResetPassword authUserResetPassword, final String email) {
		Assertions.assertNotNull(authUserResetPassword);
		Assertions.assertEquals(1L, authUserResetPassword.getId());
		Assertions.assertEquals(email, authUserResetPassword.getEmail());
		Assertions.assertEquals("a0396f47-50e8-470d-94ba-16f981cdfad6", authUserResetPassword.getUuid());
		Assertions.assertNotNull(authUserResetPassword.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserResetPassword.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("guti@aliuken.com".equals(email)) {
			Assertions.assertNull(authUserResetPassword.getLastModificationDateTime());
			Assertions.assertNull(authUserResetPassword.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(authUserResetPassword.getLastModificationDateTime());

			final AuthUser lastModificationAuthUser = authUserResetPassword.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}
	}
}
