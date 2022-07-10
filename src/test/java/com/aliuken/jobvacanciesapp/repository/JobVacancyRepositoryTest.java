package com.aliuken.jobvacanciesapp.repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.util.ApplicationContextUtil;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainApplication.class, ApplicationContextUtil.class})
@Sql("classpath:db-dump-for-tests.sql")
public class JobVacancyRepositoryTest {
	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Autowired
	private JobCompanyRepository jobCompanyRepository;

	@Autowired
	private JobCategoryRepository jobCategoryRepository;

	@Autowired
	private JobRequestRepository jobRequestRepository;

	@Test
	public void testFindByHighlightedAndStatusOrderByIdDesc_Ok() {
		List<JobVacancy> jobVacancies = jobVacancyRepository.findByHighlightedAndStatusOrderByIdDesc(false, JobVacancyStatus.CREATED);

		Assertions.assertNotNull(jobVacancies);
		Assertions.assertEquals(5, jobVacancies.size());

		for(JobVacancy jobVacancy : jobVacancies) {
			Assertions.assertNotNull(jobVacancy);

			Long jobVacancyId = jobVacancy.getId();

			if(Long.valueOf(1L).equals(jobVacancyId)) {
				this.commonTestsJobVacancy1(jobVacancy);
			} else {
				Assertions.assertNotNull(jobVacancyId);
				Assertions.assertNotNull(jobVacancy.getName());
				Assertions.assertNotNull(jobVacancy.getDescription());
				Assertions.assertNotNull(jobVacancy.getStatus());
				Assertions.assertNotNull(jobVacancy.getHighlighted());
				Assertions.assertNotNull(jobVacancy.getJobCompany());
				Assertions.assertNotNull(jobVacancy.getJobCategory());
				Assertions.assertNotNull(jobVacancy.getPublicationDateTime());
				Assertions.assertNotNull(jobVacancy.getDetails());
				Assertions.assertNotNull(jobVacancy.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = jobVacancy.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testFindByHighlightedAndStatusOrderByIdDesc_NullStatus() {
		List<JobVacancy> jobVacancies = jobVacancyRepository.findByHighlightedAndStatusOrderByIdDesc(false, null);

		Assertions.assertNotNull(jobVacancies);
		Assertions.assertTrue(jobVacancies.isEmpty());
	}

	@Test
	public void testGetEntityClass_Ok() {
		Class<JobVacancy> jobVacancyClass = jobVacancyRepository.getEntityClass();

		Assertions.assertNotNull(jobVacancyClass);
		Assertions.assertEquals(JobVacancy.class, jobVacancyClass);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(1L);

		this.commonTestsJobVacancy1(jobVacancy);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(null);

		Assertions.assertNull(jobVacancy);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(888L);

		Assertions.assertNull(jobVacancy);
	}

	@Test
	public void testFindAll_Ok() {
		List<JobVacancy> jobVacancies = jobVacancyRepository.findAll();

		Assertions.assertNotNull(jobVacancies);
		Assertions.assertEquals(16, jobVacancies.size());

		for(JobVacancy jobVacancy : jobVacancies) {
			Assertions.assertNotNull(jobVacancy);

			Long jobVacancyId = jobVacancy.getId();

			if(Long.valueOf(1L).equals(jobVacancyId)) {
				this.commonTestsJobVacancy1(jobVacancy);
			} else {
				Assertions.assertNotNull(jobVacancyId);
				Assertions.assertNotNull(jobVacancy.getName());
				Assertions.assertNotNull(jobVacancy.getDescription());
				Assertions.assertNotNull(jobVacancy.getStatus());
				Assertions.assertNotNull(jobVacancy.getHighlighted());
				Assertions.assertNotNull(jobVacancy.getJobCompany());
				Assertions.assertNotNull(jobVacancy.getJobCategory());
				Assertions.assertNotNull(jobVacancy.getPublicationDateTime());
				Assertions.assertNotNull(jobVacancy.getDetails());
				Assertions.assertNotNull(jobVacancy.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = jobVacancy.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);
		JobCategory jobCategory = jobCategoryRepository.findByIdNotOptional(1L);

		JobVacancy jobVacancy = new JobVacancy();
		jobVacancy.setName("Name 6");
		jobVacancy.setDescription("Description 6");
		jobVacancy.setSalary(BigDecimal.valueOf(16506L));
		jobVacancy.setStatus(JobVacancyStatus.CREATED);
		jobVacancy.setHighlighted(Boolean.FALSE);
		jobVacancy.setJobCompany(jobCompany);
		jobVacancy.setJobCategory(jobCategory);
		jobVacancy.setPublicationDateTime(LocalDateTime.now());
		jobVacancy.setDetails("Details 6");

		jobVacancy = jobVacancyRepository.saveAndFlush(jobVacancy);

		Assertions.assertNotNull(jobVacancy);
		Assertions.assertNotNull(jobVacancy.getId());
		Assertions.assertEquals("Name 6", jobVacancy.getName());
		Assertions.assertEquals("Description 6", jobVacancy.getDescription());
		Assertions.assertEquals(BigDecimal.valueOf(16506L), jobVacancy.getSalary());
		Assertions.assertEquals(JobVacancyStatus.CREATED, jobVacancy.getStatus());
		Assertions.assertEquals(Boolean.FALSE, jobVacancy.getHighlighted());
		Assertions.assertEquals(jobCompany, jobVacancy.getJobCompany());
		Assertions.assertEquals(jobCategory, jobVacancy.getJobCategory());
		Assertions.assertNotNull(jobVacancy.getPublicationDateTime());
		Assertions.assertEquals("Details 6", jobVacancy.getDetails());
		Assertions.assertNotNull(jobVacancy.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = jobVacancy.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(jobVacancy.getLastModificationDateTime());
		Assertions.assertNull(jobVacancy.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(1L);
		jobVacancy.setName("New name");

		jobVacancy = jobVacancyRepository.saveAndFlush(jobVacancy);

		this.commonTestsJobVacancy1(jobVacancy, "New name");
	}

	@Test
	public void testSave_Null() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobVacancyRepository.saveAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testDeleteById_Ok() {
		JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(1L);

		Set<Long> jobRequestIds = jobVacancy.getJobRequestIds();
		Assertions.assertNotNull(jobRequestIds);
		for(Long jobRequestId : jobRequestIds) {
			Assertions.assertNotNull(jobRequestId);
			jobRequestRepository.deleteByIdAndFlush(jobRequestId);
		}

		jobVacancyRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobVacancyRepository.deleteByIdAndFlush(null);
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
				jobVacancyRepository.deleteByIdAndFlush(1L);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Cannot delete or update a parent row: a foreign key constraint fails (`job-vacancies-app-db`.`job_request`, CONSTRAINT `job_request_foreign_key_4` FOREIGN KEY (`job_vacancy_id`) REFERENCES `job_vacancy` (`id`))", rootCauseMessage);
	}

	private void commonTestsJobVacancy1(JobVacancy jobVacancy) {
		this.commonTestsJobVacancy1(jobVacancy, "Administrador de datos en la nube");
	}

	private void commonTestsJobVacancy1(JobVacancy jobVacancy, String name) {
		JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(2L);
		JobCategory jobCategory = jobCategoryRepository.findByIdNotOptional(3L);

		Assertions.assertNotNull(jobVacancy);
		Assertions.assertNotNull(jobCompany);
		Assertions.assertEquals(1L, jobVacancy.getId());
		Assertions.assertEquals(name, jobVacancy.getName());
		Assertions.assertEquals("Únete al Socio Logístico con mayor presencia en México, Soft Technologies te invita a formar parte de su gran equipo de trabajo como Administrador de datos en la nube.", jobVacancy.getDescription());
		Assertions.assertEquals(new BigDecimal("20000.00"), jobVacancy.getSalary());
		Assertions.assertEquals(JobVacancyStatus.APPROVED, jobVacancy.getStatus());
		Assertions.assertEquals(false, jobVacancy.getHighlighted());
		Assertions.assertEquals(jobCompany, jobVacancy.getJobCompany());
		Assertions.assertEquals(jobCategory, jobVacancy.getJobCategory());
		Assertions.assertNotNull(jobVacancy.getPublicationDateTime());
		Assertions.assertNotNull(jobVacancy.getDetails());

		Assertions.assertNotNull(jobVacancy.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = jobVacancy.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("Administrador de datos en la nube".equals(jobVacancy.getName())) {
			Assertions.assertNull(jobVacancy.getLastModificationDateTime());
			Assertions.assertNull(jobVacancy.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(jobVacancy.getLastModificationDateTime());

			AuthUser lastModificationAuthUser = jobVacancy.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}
	}
}
