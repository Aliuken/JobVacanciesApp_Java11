package com.aliuken.jobvacanciesapp.model.comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superclass.AbstractEntitySpecificComparator;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class AuthUserRoleAuthUserFullNameComparator extends AbstractEntitySpecificComparator<AuthUserRole, String> {
	@Override
	public final @NonNull Function<AuthUserRole, String> getFirstCompareFieldFunction() {
		final Function<AuthUserRole, String> function = authUserRole -> authUserRole.getAuthUser().getFullName();
		return function;
	}

	@Override
	public boolean getIsDescendingOrder() {
		return false;
	}
}