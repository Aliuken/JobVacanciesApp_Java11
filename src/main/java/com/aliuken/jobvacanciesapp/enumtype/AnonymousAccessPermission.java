package com.aliuken.jobvacanciesapp.enumtype;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import com.aliuken.jobvacanciesapp.util.javase.stream.StreamStaticUtils;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

public enum AnonymousAccessPermission implements ConfigurableEnum<String,AnonymousAccessPermission> {
	BY_DEFAULT        ("-", "anonymousAccessPermission.accessByDefault"),
	ACCESS_ALLOWED    ("T", "anonymousAccessPermission.accessAllowed"),
	ACCESS_NOT_ALLOWED("F", "anonymousAccessPermission.accessNotAllowed");

	@Getter
	private final @NonNull String code;

	@Getter
	private final @NonNull String messageName;

	private AnonymousAccessPermission(final @NonNull String code, final @NonNull String messageName) {
		this.code = code;
		this.messageName = messageName;
	}

	public static @NonNull AnonymousAccessPermission findByValue(final String value) {
		final AnonymousAccessPermission anonymousAccessPermission;
		if(value != null) {
			anonymousAccessPermission = StreamStaticUtils.ofEnum(AnonymousAccessPermission.class, false)
				.filter(anonymousAccessPermissionAux -> value.equals(anonymousAccessPermissionAux.code))
				.findFirst()
				.orElse(AnonymousAccessPermission.ACCESS_NOT_ALLOWED);
		} else {
			anonymousAccessPermission = AnonymousAccessPermission.BY_DEFAULT;
		}

		return anonymousAccessPermission;
	}

	@Override
	public AnonymousAccessPermission getOverwrittenEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final AnonymousAccessPermission anonymousAccessPermission = configPropertiesBean.getDefaultAnonymousAccessPermissionOverwritten();
		return anonymousAccessPermission;
	}

	@Override
	public AnonymousAccessPermission getOverwritableEnumElement(final @NonNull ConfigPropertiesBean configPropertiesBean) {
		final AnonymousAccessPermission anonymousAccessPermission = configPropertiesBean.getDefaultAnonymousAccessPermission();
		return anonymousAccessPermission;
	}

	@Override
	public @NonNull AnonymousAccessPermission getFinalDefaultEnumElement() {
		return AnonymousAccessPermission.ACCESS_NOT_ALLOWED;
	}
}
