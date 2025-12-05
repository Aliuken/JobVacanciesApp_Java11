package com.aliuken.jobvacanciesapp.repository;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.MainClass;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserEntityQuery;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.spring.di.BeanFactoryUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
public class AuthUserEntityQueryRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
	@Autowired
	private AuthUserEntityQueryRepository authUserEntityQueryRepository;

	@Autowired
	private AuthUserRepository authUserRepository;

	@Test
	public void testGetEntityClass_Ok() {
		final Class<AuthUserEntityQuery> authUserEntityQueryClass = authUserEntityQueryRepository.getEntityClass();

		Assertions.assertNotNull(authUserEntityQueryClass);
		Assertions.assertEquals(AuthUserEntityQuery.class, authUserEntityQueryClass);
	}

	@Test
	public void testGetNewEntityInstance_Ok() {
		final AuthUserEntityQuery authUserEntityQuery = authUserEntityQueryRepository.getNewEntityInstance();

		Assertions.assertNotNull(authUserEntityQuery);
		Assertions.assertEquals(new AuthUserEntityQuery(), authUserEntityQuery);
	}

	@Test
	public void testFindByIdNotOptional_Ok() {
		final AuthUserEntityQuery authUserEntityQuery = authUserEntityQueryRepository.findByIdNotOptional(1L);

		this.commonTestsAuthUserEntityQuery1(authUserEntityQuery);
	}

	@Test
	public void testFindByIdNotOptional_Null() {
		final AuthUserEntityQuery authUserEntityQuery = authUserEntityQueryRepository.findByIdNotOptional(null);

		Assertions.assertNull(authUserEntityQuery);
	}

	@Test
	public void testFindByIdNotOptional_NotExists() {
		final AuthUserEntityQuery authUserEntityQuery = authUserEntityQueryRepository.findByIdNotOptional(888L);

		Assertions.assertNull(authUserEntityQuery);
	}

	@Test
	public void testFindByIdOrNewEntity_Ok() {
		final AuthUserEntityQuery authUserEntityQuery = authUserEntityQueryRepository.findByIdOrNewEntity(1L);

		this.commonTestsAuthUserEntityQuery1(authUserEntityQuery);
	}

	@Test
	public void testFindByIdOrNewEntity_Null() {
		final AuthUserEntityQuery authUserEntityQuery = authUserEntityQueryRepository.findByIdOrNewEntity(null);

		Assertions.assertNotNull(authUserEntityQuery);
		Assertions.assertEquals(new AuthUserEntityQuery(), authUserEntityQuery);
	}

	@Test
	public void testFindByIdOrNewEntity_NotExists() {
		final AuthUserEntityQuery authUserEntityQuery = authUserEntityQueryRepository.findByIdOrNewEntity(888L);

		Assertions.assertNull(authUserEntityQuery);
	}

	@Test
	public void testFindAll_Ok() {
		final List<AuthUserEntityQuery> authUserEntityQuerys = authUserEntityQueryRepository.findAll();

		Assertions.assertNotNull(authUserEntityQuerys);
		Assertions.assertEquals(45, authUserEntityQuerys.size());

		for(final AuthUserEntityQuery authUserEntityQuery : authUserEntityQuerys) {
			Assertions.assertNotNull(authUserEntityQuery);

			final Long authUserEntityQueryId = authUserEntityQuery.getId();

			if(Long.valueOf(1L).equals(authUserEntityQueryId)) {
				this.commonTestsAuthUserEntityQuery1(authUserEntityQuery);
			} else {
				Assertions.assertNotNull(authUserEntityQueryId);
				Assertions.assertNotNull(authUserEntityQuery.getAuthUser());
				Assertions.assertNotNull(authUserEntityQuery.getPageEntity());
				Assertions.assertNotNull(authUserEntityQuery.getInitialPdfDocumentPageFormat());
				Assertions.assertNotNull(authUserEntityQuery.getFinalPdfDocumentPageFormat());
				Assertions.assertNotNull(authUserEntityQuery.getLanguage());
				Assertions.assertNotNull(authUserEntityQuery.getTableSortingField());
				Assertions.assertNotNull(authUserEntityQuery.getTableSortingDirection());
				Assertions.assertNotNull(authUserEntityQuery.getTablePageSize());
				Assertions.assertNotNull(authUserEntityQuery.getPageNumber());
				Assertions.assertNotNull(authUserEntityQuery.getQueryUrl());
				Assertions.assertNotNull(authUserEntityQuery.getFirstRegistrationDateTime());

				final AuthUser firstRegistrationAuthUser = authUserEntityQuery.getFirstRegistrationAuthUser();
				Assertions.assertNotNull(firstRegistrationAuthUser);
				Assertions.assertNotNull(firstRegistrationAuthUser.getId());
				Assertions.assertNotNull(firstRegistrationAuthUser.getEmail());
			}
		}
	}

	@Test
	public void testSave_InsertOk() {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		AuthUserEntityQuery authUserEntityQuery = new AuthUserEntityQuery();
		authUserEntityQuery.setAuthUser(authUser);
		authUserEntityQuery.setPageEntity(PageEntityEnum.AUTH_USER);
		authUserEntityQuery.setInitialPdfDocumentPageFormat(PdfDocumentPageFormat.A4_VERTICAL);
		authUserEntityQuery.setFinalPdfDocumentPageFormat(PdfDocumentPageFormat.A3_HORIZONTAL);
		authUserEntityQuery.setLanguage(Language.ENGLISH);
		authUserEntityQuery.setTableSortingField(TableField.AUTH_USER_EMAIL);
		authUserEntityQuery.setTableSortingDirection(TableSortingDirection.DESC);
		authUserEntityQuery.setTablePageSize(TablePageSize.SIZE_100);
		authUserEntityQuery.setPageNumber(4);
		authUserEntityQuery.setQueryUrl("http://localhost:8080/auth-users/index?languageParam=en&filterName=&filterValue=&sortingField=email&sortingDirection=desc&pageSize=100&pageNumber=4");

		authUserEntityQuery = authUserEntityQueryRepository.saveAndFlush(authUserEntityQuery);

		Assertions.assertNotNull(authUserEntityQuery);
		Assertions.assertNotNull(authUserEntityQuery.getId());
		Assertions.assertEquals(authUser, authUserEntityQuery.getAuthUser());
		Assertions.assertEquals(PageEntityEnum.AUTH_USER, authUserEntityQuery.getPageEntity());
		Assertions.assertEquals(PdfDocumentPageFormat.A4_VERTICAL, authUserEntityQuery.getInitialPdfDocumentPageFormat());
		Assertions.assertEquals(PdfDocumentPageFormat.A3_HORIZONTAL, authUserEntityQuery.getFinalPdfDocumentPageFormat());
		Assertions.assertEquals(Language.ENGLISH, authUserEntityQuery.getLanguage());
		Assertions.assertEquals(TableField.AUTH_USER_EMAIL, authUserEntityQuery.getTableSortingField());
		Assertions.assertEquals(TableSortingDirection.DESC, authUserEntityQuery.getTableSortingDirection());
		Assertions.assertEquals(TablePageSize.SIZE_100, authUserEntityQuery.getTablePageSize());
		Assertions.assertEquals(4, authUserEntityQuery.getPageNumber());
		Assertions.assertEquals("http://localhost:8080/auth-users/index?languageParam=en&filterName=&filterValue=&sortingField=email&sortingDirection=desc&pageSize=100&pageNumber=4", authUserEntityQuery.getQueryUrl());
		Assertions.assertNotNull(authUserEntityQuery.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserEntityQuery.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authUserEntityQuery.getLastModificationDateTime());
		Assertions.assertNull(authUserEntityQuery.getLastModificationAuthUser());
	}

	@Test
	public void testSave_UpdateOk() {
		AuthUserEntityQuery authUserEntityQuery = authUserEntityQueryRepository.findByIdNotOptional(1L);
		authUserEntityQuery.setFinalPdfDocumentPageFormat(PdfDocumentPageFormat.A4_HORIZONTAL);

		authUserEntityQuery = authUserEntityQueryRepository.saveAndFlush(authUserEntityQuery);

		this.commonTestsAuthUserEntityQuery1(authUserEntityQuery, PdfDocumentPageFormat.A4_HORIZONTAL);
	}

	@Test
	public void testSave_Null() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserEntityQueryRepository.saveAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertTrue(rootCauseMessage.contains("Entity must not be null"));
	}

	@Test
	public void testDeleteById_Ok() {
		authUserEntityQueryRepository.deleteByIdAndFlush(1L);
	}

	@Test
	public void testDeleteById_NullId() {
		final InvalidDataAccessApiUsageException exception = Assertions.assertThrows(
			InvalidDataAccessApiUsageException.class, () -> {
				authUserEntityQueryRepository.deleteByIdAndFlush(null);
			}
		);

		final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

		Assertions.assertNotNull(rootCauseMessage);
		Assertions.assertEquals("The given id must not be null!", rootCauseMessage);
	}

	private void commonTestsAuthUserEntityQuery1(final AuthUserEntityQuery authUserEntityQuery) {
		this.commonTestsAuthUserEntityQuery1(authUserEntityQuery, PdfDocumentPageFormat.A3_HORIZONTAL);
	}

	private void commonTestsAuthUserEntityQuery1(final AuthUserEntityQuery authUserEntityQuery, PdfDocumentPageFormat finalPdfDocumentPageFormat) {
		final AuthUser authUser = authUserRepository.findByIdNotOptional(2L);

		Assertions.assertNotNull(authUserEntityQuery);
		Assertions.assertNotNull(authUser);
		Assertions.assertEquals(1L, authUserEntityQuery.getId());
		Assertions.assertEquals(authUser, authUserEntityQuery.getAuthUser());
		Assertions.assertEquals(PageEntityEnum.JOB_CATEGORY, authUserEntityQuery.getPageEntity());
		Assertions.assertEquals(PdfDocumentPageFormat.A3_HORIZONTAL, authUserEntityQuery.getInitialPdfDocumentPageFormat());
		Assertions.assertEquals(finalPdfDocumentPageFormat, authUserEntityQuery.getFinalPdfDocumentPageFormat());
		Assertions.assertEquals(Language.SPANISH, authUserEntityQuery.getLanguage());
		Assertions.assertNull(authUserEntityQuery.getPredefinedFilterEntity());
		Assertions.assertNull(authUserEntityQuery.getPredefinedFilterValue());
		Assertions.assertEquals(TableField.ID, authUserEntityQuery.getFilterTableField());
		Assertions.assertEquals(Constants.EMPTY_STRING, authUserEntityQuery.getFilterValue());
		Assertions.assertEquals(TableField.ID, authUserEntityQuery.getTableSortingField());
		Assertions.assertEquals(TableSortingDirection.DESC, authUserEntityQuery.getTableSortingDirection());
		Assertions.assertEquals(TablePageSize.SIZE_10, authUserEntityQuery.getTablePageSize());
		Assertions.assertEquals(0, authUserEntityQuery.getPageNumber());
		Assertions.assertEquals("http://localhost:8080/job-categories/index?languageParam=es&filterName=id&filterValue=&sortingField=id&sortingDirection=desc&pageSize=10&pageNumber=0", authUserEntityQuery.getQueryUrl());
		Assertions.assertEquals("jobCategories-1-0XV0TS7SHK6M.pdf", authUserEntityQuery.getFinalResultFileName());
		Assertions.assertNotNull(authUserEntityQuery.getFirstRegistrationDateTime());

		final AuthUser firstRegistrationAuthUser = authUserEntityQuery.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(2L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("aliuken@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNotNull(authUserEntityQuery.getLastModificationDateTime());

		final AuthUser lastModificationAuthUser = authUserEntityQuery.getLastModificationAuthUser();
		Assertions.assertNotNull(lastModificationAuthUser);
		Assertions.assertNotNull(lastModificationAuthUser.getId());
		Assertions.assertNotNull(lastModificationAuthUser.getEmail());
	}
}
