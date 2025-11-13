package com.aliuken.jobvacanciesapp.model.comparator;

import com.aliuken.jobvacanciesapp.model.comparator.superclass.AbstractEntitySpecificComparator;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;

import java.util.function.Function;

public class AuthUserRoleAuthRolePriorityComparator extends AbstractEntitySpecificComparator<AuthUserRole, Byte> {
	@Override
	public final Function<AuthUserRole, Byte> getFirstCompareFieldFunction() {
		return authUserRole -> authUserRole.getAuthRole().getPriority();
	}

	@Override
	public boolean getIsDescendingOrder() {
		return true;
	}
}