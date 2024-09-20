package com.aliuken.jobvacanciesapp.controller;

import java.nio.file.Path;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.AnonymousAccessPermission;
import com.aliuken.jobvacanciesapp.enumtype.UserInterfaceFramework;
import com.aliuken.jobvacanciesapp.model.dto.ApplicationDefaultConfigDTO;
import com.aliuken.jobvacanciesapp.model.dto.ApplicationNextConfigDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserCredentialsDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.AuthUserConverter;
import com.aliuken.jobvacanciesapp.model.dto.converter.AuthUserCredentialsConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.service.AuthUserCredentialsService;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.file.FileUtils;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;
import com.aliuken.jobvacanciesapp.util.web.MvcUtils;
import com.aliuken.jobvacanciesapp.util.web.ServletUtils;

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

	/**
	 * Method to show the edition form of the logged user
	 */
	@GetMapping("/my-user")
	public String editUserForm(HttpServletRequest httpServletRequest, Model model,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /my-user";

		final Map<String, ?> inputFlashMap = ServletUtils.getInputFlashMap(httpServletRequest);

		AuthUserDTO authUserDTO;
		if(inputFlashMap != null) {
			authUserDTO = (AuthUserDTO) inputFlashMap.get("authUserDTO");
		} else {
			authUserDTO = null;
		}

		if(authUserDTO == null) {
			final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);
			authUserDTO = AuthUserConverter.getInstance().convertEntityElement(sessionAuthUser);
		}

		model.addAttribute("authUserDTO", authUserDTO);

		return MvcUtils.getNextView("authUser/sessionAuthUserForm.html", model, operation, languageCode);
	}

	/**
	 * Method to save the logged user in the database
	 */
	@PostMapping("/my-user")
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

				return MvcUtils.getNextRedirect("/my-user", languageCode);
			}

			final String name = authUserDTO.getName();
			final String surnames = authUserDTO.getSurnames();
			final String authUserLanguageCode = authUserDTO.getLanguageCode();
			final Language authUserLanguage = Language.findByCode(authUserLanguageCode);
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

			final String pdfDocumentPageFormatCode = authUserDTO.getPdfDocumentPageFormatCode();
			PdfDocumentPageFormat pdfDocumentPageFormat = PdfDocumentPageFormat.findByCode(pdfDocumentPageFormatCode);
			if(pdfDocumentPageFormat == null) {
				pdfDocumentPageFormat = PdfDocumentPageFormat.BY_DEFAULT;
			}

			AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);
			sessionAuthUser.setName(name);
			sessionAuthUser.setSurnames(surnames);
			sessionAuthUser.setLanguage(authUserLanguage);
			sessionAuthUser.setEnabled(enabled);
			sessionAuthUser.setInitialTablePageSize(initialTablePageSize);
			sessionAuthUser.setColorMode(colorMode);
			sessionAuthUser.setPdfDocumentPageFormat(pdfDocumentPageFormat);
			sessionAuthUser = authUserService.saveAndFlush(sessionAuthUser);

			final Language finalAuthUserLanguage = Constants.ENUM_UTILS.getFinalEnumElement(authUserLanguage, Language.class);
			languageCode = finalAuthUserLanguage.getCode();

			httpServletRequest.getSession().setAttribute(Constants.SESSION_AUTH_USER, sessionAuthUser);

			final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "saveUser.successMsg", null);
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

			return MvcUtils.getNextRedirect("/my-user", languageCode);
		}
	}

	/**
	 * Method to show the change-password form of the logged user
	 */
	@GetMapping("/my-user/change-password")
	public String changePasswordForm(HttpServletRequest httpServletRequest, Model model,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /my-user/change-password";

		final Map<String, ?> inputFlashMap = ServletUtils.getInputFlashMap(httpServletRequest);

		AuthUserCredentialsDTO authUserCredentialsDTO;
		if(inputFlashMap != null) {
			authUserCredentialsDTO = (AuthUserCredentialsDTO) inputFlashMap.get("authUserCredentialsDTO");
		} else {
			authUserCredentialsDTO = null;
		}

		if(authUserCredentialsDTO == null) {
			final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);
			final AuthUserCredentials authUserCredentials = authUserCredentialsService.findByEmail(sessionAuthUser.getEmail());
			authUserCredentialsDTO = AuthUserCredentialsConverter.getInstance().convertEntityElement(authUserCredentials);
		}

		model.addAttribute("authUserCredentialsDTO", authUserCredentialsDTO);

		return MvcUtils.getNextView("authUser/sessionAuthUserChangePasswordForm.html", model, operation, languageCode);
	}

	/**
	 * Method to save the changed password of the logged user in the database
	 */
	@PostMapping("/my-user/change-password")
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

				return MvcUtils.getNextRedirect("/my-user/change-password", languageCode);
			}

			final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);
			final String email = sessionAuthUser.getEmail();
			final String password = authUserCredentialsDTO.getPassword();
			final String newPassword1 = authUserCredentialsDTO.getNewPassword1();
			final String newPassword2 = authUserCredentialsDTO.getNewPassword2();

			if(!newPassword1.equals(newPassword2)) {
				final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "saveNewPassword.newPasswordsDontMatch", null);

				redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return MvcUtils.getNextRedirect("/my-user/change-password", languageCode);
			}
			if(password.equals(newPassword1)) {
				final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "saveNewPassword.newPasswordUnchanged", null);

				redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return MvcUtils.getNextRedirect("/my-user/change-password", languageCode);
			}

			AuthUserCredentials authUserCredentials = authUserCredentialsService.findByEmail(email);
			if(authUserCredentials == null) {
				final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "saveNewPassword.emailOrPasswordIncorrect", new Object[]{email});

				redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return MvcUtils.getNextRedirect("/my-user/change-password", languageCode);
			}
			if(!passwordEncoder.matches(password, authUserCredentials.getEncryptedPassword())) {
				final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "saveNewPassword.emailOrPasswordIncorrect", new Object[]{email});

				redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return MvcUtils.getNextRedirect("/my-user/change-password", languageCode);
			}
			final String encryptedNewPassword = passwordEncoder.encode(newPassword1);
			authUserCredentials.setEncryptedPassword(encryptedNewPassword);
			authUserCredentials = authUserCredentialsService.saveAndFlush(authUserCredentials);

			final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "saveNewPassword.successMsg", null);

			redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
			redirectAttributes.addFlashAttribute("successMsg", successMsg);

			return MvcUtils.getNextRedirect("/my-user/change-password", languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("authUserCredentialsDTO", authUserCredentialsDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextRedirect("/my-user/change-password", languageCode);
		}
	}

	/**
	 * Method to delete a user
	 */
	@GetMapping("/my-user/delete")
	public String deleteById(HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
			@RequestParam(name="languageParam", required=false) String languageCode) {

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);
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

		final ApplicationDefaultConfigDTO applicationDefaultConfigDTO = this.getApplicationDefaultConfigDTO();
		model.addAttribute("applicationDefaultConfigDTO", applicationDefaultConfigDTO);

		final ApplicationNextConfigDTO initialApplicationNextConfigDTO = this.getInitialApplicationNextConfigDTO();
		model.addAttribute("applicationNextConfigDTO", initialApplicationNextConfigDTO);

		return MvcUtils.getNextView("app/configureApplicationForm.html", model, operation, languageCode);
	}

	/**
	 * Method to save the application configuration by restarting the application
	 */
	@PostMapping("/my-user/application/configure")
	public String saveApplicationConfiguration(HttpServletRequest httpServletRequest, Model model, RedirectAttributes redirectAttributes,
			@Validated ApplicationNextConfigDTO applicationNextConfigDTO, BindingResult bindingResult,
			@RequestParam(name="languageParam", required=false) String languageCode) throws MessagingException {
		try {
			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				redirectAttributes.addFlashAttribute("applicationNextConfigDTO", applicationNextConfigDTO);
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.applicationNextConfigDTO", bindingResult);

				return MvcUtils.getNextRedirect("/my-user/application/configure", languageCode);
			}

			final ApplicationNextConfigDTO initialApplicationNextConfigDTO = this.getInitialApplicationNextConfigDTO();

			final boolean hasChangedProperty = !initialApplicationNextConfigDTO.equals(applicationNextConfigDTO);

			if(hasChangedProperty) {
				final String nextDefaultLanguageCode = applicationNextConfigDTO.getNextDefaultLanguageCode();
				final Language nextDefaultLanguage = Language.findByCode(nextDefaultLanguageCode);

				final String nextDefaultAnonymousAccessPermissionValue = applicationNextConfigDTO.getNextDefaultAnonymousAccessPermissionValue();
				final AnonymousAccessPermission nextAnonymousAccessPermission = AnonymousAccessPermission.findByValue(nextDefaultAnonymousAccessPermissionValue);

				final String nextDefaultInitialTablePageSizeValue = applicationNextConfigDTO.getNextDefaultInitialTablePageSizeValue();
				final TablePageSize nextDefaultInitialTablePageSize = TablePageSize.findByValue(Integer.valueOf(nextDefaultInitialTablePageSizeValue));

				final String nextDefaultColorModeCode = applicationNextConfigDTO.getNextDefaultColorModeCode();
				final ColorMode nextDefaultColorMode = ColorMode.findByCode(nextDefaultColorModeCode);

				final String nextDefaultUserInterfaceFrameworkCode = applicationNextConfigDTO.getNextDefaultUserInterfaceFrameworkCode();
				final UserInterfaceFramework nextUserInterfaceFramework = UserInterfaceFramework.findByCode(nextDefaultUserInterfaceFrameworkCode);

				final String nextDefaultPdfDocumentPageFormatCode = applicationNextConfigDTO.getNextDefaultPdfDocumentPageFormatCode();
				final PdfDocumentPageFormat nextDefaultPdfDocumentPageFormat = PdfDocumentPageFormat.findByCode(nextDefaultPdfDocumentPageFormatCode);

				return MvcUtils.getNextRedirect("/logout", languageCode, nextDefaultLanguage, nextAnonymousAccessPermission, nextDefaultInitialTablePageSize, nextDefaultColorMode, nextUserInterfaceFramework, nextDefaultPdfDocumentPageFormat);
			} else {
				return MvcUtils.getNextRedirect("/logout", languageCode);
			}

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("applicationNextConfigDTO", applicationNextConfigDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextRedirect("/my-user/application/configure", languageCode);
		}
	}

	private ApplicationDefaultConfigDTO getApplicationDefaultConfigDTO() {
		final String authUserCurriculumFilesPath = configPropertiesBean.getAuthUserCurriculumFilesPath();
		final String authUserEntityQueryFilesPath = configPropertiesBean.getAuthUserEntityQueryFilesPath();
		final String jobCompanyLogosPath = configPropertiesBean.getJobCompanyLogosPath();
		final Boolean useAjaxToRefreshJobCompanyLogos = configPropertiesBean.isUseAjaxToRefreshJobCompanyLogos();
		final Boolean useEntityManagerCache = configPropertiesBean.isUseEntityManagerCache();
		final Boolean useParallelStreams = configPropertiesBean.isUseParallelStreams();
		final Language defaultLanguage = configPropertiesBean.getDefaultLanguage();
		final AnonymousAccessPermission defaultAnonymousAccessPermission = configPropertiesBean.getDefaultAnonymousAccessPermission();
		final TablePageSize defaultInitialTablePageSize = configPropertiesBean.getDefaultInitialTablePageSize();
		final ColorMode defaultColorMode = configPropertiesBean.getDefaultColorMode();
		final UserInterfaceFramework defaultUserInterfaceFramework = configPropertiesBean.getDefaultUserInterfaceFramework();
		final PdfDocumentPageFormat defaultPdfDocumentPageFormat = configPropertiesBean.getDefaultPdfDocumentPageFormat();

		final ApplicationDefaultConfigDTO applicationDefaultConfigDTO = new ApplicationDefaultConfigDTO(
				authUserCurriculumFilesPath, authUserEntityQueryFilesPath, jobCompanyLogosPath,
				useAjaxToRefreshJobCompanyLogos, useEntityManagerCache, useParallelStreams,
				defaultLanguage, defaultAnonymousAccessPermission, defaultInitialTablePageSize, defaultColorMode, defaultUserInterfaceFramework,
				defaultPdfDocumentPageFormat);
		return applicationDefaultConfigDTO;
	}

	private ApplicationNextConfigDTO getInitialApplicationNextConfigDTO() {
		final String overwrittenLanguageCode = ConfigPropertiesBean.CURRENT_OVERWRITTEN_LANGUAGE.getCode();
		final String overwrittenAnonymousAccessPermissionValue = ConfigPropertiesBean.CURRENT_OVERWRITTEN_ANONYMOUS_ACCESS_PERMISSION.getValue();
		final String overwrittenInitialTablePageSizeValue = String.valueOf(ConfigPropertiesBean.CURRENT_OVERWRITTEN_INITIAL_TABLE_PAGE_SIZE.getValue());
		final String overwrittenColorModeCode = ConfigPropertiesBean.CURRENT_OVERWRITTEN_COLOR_MODE.getCode();
		final String overwrittenUserInterfaceFrameworkCode = ConfigPropertiesBean.CURRENT_OVERWRITTEN_USER_INTERFACE_FRAMEWORK.getCode();
		final String overwrittenPdfDocumentPageFormatCode = ConfigPropertiesBean.CURRENT_OVERWRITTEN_PDF_DOCUMENT_PAGE_FORMAT.getCode();

		final ApplicationNextConfigDTO applicationNextConfigDTO = new ApplicationNextConfigDTO(overwrittenLanguageCode, overwrittenAnonymousAccessPermissionValue, overwrittenInitialTablePageSizeValue, overwrittenColorModeCode, overwrittenUserInterfaceFrameworkCode, overwrittenPdfDocumentPageFormatCode);
		return applicationNextConfigDTO;
	}
}
