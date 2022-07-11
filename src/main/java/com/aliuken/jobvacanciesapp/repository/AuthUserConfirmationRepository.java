package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserConfirmationRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserConfirmation> {
//	@Query("SELECT auc FROM AuthUserConfirmation auc WHERE auc.email = :email")
//	AuthUserConfirmation findByEmail(@Param("email") String email);

//	@Query("SELECT auc FROM AuthUserConfirmation auc WHERE auc.email = :email AND auc.uuid = :uuid")
//	AuthUserConfirmation findByEmailAndUuid(@Param("email") String email, @Param("uuid") String uuid);

	default AuthUserConfirmation findByEmail(String email) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);

		AuthUserConfirmation authUserConfirmation = this.executeQuerySingleResult("SELECT auc FROM AuthUserConfirmation auc WHERE auc.email = :email", parameterMap);
		return authUserConfirmation;
	}

	default AuthUserConfirmation findByEmailAndUuid(String email, String uuid) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);
		parameterMap.put("uuid", uuid);

		AuthUserConfirmation authUserConfirmation = this.executeQuerySingleResult("SELECT auc FROM AuthUserConfirmation auc WHERE auc.email = :email AND auc.uuid = :uuid", parameterMap);
		return authUserConfirmation;
	}

	@Override
	default Class<AuthUserConfirmation> getEntityClass() {
		return AuthUserConfirmation.class;
	}
}
