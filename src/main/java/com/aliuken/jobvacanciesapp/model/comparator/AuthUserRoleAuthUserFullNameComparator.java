package com.aliuken.jobvacanciesapp.model.comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superclass.AbstractEntitySpecificComparator;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;

import java.util.function.Function;

public class AuthUserRoleAuthUserFullNameComparator extends AbstractEntitySpecificComparator<AuthUserRole, String> {
	@Override
	public final Function<AuthUserRole, String> getFirstCompareFieldFunction() {
		return authUserRole -> authUserRole.getAuthUser().getFullName();
	}

	@Override
	public boolean getIsDescendingOrder() {
		return false;
	}
}