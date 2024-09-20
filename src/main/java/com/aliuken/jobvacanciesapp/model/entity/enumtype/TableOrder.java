package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Sort;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.superinterface.Internationalizable;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;

public enum TableOrder implements Serializable, Internationalizable {
	ID_ASC("idAsc", "tableOrder.idAsc", TableField.ID, Sort.Direction.ASC),
	ID_DESC("idDesc", "tableOrder.idDesc", TableField.ID, Sort.Direction.DESC),
	EMAIL_ASC("emailAsc", "tableOrder.emailAsc", TableField.EMAIL, Sort.Direction.ASC),
	EMAIL_DESC("emailDesc", "tableOrder.emailDesc", TableField.EMAIL, Sort.Direction.DESC),
	NAME_ASC("nameAsc", "tableOrder.nameAsc", TableField.NAME, Sort.Direction.ASC),
	NAME_DESC("nameDesc", "tableOrder.nameDesc", TableField.NAME, Sort.Direction.DESC),
	SURNAMES_ASC("surnamesAsc", "tableOrder.surnamesAsc", TableField.SURNAMES, Sort.Direction.ASC),
	SURNAMES_DESC("surnamesDesc", "tableOrder.surnamesDesc", TableField.SURNAMES, Sort.Direction.DESC),
	FIRST_REGISTRATION_DATE_TIME_ASC("firstRegistrationDateTimeAsc", "tableOrder.firstRegistrationDateTimeAsc", TableField.FIRST_REGISTRATION_DATE_TIME, Sort.Direction.ASC),
	FIRST_REGISTRATION_DATE_TIME_DESC("firstRegistrationDateTimeDesc", "tableOrder.firstRegistrationDateTimeDesc", TableField.FIRST_REGISTRATION_DATE_TIME, Sort.Direction.DESC),
	FIRST_REGISTRATION_AUTH_USER_EMAIL_ASC("firstRegistrationAuthUserEmailAsc", "tableOrder.firstRegistrationAuthUserEmailAsc", TableField.FIRST_REGISTRATION_AUTH_USER_EMAIL, Sort.Direction.ASC),
	FIRST_REGISTRATION_AUTH_USER_EMAIL_DESC("firstRegistrationAuthUserEmailDesc", "tableOrder.firstRegistrationAuthUserEmailDesc", TableField.FIRST_REGISTRATION_AUTH_USER_EMAIL, Sort.Direction.DESC),
	LAST_MODIFICATION_DATE_TIME_ASC("lastModificationDateTimeAsc", "tableOrder.lastModificationDateTimeAsc", TableField.LAST_MODIFICATION_DATE_TIME, Sort.Direction.ASC),
	LAST_MODIFICATION_DATE_TIME_DESC("lastModificationDateTimeDesc", "tableOrder.lastModificationDateTimeDesc", TableField.LAST_MODIFICATION_DATE_TIME, Sort.Direction.DESC),
	LAST_MODIFICATION_AUTH_USER_EMAIL_ASC("lastModificationAuthUserEmailAsc", "tableOrder.lastModificationAuthUserEmailAsc", TableField.LAST_MODIFICATION_AUTH_USER_EMAIL, Sort.Direction.ASC),
	LAST_MODIFICATION_AUTH_USER_EMAIL_DESC("lastModificationAuthUserEmailDesc", "tableOrder.lastModificationAuthUserEmailDesc", TableField.LAST_MODIFICATION_AUTH_USER_EMAIL, Sort.Direction.DESC);

	@NotNull
	private final String code;

	@NotNull
	private final String messageName;

	@NotNull
	private final TableField tableField;

	@NotNull
	private final Sort.Direction sortDirection;

	private TableOrder(final String code, final String messageName, final TableField tableField, final Sort.Direction sortDirection) {
		this.code = code;
		this.messageName = messageName;
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

	public static TableOrder findByCode(final String code) {
		if(LogicalUtils.isNullOrEmptyString(code)) {
			return null;
		}

		final TableOrder tableOrder = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableOrder.class)
			.filter(value -> value.code.equals(code))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("TableOrder code does not exist"));

		return tableOrder;
	}

	public static TableOrder[] valuesWithoutAuthUser() {
		final TableOrder[] tableOrders = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableOrder.class)
			.filter(e -> !e.tableField.isAuthUserField())
			.toArray(TableOrder[]::new);

		return tableOrders;
	}

	public static TableOrder[] valuesWithoutLastModification() {
		final TableOrder[] tableOrders = Constants.PARALLEL_STREAM_UTILS.ofEnum(TableOrder.class)
			.filter(e -> !e.tableField.isLastModificationField())
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
