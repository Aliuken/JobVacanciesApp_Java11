package com.aliuken.jobvacanciesapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserCredentialsRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserCredentials> {
	@Query("SELECT auc FROM AuthUserCredentials auc WHERE auc.email = :email")
	AuthUserCredentials findByEmail(@Param("email") String email);

	@Query("SELECT auc FROM AuthUserCredentials auc WHERE auc.email = :email AND auc.encryptedPassword = :encryptedPassword")
	AuthUserCredentials findByEmailAndEncryptedPassword(@Param("email") String email, @Param("encryptedPassword") String encryptedPassword);

	@Override
	default Class<AuthUserCredentials> getEntityClass() {
		return AuthUserCredentials.class;
	}
}