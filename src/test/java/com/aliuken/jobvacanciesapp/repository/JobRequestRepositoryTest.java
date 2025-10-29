package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.MainClass;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobRequestDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobVacancyDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.AuthUserConverter;
import com.aliuken.jobvacanciesapp.model.dto.converter.JobRequestConverter;
import com.aliuken.jobvacanciesapp.model.dto.converter.JobVacancyConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
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

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainClass.class, BeanFactoryUtils.class, ConfigPropertiesBean.class})
@Sql("classpath:db_dumps/h2-dump.sql")
public class JobRequestRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private JobRequestRepository jobRequestRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Test
	public void testFindByAuthUserAndJobVacancy_Ok() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		final JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(11L);

		final JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(authUser, jobVacancy);

		this.commonTestsJobRequest1(jobRequest);
	}

	@Test
	public void testFindByAuthUserAndJobVacancy_NullAuthUser() {
		final JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(11L);

		final JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(null, jobVacancy);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testFindByAuthUserAndJobVacancy_NullJobVacancy() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		final JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(authUser, null);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testFindByAuthUserAndJobVacancy_NotExistsAuthUser() {
		final AuthUser authUser = new AuthUser();
		authUser.setId(888L);
		authUser.setEmail("new.user@aliuken.com");

		final JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(11L);

		final JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(authUser, jobVacancy);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testFindByAuthUserAndJobVacancy_NotExistsJobVacancy() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		final JobVacancy jobVacancy = new JobVacancy();
		jobVacancy.setId(888L);
		jobVacancy.setName("newVacancy");

		final JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(authUser, jobVacancy);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testGetEntityClass_Ok() {
		final Class<JobRequest> jobRequestClass = jobRequestRepository.getEntityClass();

		Assertions.assertNotNull(jobRequestClass);
		Assertions.assertEquals(JobRequest.class, jobRequestClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final JobRequest jobRequest = jobRequestRepository.getNewEntityInstance();

		Assertions.assertNotNull(jobRequest);
		Assertions.assertEquals(new JobRequest(), jobRequest);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final JobRequest jobRequest = jobRequestRepository.findByIdNotOptional(1L);

		this.commonTestsJobRequest1(jobRequest);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final JobRequest jobRequest = jobRequestRepository.findByIdNotOptional(null);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final JobRequest jobRequest = jobRequestRepository.findByIdNotOptional(888L);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final JobRequest jobRequest = jobRequestRepository.findByIdOrNewEntity(1L);

		this.commonTestsJobRequest1(jobRequest);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final JobRequest jobRequest = jobRequestRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(jobRequest);
		Assertions.assertEquals(new JobRequest(), jobRequest);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final JobRequest jobRequest = jobRequestRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testFindAll_Ok() {
		final List<JobRequest> jobRequests = jobRequestRepository.findAll();

		Assertions.assertNotNull(jobRequests);
		Assertions.assertEquals(11, jobRequests.size());

		for(final JobRequest jobRequest : jobRequests) {
			Assertions.assertNotNull(jobRequest);

			final Long jobRequestId = jobRequest.getId();

			if(Long.valueOf(1L).equals(jobRequestId)) {
				this.commonTestsJobRequest1(jobRequest);
			} else {
				Assertions.assertNotNull(jobRequestId);

				final AuthUser authUser = jobRequest.getAuthUser();
				Assertions.assertNotNull(authUser);
				Assertions.assertNotNull(authUser.getId());
				Assertions.assertNotNull(authUser.getEmail());

				final JobVacancy jobVacancy = jobRequest.getJobVacancy();
				Assertions.assertNotNull(jobVacancy);
				Assertions.assertNotNull(jobVacancy.getId());
				Assertions.assertNotNull(jobVacancy.getName());

				Assertions.assertNotNull(jobRequest.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = jobRequest.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testFindByAuthUserAndCurriculumFileName_Ok() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		final List<JobRequest> jobRequests = jobRequestRepository.findByAuthUserAndCurriculumFileName(authUser, "EKFP0YBSmiguel.pdf");

		Assertions.assertNotNull(jobRequests);
		Assertions.assertEquals(3, jobRequests.size());

		for(final JobRequest jobRequest : jobRequests) {
			Assertions.assertNotNull(jobRequest);

			final Long jobRequestId = jobRequest.getId();

			if(Long.valueOf(1L).equals(jobRequestId)) {
				this.commonTestsJobRequest1(jobRequest);
			} else {
				Assertions.assertNotNull(jobRequestId);

				Assertions.assertEquals(authUser, jobRequest.getAuthUser());

				final JobVacancy jobVacancy = jobRequest.getJobVacancy();
				Assertions.assertNotNull(jobVacancy);
				Assertions.assertNotNull(jobVacancy.getId());
				Assertions.assertNotNull(jobVacancy.getName());

				Assertions.assertNotNull(jobRequest.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = jobRequest.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testFindByAuthUserAndCurriculumFileName_NullAuthUser() {
		final List<JobRequest> jobRequests = jobRequestRepository.findByAuthUserAndCurriculumFileName(null, "EKFP0YBSmiguel.pdf");

		Assertions.assertNotNull(jobRequests);
		Assertions.assertTrue(jobRequests.isEmpty());
	}

	@Test
	public void testFindByAuthUserAndCurriculumFileName_NullCurriculumFileName() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		final List<JobRequest> jobRequests = jobRequestRepository.findByAuthUserAndCurriculumFileName(authUser, null);

		Assertions.assertNotNull(jobRequests);
		Assertions.assertTrue(jobRequests.isEmpty());
	}

	@Test
	public void testFindByAuthUserAndCurriculumFileName_NotExists() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		final List<JobRequest> jobRequests = jobRequestRepository.findByAuthUserAndCurriculumFileName(authUser, "NOT_EXISTING_FILE.pdf");

		Assertions.assertNotNull(jobRequests);
		Assertions.assertTrue(jobRequests.isEmpty());
	}

	@Test
	public void testSave_InsertOk() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		Assertions.assertNotNull(authUser);

		final JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(6L);
		Assertions.assertNotNull(jobVacancy);

		JobRequest jobRequest = new JobRequest();
		jobRequest.setAuthUser(authUser);
		jobRequest.setJobVacancy(jobVacancy);
		jobRequest.setComments("comments");
		jobRequest.setCurriculumFileName("ExampleCV2.docx");

		jobRequest = jobRequestRepository.saveAndFlush(jobRequest);

		Assertions.assertNotNull(jobRequest);
		Assertions.assertNotNull(jobRequest.getId());
		Assertions.assertEquals(authUser, jobRequest.getAuthUser());
		Assertions.assertEquals(jobVacancy, jobRequest.getJobVacancy());
		Assertions.assertEquals("comments", jobRequest.getComments());
		Assertions.assertEquals("ExampleCV2.docx", jobRequest.getCurriculumFileName());
		Assertions.assertNotNull(jobRequest.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = jobRequest.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(jobRequest.getLastModificationDateTime());
		Assertions.assertNull(jobRequest.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		final JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(6L);
		Assertions.assertNotNull(jobVacancy);

		JobRequest jobRequest = jobRequestRepository.findByIdNotOptional(1L);
		jobRequest.setJobVacancy(jobVacancy);

		jobRequest = jobRequestRepository.saveAndFlush(jobRequest);

		this.commonTestsJobRequest1(jobRequest, jobVacancy);
	}

	@Test
	public void testSave_Null() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobRequestRepository.saveAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
				final JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(11L);

				final JobRequest jobRequest = new JobRequest();
				jobRequest.setAuthUser(authUser);
				jobRequest.setJobVacancy(jobVacancy);
				jobRequest.setComments("comments");
				jobRequest.setCurriculumFileName("ExampleCV2.docx");

				jobRequestRepository.saveAndFlush(jobRequest);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry '2-11' for key 'job_request.job_request_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		final DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				final JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(1L);

				JobRequest jobRequest = jobRequestRepository.findByIdNotOptional(1L);
				jobRequest.setJobVacancy(jobVacancy);

				jobRequest = jobRequestRepository.saveAndFlush(jobRequest);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
//		Assertions.assertEquals("Duplicate entry '2-1' for key 'job_request.job_request_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		jobRequestRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobRequestRepository.deleteByIdAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsJobRequest1(final JobRequest jobRequest) {
		final JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(11L);

		this.commonTestsJobRequest1(jobRequest, jobVacancy);
	}

	private void commonTestsJobRequest1(final JobRequest jobRequest, final JobVacancy jobVacancy) {
		Assertions.assertNotNull(jobRequest);
		Assertions.assertNotNull(jobVacancy);

		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		Assertions.assertEquals(1L, jobRequest.getId());
		Assertions.assertEquals(authUser, jobRequest.getAuthUser());
		Assertions.assertEquals(jobVacancy, jobRequest.getJobVacancy());
		Assertions.assertEquals("1. Buenas tardes. Envío mi Curriculum Vitae para aplicar para esta oferta de trabajo!", jobRequest.getComments());
		Assertions.assertEquals("EKFP0YBSmiguel.pdf", jobRequest.getCurriculumFileName());
		Assertions.assertNotNull(jobRequest.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = jobRequest.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if(Long.valueOf(11L).equals(jobVacancy.getId())) {
			Assertions.assertNull(jobRequest.getLastModificationDateTime());
			Assertions.assertNull(jobRequest.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(jobRequest.getLastModificationDateTime());

			final AuthUser lastModificationAuthUser = jobRequest.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}

		final JobRequestDTO jobRequestDTO = JobRequestConverter.getInstance().convertEntityElement(jobRequest);
		this.commonTestsJobRequestDTO1(jobRequestDTO, jobVacancy);
	}

	private void commonTestsJobRequestDTO1(final JobRequestDTO jobRequestDTO, final JobVacancy jobVacancy) {
		Assertions.assertNotNull(jobRequestDTO);
		Assertions.assertNotNull(jobVacancy);

		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		final AuthUserDTO authUserDTO = AuthUserConverter.getInstance().convertEntityElement(authUser);

		final JobVacancyDTO jobVacancyDTO = JobVacancyConverter.getInstance().convertEntityElement(jobVacancy);
		Assertions.assertNotNull(jobVacancyDTO);
		Assertions.assertNotNull(jobVacancyDTO.getId());

		Assertions.assertEquals(1L, jobRequestDTO.getId());
		Assertions.assertEquals(authUserDTO, jobRequestDTO.getAuthUser());
		Assertions.assertEquals(jobVacancyDTO, jobRequestDTO.getJobVacancy());
		Assertions.assertEquals(jobVacancyDTO.getId(), jobRequestDTO.getJobVacancyId());
		Assertions.assertEquals("1. Buenas tardes. Envío mi Curriculum Vitae para aplicar para esta oferta de trabajo!", jobRequestDTO.getComments());
		Assertions.assertEquals("EKFP0YBSmiguel.pdf", jobRequestDTO.getCurriculumFileName());
	}
}
