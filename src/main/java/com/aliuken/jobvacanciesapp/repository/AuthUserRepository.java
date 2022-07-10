package com.aliuken.jobvacanciesapp.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserRepository extends JpaRepositoryWithPaginationAndSorting<AuthUser> {
	@Query("SELECT au FROM AuthUser au WHERE au.email = :email")
	AuthUser findByEmail(@Param("email") String email);

	@Modifying
	@Query("UPDATE AuthUser au SET au.enabled=0 WHERE au.id = :authUserId")
	int lock(@Param("authUserId") long authUserId);

	@Modifying
	@Query("UPDATE AuthUser au SET au.enabled=1 WHERE au.id = :authUserId")
	int unlock(@Param("authUserId") long authUserId);

	@Override
	default Class<AuthUser> getEntityClass() {
		return AuthUser.class;
	}
}
