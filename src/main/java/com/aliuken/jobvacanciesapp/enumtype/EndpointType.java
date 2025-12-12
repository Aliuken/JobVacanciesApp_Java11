package com.aliuken.jobvacanciesapp.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.dto.EndpointRegexPatternDTO;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

//NOTE: the regular expression "([^/]+)" matches with a path parameter (formed by consecutive characters without the "/" character)
@Slf4j
public enum EndpointType implements Serializable {
	AUTH_USER_SHOW_LIST(GET, "/auth-users/index", "Show user list"),
	AUTH_USER_EXPORT_TO_PDF(GET, "/auth-users/index/export-to-pdf", "Export user list to PDF"),
	AUTH_USER_SHOW_DETAIL(GET, "/auth-users/view/([^/]+)", "Show user detail"),
	AUTH_USER_DELETE(GET, "/auth-users/delete/([^/]+)", "Delete user"),
	AUTH_USER_LOCK(GET, "/auth-users/lock/([^/]+)", "Lock user"),
	AUTH_USER_UNLOCK(GET, "/auth-users/unlock/([^/]+)", "Unlock user"),
	AUTH_USER_JOB_REQUEST_SHOW_LIST(GET, "/auth-users/job-requests/([^/]+)", "Show user job request list"),
	AUTH_USER_JOB_REQUEST_EXPORT_TO_PDF(GET, "/auth-users/job-requests/([^/]+)/export-to-pdf", "Export user job request list to PDF"),
	AUTH_USER_JOB_REQUEST_DELETE(GET, "/auth-users/job-requests/delete/([^/]+)/([^/]+)", "Delete user job request"),
	HOME(GET, "/", "Home"),
	SIGN_UP_SHOW_FORM(GET, "/signup", "Show sign up form"),
	SIGN_UP_SAVE_FORM(POST, "/signup", "Save sign up form"),
	SIGN_UP_CONFIRM(GET, "/signup-confirmed", "Confirm sign up"),
	FORGOTTEN_PASSWORD_SHOW_FORM(GET, "/forgotten-password", "Show forgotten password form"),
	FORGOTTEN_PASSWORD_SAVE_FORM(POST, "/forgotten-password", "Save forgotten password form"),
	RESET_PASSWORD_SHOW_FORM(GET, "/reset-password", "Show reset password form"),
	RESET_PASSWORD_SAVE_FORM(POST, "/reset-password", "Save reset password form"),
	JOB_VACANCY_SEARCH(GET, "/search", "Search job vacancies"),
	ABOUT_COMPANY(GET, "/about", "About the company"),
	LOGIN_SHOW_FORM(GET, "/login", "Show login form"),
	LOGIN_USE_FORM(POST, "/login", "Use login form"),
	LOGOUT(GET, "/logout", "Logout"),
	JOB_CATEGORY_SHOW_LIST(GET, "/job-categories/index", "Show job category list"),
	JOB_CATEGORY_EXPORT_TO_PDF(GET, "/job-categories/index/export-to-pdf", "Export job category list to PDF"),
	JOB_CATEGORY_SHOW_DETAIL(GET, "/job-categories/view/([^/]+)", "Show job category detail"),
	JOB_CATEGORY_SHOW_INSERT_FORM(GET, "/job-categories/create", "Show job category insert form"),
	JOB_CATEGORY_SHOW_UPDATE_FORM(GET, "/job-categories/edit/([^/]+)", "Show job category update form"),
	JOB_CATEGORY_SAVE_FORM(POST, "/job-categories/save", "Save job category form"),
	JOB_CATEGORY_DELETE(GET, "/job-categories/delete/([^/]+)", "Delete job category"),
	JOB_CATEGORY_JOB_VACANCY_SHOW_LIST(GET, "/job-categories/job-vacancies/([^/]+)", "Show job category job vacancy list"),
	JOB_CATEGORY_JOB_VACANCY_EXPORT_TO_PDF(GET, "/job-categories/job-vacancies/([^/]+)/export-to-pdf", "Export job category job vacancy list to PDF"),
	JOB_CATEGORY_JOB_VACANCY_DELETE(GET, "/job-categories/job-vacancies/delete/([^/]+)/([^/]+)", "Delete job category job vacancy"),
	JOB_CATEGORY_JOB_VACANCY_VERIFY(GET, "/job-categories/job-vacancies/verify/([^/]+)/([^/]+)", "Verify job category job vacancy"),
	JOB_COMPANY_SHOW_LIST(GET, "/job-companies/index", "Show job company list"),
	JOB_COMPANY_EXPORT_TO_PDF(GET, "/job-companies/index/export-to-pdf", "Export job company list to PDF"),
	JOB_COMPANY_SHOW_DETAIL(GET, "/job-companies/view/([^/]+)", "Show job company detail"),
	JOB_COMPANY_SHOW_INSERT_FORM(GET, "/job-companies/create", "Show job company insert form"),
	JOB_COMPANY_SHOW_UPDATE_FORM(GET, "/job-companies/edit/([^/]+)", "Show job company update form"),
	JOB_COMPANY_REFRESH_LOGO(GET, "/job-companies/refresh-logo", "Refresh job company logo"),
	JOB_COMPANY_SAVE_FORM(POST, "/job-companies/save", "Save job company form"),
	JOB_COMPANY_DELETE(GET, "/job-companies/delete/([^/]+)", "Delete job company"),
	JOB_COMPANY_JOB_VACANCY_SHOW_LIST(GET, "/job-companies/job-vacancies/([^/]+)", "Show job company job vacancy list"),
	JOB_COMPANY_JOB_VACANCY_EXPORT_TO_PDF(GET, "/job-companies/job-vacancies/([^/]+)/export-to-pdf", "Export job company job vacancy list to PDF"),
	JOB_COMPANY_JOB_VACANCY_DELETE(GET, "/job-companies/job-vacancies/delete/([^/]+)/([^/]+)", "Delete job company job vacancy"),
	JOB_COMPANY_JOB_VACANCY_VERIFY(GET, "/job-companies/job-vacancies/verify/([^/]+)/([^/]+)", "Verify job company job vacancy"),
	JOB_COMPANY_JOB_COMPANY_LOGO_SHOW_LIST(GET, "/job-companies/job-company-logos/([^/]+)", "Show job company job company logos list"),
	JOB_COMPANY_JOB_COMPANY_LOGO_EXPORT_TO_PDF(GET, "/job-companies/job-company-logos/([^/]+)/export-to-pdf", "Export job company job company logos list to PDF"),
	JOB_COMPANY_JOB_COMPANY_LOGO_DELETE(GET, "/job-companies/job-company-logos/delete/([^/]+)/([^/]+)", "Delete job company job company logo"),
	JOB_REQUEST_SHOW_LIST(GET, "/job-requests/index", "Show job request list"),
	JOB_REQUEST_EXPORT_TO_PDF(GET, "/job-requests/index/export-to-pdf", "Export job request list to PDF"),
	JOB_REQUEST_SHOW_DETAIL(GET, "/job-requests/view/([^/]+)", "Show job request detail"),
	JOB_REQUEST_SHOW_INSERT_FORM(GET, "/job-requests/create/([^/]+)", "Show job request insert form"),
	JOB_REQUEST_SAVE_FORM(POST, "/job-requests/save", "Save job request form"),
	JOB_REQUEST_DELETE(GET, "/job-requests/delete/([^/]+)", "Delete job request"),
	JOB_VACANCY_SHOW_LIST(GET, "/job-vacancies/index", "Show job vacancy list"),
	JOB_VACANCY_EXPORT_TO_PDF(GET, "/job-vacancies/index/export-to-pdf", "Export job vacancy list to PDF"),
	JOB_VACANCY_SHOW_DETAIL(GET, "/job-vacancies/view/([^/]+)", "Show job vacancy detail"),
	JOB_VACANCY_SHOW_INSERT_FORM(GET, "/job-vacancies/create", "Show job vacancy insert form"),
	JOB_VACANCY_SHOW_UPDATE_FORM(GET, "/job-vacancies/edit/([^/]+)", "Show job vacancy update form"),
	JOB_VACANCY_REFRESH_LOGO(GET, "/job-vacancies/refresh-logo", "Refresh job vacancy logo"),
	JOB_VACANCY_SAVE_FORM(POST, "/job-vacancies/save", "Save job vacancy form"),
	JOB_VACANCY_DELETE(GET, "/job-vacancies/delete/([^/]+)", "Delete job vacancy"),
	JOB_VACANCY_VERIFY(GET, "/job-vacancies/verify/([^/]+)", "Verify job vacancy"),
	JOB_VACANCY_JOB_REQUEST_SHOW_LIST(GET, "/job-vacancies/job-requests/([^/]+)", "Show job vacancy job request list"),
	JOB_VACANCY_JOB_REQUEST_EXPORT_TO_PDF(GET, "/job-vacancies/job-requests/([^/]+)/export-to-pdf", "Export job vacancy job request list to PDF"),
	JOB_VACANCY_JOB_REQUEST_DELETE(GET, "/job-vacancies/job-requests/delete/([^/]+)/([^/]+)", "Delete job vacancy job request"),
	SESSION_AUTH_USER_SHOW_FORM(GET, "/my-user", "Show session user form"),
	SESSION_AUTH_USER_SAVE_FORM(POST, "/my-user", "Save session user form"),
	SESSION_AUTH_USER_SHOW_CHANGE_PASSWORD_FORM(GET, "/my-user/change-password", "Show session user change password form"),
	SESSION_AUTH_USER_SAVE_CHANGE_PASSWORD_FORM(POST, "/my-user/change-password", "Save session user change password form"),
	SESSION_AUTH_USER_CURRICULUM_SHOW_LIST(GET, "/my-user/auth-user-curriculums", "Show session user curriculum list"),
	SESSION_AUTH_USER_CURRICULUM_EXPORT_TO_PDF(GET, "/my-user/auth-user-curriculums/export-to-pdf", "Export session user curriculum list to PDF"),
	SESSION_AUTH_USER_CURRICULUM_SHOW_DETAIL(GET, "/my-user/auth-user-curriculums/view/([^/]+)", "Show session user curriculum detail"),
	SESSION_AUTH_USER_CURRICULUM_SHOW_INSERT_FORM(GET, "/my-user/auth-user-curriculums/create", "Show session user curriculum insert form"),
	SESSION_AUTH_USER_CURRICULUM_SAVE_FORM(POST, "/my-user/auth-user-curriculums/save", "Save session user curriculum form"),
	SESSION_AUTH_USER_CURRICULUM_DELETE(GET, "/my-user/auth-user-curriculums/delete/([^/]+)", "Delete session user curriculum"),
	SESSION_AUTH_USER_ENTITY_QUERY_SHOW_LIST(GET, "/my-user/auth-user-entity-queries", "Show session user entity query list"),
	SESSION_AUTH_USER_ENTITY_QUERY_EXPORT_TO_PDF(GET, "/my-user/auth-user-entity-queries/export-to-pdf", "Export session user entity query list to PDF"),
	SESSION_AUTH_USER_ENTITY_QUERY_SHOW_DETAIL(GET, "/my-user/auth-user-entity-queries/view/([^/]+)", "Show session user entity query detail"),
	SESSION_AUTH_USER_ENTITY_QUERY_DELETE(GET, "/my-user/auth-user-entity-queries/delete/([^/]+)", "Delete session user entity query"),
	SESSION_AUTH_USER_JOB_REQUEST_SHOW_LIST(GET, "/my-user/job-requests", "Show session user job request list"),
	SESSION_AUTH_USER_JOB_REQUEST_EXPORT_TO_PDF(GET, "/my-user/job-requests/export-to-pdf", "Export session user job request list to PDF"),
	SESSION_AUTH_USER_DELETE(GET, "/my-user/delete", "Delete session user"),
	APPLICATION_CONFIG_SHOW_FORM(GET, "/my-user/app/config", "Show application configuration form"),
	APPLICATION_CONFIG_SAVE_FORM(POST, "/my-user/app/config", "Save application configuration form");

	@Getter
	@NotNull
	private final EndpointRegexPatternDTO endpointRegexPatternDTO;

	private static final Map<EndpointRegexPatternDTO, EndpointType> ENDPOINT_TYPE_MAP = EndpointType.getEndpointTypeMap();

	private EndpointType(final HttpMethod httpMethod, final String pathRegex, final String description) {
		this.endpointRegexPatternDTO = EndpointRegexPatternDTO.getNewInstance(httpMethod, pathRegex, description);
	}

	public static EndpointType getInstance(final String httpMethod, String path) {
		if(path != null && !path.equals("/") && path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		}

		for(final Map.Entry<EndpointRegexPatternDTO, EndpointType> endpointTypeMapEntry : ENDPOINT_TYPE_MAP.entrySet()) {
			final EndpointRegexPatternDTO endpointRegexPatternDTO = endpointTypeMapEntry.getKey();

			final boolean matches = endpointRegexPatternDTO.matches(httpMethod, path);

			if(matches) {
				final EndpointType endpointType = endpointTypeMapEntry.getValue();

				if(log.isInfoEnabled()) {
					final String endpointRegexPatternString = endpointRegexPatternDTO.getEndpointRegexPatternAsString();
					final String endpointTypeString = (endpointType != null) ? endpointType.toString() : null;

					final String logTrace = StringUtils.getStringJoined(
						"  The endpoint '", httpMethod, Constants.SPACE, path, "' matched with the endpoint pattern '", endpointRegexPatternString, "'. The EndpointType ", endpointTypeString, " will be used");

					log.info(logTrace);
				}

				return endpointType;
			}
		}

		if(log.isInfoEnabled()) {
			final String logTrace = StringUtils.getStringJoined(
				"  The endpoint '", httpMethod, Constants.SPACE, path, "' didn't match with any path pattern. The EndpointType ", null, " will be used");

			log.info(logTrace);
		}

		return null;
	}

	private static Map<EndpointRegexPatternDTO, EndpointType> getEndpointTypeMap() {
		final EndpointType[] endpointTypes = EndpointType.values();

		final Map<EndpointRegexPatternDTO, EndpointType> endpointTypeMap = new HashMap<>();
		final Consumer<EndpointType> endpointTypeConsumer = (endpointType -> {
			if(endpointType != null) {
				final EndpointRegexPatternDTO endpointRegexPatternDTO = endpointType.getEndpointRegexPatternDTO();
				endpointTypeMap.put(endpointRegexPatternDTO, endpointType);
			}
		});

		final Stream<EndpointType> endpointTypeStream = Constants.PARALLEL_STREAM_UTILS.ofNullableArray(endpointTypes);
		endpointTypeStream.forEach(endpointTypeConsumer);

		return endpointTypeMap;
	}
}