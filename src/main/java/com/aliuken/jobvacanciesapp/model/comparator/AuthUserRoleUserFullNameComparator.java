package com.aliuken.jobvacanciesapp.model.comparator;

import java.util.Comparator;

import com.aliuken.jobvacanciesapp.model.AuthUserRole;

public class AuthUserRoleUserFullNameComparator implements Comparator<AuthUserRole> {
	@Override
	public int compare(AuthUserRole authUserRole1, AuthUserRole authUserRole2) {
		final String userFullName1;
		if(authUserRole1 != null && authUserRole1.getAuthUser() != null &&
				authUserRole1.getAuthUser().getFullName() != null) {
			userFullName1 = authUserRole1.getAuthUser().getFullName();
		} else {
			return -1;
		}

		final String userFullName2;
		if(authUserRole2 != null && authUserRole2.getAuthUser() != null &&
				authUserRole2.getAuthUser().getFullName() != null) {
			userFullName2 = authUserRole2.getAuthUser().getFullName();
		} else {
			return 1;
		}

		return userFullName1.compareTo(userFullName2);
	}
}