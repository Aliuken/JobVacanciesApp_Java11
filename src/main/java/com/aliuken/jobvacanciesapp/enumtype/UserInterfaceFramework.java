package com.aliuken.jobvacanciesapp.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

public enum UserInterfaceFramework implements ConfigurableEnum<UserInterfaceFramework> {
	BY_DEFAULT     ("-", "uiFramework.byDefault"),
	MATERIAL_DESIGN("M", "uiFramework.materialDesign"),
	BOOTSTRAP      ("B", "uiFramework.bootstrap");

	@Getter
	private final @NonNull String code;

	@Getter
	private final @NonNull String messageName;

	private UserInterfaceFramework(final @NonNull String code, final @NonNull String messageName) {
		this.code = code;
		this.messageName = messageName;
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
	public @NonNull Class<UserInterfaceFramework> getEnumClass() {
		return UserInterfaceFramework.class;
	}

	@Override
	public ConfigurableEnum<UserInterfaceFramework> getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final UserInterfaceFramework userInterfaceFramework = configPropertiesBean.getDefaultUserInterfaceFrameworkOverwritten();
		return userInterfaceFramework;
	}

	@Override
	public ConfigurableEnum<UserInterfaceFramework> getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final UserInterfaceFramework userInterfaceFramework = configPropertiesBean.getDefaultUserInterfaceFramework();
		return userInterfaceFramework;
	}

	@Override
	public @NonNull ConfigurableEnum<UserInterfaceFramework> getFinalDefaultEnumElement() {
		return UserInterfaceFramework.MATERIAL_DESIGN;
	}
}
