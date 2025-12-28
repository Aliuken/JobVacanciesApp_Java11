package com.aliuken.jobvacanciesapp.model.comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superclass.AbstractEntitySpecificComparator;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public class AuthUserRoleAuthRolePriorityComparator extends AbstractEntitySpecificComparator<AuthUserRole, Byte> {
	@Override
	public final @NonNull Function<AuthUserRole, Byte> getFirstCompareFieldFunction() {
		final Function<AuthUserRole, Byte> function = authUserRole -> authUserRole.getAuthRole().getPriority();
		return function;
	}

	@Override
	public boolean getIsDescendingOrder() {
		return true;
	}
}