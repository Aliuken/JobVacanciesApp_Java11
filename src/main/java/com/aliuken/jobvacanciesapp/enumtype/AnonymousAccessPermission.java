package com.aliuken.jobvacanciesapp.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

public enum AnonymousAccessPermission implements ConfigurableEnum<AnonymousAccessPermission> {
	BY_DEFAULT        ("-", "anonymousAccessPermission.accessByDefault"),
	ACCESS_ALLOWED    ("T", "anonymousAccessPermission.accessAllowed"),
	ACCESS_NOT_ALLOWED("F", "anonymousAccessPermission.accessNotAllowed");

	@Getter
	private final @NonNull String value;

	@Getter
	private final @NonNull String messageName;

	private AnonymousAccessPermission(final @NonNull String value, final @NonNull String messageName) {
		this.value = value;
		this.messageName = messageName;
	}

	public static @NonNull AnonymousAccessPermission findByValue(final String value) {
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
	public @NonNull Class<AnonymousAccessPermission> getEnumClass() {
		return AnonymousAccessPermission.class;
	}

	@Override
	public ConfigurableEnum<AnonymousAccessPermission> getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final AnonymousAccessPermission anonymousAccessPermission = configPropertiesBean.getDefaultAnonymousAccessPermissionOverwritten();
		return anonymousAccessPermission;
	}

	@Override
	public ConfigurableEnum<AnonymousAccessPermission> getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final AnonymousAccessPermission anonymousAccessPermission = configPropertiesBean.getDefaultAnonymousAccessPermission();
		return anonymousAccessPermission;
	}

	@Override
	public @NonNull ConfigurableEnum<AnonymousAccessPermission> getFinalDefaultEnumElement() {
		return AnonymousAccessPermission.ACCESS_NOT_ALLOWED;
	}
}
