package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.superinterface.Internationalizable;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import java.io.Serializable;
import java.util.function.Predicate;

public enum TableField implements Serializable, Internationalizable {
	ID("id", null, "id", "abstractEntity.id", false),
	AUTH_USER_EMAIL("authUserEmail", TableFieldEntity.AUTH_USER, "email", "abstractEntityWithAuthUser.email", false),
	AUTH_USER_NAME("authUserName", TableFieldEntity.AUTH_USER, "name", "abstractEntityWithAuthUser.name", false),
	AUTH_USER_SURNAMES("authUserSurnames", TableFieldEntity.AUTH_USER, "surnames", "abstractEntityWithAuthUser.surnames", false),
	JOB_COMPANY_NAME("jobCompanyName", TableFieldEntity.JOB_COMPANY, "name", "abstractEntityWithJobCompany.name", false),
	FIRST_REGISTRATION_DATE_TIME("firstRegistrationDateTime", null, "firstRegistrationDateTime", "abstractEntity.firstRegistrationDateTime", false),
	FIRST_REGISTRATION_AUTH_USER_EMAIL("firstRegistrationAuthUserEmail", null, "firstRegistrationAuthUser.email", "abstractEntity.firstRegistrationAuthUserEmail", false),
	LAST_MODIFICATION_DATE_TIME("lastModificationDateTime", null, "lastModificationDateTime", "abstractEntity.lastModificationDateTime", true),
	LAST_MODIFICATION_AUTH_USER_EMAIL("lastModificationAuthUserEmail", null, "lastModificationAuthUser.email", "abstractEntity.lastModificationAuthUserEmail", true);

	private final static Predicate<TableField> VALUES_PREDICATE = (tableField -> true);
	private final static Predicate<TableField> VALUES_WITHOUT_LAST_MODIFICATION_PREDICATE = (tableField -> !tableField.isLastModificationField);
	private final static Predicate<TableField> VALUES_WITHOUT_AUTH_USER_PREDICATE = (tableField -> !tableField.isAuthUserField());
	private final static Predicate<TableField> VALUES_WITHOUT_AUTH_USER_AND_LAST_MODIFICATION_PREDICATE = (tableField -> (!tableField.isAuthUserField() && !tableField.isLastModificationField));
	private final static Predicate<TableField> VALUES_WITHOUT_JOB_COMPANY_PREDICATE = (tableField -> !tableField.isJobCompanyField());
	private final static Predicate<TableField> VALUES_WITHOUT_JOB_COMPANY_AND_LAST_MODIFICATION_PREDICATE = (tableField -> (!tableField.isJobCompanyField() && !tableField.isLastModificationField));
	private final static Predicate<TableField> VALUES_WITHOUT_AUTH_USER_AND_JOB_COMPANY_PREDICATE = (tableField -> !tableField.isAuthUserField() && !tableField.isJobCompanyField());
	private final static Predicate<TableField> VALUES_WITHOUT_AUTH_USER_AND_JOB_COMPANY_AND_LAST_MODIFICATION_PREDICATE = (tableField -> (!tableField.isAuthUserField() && !tableField.isJobCompanyField() && !tableField.isLastModificationField));

	@Getter
	private final @NonNull String code;

	@Getter
    private final TableFieldEntity entity;

	@Getter
	private final @NonNull String partialFieldPath;

	@Getter
	private final @NonNull String finalFieldPath;

	@Getter
	private final @NonNull String messageName;

	@Getter
	private final boolean isLastModificationField;

	private TableField(final @NonNull String code, final TableFieldEntity entity, final @NonNull String partialFieldPath, final @NonNull String messageName, final boolean isLastModificationField) {
		this.code = code;
		this.entity = entity;
		this.partialFieldPath = partialFieldPath;

		if(entity != null) {
			final String entityName = entity.getLowerCasedEntityName();
			this.finalFieldPath = StringUtils.getStringJoined(entityName, Constants.DOT, partialFieldPath);
		} else {
			this.finalFieldPath = partialFieldPath;
		}

		this.messageName = messageName;
		this.isLastModificationField = isLastModificationField;
	}

	public boolean isAuthUserField() {
		return (TableFieldEntity.AUTH_USER == entity);
	}

	public boolean isJobCompanyField() {
		return (TableFieldEntity.JOB_COMPANY == entity);
	}

	public static TableField findByCode(final String code) {
		if(LogicalUtils.isNullOrEmptyString(code)) {
			return null;
		}

		final TableField tableField = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableField.class)
			.filter(e -> code.equals(e.code))
			.findFirst()
			.orElse(null);
		return tableField;
	}

	public static @NonNull TableField[] valuesForCombo(final boolean isAuthUserEntityOrContainsDifferentAuthUsers, final boolean isJobCompanyEntityOrContainsDifferentJobCompanies, final boolean isUnmodifiableEntity) {
		final TableField[] tableFields = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableField.class)
			.filter(TableField.valuesForComboPredicate(isAuthUserEntityOrContainsDifferentAuthUsers, isJobCompanyEntityOrContainsDifferentJobCompanies, isUnmodifiableEntity))
			.toArray(TableField[]::new);
		return tableFields;
	}

	private static @NonNull Predicate<? super TableField> valuesForComboPredicate(final boolean isAuthUserEntityOrContainsDifferentAuthUsers, final boolean isJobCompanyEntityOrContainsDifferentJobCompanies, final boolean isUnmodifiableEntity) {
		final Predicate<? super TableField> valuesForComboPredicate;
		if(isAuthUserEntityOrContainsDifferentAuthUsers && isJobCompanyEntityOrContainsDifferentJobCompanies) {
			if(isUnmodifiableEntity) {
				valuesForComboPredicate = TableField.VALUES_WITHOUT_LAST_MODIFICATION_PREDICATE;
			} else {
				valuesForComboPredicate = TableField.VALUES_PREDICATE;
			}
		} else if(isJobCompanyEntityOrContainsDifferentJobCompanies) {
			if(isUnmodifiableEntity) {
				valuesForComboPredicate = TableField.VALUES_WITHOUT_AUTH_USER_AND_LAST_MODIFICATION_PREDICATE;
			} else {
				valuesForComboPredicate = TableField.VALUES_WITHOUT_AUTH_USER_PREDICATE;
			}
		} else if(isAuthUserEntityOrContainsDifferentAuthUsers) {
			if(isUnmodifiableEntity) {
				valuesForComboPredicate = TableField.VALUES_WITHOUT_JOB_COMPANY_AND_LAST_MODIFICATION_PREDICATE;
			} else {
				valuesForComboPredicate = TableField.VALUES_WITHOUT_JOB_COMPANY_PREDICATE;
			}
		} else {
			if(isUnmodifiableEntity) {
				valuesForComboPredicate = TableField.VALUES_WITHOUT_AUTH_USER_AND_JOB_COMPANY_AND_LAST_MODIFICATION_PREDICATE;
			} else {
				valuesForComboPredicate = TableField.VALUES_WITHOUT_AUTH_USER_AND_JOB_COMPANY_PREDICATE;
			}
		}
		return valuesForComboPredicate;
	}
}
