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
import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.util.ApplicationContextUtil;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainApplication.class, ApplicationContextUtil.class})
@Sql("classpath:db-dump-for-tests.sql")
public class JobCategoryRepositoryTest {
	@Autowired
	private JobCategoryRepository jobCategoryRepository;

	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Test
	public void testFindByName_Ok() {
		JobCategory jobCategory = jobCategoryRepository.findByName("Arquitectura");

		this.commonTestsJobCategory1(jobCategory);
	}

	@Test
	public void testFindByName_Null() {
		JobCategory jobCategory = jobCategoryRepository.findByName(null);

		Assertions.assertNull(jobCategory);
	}

	@Test
	public void testFindByName_NotExists() {
		JobCategory jobCategory = jobCategoryRepository.findByName("NOT_EXISTING_VALUE");

		Assertions.assertNull(jobCategory);
	}

	@Test
	public void testGetEntityClass_Ok() {
		Class<JobCategory> jobCategoryClass = jobCategoryRepository.getEntityClass();

		Assertions.assertNotNull(jobCategoryClass);
		Assertions.assertEquals(JobCategory.class, jobCategoryClass);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		JobCategory jobCategory = jobCategoryRepository.findByIdNotOptional(1L);

		this.commonTestsJobCategory1(jobCategory);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		JobCategory jobCategory = jobCategoryRepository.findByIdNotOptional(null);

		Assertions.assertNull(jobCategory);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		JobCategory jobCategory = jobCategoryRepository.findByIdNotOptional(888L);

		Assertions.assertNull(jobCategory);
	}

	@Test
	public void testFindAll_Ok() {
		List<JobCategory> jobCategories = jobCategoryRepository.findAll();

		Assertions.assertNotNull(jobCategories);
		Assertions.assertEquals(16, jobCategories.size());

		for(JobCategory jobCategory : jobCategories) {
			Assertions.assertNotNull(jobCategory);

			Long jobCategoryId = jobCategory.getId();

			if(Long.valueOf(1L).equals(jobCategoryId)) {
				this.commonTestsJobCategory1(jobCategory);
			} else {
				Assertions.assertNotNull(jobCategoryId);
				Assertions.assertNotNull(jobCategory.getName());
				Assertions.assertNotNull(jobCategory.getDescription());
				Assertions.assertNotNull(jobCategory.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = jobCategory.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		JobCategory jobCategory = new JobCategory();
		jobCategory.setName("NEW_NAME");
		jobCategory.setDescription("NEW_DESCRIPTION");

		jobCategory = jobCategoryRepository.saveAndFlush(jobCategory);

		Assertions.assertNotNull(jobCategory);
		Assertions.assertNotNull(jobCategory.getId());
		Assertions.assertEquals("NEW_NAME", jobCategory.getName());
		Assertions.assertEquals("NEW_DESCRIPTION", jobCategory.getDescription());
		Assertions.assertNotNull(jobCategory.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = jobCategory.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(jobCategory.getLastModificationDateTime());
		Assertions.assertNull(jobCategory.getLastModificationAuthUser());

		Set<JobVacancy> jobVacancies = jobCategory.getJobVacancies();
		Assertions.assertNull(jobVacancies);

		Set<Long> jobVacancyIds = jobCategory.getJobVacancyIds();
		Assertions.assertNotNull(jobVacancyIds);
		Assertions.assertTrue(jobVacancyIds.isEmpty());

		Set<String> jobVacancyNames = jobCategory.getJobVacancyNames();
		Assertions.assertNotNull(jobVacancyNames);
		Assertions.assertTrue(jobVacancyNames.isEmpty());
	}

	@Test
	public void testSave_UpdateOk() {
		JobCategory jobCategory = jobCategoryRepository.findByIdNotOptional(1L);
		jobCategory.setName("NEW_NAME");

		jobCategory = jobCategoryRepository.saveAndFlush(jobCategory);

		this.commonTestsJobCategory1(jobCategory, "NEW_NAME");
	}

	@Test
	public void testSave_Null() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobCategoryRepository.saveAndFlush(null);
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
				JobCategory jobCategory = new JobCategory();
				jobCategory.setName("Arquitectura");
				jobCategory.setDescription("NEW_DESCRIPTION");

				jobCategoryRepository.saveAndFlush(jobCategory);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'Arquitectura' for key 'job_category.job_category_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				JobCategory jobCategory = jobCategoryRepository.findByIdNotOptional(2L);
				jobCategory.setName("Arquitectura");

				jobCategory = jobCategoryRepository.saveAndFlush(jobCategory);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry 'Arquitectura' for key 'job_category.job_category_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		JobCategory jobCategory = jobCategoryRepository.findByIdNotOptional(1L);
		Assertions.assertNotNull(jobCategory);

		Set<Long> jobVacancyIds = jobCategory.getJobVacancyIds();
		Assertions.assertNotNull(jobVacancyIds);
		for(Long jobVacancyId : jobVacancyIds) {
			jobVacancyRepository.deleteByIdAndFlush(jobVacancyId);
		}

		jobCategoryRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobCategoryRepository.deleteByIdAndFlush(null);
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
				jobCategoryRepository.deleteByIdAndFlush(1L);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Cannot delete or update a parent row: a foreign key constraint fails (`job-vacancies-app-db`.`job_vacancy`, CONSTRAINT `job_vacancy_foreign_key_4` FOREIGN KEY (`job_category_id`) REFERENCES `job_category` (`id`))", rootCauseMessage);
	}

	private void commonTestsJobCategory1(JobCategory jobCategory){
		this.commonTestsJobCategory1(jobCategory, "Arquitectura");
	}

	private void commonTestsJobCategory1(JobCategory jobCategory, String name) {
		Assertions.assertNotNull(jobCategory);
		Assertions.assertEquals(1L, jobCategory.getId());
		Assertions.assertEquals(name, jobCategory.getName());
		Assertions.assertEquals("Habilidades para diseñar, dirigir y construir proyectos arquitectónicos, que pueden ir desde diseños en pequeña escala (como casas), hasta gran escala (como el planeamiento de una ciudad).", jobCategory.getDescription());
		Assertions.assertNotNull(jobCategory.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = jobCategory.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("Arquitectura".equals(name)) {
			Assertions.assertNull(jobCategory.getLastModificationDateTime());
			Assertions.assertNull(jobCategory.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(jobCategory.getLastModificationDateTime());

			AuthUser lastModificationAuthUser = jobCategory.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}

		Set<JobVacancy> jobVacancies = jobCategory.getJobVacancies();
		Assertions.assertNotNull(jobVacancies);
		Assertions.assertEquals(6, jobVacancies.size());

		for(JobVacancy jobVacancy : jobVacancies) {
			Assertions.assertNotNull(jobVacancy);
			Assertions.assertNotNull(jobVacancy.getId());
			Assertions.assertNotNull(jobVacancy.getName());
			Assertions.assertEquals(jobCategory, jobVacancy.getJobCategory());
			Assertions.assertNotNull(jobVacancy.getFirstRegistrationDateTime());

			AuthUser firstRegistrationAuthUser2 = jobVacancy.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		Set<Long> jobVacancyIds = jobCategory.getJobVacancyIds();
		Assertions.assertNotNull(jobVacancyIds);
		Assertions.assertEquals(6, jobVacancyIds.size());

		for(Long jobVacancyId : jobVacancyIds) {
			Assertions.assertNotNull(jobVacancyId);
		}

		Set<String> jobVacancyNames = jobCategory.getJobVacancyNames();
		Assertions.assertNotNull(jobVacancyNames);
		Assertions.assertEquals(6, jobVacancyNames.size());

		for(String jobVacancyName : jobVacancyNames) {
			Assertions.assertNotNull(jobVacancyName);
		}
	}
}
