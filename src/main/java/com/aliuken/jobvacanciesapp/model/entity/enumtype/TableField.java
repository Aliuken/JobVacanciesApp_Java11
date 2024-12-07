package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import java.io.Serializable;
import java.util.function.Predicate;

import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.superinterface.Internationalizable;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

public enum TableField implements Serializable, Internationalizable {
	ID("id", "id", "abstractEntity.id", false, false),
	EMAIL("email", "email", "abstractEntityWithAuthUser.email", true, false),
	NAME("name", "name", "abstractEntityWithAuthUser.name", true, false),
	SURNAMES("surnames", "surnames", "abstractEntityWithAuthUser.surnames", true, false),
	FIRST_REGISTRATION_DATE_TIME("firstRegistrationDateTime", "firstRegistrationDateTime", "abstractEntity.firstRegistrationDateTime", false, false),
	FIRST_REGISTRATION_AUTH_USER_EMAIL("firstRegistrationAuthUserEmail", "firstRegistrationAuthUser.email", "abstractEntity.firstRegistrationAuthUserEmail", false, false),
	LAST_MODIFICATION_DATE_TIME("lastModificationDateTime", "lastModificationDateTime", "abstractEntity.lastModificationDateTime", false, true),
	LAST_MODIFICATION_AUTH_USER_EMAIL("lastModificationAuthUserEmail", "lastModificationAuthUser.email", "abstractEntity.lastModificationAuthUserEmail", false, true);

	private final static Predicate<TableField> VALUES_PREDICATE = (tableField -> true);
	private final static Predicate<TableField> VALUES_WITHOUT_AUTH_USER_PREDICATE = (tableField -> !tableField.isAuthUserField);
	private final static Predicate<TableField> VALUES_WITHOUT_LAST_MODIFICATION = (tableField -> !tableField.isLastModificationField);
	private final static Predicate<TableField> VALUES_WITHOUT_AUTH_USER_AND_LAST_MODIFICATION_PREDICATE = (tableField -> (!tableField.isAuthUserField && !tableField.isLastModificationField));

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

	@Override
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
		if(LogicalUtils.isNullOrEmptyString(code)) {
			return null;
		}

		final TableField tableField = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableField.class)
			.filter(e -> code.equals(e.code))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("TableField code does not exist"));

		return tableField;
	}

	public static TableField[] values(final boolean entityWithAuthUserFields, final boolean isUnmodifiableEntity) {
		final TableField[] tableFields = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableField.class)
			.filter(TableField.valuesPredicate(entityWithAuthUserFields, isUnmodifiableEntity))
			.toArray(TableField[]::new);
		return tableFields;
	}

	private static Predicate<? super TableField> valuesPredicate(final boolean entityWithAuthUserFields, final boolean isUnmodifiableEntity) {
		final Predicate<? super TableField> valuesPredicate;
		if(entityWithAuthUserFields) {
			if(isUnmodifiableEntity) {
				valuesPredicate = TableField.VALUES_WITHOUT_LAST_MODIFICATION;
			} else {
				valuesPredicate = TableField.VALUES_PREDICATE;
			}
		} else {
			if(isUnmodifiableEntity) {
				valuesPredicate = TableField.VALUES_WITHOUT_AUTH_USER_AND_LAST_MODIFICATION_PREDICATE;
			} else {
				valuesPredicate = TableField.VALUES_WITHOUT_AUTH_USER_PREDICATE;
			}
		}
		return valuesPredicate;
	}
}
