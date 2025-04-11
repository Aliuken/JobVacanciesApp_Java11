package com.aliuken.jobvacanciesapp.model.comparator;

import java.util.Comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superinterface.AbstractEntityIdComparator;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;

public class AuthUserRoleAuthUserFullNameComparator implements Comparator<AuthUserRole>, AbstractEntityIdComparator {
	@Override
	public int compare(final AuthUserRole authUserRole1, final AuthUserRole authUserRole2) {
		final AuthUser authUser1;
		final String authUserFullName1;
		if(authUserRole1 != null) {
			authUser1 = authUserRole1.getAuthUser();
			authUserFullName1 = authUser1.getFullName();
		} else {
			return -1;
		}

		final AuthUser authUser2;
		final String authUserFullName2;
		if(authUserRole2 != null) {
			authUser2 = authUserRole2.getAuthUser();
			authUserFullName2 = authUser2.getFullName();
		} else {
			return 1;
		}

		int result = authUserFullName1.compareTo(authUserFullName2);
		if(result == 0) {
			result = this.compareAbstractEntityIdDesc(authUser1, authUser2);
		}
		return result;
	}
}