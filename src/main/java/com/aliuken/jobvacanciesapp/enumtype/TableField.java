package com.aliuken.jobvacanciesapp.enumtype;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

public enum TableField implements Serializable {
	ID("id", "id", "abstractEntity.id", false, false),
	EMAIL("email", "email", "abstractEntityWithAuthUser.email", true, false),
	NAME("name", "name", "abstractEntityWithAuthUser.name", true, false),
	SURNAMES("surnames", "surnames", "abstractEntityWithAuthUser.surnames", true, false),
	FIRST_REGISTRATION_DATE_TIME("firstRegistrationDateTime", "firstRegistrationDateTime", "abstractEntity.firstRegistrationDateTime", false, false),
	FIRST_REGISTRATION_AUTH_USER_EMAIL("firstRegistrationAuthUserEmail", "firstRegistrationAuthUser.email", "abstractEntity.firstRegistrationAuthUserEmail", false, false),
	LAST_MODIFICATION_DATE_TIME("lastModificationDateTime", "lastModificationDateTime", "abstractEntity.lastModificationDateTime", false, true),
	LAST_MODIFICATION_AUTH_USER_EMAIL("lastModificationAuthUserEmail", "lastModificationAuthUser.email", "abstractEntity.lastModificationAuthUserEmail", false, true);

	@NotNull
	private final String code;

	@NotNull
	private final String fieldPath;

	@NotNull
	private final String messageName;

	private final boolean isAuthUserField;

	private final String authUserFieldPath;

	private final boolean isLastModificationField;

	private TableField(final String code, final String fieldPath, final String messageName, final boolean isAuthUserField, final boolean isLastModificationField) {
		this.code = code;
		this.fieldPath = fieldPath;
		this.messageName = messageName;
		this.isAuthUserField = isAuthUserField;
		if(isAuthUserField) {
			this.authUserFieldPath = StringUtils.getStringJoined("authUser.", fieldPath);
		} else {
			this.authUserFieldPath = null;
		}
		this.isLastModificationField = isLastModificationField;
	}

	public String getCode() {
		return code;
	}

	public String getFieldPath() {
		return fieldPath;
	}

	public String getMessageName() {
		return messageName;
	}

	public boolean isAuthUserField() {
		return isAuthUserField;
	}

	public String getAuthUserFieldPath() {
		return authUserFieldPath;
	}

	public boolean isLastModificationField() {
		return isLastModificationField;
	}

	public static TableField findByCode(final String code) {
		if(LogicalUtils.isNullOrEmpty(code)) {
			return null;
		}

		final TableField tableField = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableField.class)
			.filter(e -> code.equals(e.code))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("TableField code does not exist"));

		return tableField;
	}

	public static TableField[] valuesWithoutLastModification() {
		final TableField[] tableFields = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableField.class)
			.filter(e -> !e.isLastModificationField)
			.toArray(TableField[]::new);

		return tableFields;
	}

	public static TableField[] valuesWithoutAuthUser() {
		final TableField[] tableFields = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableField.class)
			.filter(e -> !e.isAuthUserField)
			.toArray(TableField[]::new);

		return tableFields;
	}

	public static TableField[] valuesWithoutAuthUserAndLastModification() {
		final TableField[] tableFields = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableField.class)
			.filter(e -> (!e.isAuthUserField && !e.isLastModificationField))
			.toArray(TableField[]::new);

		return tableFields;
	}

}
