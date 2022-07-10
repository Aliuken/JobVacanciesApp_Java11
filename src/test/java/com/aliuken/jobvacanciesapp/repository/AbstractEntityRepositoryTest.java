package com.aliuken.jobvacanciesapp.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.aliuken.jobvacanciesapp.MainApplication;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserLanguage;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = {MainApplication.class, AbstractEntityRepositoryImpl.class})
@Sql("classpath:db-dump-for-tests.sql")
public class AbstractEntityRepositoryTest {
	@Autowired
	private AbstractEntityRepository<AuthUser> abstractEntityRepository;

	@Test
	public void testRefreshEntity_Ok() {
		AuthUser authUser = new AuthUser();
		authUser.setId(2L);

		authUser = abstractEntityRepository.refreshEntity(authUser);

		Assertions.assertNotNull(authUser);
		Assertions.assertEquals(2L, authUser.getId());
		Assertions.assertEquals("aliuken@aliuken.com", authUser.getEmail());
		Assertions.assertEquals("Aliuken", authUser.getName());
		Assertions.assertEquals("Master", authUser.getSurnames());
		Assertions.assertEquals(AuthUserLanguage.ENGLISH, authUser.getLanguage());
		Assertions.assertEquals(Boolean.TRUE, authUser.getEnabled());
		Assertions.assertNotNull(authUser.getFirstRegistrationDateTime());

		AuthUser firstRegistrationAuthUser = authUser.getFirstRegistrationAuthUser();
		Assertions.assertNotNull(firstRegistrationAuthUser);
		Assertions.assertEquals(1L, firstRegistrationAuthUser.getId());
		Assertions.assertEquals("anonymous@aliuken.com", firstRegistrationAuthUser.getEmail());

		Assertions.assertNull(authUser.getLastModificationDateTime());
		Assertions.assertNull(authUser.getLastModificationAuthUser());
	}

	@Test
	public void testRefreshEntity_Null() {
		AuthUser authUser = abstractEntityRepository.refreshEntity(null);

		Assertions.assertNull(authUser);
	}

	@Test
	public void testRefreshEntity_IdNotExists() {
		AuthUser authUser = new AuthUser();
		authUser.setId(888L);

		authUser = abstractEntityRepository.refreshEntity(authUser);

		Assertions.assertNull(authUser);
	}

	@Test
	public void testRefreshEntity_NoId() {
		AuthUser authUser = new AuthUser();
		authUser.setEmail("aliuken@aliuken.com");

		authUser = abstractEntityRepository.refreshEntity(authUser);

		Assertions.assertNull(authUser);
	}
}
