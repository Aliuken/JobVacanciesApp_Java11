package com.aliuken.jobvacanciesapp.enumtype;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Sort;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;

public enum TableOrder implements Serializable {
	ID_ASC("idAsc", TableField.ID, "tableOrder.idAsc", Sort.Direction.ASC),
	ID_DESC("idDesc", TableField.ID, "tableOrder.idDesc", Sort.Direction.DESC),
	EMAIL_ASC("emailAsc", TableField.EMAIL, "tableOrder.emailAsc", Sort.Direction.ASC),
	EMAIL_DESC("emailDesc", TableField.EMAIL, "tableOrder.emailDesc", Sort.Direction.DESC),
	NAME_ASC("nameAsc", TableField.NAME, "tableOrder.nameAsc", Sort.Direction.ASC),
	NAME_DESC("nameDesc", TableField.NAME, "tableOrder.nameDesc", Sort.Direction.DESC),
	SURNAMES_ASC("surnamesAsc", TableField.SURNAMES, "tableOrder.surnamesAsc", Sort.Direction.ASC),
	SURNAMES_DESC("surnamesDesc", TableField.SURNAMES, "tableOrder.surnamesDesc", Sort.Direction.DESC),
	FIRST_REGISTRATION_DATE_TIME_ASC("firstRegistrationDateTimeAsc", TableField.FIRST_REGISTRATION_DATE_TIME, "tableOrder.firstRegistrationDateTimeAsc", Sort.Direction.ASC),
	FIRST_REGISTRATION_DATE_TIME_DESC("firstRegistrationDateTimeDesc", TableField.FIRST_REGISTRATION_DATE_TIME, "tableOrder.firstRegistrationDateTimeDesc", Sort.Direction.DESC),
	FIRST_REGISTRATION_AUTH_USER_EMAIL_ASC("firstRegistrationAuthUserEmailAsc", TableField.FIRST_REGISTRATION_AUTH_USER_EMAIL, "tableOrder.firstRegistrationAuthUserEmailAsc", Sort.Direction.ASC),
	FIRST_REGISTRATION_AUTH_USER_EMAIL_DESC("firstRegistrationAuthUserEmailDesc", TableField.FIRST_REGISTRATION_AUTH_USER_EMAIL, "tableOrder.firstRegistrationAuthUserEmailDesc", Sort.Direction.DESC),
	LAST_MODIFICATION_DATE_TIME_ASC("lastModificationDateTimeAsc", TableField.LAST_MODIFICATION_DATE_TIME, "tableOrder.lastModificationDateTimeAsc", Sort.Direction.ASC),
	LAST_MODIFICATION_DATE_TIME_DESC("lastModificationDateTimeDesc", TableField.LAST_MODIFICATION_DATE_TIME, "tableOrder.lastModificationDateTimeDesc", Sort.Direction.DESC),
	LAST_MODIFICATION_AUTH_USER_EMAIL_ASC("lastModificationAuthUserEmailAsc", TableField.LAST_MODIFICATION_AUTH_USER_EMAIL, "tableOrder.lastModificationAuthUserEmailAsc", Sort.Direction.ASC),
	LAST_MODIFICATION_AUTH_USER_EMAIL_DESC("lastModificationAuthUserEmailDesc", TableField.LAST_MODIFICATION_AUTH_USER_EMAIL, "tableOrder.lastModificationAuthUserEmailDesc", Sort.Direction.DESC);

	@NotNull
	private final String code;

	@NotNull
	private final TableField tableField;

	@NotNull
	private final String messageName;

	@NotNull
	private final Sort.Direction sortDirection;

	private TableOrder(final String code, final TableField tableField, final String messageName, final Sort.Direction sortDirection) {
		this.code = code;
		this.tableField = tableField;
		this.messageName = messageName;
		this.sortDirection = sortDirection;
	}

	public String getCode() {
		return code;
	}

	public TableField getTableField() {
		return tableField;
	}

	public String getMessageName() {
		return messageName;
	}

	public Sort.Direction getSortDirection() {
		return sortDirection;
	}

	public static TableOrder findByCode(final String code) {
		if(LogicalUtils.isNullOrEmpty(code)) {
			return null;
		}

		final TableOrder tableOrder = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableOrder.class)
			.filter(value -> value.code.equals(code))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("TableOrder code does not exist"));

		return tableOrder;
	}

	public static TableOrder[] valuesWithoutLastModification() {
		final TableOrder[] tableOrders = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableOrder.class)
			.filter(e -> !e.tableField.isLastModificationField())
			.toArray(TableOrder[]::new);

		return tableOrders;
	}

	public static TableOrder[] valuesWithoutAuthUser() {
		final TableOrder[] tableOrders = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableOrder.class)
			.filter(e -> !e.tableField.isAuthUserField())
			.toArray(TableOrder[]::new);

		return tableOrders;
	}

	public static TableOrder[] valuesWithoutAuthUserAndLastModification() {
		final TableOrder[] tableOrders = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableOrder.class)
			.filter(e -> (!e.tableField.isAuthUserField() && !e.tableField.isLastModificationField()))
			.toArray(TableOrder[]::new);

		return tableOrders;
	}

}
