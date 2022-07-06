package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;
import java.util.EnumSet;

import javax.validation.constraints.NotNull;

public enum TableField implements Serializable {
	ID("id", "abstractEntity.id", false),
	FIRST_REGISTRATION_DATE_TIME("firstRegistrationDateTime", "abstractEntity.firstRegistrationDateTime", false),
	FIRST_REGISTRATION_AUTH_USER_EMAIL("firstRegistrationAuthUserEmail", "abstractEntity.firstRegistrationAuthUserEmail", false),
	LAST_MODIFICATION_DATE_TIME("lastModificationDateTime", "abstractEntity.lastModificationDateTime", false),
	LAST_MODIFICATION_AUTH_USER_EMAIL("lastModificationAuthUserEmail", "abstractEntity.lastModificationAuthUserEmail", false),
	EMAIL("email", "abstractEntityWithAuthUser.email", true),
	NAME("name", "abstractEntityWithAuthUser.name", true),
	SURNAMES("surnames", "abstractEntityWithAuthUser.surnames", true);

    @NotNull
	private final String code;

	@NotNull
	private final String messageName;

	private final boolean isAuthUserField;

	private TableField(final String code, final String messageName, final boolean isAuthUserField) {
		this.code = code;
		this.messageName = messageName;
		this.isAuthUserField = isAuthUserField;
	}

	public String getCode() {
		return code;
	}

	public String getMessageName() {
		return messageName;
	}

	public boolean isAuthUserField() {
		return isAuthUserField;
	}

	public static TableField findByCode(final String code) {
		if(code != null && !code.isEmpty()) {
			TableField tableField = EnumSet.allOf(TableField.class).stream().parallel()
		       .filter(e -> code.equals(e.code))
		       .findFirst()
		       .orElseThrow(() -> new IllegalArgumentException("TableField code does not exist"));

			return tableField;
		} else {
			return null;
		}
	}

	public static TableField[] valuesWithoutAuthUser() {
		TableField[] tableFields = EnumSet.allOf(TableField.class).stream().parallel()
			.filter(e -> e.isAuthUserField == false)
			.toArray(TableField[]::new);

		return tableFields;
	}

}
