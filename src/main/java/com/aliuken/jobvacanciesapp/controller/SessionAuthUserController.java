package com.aliuken.jobvacanciesapp.controller;

import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.AnonymousAccessPermission;
import com.aliuken.jobvacanciesapp.enumtype.UserInterfaceFramework;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.ApplicationConfigDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserCredentialsDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.AuthUserConverter;
import com.aliuken.jobvacanciesapp.model.dto.converter.AuthUserCredentialsConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.service.AuthUserCredentialsService;
import com.aliuken.jobvacanciesapp.service.AuthUserCurriculumService;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.FileUtils;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;
import com.aliuken.jobvacanciesapp.util.web.MvcUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SessionAuthUserController {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	@Autowired
	private AuthUserController authUserController;

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private AuthUserCredentialsService authUserCredentialsService;

	@Autowired
	private JobRequestService jobRequestService;

	@Autowired
	private AuthUserCurriculumService authUserCurriculumService;

	/**
	 * Method to show the edition form of the logged user
	 */
	@GetMapping("/my-user/auth-users")
	public String editUserForm(HttpServletRequest httpServletRequest, Model model,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /my-user/auth-users";

		final Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(httpServletRequest);

		AuthUserDTO authUserDTO;
		if(inputFlashMap != null) {
			authUserDTO = (AuthUserDTO) inputFlashMap.get("authUserDTO");
		} else {
			authUserDTO = null;
		}

		if(authUserDTO == null) {
			final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest, authUserService);
			authUserDTO = AuthUserConverter.getInstance().convertEntityElement(sessionAuthUser);
		}

		model.addAttribute("authUserDTO", authUserDTO);

		return MvcUtils.getNextView("authUser/sessionAuthUserForm.html", model, operation, languageCode);
	}

	/**
	 * Method to save the logged user in the database
	 */
	@PostMapping("/my-user/auth-users")
	public String saveUser(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
			@Validated AuthUserDTO authUserDTO, BindingResult bindingResult,
			@RequestParam(name="languageParam", required=false) String languageCode) throws MessagingException {
		try {
			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				redirectAttributes.addFlashAttribute("authUserDTO", authUserDTO);
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.authUserDTO", bindingResult);

				return MvcUtils.getNextRedirect("/my-user/auth-users", languageCode);
			}

			final String name = authUserDTO.getName();
			final String surnames = authUserDTO.getSurnames();
			final Boolean enabled = authUserDTO.getEnabled();

			final Integer initialTablePageSizeValue = authUserDTO.getInitialTablePageSizeValue();
			TablePageSize initialTablePageSize = TablePageSize.findByValue(initialTablePageSizeValue);
			if(initialTablePageSize == null) {
				initialTablePageSize = TablePageSize.BY_DEFAULT;
			}

			final String colorModeCode = authUserDTO.getColorModeCode();
			ColorMode colorMode = ColorMode.findByCode(colorModeCode);
			if(colorMode == null) {
				colorMode = ColorMode.BY_DEFAULT;
			}

			final Language language = Language.findByCode(languageCode);

			AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest, authUserService);
			sessionAuthUser.setName(name);
			sessionAuthUser.setSurnames(surnames);
			sessionAuthUser.setLanguage(language);
			sessionAuthUser.setEnabled(enabled);
			sessionAuthUser.setInitialTablePageSize(initialTablePageSize);
			sessionAuthUser.setColorMode(colorMode);
			sessionAuthUser = authUserService.saveAndFlush(sessionAuthUser);

			httpServletRequest.getSession().setAttribute(Constants.SESSION_AUTH_USER, sessionAuthUser);

			final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "saveUser.successMsg", null);
			redirectAttributes.addFlashAttribute("successMsg", successMsg);

			return MvcUtils.getNextRedirect("/auth-users/view/" + sessionAuthUser.getId(), languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("authUserDTO", authUserDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextRedirect("/my-user/auth-users", languageCode);
		}
	}

	/**
	 * Method to show the change-password form of the logged user
	 */
	@GetMapping("/my-user/auth-users/change-password")
	public String changePasswordForm(HttpServletRequest httpServletRequest, Model model,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /my-user/auth-users/change-password";

		final Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(httpServletRequest);

		AuthUserCredentialsDTO authUserCredentialsDTO;
		if(inputFlashMap != null) {
			authUserCredentialsDTO = (AuthUserCredentialsDTO) inputFlashMap.get("authUserCredentialsDTO");
		} else {
			authUserCredentialsDTO = null;
		}

		if(authUserCredentialsDTO == null) {
			final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest, authUserService);
			final AuthUserCredentials authUserCredentials = authUserCredentialsService.findByEmail(sessionAuthUser.getEmail());
			authUserCredentialsDTO = AuthUserCredentialsConverter.getInstance().convertEntityElement(authUserCredentials);
		}

		model.addAttribute("authUserCredentialsDTO", authUserCredentialsDTO);

		return MvcUtils.getNextView("authUser/sessionAuthUserChangePasswordForm.html", model, operation, languageCode);
	}

	/**
	 * Method to save the changed password of the logged user in the database
	 */
	@PostMapping("/my-user/auth-users/change-password")
	public String saveNewPassword(HttpServletRequest httpServletRequest, Model model, RedirectAttributes redirectAttributes,
			@Validated AuthUserCredentialsDTO authUserCredentialsDTO, BindingResult bindingResult,
			@RequestParam(name="languageParam", required=false) String languageCode) throws MessagingException {
		try {
			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.authUserCredentialsDTO", bindingResult);

				return MvcUtils.getNextRedirect("/my-user/auth-users/change-password", languageCode);
			}

			final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest, authUserService);
			final String email = sessionAuthUser.getEmail();
			final String password = authUserCredentialsDTO.getPassword();
			final String newPassword1 = authUserCredentialsDTO.getNewPassword1();
			final String newPassword2 = authUserCredentialsDTO.getNewPassword2();

			if(!newPassword1.equals(newPassword2)) {
				final String errorMsg = StringUtils.getInternationalizedMessage(languageCode, "saveNewPassword.newPasswordsDontMatch", null);

				redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return MvcUtils.getNextRedirect("/my-user/auth-users/change-password", languageCode);
			}
			if(password.equals(newPassword1)) {
				final String errorMsg = StringUtils.getInternationalizedMessage(languageCode, "saveNewPassword.newPasswordUnchanged", null);

				redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return MvcUtils.getNextRedirect("/my-user/auth-users/change-password", languageCode);
			}

			AuthUserCredentials authUserCredentials = authUserCredentialsService.findByEmail(email);
			if(authUserCredentials == null) {
				final String errorMsg = StringUtils.getInternationalizedMessage(languageCode, "saveNewPassword.emailOrPasswordIncorrect", new Object[]{email});

				redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return MvcUtils.getNextRedirect("/my-user/auth-users/change-password", languageCode);
			}
			if(!passwordEncoder.matches(password, authUserCredentials.getEncryptedPassword())) {
				final String errorMsg = StringUtils.getInternationalizedMessage(languageCode, "saveNewPassword.emailOrPasswordIncorrect", new Object[]{email});

				redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return MvcUtils.getNextRedirect("/my-user/auth-users/change-password", languageCode);
			}
			final String encryptedNewPassword = passwordEncoder.encode(newPassword1);
			authUserCredentials.setEncryptedPassword(encryptedNewPassword);
			authUserCredentials = authUserCredentialsService.saveAndFlush(authUserCredentials);

			final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "saveNewPassword.successMsg", null);

			redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
			redirectAttributes.addFlashAttribute("successMsg", successMsg);

			return MvcUtils.getNextRedirect("/my-user/auth-users/change-password", languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextRedirect("/my-user/auth-users/change-password", languageCode);
		}
	}

	/**
	 * Method to show the list of job requests of the logged user with pagination
	 */
	@GetMapping("/my-user/auth-users/job-requests")
	public String getJobRequests(HttpServletRequest httpServletRequest, Model model, Pageable pageable,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /my-user/auth-users/job-requests";

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest, authUserService);
		final Long sessionAuthUserId = sessionAuthUser.getId();
		final String sessionAuthUserEmail = sessionAuthUser.getEmail();

		try {
			if(tableSearchDTO == null || tableSearchDTO.hasEmptyAttribute()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDTOString = Objects.toString(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDTOString));
				}

				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("authUserId", sessionAuthUserId);
				model.addAttribute("authUserEmail", sessionAuthUserEmail);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", "/my-user/auth-users/job-requests");

				return MvcUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, false);
			}

			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("authUserId", sessionAuthUserId);
				model.addAttribute("authUserEmail", sessionAuthUserEmail);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", "/my-user/auth-users/job-requests");
				model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);

				return MvcUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobRequest> pageWithExceptionDTO = jobRequestService.getAuthUserEntityPage(sessionAuthUserId, tableSearchDTO, pageable);
			final Page<JobRequest> jobRequests = pageWithExceptionDTO.getPage();
			final Exception exception = pageWithExceptionDTO.getException();

			model.addAttribute("authUserId", sessionAuthUserId);
			model.addAttribute("authUserEmail", sessionAuthUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", "/my-user/auth-users/job-requests");

			if(exception != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return MvcUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<JobRequest> jobRequests = Page.empty();
			model.addAttribute("authUserId", sessionAuthUserId);
			model.addAttribute("authUserEmail", sessionAuthUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", "/my-user/auth-users/job-requests");

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to show the list of curriculums of the logged user with pagination
	 */
	@GetMapping("/my-user/auth-users/auth-user-curriculums")
	public String getAuthUserCurriculums(HttpServletRequest httpServletRequest, Model model, Pageable pageable,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /my-user/auth-users/auth-user-curriculums";

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest, authUserService);
		final Long sessionAuthUserId = sessionAuthUser.getId();
		final String sessionAuthUserEmail = sessionAuthUser.getEmail();

		try {
			if(tableSearchDTO == null || tableSearchDTO.hasEmptyAttribute()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDTOString = Objects.toString(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDTOString));
				}

				final Page<AuthUserCurriculum> authUserCurriculums = Page.empty();
				model.addAttribute("authUserId", sessionAuthUserId);
				model.addAttribute("authUserEmail", sessionAuthUserEmail);
				model.addAttribute("authUserCurriculums", authUserCurriculums);

				return MvcUtils.getNextViewWithTable("authUser/sessionAuthUserCurriculums.html", model, operation, tableSearchDTO, false);
			}

			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				final Page<AuthUserCurriculum> authUserCurriculums = Page.empty();
				model.addAttribute("authUserId", sessionAuthUserId);
				model.addAttribute("authUserEmail", sessionAuthUserEmail);
				model.addAttribute("authUserCurriculums", authUserCurriculums);
				model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);

				return MvcUtils.getNextViewWithTable("authUser/sessionAuthUserCurriculums.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<AuthUserCurriculum> pageWithExceptionDTO = authUserCurriculumService.getAuthUserEntityPage(sessionAuthUserId, tableSearchDTO, pageable);
			final Page<AuthUserCurriculum> authUserCurriculums = pageWithExceptionDTO.getPage();
			final Exception exception = pageWithExceptionDTO.getException();

			model.addAttribute("authUserId", sessionAuthUserId);
			model.addAttribute("authUserEmail", sessionAuthUserEmail);
			model.addAttribute("authUserCurriculums", authUserCurriculums);

			if(exception != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return MvcUtils.getNextViewWithTable("authUser/sessionAuthUserCurriculums.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<AuthUserCurriculum> authUserCurriculums = Page.empty();
			model.addAttribute("authUserId", sessionAuthUserId);
			model.addAttribute("authUserEmail", sessionAuthUserEmail);
			model.addAttribute("authUserCurriculums", authUserCurriculums);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextViewWithTable("authUser/sessionAuthUserCurriculums.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to delete a user
	 */
	@GetMapping("/my-user/auth-users/delete")
	public String deleteById(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
			@RequestParam(name="languageParam", required=false) String languageCode) {

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest, authUserService);
		final Long sessionAuthUserId = sessionAuthUser.getId();

		authUserController.deleteById(sessionAuthUserId);

		final String authUserCurriculumFilesPath = configPropertiesBean.getAuthUserCurriculumFilesPath();
		final String sessionAuthUserIdString = Long.toString(sessionAuthUserId);
		final Path finalFilePath = Path.of(authUserCurriculumFilesPath, sessionAuthUserIdString);
		FileUtils.deletePathRecursively(finalFilePath);

		httpServletRequest.getSession().setAttribute(Constants.SESSION_ACCOUNT_DELETED, Boolean.TRUE);

		return MvcUtils.getNextRedirect("/logout", languageCode);
	}

	/**
	 * Method to show the configure-application form
	 */
	@GetMapping("/my-user/application/configure")
	public String configureApplicationForm(HttpServletRequest httpServletRequest, Model model,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /my-user/application/configure";

		final Language defaultLanguage = configPropertiesBean.getCurrentDefaultLanguage();
		final String defaultLanguageCode = defaultLanguage.getCode();

		final AnonymousAccessPermission anonymousAccessPermission = configPropertiesBean.getAnonymousAccessPermissionOverwritten();
		final String anonymousAccessPermissionName = anonymousAccessPermission.name();

		final TablePageSize defaultInitialTablePageSize = configPropertiesBean.getDefaultInitialTablePageSizeOverwritten();
		final String defaultInitialTablePageSizeValue = String.valueOf(defaultInitialTablePageSize.getValue());

		final ColorMode defaultColorMode = configPropertiesBean.getDefaultColorModeOverwritten();
		final String defaultColorModeCode = defaultColorMode.getCode();

		final UserInterfaceFramework userInterfaceFramework = configPropertiesBean.getCurrentUserInterfaceFramework();
		final String userInterfaceFrameworkCode = userInterfaceFramework.getCode();

		final ApplicationConfigDTO applicationConfigDTO = new ApplicationConfigDTO(defaultLanguageCode, anonymousAccessPermissionName, defaultInitialTablePageSizeValue, defaultColorModeCode, userInterfaceFrameworkCode);
		model.addAttribute("applicationConfigDTO", applicationConfigDTO);

		return MvcUtils.getNextView("app/configureApplicationForm.html", model, operation, languageCode);
	}

	/**
	 * Method to save the application configuration by restarting the application
	 */
	@PostMapping("/my-user/application/configure")
	public String saveApplicationConfiguration(HttpServletRequest httpServletRequest, Model model, RedirectAttributes redirectAttributes,
			@Validated ApplicationConfigDTO applicationConfigDTO, BindingResult bindingResult,
			@RequestParam(name="languageParam", required=false) String languageCode) throws MessagingException {
		try {
			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				redirectAttributes.addFlashAttribute("applicationConfigDTO", applicationConfigDTO);
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.applicationConfigDTO", bindingResult);

				return MvcUtils.getNextRedirect("/my-user/application/configure", languageCode);
			}

			final String nextDefaultLanguageCode = applicationConfigDTO.getNextDefaultLanguageCode();
			final Language nextDefaultLanguage = Language.findByCode(nextDefaultLanguageCode);

			final String nextAnonymousAccessPermissionName = applicationConfigDTO.getNextAnonymousAccessPermissionName();
			final AnonymousAccessPermission nextAnonymousAccessPermission = AnonymousAccessPermission.valueOf(nextAnonymousAccessPermissionName);

			final String nextDefaultInitialTablePageSizeValue = applicationConfigDTO.getNextDefaultInitialTablePageSizeValue();
			final TablePageSize nextDefaultInitialTablePageSize = TablePageSize.findByValue(Integer.valueOf(nextDefaultInitialTablePageSizeValue));

			final String nextDefaultColorModeCode = applicationConfigDTO.getNextDefaultColorModeCode();
			final ColorMode nextDefaultColorMode = ColorMode.findByCode(nextDefaultColorModeCode);

			final String nextUserInterfaceFrameworkCode = applicationConfigDTO.getNextUserInterfaceFrameworkCode();
			final UserInterfaceFramework nextUserInterfaceFramework = UserInterfaceFramework.findByCode(nextUserInterfaceFrameworkCode);

			final boolean hasChangedCurrentDefaultLanguage = this.hasChangedCurrentDefaultLanguage(nextDefaultLanguage);
			final boolean hasChangedAnonymousAccessPermissionOverwritten = this.hasChangedAnonymousAccessPermissionOverwritten(nextAnonymousAccessPermission);
			final boolean hasChangedDefaultInitialTablePageSizeOverwritten = this.hasChangedDefaultInitialTablePageSizeOverwritten(nextDefaultInitialTablePageSize);
			final boolean hasChangedDefaultColorModeOverwritten = this.hasChangedDefaultColorModeOverwritten(nextDefaultColorMode);
			final boolean hasChangedCurrentUserInterfaceFramework = this.hasChangedCurrentUserInterfaceFramework(nextUserInterfaceFramework);

			final boolean hasChangedProperty = (hasChangedCurrentDefaultLanguage || hasChangedAnonymousAccessPermissionOverwritten || hasChangedDefaultInitialTablePageSizeOverwritten || hasChangedDefaultColorModeOverwritten || hasChangedCurrentUserInterfaceFramework);

			if(hasChangedProperty) {
				return MvcUtils.getNextRedirect("/logout", languageCode, nextDefaultLanguage, nextAnonymousAccessPermission, nextDefaultInitialTablePageSize, nextDefaultColorMode, nextUserInterfaceFramework);
			} else {
				return MvcUtils.getNextRedirect("/logout", languageCode);
			}

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("applicationConfigDTO", applicationConfigDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextRedirect("/my-user/application/configure", languageCode);
		}
	}

	private boolean hasChangedCurrentDefaultLanguage(final Language nextDefaultLanguage) {
		final Language currentDefaultLanguage = configPropertiesBean.getCurrentDefaultLanguage();
		final boolean result = (nextDefaultLanguage != currentDefaultLanguage);
		return result;
	}

	private boolean hasChangedAnonymousAccessPermissionOverwritten(final AnonymousAccessPermission nextAnonymousAccessPermission) {
		final AnonymousAccessPermission currentAnonymousAccessPermissionOverwritten = configPropertiesBean.getAnonymousAccessPermissionOverwritten();
		final boolean result = (nextAnonymousAccessPermission != currentAnonymousAccessPermissionOverwritten);
		return result;
	}

	private boolean hasChangedDefaultInitialTablePageSizeOverwritten(final TablePageSize nextDefaultInitialTablePageSize) {
		final TablePageSize currentDefaultInitialTablePageSizeOverwritten = configPropertiesBean.getDefaultInitialTablePageSizeOverwritten();
		final boolean result = (nextDefaultInitialTablePageSize != currentDefaultInitialTablePageSizeOverwritten);
		return result;
	}

	private boolean hasChangedDefaultColorModeOverwritten(final ColorMode nextDefaultColorMode) {
		final ColorMode currentDefaultColorModeOverwritten = configPropertiesBean.getDefaultColorModeOverwritten();
		final boolean result = (nextDefaultColorMode != currentDefaultColorModeOverwritten);
		return result;
	}

	private boolean hasChangedCurrentUserInterfaceFramework(final UserInterfaceFramework nextUserInterfaceFramework) {
		final UserInterfaceFramework currentUserInterfaceFramework = configPropertiesBean.getCurrentUserInterfaceFramework();
		final boolean result = (nextUserInterfaceFramework != currentUserInterfaceFramework);
		return result;
	}

}
