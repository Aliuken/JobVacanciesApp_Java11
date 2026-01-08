package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.MainClass;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserCurriculumDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.AuthUserConverter;
import com.aliuken.jobvacanciesapp.model.dto.converter.AuthUserCurriculumConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainClass.class, BeanFactoryUtils.class, ConfigPropertiesBean.class})
@Sql("classpath:db_dumps/h2-dump.sql")
public class AuthUserCurriculumRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private AuthUserCurriculumRepository authUserCurriculumRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Test
	public void testFindByAuthUserAndFileName_Ok() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByAuthUserAndFileName(authUser, "EKFP0YBSmiguel.pdf");

		this.commonTestsAuthUserCurriculum1(authUserCurriculum);
	}

	@Test
	public void testFindByAuthUserAndFileName_NullAuthUser() {
		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByAuthUserAndFileName(null, "EKFP0YBSmiguel.pdf");

		Assertions.assertNull(authUserCurriculum);
	}

	@Test
	public void testFindByAuthUserAndFileName_NullFileName() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByAuthUserAndFileName(authUser, null);

		Assertions.assertNull(authUserCurriculum);
	}

	@Test
	public void testFindByAuthUserAndFileName_NotExistsAuthUser() {
		final AuthUser authUser = new AuthUser();
		authUser.setId(888L);
		authUser.setEmail("new.user@aliuken.com");

		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByAuthUserAndFileName(authUser, "EKFP0YBSmiguel.pdf");

		Assertions.assertNull(authUserCurriculum);
	}

	@Test
	public void testFindByAuthUserAndFileName_NotExistsFileName() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByAuthUserAndFileName(authUser, "NOT_EXISTING_VALUE");

		Assertions.assertNull(authUserCurriculum);
	}

	@Test
	public void testGetEntityClass_Ok() {
		final Class<AuthUserCurriculum> authUserCurriculumClass = authUserCurriculumRepository.getEntityClass();

		Assertions.assertNotNull(authUserCurriculumClass);
		Assertions.assertEquals(AuthUserCurriculum.class, authUserCurriculumClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.getNewEntityInstance();

		Assertions.assertNotNull(authUserCurriculum);
		Assertions.assertEquals(new AuthUserCurriculum(), authUserCurriculum);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByIdNotOptional(1L);

		this.commonTestsAuthUserCurriculum1(authUserCurriculum);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUserCurriculum);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUserCurriculum);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByIdOrNewEntity(1L);

		this.commonTestsAuthUserCurriculum1(authUserCurriculum);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(authUserCurriculum);
		Assertions.assertEquals(new AuthUserCurriculum(), authUserCurriculum);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(authUserCurriculum);
	}

	@Test
	public void testFindAll_Ok() {
		final List<AuthUserCurriculum> authUserCurriculums = authUserCurriculumRepository.findAll();

		Assertions.assertNotNull(authUserCurriculums);
		Assertions.assertEquals(7, authUserCurriculums.size());

		for(final AuthUserCurriculum authUserCurriculum : authUserCurriculums) {
			Assertions.assertNotNull(authUserCurriculum);

			final Long authUserCurriculumId = authUserCurriculum.getId();

			if(Long.valueOf(1L).equals(authUserCurriculumId)) {
				this.commonTestsAuthUserCurriculum1(authUserCurriculum);
			} else {
				Assertions.assertNotNull(authUserCurriculumId);
				Assertions.assertNotNull(authUserCurriculum.getAuthUser());
				Assertions.assertNotNull(authUserCurriculum.getFileName());
				Assertions.assertNotNull(authUserCurriculum.getDescription());
				Assertions.assertNotNull(authUserCurriculum.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = authUserCurriculum.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		AuthUserCurriculum authUserCurriculum = new AuthUserCurriculum();
		authUserCurriculum.setAuthUser(authUser);
		authUserCurriculum.setFileName("newCV2022.pdf");
		authUserCurriculum.setDescription("New CV 2022");

		authUserCurriculum = authUserCurriculumRepository.saveAndFlush(authUserCurriculum);

		Assertions.assertNotNull(authUserCurriculum);
		Assertions.assertNotNull(authUserCurriculum.getId());
		Assertions.assertEquals(authUser, authUserCurriculum.getAuthUser());
		Assertions.assertEquals("newCV2022.pdf", authUserCurriculum.getFileName());
		Assertions.assertEquals("New CV 2022", authUserCurriculum.getDescription());
		Assertions.assertNotNull(authUserCurriculum.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserCurriculum.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authUserCurriculum.getLastModificationDateTime());
		Assertions.assertNull(authUserCurriculum.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByIdNotOptional(1L);
		authUserCurriculum.setDescription("New CV 2022");

		authUserCurriculum = authUserCurriculumRepository.saveAndFlush(authUserCurriculum);

		this.commonTestsAuthUserCurriculum1(authUserCurriculum, "New CV 2022");
	}

	@Test
	public void testSave_Null() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserCurriculumRepository.saveAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertAuthUserAndFileNameExist() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

				final AuthUserCurriculum authUserCurriculum = new AuthUserCurriculum();
				authUserCurriculum.setAuthUser(authUser);
				authUserCurriculum.setFileName("EKFP0YBSmiguel.pdf");
				authUserCurriculum.setDescription("New CV 2022");

				authUserCurriculumRepository.saveAndFlush(authUserCurriculum);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry '2-EKFP0YBSmiguel.pdf' for key 'auth_user_curriculum.auth_user_curriculum_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateAuthUserAndFileNameExist() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUserCurriculum authUserCurriculum = authUserCurriculumRepository.findByIdNotOptional(1L);
				authUserCurriculum.setFileName("ExampleCV1.docx");

				authUserCurriculum = authUserCurriculumRepository.saveAndFlush(authUserCurriculum);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry '2-ExampleCV1.docx' for key 'auth_user_curriculum.auth_user_curriculum_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		authUserCurriculumRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserCurriculumRepository.deleteByIdAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsAuthUserCurriculum1(final AuthUserCurriculum authUserCurriculum) {
		this.commonTestsAuthUserCurriculum1(authUserCurriculum, "CV 2021");
	}

	private void commonTestsAuthUserCurriculum1(final AuthUserCurriculum authUserCurriculum, final String description) {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		Assertions.assertNotNull(authUserCurriculum);
		Assertions.assertNotNull(authUser);
		Assertions.assertEquals(1L, authUserCurriculum.getId());
		Assertions.assertEquals(authUser, authUserCurriculum.getAuthUser());
		Assertions.assertEquals("EKFP0YBSmiguel.pdf", authUserCurriculum.getFileName());
		Assertions.assertEquals(description, authUserCurriculum.getDescription());
		Assertions.assertNotNull(authUserCurriculum.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserCurriculum.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("CV 2021".equals(authUserCurriculum.getDescription())) {
			Assertions.assertNull(authUserCurriculum.getLastModificationDateTime());
			Assertions.assertNull(authUserCurriculum.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(authUserCurriculum.getLastModificationDateTime());

			final AuthUser lastModificationAuthUser = authUserCurriculum.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}

		final AuthUserCurriculumDTO authUserCurriculumDTO = AuthUserCurriculumConverter.getInstance().convertEntityElement(authUserCurriculum);
		this.commonTestsAuthUserCurriculumDTO1(authUserCurriculumDTO, description);
	}

	private void commonTestsAuthUserCurriculumDTO1(final AuthUserCurriculumDTO authUserCurriculumDTO, final String description) {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		Assertions.assertNotNull(authUser);

		final AuthUserDTO authUserDTO = AuthUserConverter.getInstance().convertEntityElement(authUser);
		Assertions.assertNotNull(authUserCurriculumDTO);
		Assertions.assertNotNull(authUserDTO);
		Assertions.assertEquals(1L, authUserCurriculumDTO.getId());
		Assertions.assertEquals(authUserDTO, authUserCurriculumDTO.getAuthUser());

		final MultipartFile curriculumFile = authUserCurriculumDTO.getCurriculumFile();
		Assertions.assertNotNull(curriculumFile);
		Assertions.assertTrue(curriculumFile.isEmpty());

		Assertions.assertEquals("EKFP0YBSmiguel.pdf", authUserCurriculumDTO.getFileName());
		Assertions.assertEquals(description, authUserCurriculumDTO.getDescription());
	}
}
