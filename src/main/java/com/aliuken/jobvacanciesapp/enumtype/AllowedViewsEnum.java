package com.aliuken.jobvacanciesapp.enumtype;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.validation.constraints.NotEmpty;

import com.aliuken.jobvacanciesapp.Constants;

public enum AllowedViewsEnum implements Serializable {
	ANONYMOUS_ACCESS_ALLOWED(true),
	ANONYMOUS_ACCESS_NOT_ALLOWED(false);

	private final String[] VARIABLE_VIEWS_ARRAY = new String[]{"/", "/search", "/auth-users/view/**", "/job-categories/view/**", "/job-companies/view/**", "/job-vacancies/view/**"};
	private final String[] FIXED_ANONYMOUS_VIEWS_ARRAY = new String[]{"/login", "/logout", "/signup", "/signup-confirmed", "/about"};
	private final String[] FIXED_USER_VIEWS_ARRAY = new String[]{"/my-user/auth-users/**", "/my-user/auth-user-curriculums/**", "/job-requests/create/**", "/job-requests/save/**", "/job-requests/view/**"};
	private final String[] SUPERVISOR_VIEWS_ARRAY = new String[]{"/job-requests/**", "/job-vacancies/**", "/job-categories/**", "/job-companies/**"};
	private final String[] ADMINISTRATOR_VIEWS_ARRAY = new String[]{"/auth-users/**", "/my-user/application/**"};

	private final boolean anonymousAccessAllowed;

	@NotEmpty
	private final String[] anonymousViewsArray;

	@NotEmpty
	private final String[] userViewsArray;

	@NotEmpty
	private final String[] supervisorViewsArray;

	@NotEmpty
	private final String[] administratorViewsArray;

	private static final Map<Boolean, AllowedViewsEnum> ALLOWED_VIEWS_MAP = AllowedViewsEnum.getAllowedViewsMap();

	private AllowedViewsEnum(final boolean anonymousAccessAllowed) {
		this.anonymousAccessAllowed = anonymousAccessAllowed;

		if(anonymousAccessAllowed) {
			this.anonymousViewsArray = Constants.PARALLEL_STREAM_UTILS.ofArrays(String[]::new, FIXED_ANONYMOUS_VIEWS_ARRAY, VARIABLE_VIEWS_ARRAY);
			this.userViewsArray = FIXED_USER_VIEWS_ARRAY;
		} else {
			this.anonymousViewsArray = FIXED_ANONYMOUS_VIEWS_ARRAY;
			this.userViewsArray = Constants.PARALLEL_STREAM_UTILS.ofArrays(String[]::new, FIXED_USER_VIEWS_ARRAY, VARIABLE_VIEWS_ARRAY);
		}

		this.supervisorViewsArray = SUPERVISOR_VIEWS_ARRAY;
		this.administratorViewsArray = ADMINISTRATOR_VIEWS_ARRAY;
	}

	public boolean isAnonymousAccessAllowed() {
		return anonymousAccessAllowed;
	}

	public String[] getAnonymousViewsArray() {
		return anonymousViewsArray;
	}

	public String[] getUserViewsArray() {
		return userViewsArray;
	}

	public String[] getSupervisorViewsArray() {
		return supervisorViewsArray;
	}

	public String[] getAdministratorViewsArray() {
		return administratorViewsArray;
	}

	public static AllowedViewsEnum getInstance(final boolean anonymousAccessAllowed) {
		final AllowedViewsEnum allowedViewsEnum = ALLOWED_VIEWS_MAP.get(anonymousAccessAllowed);
		return allowedViewsEnum;
	}

	private static Map<Boolean, AllowedViewsEnum> getAllowedViewsMap() {
		final AllowedViewsEnum[] allowedViewsEnums = AllowedViewsEnum.values();

		final Map<Boolean, AllowedViewsEnum> allowedViewsMap = new HashMap<>();
		final Consumer<AllowedViewsEnum> allowedViewsEnumConsumer = (allowedViewsEnum -> {
			if(allowedViewsEnum != null) {
				final boolean anonymousAccessAllowed = allowedViewsEnum.isAnonymousAccessAllowed();
				allowedViewsMap.put(anonymousAccessAllowed, allowedViewsEnum);
			}
		});

		final Stream<AllowedViewsEnum> allowedViewsEnumStream = Constants.PARALLEL_STREAM_UTILS.ofNullableArray(allowedViewsEnums);
		allowedViewsEnumStream.forEach(allowedViewsEnumConsumer);

		return allowedViewsMap;
	}

}
