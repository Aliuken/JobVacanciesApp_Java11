package com.aliuken.jobvacanciesapp.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public enum AllowedViewsEnum implements Serializable {
	ANONYMOUS_ACCESS_BY_DEFAULT (AnonymousAccessPermission.BY_DEFAULT),
	ANONYMOUS_ACCESS_ALLOWED    (AnonymousAccessPermission.ACCESS_ALLOWED),
	ANONYMOUS_ACCESS_NOT_ALLOWED(AnonymousAccessPermission.ACCESS_NOT_ALLOWED);

	private final String[] VARIABLE_VIEWS_ARRAY = new String[]{
		"/",
		"/search",
		"/auth-users/view/**",
		"/job-categories/view/**",
		"/job-companies/view/**",
		"/job-vacancies/view/**"
	};

	private final String[] FIXED_ANONYMOUS_VIEWS_ARRAY = new String[]{
		"/login",
		"/logout",
		"/signup",
		"/signup-confirmed",
		"/forgotten-password",
		"/reset-password",
		"/about"
	};

	private final String[] FIXED_USER_VIEWS_ARRAY = new String[]{
		"/my-user/**",
		"/my-user/auth-user-curriculums/**",
		"/my-user/auth-user-entity-queries/**",
		"/job-requests/create/**",
		"/job-requests/save/**",
		"/job-requests/view/**"
	};

	private final String[] SUPERVISOR_VIEWS_ARRAY = new String[]{
		"/job-requests/**",
		"/job-vacancies/**",
		"/job-categories/**",
		"/job-companies/**"
	};

	private final String[] ADMINISTRATOR_VIEWS_ARRAY = new String[]{
		"/auth-users/**",
		"/my-user/app/**"
	};

	@Getter
    private final @NonNull AnonymousAccessPermission anonymousAccessPermission;

	@Getter
    @NotEmpty
	private final String[] anonymousViewsArray;

	@Getter
    @NotEmpty
	private final String[] userViewsArray;

	@Getter
    @NotEmpty
	private final String[] supervisorViewsArray;

	@Getter
    @NotEmpty
	private final String[] administratorViewsArray;

	private static final @NonNull Map<AnonymousAccessPermission, AllowedViewsEnum> ALLOWED_VIEWS_MAP = AllowedViewsEnum.getAllowedViewsMap();

	private AllowedViewsEnum(final AnonymousAccessPermission anonymousAccessPermission) {
		this.anonymousAccessPermission = Constants.ENUM_UTILS.getFinalEnumElement(anonymousAccessPermission, AnonymousAccessPermission.class);

		if(AnonymousAccessPermission.ACCESS_ALLOWED == this.anonymousAccessPermission) {
			this.anonymousViewsArray = Constants.PARALLEL_STREAM_UTILS.joinArrays(String[]::new, FIXED_ANONYMOUS_VIEWS_ARRAY, VARIABLE_VIEWS_ARRAY);
			this.userViewsArray = FIXED_USER_VIEWS_ARRAY;
		} else {
			this.anonymousViewsArray = FIXED_ANONYMOUS_VIEWS_ARRAY;
			this.userViewsArray = Constants.PARALLEL_STREAM_UTILS.joinArrays(String[]::new, FIXED_USER_VIEWS_ARRAY, VARIABLE_VIEWS_ARRAY);
		}

		this.supervisorViewsArray = SUPERVISOR_VIEWS_ARRAY;
		this.administratorViewsArray = ADMINISTRATOR_VIEWS_ARRAY;
	}

    public static AllowedViewsEnum getInstance(final AnonymousAccessPermission anonymousAccessPermission) {
		final AllowedViewsEnum allowedViewsEnum = ALLOWED_VIEWS_MAP.get(anonymousAccessPermission);
		return allowedViewsEnum;
	}

	private static @NonNull Map<AnonymousAccessPermission, AllowedViewsEnum> getAllowedViewsMap() {
		final AllowedViewsEnum[] allowedViewsEnumElements = AllowedViewsEnum.values();

		final Map<AnonymousAccessPermission, AllowedViewsEnum> allowedViewsMap = new HashMap<>();
		final Consumer<AllowedViewsEnum> allowedViewsEnumConsumer = (allowedViewsEnum -> {
			if(allowedViewsEnum != null) {
				final AnonymousAccessPermission anonymousAccessPermission = allowedViewsEnum.getAnonymousAccessPermission();
				allowedViewsMap.put(anonymousAccessPermission, allowedViewsEnum);
			}
		});

		final Stream<AllowedViewsEnum> allowedViewsEnumStream = Constants.PARALLEL_STREAM_UTILS.ofNullableArray(allowedViewsEnumElements);
		allowedViewsEnumStream.forEach(allowedViewsEnumConsumer);

		return allowedViewsMap;
	}
}
