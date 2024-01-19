package com.aliuken.jobvacanciesapp.enumtype;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public enum AnonymousAccessPermission implements Serializable {
	ACCESS_BY_PROPERTY("anonymousAccessPermission.accessByProperty"),
	ACCESS_ALLOWED("anonymousAccessPermission.accessAllowed"),
	ACCESS_NOT_ALLOWED("anonymousAccessPermission.accessNotAllowed");

	@NotNull
	private final String messageName;

	private AnonymousAccessPermission(@NotNull final String messageName) {
		this.messageName = messageName;
	}

	public String getMessageName() {
		return messageName;
	}

}
