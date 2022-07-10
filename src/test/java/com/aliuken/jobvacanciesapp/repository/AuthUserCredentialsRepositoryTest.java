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
import com.aliuken.jobvacanciesapp.model.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.util.ApplicationContextUtil;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainApplication.class, ApplicationContextUtil.class})
@Sql("classpath:db-dump-for-tests.sql")
public class AuthUserCredentialsRepositoryTest {
	@Autowired
	private AuthUserCredentialsRepository authUserCredentialsRepository;

	@Test
	public void testFindByEmail_Ok() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmail("aliuken@aliuken.com");

		this.commonTestsAuthUserCredentials1(authUserCredentials);
	}

	@Test
	public void testFindByEmail_Null() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmail(null);

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByEmail_NotExists() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmail("NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByEmailAndEncryptedPassword_Ok() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmailAndEncryptedPassword("aliuken@aliuken.com", "$2a$10$emZdB6.UCgKUSSP5XbtUUu3EJC90ORzvBGgmWLEcHxzd.rNwTTzWq");

		this.commonTestsAuthUserCredentials1(authUserCredentials);
	}

	@Test
	public void testFindByEmailAndEncryptedPassword_NullEmail() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmailAndEncryptedPassword(null, "$2a$10$emZdB6.UCgKUSSP5XbtUUu3EJC90ORzvBGgmWLEcHxzd.rNwTTzWq");

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByEmailAndEncryptedPassword_NullEncryptedPassword() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmailAndEncryptedPassword("aliuken@aliuken.com", null);

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByEmailAndEncryptedPassword_NotExistsEmail() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmailAndEncryptedPassword("NOT_EXISTING_VALUE", "$2a$10$emZdB6.UCgKUSSP5XbtUUu3EJC90ORzvBGgmWLEcHxzd.rNwTTzWq");

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByEmailAndEncryptedPassword_NotExistsEncryptedPassword() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmailAndEncryptedPassword("aliuken@aliuken.com", "NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testGetEntityClass_Ok() {
		Class<AuthUserCredentials> authUserCredentialsClass = authUserCredentialsRepository.getEntityClass();

		Assertions.assertNotNull(authUserCredentialsClass);
		Assertions.assertEquals(AuthUserCredentials.class, authUserCredentialsClass);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdNotOptional(1L);

		this.commonTestsAuthUserCredentials1(authUserCredentials);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindAll_Ok() {
		List<AuthUserCredentials> authUserCredentialss = authUserCredentialsRepository.findAll();

		Assertions.assertNotNull(authUserCredentialss);
		Assertions.assertEquals(7, authUserCredentialss.size());

		for(AuthUserCredentials authUserCredentials : authUserCredentialss) {
			Assertions.assertNotNull(authUserCredentials);

			Long authUserCredentialsId = authUserCredentials.getId();

			if(Long.valueOf(1L).equals(authUserCredentialsId)) {
				this.commonTestsAuthUserCredentials1(authUserCredentials);
			} else {
				Assertions.assertNotNull(authUserCredentialsId);
				Assertions.assertNotNull(authUserCredentials.getEmail());
				Assertions.assertNotNull(authUserCredentials.getEncryptedPassword());
				Assertions.assertNotNull(authUserCredentials.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = authUserCredentials.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		AuthUserCredentials authUserCredentials = new AuthUserCredentials();
		authUserCredentials.setEmail("new.user@aliuken.com");
		authUserCredentials.setEncryptedPassword("$2a$10$emZdB6.UCgKUSSP5XbtUUu3EJC90ORzvBGgmWLEcHxzd.rNwTTzWq");

		authUserCredentials = authUserCredentialsRepository.saveAndFlush(authUserCredentials);

		Assertions.assertNotNull(authUserCredentials);
		Assertions.assertNotNull(authUserCredentials.getId());
		Assertions.assertEquals("new.user@aliuken.com", authUserCredentials.getEmail());
		Assertions.assertEquals("$2a$10$emZdB6.UCgKUSSP5XbtUUu3EJC90ORzvBGgmWLEcHxzd.rNwTTzWq", authUserCredentials.getEncryptedPassword());
		Assertions.assertNotNull(authUserCredentials.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = authUserCredentials.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authUserCredentials.getLastModificationDateTime());
		Assertions.assertNull(authUserCredentials.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdNotOptional(1L);
		authUserCredentials.setEmail("new.user@aliuken.com");

		authUserCredentials = authUserCredentialsRepository.saveAndFlush(authUserCredentials);

		this.commonTestsAuthUserCredentials1(authUserCredentials, "new.user@aliuken.com");
	}

	@Test
	public void testSave_Null() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserCredentialsRepository.saveAndFlush(null);
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
				AuthUserCredentials authUserCredentials = new AuthUserCredentials();
				authUserCredentials.setEmail("aliuken@aliuken.com");
				authUserCredentials.setEncryptedPassword("$2a$10$3/Cf9LVNbfJ.nuotE4J7uO2COFCbBtPFjufVzkzKh2iR4EPlBaXna");

				authUserCredentialsRepository.saveAndFlush(authUserCredentials);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'aliuken@aliuken.com' for key 'auth_user_credentials.auth_user_credentials_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdNotOptional(1L);
				authUserCredentials.setEmail("luis@aliuken.com");

				authUserCredentials = authUserCredentialsRepository.saveAndFlush(authUserCredentials);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'luis@aliuken.com' for key 'auth_user_credentials.auth_user_credentials_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		authUserCredentialsRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserCredentialsRepository.deleteByIdAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsAuthUserCredentials1(AuthUserCredentials authUserCredentials){
		this.commonTestsAuthUserCredentials1(authUserCredentials, "aliuken@aliuken.com");
	}

	private void commonTestsAuthUserCredentials1(AuthUserCredentials authUserCredentials, String email) {
		Assertions.assertNotNull(authUserCredentials);
		Assertions.assertEquals(1L, authUserCredentials.getId());
		Assertions.assertEquals(email, authUserCredentials.getEmail());
		Assertions.assertEquals("$2a$10$emZdB6.UCgKUSSP5XbtUUu3EJC90ORzvBGgmWLEcHxzd.rNwTTzWq", authUserCredentials.getEncryptedPassword());
		Assertions.assertNotNull(authUserCredentials.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = authUserCredentials.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("aliuken@aliuken.com".equals(email)) {
			Assertions.assertNull(authUserCredentials.getLastModificationDateTime());
			Assertions.assertNull(authUserCredentials.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(authUserCredentials.getLastModificationDateTime());

			AuthUser lastModificationAuthUser = authUserCredentials.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}
	}
}
