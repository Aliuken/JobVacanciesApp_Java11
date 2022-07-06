package com.aliuken.jobvacanciesapp.repository;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserConfirmationRepository extends JpaRepositoryWithPaginationAndSorting<AuthUserConfirmation> {
	AuthUserConfirmation findByEmail(String email);
	AuthUserConfirmation findByEmailAndUuid(String email, String uuid);
	
	@Override
	default Class<AuthUserConfirmation> getEntityClass() {
		return AuthUserConfirmation.class;
	}
}
