package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.MainClass;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyLogoDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.JobCompanyConverter;
import com.aliuken.jobvacanciesapp.model.dto.converter.JobCompanyLogoConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
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
import java.util.Set;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainClass.class, BeanFactoryUtils.class, ConfigPropertiesBean.class})
@Sql("classpath:db_dumps/h2-dump.sql")
public class JobCompanyRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
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
		final JobCompany jobCompany = jobCompanyRepository.findByName("Quality");

		this.commonTestsJobCompany1(jobCompany);
	}

	@Test
	public void testFindByName_Null() {
		final JobCompany jobCompany = jobCompanyRepository.findByName(null);

		Assertions.assertNull(jobCompany);
	}

	@Test
	public void testFindByName_NotExists() {
		final JobCompany jobCompany = jobCompanyRepository.findByName("NOT_EXISTING_VALUE");

		Assertions.assertNull(jobCompany);
	}

	@Test
	public void testGetEntityClass_Ok() {
		final Class<JobCompany> jobCompanyClass = jobCompanyRepository.getEntityClass();

		Assertions.assertNotNull(jobCompanyClass);
		Assertions.assertEquals(JobCompany.class, jobCompanyClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final JobCompany jobCompany = jobCompanyRepository.getNewEntityInstance();

		Assertions.assertNotNull(jobCompany);
		Assertions.assertEquals(new JobCompany(), jobCompany);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);

		this.commonTestsJobCompany1(jobCompany);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(null);

		Assertions.assertNull(jobCompany);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(888L);

		Assertions.assertNull(jobCompany);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final JobCompany jobCompany = jobCompanyRepository.findByIdOrNewEntity(1L);

		this.commonTestsJobCompany1(jobCompany);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final JobCompany jobCompany = jobCompanyRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(jobCompany);
		Assertions.assertEquals(new JobCompany(), jobCompany);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final JobCompany jobCompany = jobCompanyRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(jobCompany);
	}

	@Test
	public void testFindAll_Ok() {
		final List<JobCompany> jobCategories = jobCompanyRepository.findAll();

		Assertions.assertNotNull(jobCategories);
		Assertions.assertEquals(23, jobCategories.size());

		for(final JobCompany jobCompany : jobCategories) {
			Assertions.assertNotNull(jobCompany);

			final Long jobCompanyId = jobCompany.getId();

			if(Long.valueOf(1L).equals(jobCompanyId)) {
				this.commonTestsJobCompany1(jobCompany);
			} else {
				Assertions.assertNotNull(jobCompanyId);
				Assertions.assertNotNull(jobCompany.getName());
				Assertions.assertNotNull(jobCompany.getDescription());
				Assertions.assertNotNull(jobCompany.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = jobCompany.getFirstRegistrationAuthUser();
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

		final AuthUser firstRegistrationAuthUser = jobCompany.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(jobCompany.getLastModificationDateTime());
		Assertions.assertNull(jobCompany.getLastModificationAuthUser());

		final Set<JobVacancy> jobVacancies = jobCompany.getJobVacancies();
		Assertions.assertNull(jobVacancies);

		final Set<Long> jobVacancyIds = jobCompany.getJobVacancyIds();
		Assertions.assertNotNull(jobVacancyIds);
		Assertions.assertTrue(jobVacancyIds.isEmpty());

		final Set<String> jobVacancyNames = jobCompany.getJobVacancyNames();
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
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobCompanyRepository.saveAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertNameExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				final JobCompany jobCompany = new JobCompany();
				jobCompany.setName("Quality");
				jobCompany.setDescription("NEW_DESCRIPTION");

				jobCompanyRepository.saveAndFlush(jobCompany);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'Quality' for key 'job_company.job_company_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(2L);
				jobCompany.setName("Quality");

				jobCompany = jobCompanyRepository.saveAndFlush(jobCompany);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry 'Quality' for key 'job_company.job_company_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		final JobCompany jobCompany = jobCompanyRepository.findByIdNotOptional(1L);
		Assertions.assertNotNull(jobCompany);

		final Set<JobVacancy> jobVacancies = jobCompany.getJobVacancies();
		Assertions.assertNotNull(jobVacancies);
		for(final JobVacancy jobVacancy : jobVacancies) {
			Assertions.assertNotNull(jobVacancy);
			final Set<Long> jobRequestIds = jobVacancy.getJobRequestIds();
			Assertions.assertNotNull(jobRequestIds);
			for(final Long jobRequestId : jobRequestIds) {
				jobRequestRepository.deleteByIdAndFlush(jobRequestId);
			}

			jobVacancyRepository.deleteByIdAndFlush(jobVacancy.getId());
		}

		final Set<Long> jobCompanyLogoIds = jobCompany.getJobCompanyLogoIds();
		Assertions.assertNotNull(jobCompanyLogoIds);
		for(final Long jobCompanyLogoId : jobCompanyLogoIds) {
			jobCompanyLogoRepository.deleteByIdAndFlush(jobCompanyLogoId);
		}

		jobCompanyRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobCompanyRepository.deleteByIdAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	@Test
	public void testDeleteById_ForeignKeyViolation() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				jobCompanyRepository.deleteByIdAndFlush(1L);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Cannot delete or update a parent row: a foreign key constraint fails (`job-vacancies-app-db`.`job_company_logo`, CONSTRAINT `job_company_logo_foreign_key_3` FOREIGN KEY (`job_company_id`) REFERENCES `job_company` (`id`))", rootCauseMessage);
	}

	private void commonTestsJobCompany1(final JobCompany jobCompany) {
		this.commonTestsJobCompany1(jobCompany, "Quality");
	}

	private void commonTestsJobCompany1(final JobCompany jobCompany, final String name) {
		Assertions.assertNotNull(jobCompany);
		Assertions.assertEquals(1L, jobCompany.getId());
		Assertions.assertEquals(name, jobCompany.getName());
		Assertions.assertEquals("Quality", jobCompany.getDescription());
		Assertions.assertEquals("logo1.png", jobCompany.getSelectedLogoFileName());
		Assertions.assertNotNull(jobCompany.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = jobCompany.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if("Quality".equals(name)) {
			Assertions.assertNull(jobCompany.getLastModificationDateTime());
			Assertions.assertNull(jobCompany.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(jobCompany.getLastModificationDateTime());

			final AuthUser lastModificationAuthUser = jobCompany.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}

		final Set<JobVacancy> jobVacancies = jobCompany.getJobVacancies();
		Assertions.assertNotNull(jobVacancies);
		Assertions.assertEquals(6, jobVacancies.size());

		for(final JobVacancy jobVacancy : jobVacancies) {
			Assertions.assertNotNull(jobVacancy);
			Assertions.assertNotNull(jobVacancy.getId());
			Assertions.assertNotNull(jobVacancy.getName());
			Assertions.assertEquals(jobCompany, jobVacancy.getJobCompany());
			Assertions.assertNotNull(jobVacancy.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = jobVacancy.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<JobCompanyLogo> jobCompanyLogos = jobCompany.getJobCompanyLogos();
		Assertions.assertNotNull(jobCompanyLogos);
		Assertions.assertEquals(6, jobCompanyLogos.size());

		for(final JobCompanyLogo jobCompanyLogo : jobCompanyLogos) {
			Assertions.assertNotNull(jobCompanyLogo);
			Assertions.assertNotNull(jobCompanyLogo.getId());
			Assertions.assertEquals(jobCompany, jobCompanyLogo.getJobCompany());
			Assertions.assertNotNull(jobCompanyLogo.getFileName());
			Assertions.assertNotNull(jobCompanyLogo.getFirstRegistrationDateTime());

			final AuthUser firstRegistrationAuthUser2 = jobCompanyLogo.getFirstRegistrationAuthUser();
			Assertions.assertNotNull(firstRegistrationAuthUser2);
			Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
			Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());
		}

		final Set<Long> jobVacancyIds = jobCompany.getJobVacancyIds();
		Assertions.assertNotNull(jobVacancyIds);
		Assertions.assertEquals(6, jobVacancyIds.size());

		for(final Long jobVacancyId : jobVacancyIds) {
			Assertions.assertNotNull(jobVacancyId);
		}

		final Set<String> jobVacancyNames = jobCompany.getJobVacancyNames();
		Assertions.assertNotNull(jobVacancyNames);
		Assertions.assertEquals(6, jobVacancyNames.size());

		for(final String jobVacancyName : jobVacancyNames) {
			Assertions.assertNotNull(jobVacancyName);
		}

		final Set<Long> jobCompanyLogoIds = jobCompany.getJobCompanyLogoIds();
		Assertions.assertNotNull(jobCompanyLogoIds);
		Assertions.assertEquals(6, jobCompanyLogoIds.size());

		for(final Long jobCompanyLogoId : jobCompanyLogoIds) {
			Assertions.assertNotNull(jobCompanyLogoId);
		}

		final Set<String> jobCompanyLogoSelectionNames = jobCompany.getJobCompanyLogoSelectionNames();
		Assertions.assertNotNull(jobCompanyLogoSelectionNames);
		Assertions.assertEquals(6, jobCompanyLogoSelectionNames.size());

		for(final String jobCompanyLogoSelectionName : jobCompanyLogoSelectionNames) {
			Assertions.assertNotNull(jobCompanyLogoSelectionName);
		}

		final JobCompanyLogo selectedJobCompanyLogo = jobCompany.getSelectedJobCompanyLogo();
		Assertions.assertNotNull(selectedJobCompanyLogo);
		Assertions.assertNotNull(selectedJobCompanyLogo.getId());
		Assertions.assertEquals(jobCompany, selectedJobCompanyLogo.getJobCompany());
		Assertions.assertNotNull(selectedJobCompanyLogo.getFileName());
		Assertions.assertNotNull(selectedJobCompanyLogo.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser2 = selectedJobCompanyLogo.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser2);
		Assertions.assertNotNull(firstRegistrationAuthUser2.getId());
		Assertions.assertNotNull(firstRegistrationAuthUser2.getEmail());

		Assertions.assertNotNull(jobCompany.getSelectedJobCompanyLogoId());
		Assertions.assertNotNull(jobCompany.getSelectedJobCompanyLogoFilePath());
		Assertions.assertNotNull(jobCompany.getSelectedJobCompanyLogoSelectionName());

		final JobCompanyDTO jobCompanyDTO = JobCompanyConverter.getInstance().convertEntityElement(jobCompany);
		this.commonTestsJobCompanyDTO1(jobCompanyDTO, name, jobCompany.getSelectedJobCompanyLogoId(), jobCompany.getSelectedJobCompanyLogoFilePath(), jobCompany.getJobCompanyLogos());
	}

	private void commonTestsJobCompanyDTO1(final JobCompanyDTO jobCompanyDTO, final String name, final Long selectedJobCompanyLogoId, final String selectedJobCompanyLogoFilePath, final Set<JobCompanyLogo> jobCompanyLogos) {
		Assertions.assertNotNull(jobCompanyDTO);
		Assertions.assertEquals(1L, jobCompanyDTO.getId());
		Assertions.assertEquals(name, jobCompanyDTO.getName());
		Assertions.assertEquals("Quality", jobCompanyDTO.getDescription());
		Assertions.assertEquals(Boolean.TRUE, jobCompanyDTO.getIsSelectedLogo());
		Assertions.assertEquals(selectedJobCompanyLogoId, jobCompanyDTO.getSelectedLogoId());
		Assertions.assertEquals(selectedJobCompanyLogoFilePath, jobCompanyDTO.getSelectedLogoFilePath());

		final Set<JobCompanyLogoDTO> jobCompanyLogoDTOs = JobCompanyLogoConverter.getInstance().convertEntitySet(jobCompanyLogos);
		Assertions.assertEquals(jobCompanyLogoDTOs, jobCompanyDTO.getJobCompanyLogos());
	}
}
