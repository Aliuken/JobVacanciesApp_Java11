package com.aliuken.jobvacanciesapp.repository;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.repository.superinterface.JpaRepositoryWithPaginationAndSorting;

@Repository
public interface AuthUserRepository extends JpaRepositoryWithPaginationAndSorting<AuthUser> {
//	@Query("SELECT au FROM AuthUser au WHERE au.email = :email")
//	AuthUser findByEmail(@Param("email") String email);

//	@Modifying
//	@Query("UPDATE AuthUser au SET au.enabled=0 WHERE au.id = :authUserId")
//	int lock(@Param("authUserId") long authUserId);

//	@Modifying
//	@Query("UPDATE AuthUser au SET au.enabled=1 WHERE au.id = :authUserId")
//	int unlock(@Param("authUserId") long authUserId);

	default AuthUser findByEmail(String email) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("email", email);

		AuthUser authUser = this.executeQuerySingleResult("SELECT au FROM AuthUser au WHERE au.email = :email", parameterMap);
		return authUser;
	}

	default int lock(Long authUserId) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("authUserId", authUserId);

		int rows = this.executeUpdate("UPDATE AuthUser au SET au.enabled=0 WHERE au.id = :authUserId", parameterMap);
		return rows;
	}

	default int unlock(Long authUserId) {
		Map<String, Object> parameterMap = new HashMap<>();
		parameterMap.put("authUserId", authUserId);

		int rows = this.executeUpdate("UPDATE AuthUser au SET au.enabled=1 WHERE au.id = :authUserId", parameterMap);
		return rows;
	}

	@Override
	default Class<AuthUser> getEntityClass() {
		return AuthUser.class;
	}
}
