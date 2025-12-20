package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.MainClass;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserCredentialsDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.AuthUserCredentialsConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
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
public class AuthUserCredentialsRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private AuthUserCredentialsRepository authUserCredentialsRepository;

	@Test
	public void testFindByEmail_Ok() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmail("aliuken@aliuken.com");

		this.commonTestsAuthUserCredentials1(authUserCredentials);
	}

	@Test
	public void testFindByEmail_Null() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmail(null);

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByEmail_NotExists() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmail("NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByEmailAndEncryptedPassword_Ok() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmailAndEncryptedPassword("aliuken@aliuken.com", "$2a$10$emZdB6.UCgKUSSP5XbtUUu3EJC90ORzvBGgmWLEcHxzd.rNwTTzWq");

		this.commonTestsAuthUserCredentials1(authUserCredentials);
	}

	@Test
	public void testFindByEmailAndEncryptedPassword_NullEmail() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmailAndEncryptedPassword(null, "$2a$10$emZdB6.UCgKUSSP5XbtUUu3EJC90ORzvBGgmWLEcHxzd.rNwTTzWq");

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByEmailAndEncryptedPassword_NullEncryptedPassword() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmailAndEncryptedPassword("aliuken@aliuken.com", null);

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByEmailAndEncryptedPassword_NotExistsEmail() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmailAndEncryptedPassword("NOT_EXISTING_VALUE", "$2a$10$emZdB6.UCgKUSSP5XbtUUu3EJC90ORzvBGgmWLEcHxzd.rNwTTzWq");

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByEmailAndEncryptedPassword_NotExistsEncryptedPassword() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByEmailAndEncryptedPassword("aliuken@aliuken.com", "NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testGetEntityClass_Ok() {
		final Class<AuthUserCredentials> authUserCredentialsClass = authUserCredentialsRepository.getEntityClass();

		Assertions.assertNotNull(authUserCredentialsClass);
		Assertions.assertEquals(AuthUserCredentials.class, authUserCredentialsClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.getNewEntityInstance();

		Assertions.assertNotNull(authUserCredentials);
		Assertions.assertEquals(new AuthUserCredentials(), authUserCredentials);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdNotOptional(1L);

		this.commonTestsAuthUserCredentials1(authUserCredentials);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdOrNewEntity(1L);

		this.commonTestsAuthUserCredentials1(authUserCredentials);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(authUserCredentials);
		Assertions.assertEquals(new AuthUserCredentials(), authUserCredentials);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(authUserCredentials);
	}

	@Test
	public void testFindAll_Ok() {
		final List<AuthUserCredentials> authUserCredentialsList = authUserCredentialsRepository.findAll();

		Assertions.assertNotNull(authUserCredentialsList);
		Assertions.assertEquals(8, authUserCredentialsList.size());

		for(final AuthUserCredentials authUserCredentials : authUserCredentialsList) {
			Assertions.assertNotNull(authUserCredentials);

			final Long authUserCredentialsId = authUserCredentials.getId();

			if(Long.valueOf(1L).equals(authUserCredentialsId)) {
				this.commonTestsAuthUserCredentials1(authUserCredentials);
			} else {
				Assertions.assertNotNull(authUserCredentialsId);
				Assertions.assertNotNull(authUserCredentials.getEmail());
				Assertions.assertNotNull(authUserCredentials.getEncryptedPassword());
				Assertions.assertNotNull(authUserCredentials.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = authUserCredentials.getFirstRegistrationAuthUser();
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

		final AuthUser firstRegistrationAuthUser = authUserCredentials.getFirstRegistrationAuthUser();
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
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserCredentialsRepository.saveAndFlush(null);
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
				final AuthUserCredentials authUserCredentials = new AuthUserCredentials();
				authUserCredentials.setEmail("aliuken@aliuken.com");
				authUserCredentials.setEncryptedPassword("$2a$10$3/Cf9LVNbfJ.nuotE4J7uO2COFCbBtPFjufVzkzKh2iR4EPlBaXna");

				authUserCredentialsRepository.saveAndFlush(authUserCredentials);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'aliuken@aliuken.com' for key 'auth_user_credentials.auth_user_credentials_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUserCredentials authUserCredentials = authUserCredentialsRepository.findByIdNotOptional(1L);
				authUserCredentials.setEmail("luis@aliuken.com");

				authUserCredentials = authUserCredentialsRepository.saveAndFlush(authUserCredentials);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'luis@aliuken.com' for key 'auth_user_credentials.auth_user_credentials_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		authUserCredentialsRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserCredentialsRepository.deleteByIdAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsAuthUserCredentials1(final AuthUserCredentials authUserCredentials) {
		this.commonTestsAuthUserCredentials1(authUserCredentials, "aliuken@aliuken.com");
	}

	private void commonTestsAuthUserCredentials1(final AuthUserCredentials authUserCredentials, final String email) {
		Assertions.assertNotNull(authUserCredentials);
		Assertions.assertEquals(1L, authUserCredentials.getId());
		Assertions.assertEquals(email, authUserCredentials.getEmail());
		Assertions.assertEquals("$2a$10$emZdB6.UCgKUSSP5XbtUUu3EJC90ORzvBGgmWLEcHxzd.rNwTTzWq", authUserCredentials.getEncryptedPassword());
		Assertions.assertNotNull(authUserCredentials.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserCredentials.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("aliuken@aliuken.com".equals(email)) {
			Assertions.assertNull(authUserCredentials.getLastModificationDateTime());
			Assertions.assertNull(authUserCredentials.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(authUserCredentials.getLastModificationDateTime());

			final AuthUser lastModificationAuthUser = authUserCredentials.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}

		final AuthUserCredentialsDTO authUserCredentialsDTO = AuthUserCredentialsConverter.getInstance().convertEntityElement(authUserCredentials);
		this.commonTestsAuthUserCredentialsDTO1(authUserCredentialsDTO, email);
	}

	private void commonTestsAuthUserCredentialsDTO1(AuthUserCredentialsDTO authUserCredentialsDTO, String email) {
		Assertions.assertNotNull(authUserCredentialsDTO);
		Assertions.assertEquals(1L, authUserCredentialsDTO.getId());
		Assertions.assertEquals(email, authUserCredentialsDTO.getEmail());
		Assertions.assertNull(authUserCredentialsDTO.getPassword());
		Assertions.assertNull(authUserCredentialsDTO.getNewPassword1());
		Assertions.assertNull(authUserCredentialsDTO.getNewPassword2());
	}
}
