package com.aliuken.jobvacanciesapp.enumtype;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.Constants;

public enum UserInterfaceFramework implements Serializable {
	MATERIAL_DESIGN("M", "Material Design"),
	BOOTSTRAP("B", "Bootstrap");

	@NotNull
	private final String code;

	@NotNull
	private final String value;

	private UserInterfaceFramework(final String code, final String value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}

	public static UserInterfaceFramework findByCode(final String code) {
		final UserInterfaceFramework userInterfaceFramework;
		if(code != null) {
			userInterfaceFramework = Constants.PARALLEL_STREAM_UTILS.ofEnum(UserInterfaceFramework.class)
				.filter(userInterfaceFrameworkAux -> code.equals(userInterfaceFrameworkAux.code))
				.findFirst()
				.orElse(null);
		} else {
			userInterfaceFramework = null;
		}

		return userInterfaceFramework;
	}

}
