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
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.util.ApplicationContextUtil;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainApplication.class, ApplicationContextUtil.class})
@Sql("classpath:db-dump-for-tests.sql")
public class JobRequestRepositoryTest {
	@Autowired
	private JobRequestRepository jobRequestRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Autowired
	private JobVacancyRepository jobVacancyRepository;

	@Test
	public void testFindByAuthUserAndJobVacancy_Ok() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(11L);

		JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(authUser, jobVacancy);

		this.commonTestsJobRequest1(jobRequest);
	}

	@Test
	public void testFindByAuthUserAndJobVacancy_NullAuthUser() {
		JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(11L);

		JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(null, jobVacancy);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testFindByAuthUserAndJobVacancy_NullJobVacancy() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(authUser, null);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testFindByAuthUserAndJobVacancy_NotExistsAuthUser() {
		AuthUser authUser = new AuthUser();
		authUser.setId(888L);
		authUser.setEmail("new.user@aliuken.com");

		JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(11L);

		JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(authUser, jobVacancy);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testFindByAuthUserAndJobVacancy_NotExistsJobVacancy() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		JobVacancy jobVacancy = new JobVacancy();
		jobVacancy.setId(888L);
		jobVacancy.setName("newVacancy");

		JobRequest jobRequest = jobRequestRepository.findByAuthUserAndJobVacancy(authUser, jobVacancy);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testGetEntityClass_Ok() {
		Class<JobRequest> jobRequestClass = jobRequestRepository.getEntityClass();

		Assertions.assertNotNull(jobRequestClass);
		Assertions.assertEquals(JobRequest.class, jobRequestClass);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		JobRequest jobRequest = jobRequestRepository.findByIdNotOptional(1L);

		this.commonTestsJobRequest1(jobRequest);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		JobRequest jobRequest = jobRequestRepository.findByIdNotOptional(null);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		JobRequest jobRequest = jobRequestRepository.findByIdNotOptional(888L);

		Assertions.assertNull(jobRequest);
	}

	@Test
	public void testFindAll_Ok() {
		List<JobRequest> jobRequests = jobRequestRepository.findAll();

		Assertions.assertNotNull(jobRequests);
		Assertions.assertEquals(10, jobRequests.size());

		for(JobRequest jobRequest : jobRequests) {
			Assertions.assertNotNull(jobRequest);

			Long jobRequestId = jobRequest.getId();

			if(Long.valueOf(1L).equals(jobRequestId)) {
				this.commonTestsJobRequest1(jobRequest);
			} else {
				Assertions.assertNotNull(jobRequestId);

				AuthUser authUser = jobRequest.getAuthUser();
				Assertions.assertNotNull(authUser);
				Assertions.assertNotNull(authUser.getId());
				Assertions.assertNotNull(authUser.getEmail());

				JobVacancy jobVacancy = jobRequest.getJobVacancy();
				Assertions.assertNotNull(jobVacancy);
				Assertions.assertNotNull(jobVacancy.getId());
				Assertions.assertNotNull(jobVacancy.getName());

				Assertions.assertNotNull(jobRequest.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = jobRequest.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testFindByAuthUserAndCurriculumFileName_Ok() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		List<JobRequest> jobRequests = jobRequestRepository.findByAuthUserAndCurriculumFileName(authUser, "EKFP0YBSmiguel.pdf");

		Assertions.assertNotNull(jobRequests);
		Assertions.assertEquals(3, jobRequests.size());

		for(JobRequest jobRequest : jobRequests) {
			Assertions.assertNotNull(jobRequest);

			Long jobRequestId = jobRequest.getId();

			if(Long.valueOf(1L).equals(jobRequestId)) {
				this.commonTestsJobRequest1(jobRequest);
			} else {
				Assertions.assertNotNull(jobRequestId);

				Assertions.assertEquals(authUser, jobRequest.getAuthUser());

				JobVacancy jobVacancy = jobRequest.getJobVacancy();
				Assertions.assertNotNull(jobVacancy);
				Assertions.assertNotNull(jobVacancy.getId());
				Assertions.assertNotNull(jobVacancy.getName());

				Assertions.assertNotNull(jobRequest.getFirstRegistrationDateTime());

				AuthUser firstRegistrationAuthUser = jobRequest.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testFindByAuthUserAndCurriculumFileName_NullAuthUser() {
		List<JobRequest> jobRequests = jobRequestRepository.findByAuthUserAndCurriculumFileName(null, "EKFP0YBSmiguel.pdf");

		Assertions.assertNotNull(jobRequests);
		Assertions.assertTrue(jobRequests.isEmpty());
	}

	@Test
	public void testFindByAuthUserAndCurriculumFileName_NullCurriculumFileName() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		List<JobRequest> jobRequests = jobRequestRepository.findByAuthUserAndCurriculumFileName(authUser, null);

		Assertions.assertNotNull(jobRequests);
		Assertions.assertTrue(jobRequests.isEmpty());
	}

	@Test
	public void testFindByAuthUserAndCurriculumFileName_NotExists() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		List<JobRequest> jobRequests = jobRequestRepository.findByAuthUserAndCurriculumFileName(authUser, "NOT_EXISTING_FILE.pdf");

		Assertions.assertNotNull(jobRequests);
		Assertions.assertTrue(jobRequests.isEmpty());
	}

	@Test
	public void testSave_InsertOk() {
		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
		Assertions.assertNotNull(authUser);

		JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(6L);
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

		AuthUser firstRegistrationAuthUser = jobRequest.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(jobRequest.getLastModificationDateTime());
		Assertions.assertNull(jobRequest.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(6L);
		Assertions.assertNotNull(jobVacancy);

		JobRequest jobRequest = jobRequestRepository.findByIdNotOptional(1L);
		jobRequest.setJobVacancy(jobVacancy);

		jobRequest = jobRequestRepository.saveAndFlush(jobRequest);

		this.commonTestsJobRequest1(jobRequest, jobVacancy);
	}

	@Test
	public void testSave_Null() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobRequestRepository.saveAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testSave_InsertExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				AuthUser authUser = authUserRepository.findByIdNotOptional(2L);
				JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(11L);

				JobRequest jobRequest = new JobRequest();
				jobRequest.setAuthUser(authUser);
				jobRequest.setJobVacancy(jobVacancy);
				jobRequest.setComments("comments");
				jobRequest.setCurriculumFileName("ExampleCV2.docx");

				jobRequestRepository.saveAndFlush(jobRequest);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry '2-11' for key 'job_request.job_request_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testSave_UpdateNameExists() {
		DataIntegrityViolationException exception = Assertions.assertThrows(
			DataIntegrityViolationException.class, () -> {
				JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(1L);

				JobRequest jobRequest = jobRequestRepository.findByIdNotOptional(1L);
				jobRequest.setJobVacancy(jobVacancy);

				jobRequest = jobRequestRepository.saveAndFlush(jobRequest);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("Duplicate entry '2-1' for key 'job_request.job_request_unique_key_1'", rootCauseMessage);
	}

	@Test
	public void testDeleteById_Ok() {
		jobRequestRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				jobRequestRepository.deleteByIdAndFlush(null);
			}
		);

		String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsJobRequest1(JobRequest jobRequest){
		JobVacancy jobVacancy = jobVacancyRepository.findByIdNotOptional(11L);

		this.commonTestsJobRequest1(jobRequest, jobVacancy);
	}

	private void commonTestsJobRequest1(JobRequest jobRequest, JobVacancy jobVacancy) {
		Assertions.assertNotNull(jobRequest);
		Assertions.assertNotNull(jobVacancy);

		AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		Assertions.assertEquals(1L, jobRequest.getId());
		Assertions.assertEquals(authUser, jobRequest.getAuthUser());
		Assertions.assertEquals(jobVacancy, jobRequest.getJobVacancy());
		Assertions.assertEquals("1. Buenas tardes. Envío mi Curriculum Vitae para aplicar para esta oferta de trabajo!", jobRequest.getComments());
		Assertions.assertEquals("EKFP0YBSmiguel.pdf", jobRequest.getCurriculumFileName());
		Assertions.assertNotNull(jobRequest.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = jobRequest.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		if(Long.valueOf(11L).equals(jobVacancy.getId())) {
			Assertions.assertNull(jobRequest.getLastModificationDateTime());
			Assertions.assertNull(jobRequest.getLastModificationAuthUser());
		} else {
			Assertions.assertNotNull(jobRequest.getLastModificationDateTime());

			AuthUser lastModificationAuthUser = jobRequest.getLastModificationAuthUser();
			Assertions.assertNotNull(lastModificationAuthUser);
			Assertions.assertNotNull(lastModificationAuthUser.getId());
			Assertions.assertNotNull(lastModificationAuthUser.getEmail());
		}
	}
}
