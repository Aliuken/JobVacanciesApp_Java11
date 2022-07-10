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
import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.util.ApplicationContextUtil;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainApplication.class, ApplicationContextUtil.class})
@Sql("classpath:db-dump-for-tests.sql")
public class JobCompanyLogoRepositoryTest {
	@Autowired
	private JobCompanyLogoRepository jobCompanyLogoRepository;

	@Autowired
	private JobCompanyRepository jobCompanyRepository;

	@Test
	public void testFindByJobCompanyAndFileName_Ok() {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByJobCompanyAndFileName(jobCompany, "logo1.png");

		this.commonTestsJobCompanyLogo1(jobCompanyLogo);
	}

	@Test
	public void testFindByJobCompanyAndFileName_NullJobCompany() {
		JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByJobCompanyAndFileName(null, "logo1.png");

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testFindByJobCompanyAndFileName_NullFileName() {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByJobCompanyAndFileName(jobCompany, null);

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testFindByJobCompanyAndFileName_NotExistsJobCompany() {
		JobCompany jobCompany = new JobCompany();
		jobCompany.setId(888L);
		jobCompany.setName("NEW_COMPANY");

		JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByJobCompanyAndFileName(jobCompany, "logo1.png");

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testFindByJobCompanyAndFileName_NotExistsFileName() {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByJobCompanyAndFileName(jobCompany, "NOT_EXISTING_VALUE");

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testGetEntityClass_Ok() {
		Class<JobCompanyLogo> jobCompanyLogoClass = jobCompanyLogoRepository.getEntityClass();

		Assertions.assertNotNull(jobCompanyLogoClass);
		Assertions.assertEquals(JobCompanyLogo.class, jobCompanyLogoClass);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdNotOptional(1L);

		this.commonTestsJobCompanyLogo1(jobCompanyLogo);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdNotOptional(null);

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdNotOptional(888L);

		Assertions.assertNull(jobCompanyLogo);
	}

	@Test
	public void testFindAll_Ok() {
		List<JobCompanyLogo> jobCompanyLogos = jobCompanyLogoRepository.findAll();

		Assertions.assertNotNull(jobCompanyLogos);
		Assertions.assertEquals(28, jobCompanyLogos.size());

		for(JobCompanyLogo jobCompanyLogo : jobCompanyLogos) {
			Assertions.assertNotNull(jobCompanyLogo);

			Long jobCompanyLogoId = jobCompanyLogo.getId();

			if(Long.valueOf(1L).equals(jobCompanyLogoId)) {
				this.commonTestsJobCompanyLogo1(jobCompanyLogo);
			} else {
				Assertions.assertNotNull(jobCompanyLogoId);
				Assertions.assertNotNull(jobCompanyLogo.getJobCompany());
				Assertions.assertNotNull(jobCompanyLogo.getFileName());
				Assertions.assertNotNull(jobCompanyLogo.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = jobCompanyLogo.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		JobCompanyLogo jobCompanyLogo = new JobCompanyLogo();
		jobCompanyLogo.setJobCompany(jobCompany);
		jobCompanyLogo.setFileName("newLogo.png");

		jobCompanyLogo = jobCompanyLogoRepository.saveAndFlush(jobCompanyLogo);

		Assertions.assertNotNull(jobCompanyLogo);
		Assertions.assertNotNull(jobCompanyLogo.getId());
		Assertions.assertEquals(jobCompany, jobCompanyLogo.getJobCompany());
		Assertions.assertEquals("newLogo.png", jobCompanyLogo.getFileName());
		Assertions.assertNotNull(jobCompanyLogo.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = jobCompanyLogo.getFirstRegistrationAuthUser();
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
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobCompanyLogoRepository.saveAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertJobCompanyAndFileNameExist() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

				JobCompanyLogo jobCompanyLogo = new JobCompanyLogo();
				jobCompanyLogo.setJobCompany(jobCompany);
				jobCompanyLogo.setFileName("logo1.png");

				jobCompanyLogoRepository.saveAndFlush(jobCompanyLogo);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry '1-logo1.png' for key 'job_company_logo.job_company_logo_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateJobCompanyAndFileNameExist() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				JobCompanyLogo jobCompanyLogo = jobCompanyLogoRepository.findByIdNotOptional(1L);
				jobCompanyLogo.setFileName("AYHRERYKlogo-bitcoin.png");

				jobCompanyLogo = jobCompanyLogoRepository.saveAndFlush(jobCompanyLogo);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry '1-AYHRERYKlogo-bitcoin.png' for key 'job_company_logo.job_company_logo_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		jobCompanyLogoRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobCompanyLogoRepository.deleteByIdAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsJobCompanyLogo1(JobCompanyLogo jobCompanyLogo) {
		this.commonTestsJobCompanyLogo1(jobCompanyLogo, "logo1.png");
	}

	private void commonTestsJobCompanyLogo1(JobCompanyLogo jobCompanyLogo, String fileName) {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		Assertions.assertNotNull(jobCompanyLogo);
		Assertions.assertNotNull(jobCompany);
		Assertions.assertEquals(1L, jobCompanyLogo.getId());
		Assertions.assertEquals(jobCompany, jobCompanyLogo.getJobCompany());
		Assertions.assertEquals(fileName, jobCompanyLogo.getFileName());
		Assertions.assertNotNull(jobCompanyLogo.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = jobCompanyLogo.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("logo1.png".equals(jobCompanyLogo.getFileName())) {
			Assertions.assertNull(jobCompanyLogo.getLastModificationDateTime());
			Assertions.assertNull(jobCompanyLogo.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(jobCompanyLogo.getLastModificationDateTime());

			AuthUser lastModificationAuthUser = jobCompanyLogo.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}
	}
}
