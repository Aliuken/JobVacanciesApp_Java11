package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import java.io.Serializable;
import java.util.function.Predicate;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Sort;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.superinterface.Internationalizable;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

public enum TableSorting implements Serializable, Internationalizable {
	ID_ASC("idAsc", TableField.ID, Sort.Direction.ASC),
	ID_DESC("idDesc", TableField.ID, Sort.Direction.DESC),
	EMAIL_ASC("emailAsc", TableField.EMAIL, Sort.Direction.ASC),
	EMAIL_DESC("emailDesc", TableField.EMAIL, Sort.Direction.DESC),
	NAME_ASC("nameAsc", TableField.NAME, Sort.Direction.ASC),
	NAME_DESC("nameDesc", TableField.NAME, Sort.Direction.DESC),
	SURNAMES_ASC("surnamesAsc", TableField.SURNAMES, Sort.Direction.ASC),
	SURNAMES_DESC("surnamesDesc", TableField.SURNAMES, Sort.Direction.DESC),
	FIRST_REGISTRATION_DATE_TIME_ASC("firstRegistrationDateTimeAsc", TableField.FIRST_REGISTRATION_DATE_TIME, Sort.Direction.ASC),
	FIRST_REGISTRATION_DATE_TIME_DESC("firstRegistrationDateTimeDesc", TableField.FIRST_REGISTRATION_DATE_TIME, Sort.Direction.DESC),
	FIRST_REGISTRATION_AUTH_USER_EMAIL_ASC("firstRegistrationAuthUserEmailAsc", TableField.FIRST_REGISTRATION_AUTH_USER_EMAIL, Sort.Direction.ASC),
	FIRST_REGISTRATION_AUTH_USER_EMAIL_DESC("firstRegistrationAuthUserEmailDesc", TableField.FIRST_REGISTRATION_AUTH_USER_EMAIL, Sort.Direction.DESC),
	LAST_MODIFICATION_DATE_TIME_ASC("lastModificationDateTimeAsc", TableField.LAST_MODIFICATION_DATE_TIME, Sort.Direction.ASC),
	LAST_MODIFICATION_DATE_TIME_DESC("lastModificationDateTimeDesc", TableField.LAST_MODIFICATION_DATE_TIME, Sort.Direction.DESC),
	LAST_MODIFICATION_AUTH_USER_EMAIL_ASC("lastModificationAuthUserEmailAsc", TableField.LAST_MODIFICATION_AUTH_USER_EMAIL, Sort.Direction.ASC),
	LAST_MODIFICATION_AUTH_USER_EMAIL_DESC("lastModificationAuthUserEmailDesc", TableField.LAST_MODIFICATION_AUTH_USER_EMAIL, Sort.Direction.DESC);

	private final static Predicate<TableSorting> VALUES_PREDICATE = (tableSorting -> true);
	private final static Predicate<TableSorting> VALUES_WITHOUT_AUTH_USER_PREDICATE = (tableSorting -> !tableSorting.tableField.isAuthUserField());
	private final static Predicate<TableSorting> VALUES_WITHOUT_LAST_MODIFICATION = (tableSorting -> !tableSorting.tableField.isLastModificationField());
	private final static Predicate<TableSorting> VALUES_WITHOUT_AUTH_USER_AND_LAST_MODIFICATION_PREDICATE = (tableSorting -> (!tableSorting.tableField.isAuthUserField() && !tableSorting.tableField.isLastModificationField()));

	@NotNull
	private final String code;

	@NotNull
	private final String messageName;

	@NotNull
	private final TableField tableField;

	@NotNull
	private final Sort.Direction sortDirection;

	private static final String MESSAGE_NAME_PREFIX = "tableSorting.";

	private TableSorting(final String code, final TableField tableField, final Sort.Direction sortDirection) {
		this.code = code;
		this.messageName = StringUtils.getStringJoined(TableSorting.MESSAGE_NAME_PREFIX, code);
		this.tableField = tableField;
		this.sortDirection = sortDirection;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessageName() {
		return messageName;
	}

	public TableField getTableField() {
		return tableField;
	}

	public Sort.Direction getSortDirection() {
		return sortDirection;
	}

	public static TableSorting findByCode(final String code) {
		if(LogicalUtils.isNullOrEmptyString(code)) {
			return null;
		}

		final TableSorting tableSorting = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableSorting.class)
			.filter(value -> value.code.equals(code))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("TableSorting code does not exist"));

		return tableSorting;
	}

	public static TableSorting[] values(final boolean entityWithAuthUserFields, final boolean isUnmodifiableEntity) {
		final TableSorting[] tableSortings = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableSorting.class)
			.filter(TableSorting.valuesPredicate(entityWithAuthUserFields, isUnmodifiableEntity))
			.toArray(TableSorting[]::new);
		return tableSortings;
	}

	private static Predicate<? super TableSorting> valuesPredicate(final boolean entityWithAuthUserFields, final boolean isUnmodifiableEntity) {
		final Predicate<? super TableSorting> valuesPredicate;
		if(entityWithAuthUserFields) {
			if(isUnmodifiableEntity) {
				valuesPredicate = TableSorting.VALUES_WITHOUT_LAST_MODIFICATION;
			} else {
				valuesPredicate = TableSorting.VALUES_PREDICATE;
			}
		} else {
			if(isUnmodifiableEntity) {
				valuesPredicate = TableSorting.VALUES_WITHOUT_AUTH_USER_AND_LAST_MODIFICATION_PREDICATE;
			} else {
				valuesPredicate = TableSorting.VALUES_WITHOUT_AUTH_USER_PREDICATE;
			}
		}
		return valuesPredicate;
	}
}
