package com.aliuken.jobvacanciesapp.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserConfirmationRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserConfirmation> {
	@Query("SELECT auc FROM AuthUserConfirmation auc WHERE auc.email = :email")
	AuthUserConfirmation findByEmail(@Param("email") String email);

	@Query("SELECT auc FROM AuthUserConfirmation auc WHERE auc.email = :email AND auc.uuid = :uuid")
	AuthUserConfirmation findByEmailAndUuid(@Param("email") String email, @Param("uuid") String uuid);

	@Override
	default Class<AuthUserConfirmation> getEntityClass() {
		return AuthUserConfirmation.class;
	}
}
