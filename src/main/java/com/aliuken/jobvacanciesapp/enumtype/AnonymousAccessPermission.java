package com.aliuken.jobvacanciesapp.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import lombok.Getter;

import javax.validation.constraints.NotNull;

public enum AnonymousAccessPermission implements ConfigurableEnum<AnonymousAccessPermission> {
	BY_DEFAULT        ("-", "anonymousAccessPermission.accessByDefault"),
	ACCESS_ALLOWED    ("T", "anonymousAccessPermission.accessAllowed"),
	ACCESS_NOT_ALLOWED("F", "anonymousAccessPermission.accessNotAllowed");

	@Getter
    @NotNull
	private final String value;

	@Getter
	@NotNull
	private final String messageName;

	private AnonymousAccessPermission(final String value, final String messageName) {
		this.value = value;
		this.messageName = messageName;
	}

	public static AnonymousAccessPermission findByValue(final String value) {
		final AnonymousAccessPermission anonymousAccessPermission;
		if(value != null) {
			anonymousAccessPermission = Constants.PARALLEL_STREAM_UTILS.ofEnum(AnonymousAccessPermission.class)
				.filter(anonymousAccessPermissionAux -> value.equals(anonymousAccessPermissionAux.value))
				.findFirst()
				.orElse(AnonymousAccessPermission.ACCESS_NOT_ALLOWED);
		} else {
			anonymousAccessPermission = AnonymousAccessPermission.BY_DEFAULT;
		}

		return anonymousAccessPermission;
	}

	@Override
	public ConfigurableEnum<AnonymousAccessPermission> getOverwrittenEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final AnonymousAccessPermission anonymousAccessPermission = configPropertiesBean.getDefaultAnonymousAccessPermissionOverwritten();
		return anonymousAccessPermission;
	}

	@Override
	public ConfigurableEnum<AnonymousAccessPermission> getOverwritableEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final AnonymousAccessPermission anonymousAccessPermission = configPropertiesBean.getDefaultAnonymousAccessPermission();
		return anonymousAccessPermission;
	}

	@Override
	public ConfigurableEnum<AnonymousAccessPermission> getFinalDefaultEnumElement() {
		return AnonymousAccessPermission.ACCESS_NOT_ALLOWED;
	}
}
