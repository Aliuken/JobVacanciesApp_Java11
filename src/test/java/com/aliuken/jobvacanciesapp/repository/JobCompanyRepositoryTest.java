package com.aliuken.jobvacanciesapp.repository;

import java.util.List;
import java.util.Set;

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
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.util.ApplicationContextUtil;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainApplication.class, ApplicationContextUtil.class})
@Sql("classpath:db-dump-for-tests.sql")
public class JobCompanyRepositoryTest {
	@Autowired
	private JobCompanyRepository jobCompanyRepository;

	@Autowired
	private JobCompanyLogoRepository jobCompanyLogoRepository;

	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Autowired
	private JobRequestRepository jobRequestRepository;

	@Test
	public void testFindByName_Ok() {
		JobCompany jobCompany = jobCompanyRepository.findByName("Quality");

		this.commonTestsJobCompany1(jobCompany);
	}

	@Test
	public void testFindByName_Null() {
		JobCompany jobCompany = jobCompanyRepository.findByName(null);

		Assertions.assertNull(jobCompany);
	}

	@Test
	public void testFindByName_NotExists() {
		JobCompany jobCompany = jobCompanyRepository.findByName("NOT_EXISTING_VALUE");

		Assertions.assertNull(jobCompany);
	}

	@Test
	public void testGetEntityClass_Ok() {
		Class<JobCompany> jobCompanyClass = jobCompanyRepository.getEntityClass();

		Assertions.assertNotNull(jobCompanyClass);
		Assertions.assertEquals(JobCompany.class, jobCompanyClass);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		this.commonTestsJobCompany1(jobCompany);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(null);

		Assertions.assertNull(jobCompany);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(888L);

		Assertions.assertNull(jobCompany);
	}

	@Test
	public void testFindAll_Ok() {
		List<JobCompany> jobCategories = jobCompanyRepository.findAll();

		Assertions.assertNotNull(jobCategories);
		Assertions.assertEquals(23, jobCategories.size());

		for(JobCompany jobCompany : jobCategories) {
			Assertions.assertNotNull(jobCompany);

			Long jobCompanyId = jobCompany.getId();

			if(Long.valueOf(1L).equals(jobCompanyId)) {
				this.commonTestsJobCompany1(jobCompany);
			} else {
				Assertions.assertNotNull(jobCompanyId);
				Assertions.assertNotNull(jobCompany.getName());
				Assertions.assertNotNull(jobCompany.getDescription());
				Assertions.assertNotNull(jobCompany.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = jobCompany.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		JobCompany jobCompany = new JobCompany();
		jobCompany.setName("NEW_NAME");
		jobCompany.setDescription("NEW_DESCRIPTION");

		jobCompany = jobCompanyRepository.saveAndFlush(jobCompany);

		Assertions.assertNotNull(jobCompany);
		Assertions.assertNotNull(jobCompany.getId());
		Assertions.assertEquals("NEW_NAME", jobCompany.getName());
		Assertions.assertEquals("NEW_DESCRIPTION", jobCompany.getDescription());
		Assertions.assertNotNull(jobCompany.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = jobCompany.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(jobCompany.getLastModificationDateTime());
		Assertions.assertNull(jobCompany.getLastModificationAuthUser());

		Set<JobVacancy> jobVacancies = jobCompany.getJobVacancies();
		Assertions.assertNull(jobVacancies);

		Set<Long> jobVacancyIds = jobCompany.getJobVacancyIds();
		Assertions.assertNotNull(jobVacancyIds);
		Assertions.assertTrue(jobVacancyIds.isEmpty());

		Set<String> jobVacancyNames = jobCompany.getJobVacancyNames();
		Assertions.assertNotNull(jobVacancyNames);
		Assertions.assertTrue(jobVacancyNames.isEmpty());
	}

	@Test
	public void testSave_UpdateOk() {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);
		jobCompany.setName("NEW_NAME");

		jobCompany = jobCompanyRepository.saveAndFlush(jobCompany);

		this.commonTestsJobCompany1(jobCompany, "NEW_NAME");
	}

	@Test
	public void testSave_Null() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobCompanyRepository.saveAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertNameExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				JobCompany jobCompany = new JobCompany();
				jobCompany.setName("Quality");
				jobCompany.setDescription("NEW_DESCRIPTION");

				jobCompanyRepository.saveAndFlush(jobCompany);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'Quality' for key 'job_company.job_company_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(2L);
				jobCompany.setName("Quality");

				jobCompany = jobCompanyRepository.saveAndFlush(jobCompany);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'Quality' for key 'job_company.job_company_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);
		Assertions.assertNotNull(jobCompany);

		Set<JobVacancy> jobVacancies = jobCompany.getJobVacancies();
		Assertions.assertNotNull(jobVacancies);
		for(JobVacancy jobVacancy : jobVacancies) {
			Assertions.assertNotNull(jobVacancy);
			Set<Long> jobRequestIds = jobVacancy.getJobRequestIds();
			Assertions.assertNotNull(jobRequestIds);
			for(Long jobRequestId : jobRequestIds) {
				jobRequestRepository.deleteByIdAndFlush(jobRequestId);
			}

			jobVacancyRepository.deleteByIdAndFlush(jobVacancy.getId());
		}

		Set<Long> jobCompanyLogoIds = jobCompany.getJobCompanyLogoIds();
		Assertions.assertNotNull(jobCompanyLogoIds);
		for(Long jobCompanyLogoId : jobCompanyLogoIds) {
			jobCompanyLogoRepository.deleteByIdAndFlush(jobCompanyLogoId);
		}

		jobCompanyRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobCompanyRepository.deleteByIdAndFlush(null);
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
				jobCompanyRepository.deleteByIdAndFlush(1L);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Cannot delete or update a parent row: a foreign key constraint fails (`job-vacancies-app-db`.`job_company_logo`, CONSTRAINT `job_company_logo_foreign_key_3` FOREIGN KEY (`job_company_id`) REFERENCES `job_company` (`id`))", rootCauseMessage);
	}

	private void commonTestsJobCompany1(JobCompany jobCompany){
		this.commonTestsJobCompany1(jobCompany, "Quality");
	}

	private void commonTestsJobCompany1(JobCompany jobCompany, String name) {
		Assertions.assertNotNull(jobCompany);
		Assertions.assertEquals(1L, jobCompany.getId());
		Assertions.assertEquals(name, jobCompany.getName());
		Assertions.assertEquals("Quality", jobCompany.getDescription());
		Assertions.assertEquals("logo1.png", jobCompany.getSelectedLogoFileName());
		Assertions.assertNotNull(jobCompany.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = jobCompany.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("Quality".equals(name)) {
			Assertions.assertNull(jobCompany.getLastModificationDateTime());
			Assertions.assertNull(jobCompany.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(jobCompany.getLastModificationDateTime());

			AuthUser lastModificationAuthUser = jobCompany.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}

		Set<JobVacancy> jobVacancies = jobCompany.getJobVacancies();
		Assertions.assertNotNull(jobVacancies);
		Assertions.assertEquals(6, jobVacancies.size());

		for(JobVacancy jobVacancy : jobVacancies) {
			Assertions.assertNotNull(jobVacancy);
			Assertions.assertNotNull(jobVacancy.getId());
			Assertions.assertNotNull(jobVacancy.getName());
			Assertions.assertEquals(jobCompany, jobVacancy.getJobCompany());
			Assertions.assertNotNull(jobVacancy.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = jobVacancy.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<JobCompanyLogo> jobCompanyLogos = jobCompany.getJobCompanyLogos();
		Assertions.assertNotNull(jobCompanyLogos);
		Assertions.assertEquals(6, jobCompanyLogos.size());

		for(JobCompanyLogo jobCompanyLogo : jobCompanyLogos) {
			Assertions.assertNotNull(jobCompanyLogo);
			Assertions.assertNotNull(jobCompanyLogo.getId());
			Assertions.assertEquals(jobCompany, jobCompanyLogo.getJobCompany());
			Assertions.assertNotNull(jobCompanyLogo.getFileName());
			Assertions.assertNotNull(jobCompanyLogo.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = jobCompanyLogo.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<Long> jobVacancyIds = jobCompany.getJobVacancyIds();
		Assertions.assertNotNull(jobVacancyIds);
		Assertions.assertEquals(6, jobVacancyIds.size());

		for(Long jobVacancyId : jobVacancyIds) {
			Assertions.assertNotNull(jobVacancyId);
		}

		Set<String> jobVacancyNames = jobCompany.getJobVacancyNames();
		Assertions.assertNotNull(jobVacancyNames);
		Assertions.assertEquals(6, jobVacancyNames.size());

		for(String jobVacancyName : jobVacancyNames) {
			Assertions.assertNotNull(jobVacancyName);
		}

		Set<Long> jobCompanyLogoIds = jobCompany.getJobCompanyLogoIds();
		Assertions.assertNotNull(jobCompanyLogoIds);
		Assertions.assertEquals(6, jobCompanyLogoIds.size());

		for(Long jobCompanyLogoId : jobCompanyLogoIds) {
			Assertions.assertNotNull(jobCompanyLogoId);
		}

		Set<String> jobCompanyLogoSelectionNames = jobCompany.getJobCompanyLogoSelectionNames();
		Assertions.assertNotNull(jobCompanyLogoSelectionNames);
		Assertions.assertEquals(6, jobCompanyLogoSelectionNames.size());

		for(String jobCompanyLogoSelectionName : jobCompanyLogoSelectionNames) {
			Assertions.assertNotNull(jobCompanyLogoSelectionName);
		}

		JobCompanyLogo selectedJobCompanyLogo = jobCompany.getSelectedJobCompanyLogo();
		Assertions.assertNotNull(selectedJobCompanyLogo);
		Assertions.assertNotNull(selectedJobCompanyLogo.getId());
		Assertions.assertEquals(jobCompany, selectedJobCompanyLogo.getJobCompany());
		Assertions.assertNotNull(selectedJobCompanyLogo.getFileName());
		Assertions.assertNotNull(selectedJobCompanyLogo.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser2 = selectedJobCompanyLogo.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser2);
		Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
		Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());

		Assertions.assertNotNull(jobCompany.getSelectedJobCompanyLogoId());
		Assertions.assertNotNull(jobCompany.getSelectedJobCompanyLogoFilePath());
		Assertions.assertNotNull(jobCompany.getSelectedJobCompanyLogoSelectionName());
	}
}
