package com.aliuken.jobvacanciesapp.model.dto;

import java.io.Serializable;
import java.util.EnumSet;

import javax.validation.constraints.NotNull;

public enum TableOrder implements Serializable {
	ID_ASC("idAsc", TableField.ID, "tableOrder.idAsc"),
	ID_DESC("idDesc", TableField.ID, "tableOrder.idDesc"),
	FIRST_REGISTRATION_DATE_TIME_ASC("firstRegistrationDateTimeAsc", TableField.FIRST_REGISTRATION_DATE_TIME, "tableOrder.firstRegistrationDateTimeAsc"),
	FIRST_REGISTRATION_DATE_TIME_DESC("firstRegistrationDateTimeDesc", TableField.FIRST_REGISTRATION_DATE_TIME, "tableOrder.firstRegistrationDateTimeDesc"),
	FIRST_REGISTRATION_AUTH_USER_EMAIL_ASC("firstRegistrationAuthUserEmailAsc", TableField.FIRST_REGISTRATION_AUTH_USER_EMAIL, "tableOrder.firstRegistrationAuthUserEmailAsc"),
	FIRST_REGISTRATION_AUTH_USER_EMAIL_DESC("firstRegistrationAuthUserEmailDesc", TableField.FIRST_REGISTRATION_AUTH_USER_EMAIL, "tableOrder.firstRegistrationAuthUserEmailDesc"),
	LAST_MODIFICATION_DATE_TIME_ASC("lastModificationDateTimeAsc", TableField.LAST_MODIFICATION_DATE_TIME, "tableOrder.lastModificationDateTimeAsc"),
	LAST_MODIFICATION_DATE_TIME_DESC("lastModificationDateTimeDesc", TableField.LAST_MODIFICATION_DATE_TIME, "tableOrder.lastModificationDateTimeDesc"),
	LAST_MODIFICATION_AUTH_USER_EMAIL_ASC("lastModificationAuthUserEmailAsc", TableField.LAST_MODIFICATION_AUTH_USER_EMAIL, "tableOrder.lastModificationAuthUserEmailAsc"),
	LAST_MODIFICATION_AUTH_USER_EMAIL_DESC("lastModificationAuthUserEmailDesc", TableField.LAST_MODIFICATION_AUTH_USER_EMAIL, "tableOrder.lastModificationAuthUserEmailDesc"),
	EMAIL_ASC("emailAsc", TableField.EMAIL, "tableOrder.emailAsc"),
	EMAIL_DESC("emailDesc", TableField.EMAIL, "tableOrder.emailDesc"),
	NAME_ASC("nameAsc", TableField.NAME, "tableOrder.nameAsc"),
	NAME_DESC("nameDesc", TableField.NAME, "tableOrder.nameDesc"),
	SURNAMES_ASC("surnamesAsc", TableField.SURNAMES, "tableOrder.surnamesAsc"),
	SURNAMES_DESC("surnamesDesc", TableField.SURNAMES, "tableOrder.surnamesDesc");

    @NotNull
	private final String code;

    @NotNull
    private final TableField tableField;

	@NotNull
	private final String messageName;

	private TableOrder(final String code, final TableField tableField, final String messageName) {
		this.code = code;
		this.tableField = tableField;
		this.messageName = messageName;
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

	public static TableOrder findByCode(final String code) {
		if(code != null && !code.isEmpty()) {
			TableOrder tableOrder = EnumSet.allOf(TableOrder.class).stream().parallel()
		       .filter(value -> value.code.equals(code))
		       .findFirst()
		       .orElseThrow(() -> new IllegalArgumentException("TableOrder code does not exist"));

			return tableOrder;
		} else {
			return null;
		}
	}
	
	public static TableOrder[] valuesWithoutAuthUser() {
		TableOrder[] tableOrders = EnumSet.allOf(TableOrder.class).stream().parallel()
			.filter(e -> e.tableField.isAuthUserField() == false)
			.toArray(TableOrder[]::new);

		return tableOrders;
	}

}
