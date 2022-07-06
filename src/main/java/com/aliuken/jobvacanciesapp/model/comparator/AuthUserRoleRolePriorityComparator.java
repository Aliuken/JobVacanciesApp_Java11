package com.aliuken.jobvacanciesapp.model.comparator;

import java.util.Comparator;

import com.aliuken.jobvacanciesapp.model.AuthUserRole;

public class AuthUserRoleRolePriorityComparator implements Comparator<AuthUserRole> {
	@Override
	public int compare(AuthUserRole authUserRole1, AuthUserRole authUserRole2) {
		final Byte priority1;
		if(authUserRole1 != null && authUserRole1.getAuthRole() != null &&
				authUserRole1.getAuthRole().getPriority() != null) {
			priority1 = authUserRole1.getAuthRole().getPriority();
		} else {
			return 1;
		}

		final Byte priority2;
		if(authUserRole2 != null && authUserRole2.getAuthRole() != null &&
				authUserRole2.getAuthRole().getPriority() != null) {
			priority2 = authUserRole2.getAuthRole().getPriority();
		} else {
			return -1;
		}

		return priority2.compareTo(priority1);
	}
}