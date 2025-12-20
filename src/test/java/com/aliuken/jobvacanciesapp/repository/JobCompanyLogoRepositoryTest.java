package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.MainClass;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyLogoDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.JobCompanyLogoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainClass.class, BeanFactoryUtils.class, ConfigPropertiesBean.class})
@Sql("classpath:db_dumps/h2-dump.sql")
public class JobCompanyLogoRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private JobCompanyLogoRepository jobCompanyLogoRepository;

	@Autowired
	private JobCompanyRepository jobCompanyRepository;

	@Test
	public void testFindByJobCompanyAndFileName_Ok() {
		final JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByJobCompanyAndFileName(jobCompany, "logo1.png");

		this.commonTestsJobCompanyLogo1(jobCompanyLogo);
	}

	@Test
	public void testFindByJobCompanyAndFileName_NullJobCompany() {
		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByJobCompanyAndFileName(null, "logo1.png");

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testFindByJobCompanyAndFileName_NullFileName() {
		final JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByJobCompanyAndFileName(jobCompany, null);

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testFindByJobCompanyAndFileName_NotExistsJobCompany() {
		final JobCompany jobCompany = new JobCompany();
		jobCompany.setId(888L);
		jobCompany.setName("NEW_COMPANY");

		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByJobCompanyAndFileName(jobCompany, "logo1.png");

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testFindByJobCompanyAndFileName_NotExistsFileName() {
		final JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByJobCompanyAndFileName(jobCompany, "NOT_EXISTING_VALUE");

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testGetEntityClass_Ok() {
		final Class<JobCompanyLogo> jobCompanyLogoClass = jobCompanyLogoRepository.getEntityClass();

		Assertions.assertNotNull(jobCompanyLogoClass);
		Assertions.assertEquals(JobCompanyLogo.class, jobCompanyLogoClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.getNewEntityInstance();

		Assertions.assertNotNull(jobCompanyLogo);
		Assertions.assertEquals(new JobCompanyLogo(), jobCompanyLogo);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdNotOptional(1L);

		this.commonTestsJobCompanyLogo1(jobCompanyLogo);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdNotOptional(null);

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdNotOptional(888L);

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdOrNewEntity(1L);

		this.commonTestsJobCompanyLogo1(jobCompanyLogo);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(jobCompanyLogo);
		Assertions.assertEquals(new JobCompanyLogo(), jobCompanyLogo);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testFindAll_Ok() {
		final List<JobCompanyLogo> jobCompanyLogos = jobCompanyLogoRepository.findAll();

		Assertions.assertNotNull(jobCompanyLogos);
		Assertions.assertEquals(28, jobCompanyLogos.size());

		for(final JobCompanyLogo jobCompanyLogo : jobCompanyLogos) {
			Assertions.assertNotNull(jobCompanyLogo);

			final Long jobCompanyLogoId = jobCompanyLogo.getId();

			if(Long.valueOf(1L).equals(jobCompanyLogoId)) {
				this.commonTestsJobCompanyLogo1(jobCompanyLogo);
			} else {
				Assertions.assertNotNull(jobCompanyLogoId);
				Assertions.assertNotNull(jobCompanyLogo.getJobCompany());
				Assertions.assertNotNull(jobCompanyLogo.getFileName());
				Assertions.assertNotNull(jobCompanyLogo.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = jobCompanyLogo.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		final JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		JobCompanyLogo jobCompanyLogo = new JobCompanyLogo();
		jobCompanyLogo.setJobCompany(jobCompany);
		jobCompanyLogo.setFileName("newLogo.png");

		jobCompanyLogo = jobCompanyLogoRepository.saveAndFlush(jobCompanyLogo);

		Assertions.assertNotNull(jobCompanyLogo);
		Assertions.assertNotNull(jobCompanyLogo.getId());
		Assertions.assertEquals(jobCompany, jobCompanyLogo.getJobCompany());
		Assertions.assertEquals("newLogo.png", jobCompanyLogo.getFileName());
		Assertions.assertNotNull(jobCompanyLogo.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = jobCompanyLogo.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(jobCompanyLogo.getLastModificationDateTime());
		Assertions.assertNull(jobCompanyLogo.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdNotOptional(1L);
		jobCompanyLogo.setFileName("newLogo.png");

		jobCompanyLogo = jobCompanyLogoRepository.saveAndFlush(jobCompanyLogo);

		this.commonTestsJobCompanyLogo1(jobCompanyLogo, "newLogo.png");
	}

	@Test
	public void testSave_Null() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobCompanyLogoRepository.saveAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertJobCompanyAndFileNameExist() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				final JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

				final JobCompanyLogo jobCompanyLogo = new JobCompanyLogo();
				jobCompanyLogo.setJobCompany(jobCompany);
				jobCompanyLogo.setFileName("logo1.png");

				jobCompanyLogoRepository.saveAndFlush(jobCompanyLogo);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry '1-logo1.png' for key 'job_company_logo.job_company_logo_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateJobCompanyAndFileNameExist() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdNotOptional(1L);
				jobCompanyLogo.setFileName("AYHRERYKlogo-bitcoin.png");

				jobCompanyLogo = jobCompanyLogoRepository.saveAndFlush(jobCompanyLogo);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry '1-AYHRERYKlogo-bitcoin.png' for key 'job_company_logo.job_company_logo_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		jobCompanyLogoRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobCompanyLogoRepository.deleteByIdAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsJobCompanyLogo1(final JobCompanyLogo jobCompanyLogo) {
		this.commonTestsJobCompanyLogo1(jobCompanyLogo, "logo1.png");
	}

	private void commonTestsJobCompanyLogo1(final JobCompanyLogo jobCompanyLogo, final String fileName) {
		final JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		Assertions.assertNotNull(jobCompanyLogo);
		Assertions.assertNotNull(jobCompany);
		Assertions.assertNotNull(jobCompany.getId());
		Assertions.assertEquals(1L, jobCompanyLogo.getId());
		Assertions.assertEquals(jobCompany, jobCompanyLogo.getJobCompany());
		Assertions.assertEquals(fileName, jobCompanyLogo.getFileName());

		final String jobCompanyIdString = jobCompany.getIdString();
		final String filePath = StringUtils.getStringJoined(jobCompanyIdString, "/", fileName);
		Assertions.assertEquals(filePath, jobCompanyLogo.getFilePath());

		final String jobCompanyLogoIdString = jobCompanyLogo.getIdString();
		final String firstRegistrationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(jobCompanyLogo.getFirstRegistrationDateTime());
		final String selectionName = StringUtils.getStringJoined("Logo ", jobCompanyLogoIdString, Constants.SPACE, firstRegistrationDateTimeString);
		Assertions.assertEquals(selectionName, jobCompanyLogo.getSelectionName());

		Assertions.assertNotNull(jobCompanyLogo.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = jobCompanyLogo.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("logo1.png".equals(jobCompanyLogo.getFileName())) {
			Assertions.assertNull(jobCompanyLogo.getLastModificationDateTime());
			Assertions.assertNull(jobCompanyLogo.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(jobCompanyLogo.getLastModificationDateTime());

			final AuthUser lastModificationAuthUser = jobCompanyLogo.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}

		final JobCompanyLogoDTO jobCompanyLogoDTO = JobCompanyLogoConverter.getInstance().convertEntityElement(jobCompanyLogo);
		this.commonTestsJobCompanyLogoDTO1(jobCompanyLogoDTO, fileName, jobCompanyLogo.getFirstRegistrationDateTime());
	}

	private void commonTestsJobCompanyLogoDTO1(final JobCompanyLogoDTO jobCompanyLogoDTO, final String fileName, final LocalDateTime firstRegistrationDateTime) {
		final JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		Assertions.assertNotNull(jobCompanyLogoDTO);
		Assertions.assertNotNull(jobCompany);
		Assertions.assertNotNull(jobCompany.getId());
		Assertions.assertEquals(1L, jobCompanyLogoDTO.getId());
		Assertions.assertEquals(fileName, jobCompanyLogoDTO.getFileName());

		final String jobCompanyIdString = jobCompany.getIdString();
		final String filePath = StringUtils.getStringJoined(jobCompanyIdString, "/", fileName);
		Assertions.assertEquals(filePath, jobCompanyLogoDTO.getFilePath());

		final String jobCompanyLogoIdString = Objects.toString(jobCompanyLogoDTO.getId());
		final String firstRegistrationDateTimeString = Constants.DATE_TIME_UTILS.convertToString(firstRegistrationDateTime);
		final String selectionName = StringUtils.getStringJoined("Logo ", jobCompanyLogoIdString, Constants.SPACE, firstRegistrationDateTimeString);
		Assertions.assertEquals(selectionName, jobCompanyLogoDTO.getSelectionName());
	}
}
