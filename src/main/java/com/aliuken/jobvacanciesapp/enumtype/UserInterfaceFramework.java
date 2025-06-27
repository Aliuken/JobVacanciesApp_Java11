package com.aliuken.jobvacanciesapp.enumtype;

import javax.validation.constraints.NotNull;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;

public enum UserInterfaceFramework implements ConfigurableEnum<UserInterfaceFramework> {
	BY_DEFAULT     ("-", "uiFramework.byDefault"),
	MATERIAL_DESIGN("M", "uiFramework.materialDesign"),
	BOOTSTRAP      ("B", "uiFramework.bootstrap");

	@NotNull
	private final String code;

	@NotNull
	private final String messageName;

	private UserInterfaceFramework(final String code, final String messageName) {
		this.code = code;
		this.messageName = messageName;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessageName() {
		return messageName;
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

	@Override
	public ConfigurableEnum<UserInterfaceFramework> getOverwrittenEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final UserInterfaceFramework userInterfaceFramework = configPropertiesBean.getDefaultUserInterfaceFrameworkOverwritten();
		return userInterfaceFramework;
	}

	@Override
	public ConfigurableEnum<UserInterfaceFramework> getOverwritableEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final UserInterfaceFramework userInterfaceFramework = configPropertiesBean.getDefaultUserInterfaceFramework();
		return userInterfaceFramework;
	}

	@Override
	public ConfigurableEnum<UserInterfaceFramework> getFinalDefaultEnumElement() {
		return UserInterfaceFramework.MATERIAL_DESIGN;
	}
}
