package com.aliuken.jobvacanciesapp.model.comparator;

import java.util.Comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superinterface.AbstractEntityIdComparator;
import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;

public class AuthUserRoleAuthRolePriorityComparator implements Comparator<AuthUserRole>, AbstractEntityIdComparator {
	@Override
	public int compare(final AuthUserRole authUserRole1, final AuthUserRole authUserRole2) {
		final AuthRole authRole1;
		final Byte authRolePriority1;
		if(authUserRole1 != null) {
			authRole1 = authUserRole1.getAuthRole();
			authRolePriority1 = authRole1.getPriority();
		} else {
			return 1;
		}

		final AuthRole authRole2;
		final Byte authRolePriority2;
		if(authUserRole2 != null) {
			authRole2 = authUserRole2.getAuthRole();
			authRolePriority2 = authRole2.getPriority();
		} else {
			return -1;
		}

		int result = authRolePriority2.compareTo(authRolePriority1);
		if(result == 0) {
			result = this.compareAbstractEntityIdDesc(authRole1, authRole2);
		}
		return result;
	}
}