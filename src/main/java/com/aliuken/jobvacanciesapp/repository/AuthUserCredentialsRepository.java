package com.aliuken.jobvacanciesapp.repository;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserCredentialsRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserCredentials> {
	AuthUserCredentials findByEmail(String email);
	AuthUserCredentials findByEmailAndEncryptedPassword(String email, String encryptedPassword);
	
	@Override
	default Class<AuthUserCredentials> getEntityClass() {
		return AuthUserCredentials.class;
	}
}