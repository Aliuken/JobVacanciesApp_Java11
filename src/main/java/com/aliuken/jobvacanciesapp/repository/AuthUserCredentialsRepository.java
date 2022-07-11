package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserCredentialsRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserCredentials> {
//	@Query("SELECT auc FROM AuthUserCredentials auc WHERE auc.email = :email")
//	AuthUserCredentials findByEmail(@Param("email") String email);

//	@Query("SELECT auc FROM AuthUserCredentials auc WHERE auc.email = :email AND auc.encryptedPassword = :encryptedPassword")
//	AuthUserCredentials findByEmailAndEncryptedPassword(@Param("email") String email, @Param("encryptedPassword") String encryptedPassword);

	default AuthUserCredentials findByEmail(String email) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);

		AuthUserCredentials authUserCredentials = this.executeQuerySingleResult("SELECT auc FROM AuthUserCredentials auc WHERE auc.email = :email", parameterMap);
		return authUserCredentials;
	}

	default AuthUserCredentials findByEmailAndEncryptedPassword(String email, String encryptedPassword) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);
		parameterMap.put("encryptedPassword", encryptedPassword);

		AuthUserCredentials authUserCredentials = this.executeQuerySingleResult("SELECT auc FROM AuthUserCredentials auc WHERE auc.email = :email AND auc.encryptedPassword = :encryptedPassword", parameterMap);
		return authUserCredentials;
	}

	@Override
	default Class<AuthUserCredentials> getEntityClass() {
		return AuthUserCredentials.class;
	}
}