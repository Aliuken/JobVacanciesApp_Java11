package com.aliuken.jobvacanciesapp.controller;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserEmailDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserForSignupDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserResetPasswordDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobVacancyDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.AuthUserResetPasswordConverter;
import com.aliuken.jobvacanciesapp.model.dto.converter.JobVacancyConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserResetPassword;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserSignUpConfirmation;
import com.aliuken.jobvacanciesapp.model.entity.JobCategory;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.service.AuthRoleService;
import com.aliuken.jobvacanciesapp.service.AuthUserCredentialsService;
import com.aliuken.jobvacanciesapp.service.AuthUserResetPasswordService;
import com.aliuken.jobvacanciesapp.service.AuthUserRoleService;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.AuthUserSignUpConfirmationService;
import com.aliuken.jobvacanciesapp.service.EmailService;
import com.aliuken.jobvacanciesapp.service.JobCategoryService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.security.RandomUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerNavigationUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerServletUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Controller
@Slf4j
public class HomeController {

	@Autowired
	private JobCategoryService jobCategoryService;

	@Autowired
	private JobVacancyService jobVacancyService;

	@Autowired
	private AuthUserCredentialsService authUserCredentialsService;

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private AuthRoleService authRoleService;

	@Autowired
	private AuthUserRoleService authUserRoleService;

	@Autowired
	private AuthUserSignUpConfirmationService authUserSignUpConfirmationService;

	@Autowired
	private AuthUserResetPasswordService authUserResetPasswordService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	/**
	 * Method to show the first page of the application
	 */
	@GetMapping("/")
	public String home(Model model,
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="accountDeleted", required=false) Boolean accountDeleted) {
		final String operation = "GET /";

		if(Boolean.TRUE.equals(accountDeleted)) {
			final String deleteAccountSuccessMessage = I18nUtils.getInternationalizedMessage(languageCode, Constants.DELETE_ACCOUNT_SUCCESS_MESSAGE_NAME, null);
			model.addAttribute(Constants.DELETE_ACCOUNT_SUCCESS_MESSAGE, deleteAccountSuccessMessage);
		}

		return ControllerNavigationUtils.getNextView("home.html", model, operation, languageCode);
	}

	/**
	 * Method to show the signup form
	 */
	@GetMapping("/signup")
	public String signupForm(HttpServletRequest httpServletRequest, Model model,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /signup";

		final Map<String, ?> inputFlashMap = ControllerServletUtils.getInputFlashMap(httpServletRequest);

		AuthUserForSignupDTO authUserForSignupDTO;
		if(inputFlashMap != null) {
			authUserForSignupDTO = (AuthUserForSignupDTO) inputFlashMap.get("authUserForSignupDTO");
			if(authUserForSignupDTO == null) {
				authUserForSignupDTO = AuthUserForSignupDTO.getNewInstance();
			}
		} else {
			authUserForSignupDTO = AuthUserForSignupDTO.getNewInstance();
		}

		model.addAttribute("authUserForSignupDTO", authUserForSignupDTO);

		return ControllerNavigationUtils.getNextView("signupForm.html", model, operation, languageCode);
	}

	/**
	 * Method to send an email to signup in the application
	 */
	@PostMapping("/signup")
	public String signupSave(RedirectAttributes redirectAttributes,
			@Validated @NonNull AuthUserForSignupDTO authUserForSignupDTO, BindingResult bindingResult,
			@RequestParam(name="languageParam", required=false) String languageCode) throws MessagingException {
		try {
			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				redirectAttributes.addFlashAttribute("authUserForSignupDTO", authUserForSignupDTO);
				//redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.authUserForSignupDTO", bindingResult);
				redirectAttributes.addFlashAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextRedirect("/signup", languageCode);
			}

			final String email = authUserForSignupDTO.getEmail();
			final String password1 = authUserForSignupDTO.getPassword1();
			final String password2 = authUserForSignupDTO.getPassword2();
			final String name = authUserForSignupDTO.getName();
			final String surnames = authUserForSignupDTO.getSurnames();

			if(!password1.equals(password2)) {
				final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "signupSave.passwordsDontMatch", null);

				redirectAttributes.addFlashAttribute("authUserForSignupDTO", authUserForSignupDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return ControllerNavigationUtils.getNextRedirect("/signup", languageCode);
			}

			final Language language = Language.findByCode(languageCode);
			Objects.requireNonNull(language, "language cannot be null");

			AuthUser authUser = authUserService.findByEmail(email);
			if(authUser != null) {
				if(Boolean.TRUE.equals(authUser.getEnabled())) {
					final String errorMsg = I18nUtils.getInternationalizedMessage(language, "signupSave.emailAlreadyExists", new Object[]{email});
					redirectAttributes.addFlashAttribute("authUserForSignupDTO", authUserForSignupDTO);
					redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

					return ControllerNavigationUtils.getNextRedirect("/signup", languageCode);
				}
			} else {
				authUser = new AuthUser();
				authUser.setEmail(email);
			}
			authUser.setName(name);
			authUser.setSurnames(surnames);
			authUser.setLanguage(language);
			authUser.setEnabled(Boolean.FALSE);
			authUser.setInitialTableSortingDirection(TableSortingDirection.BY_DEFAULT);
			authUser.setInitialTablePageSize(TablePageSize.BY_DEFAULT);
			authUser.setColorMode(ColorMode.BY_DEFAULT);
			authUser.setPdfDocumentPageFormat(PdfDocumentPageFormat.BY_DEFAULT);

			authUser = authUserService.saveAndFlush(authUser);

			final AuthRole authRole = authRoleService.findByName(AuthRole.USER);

			AuthUserRole authUserRole = authUserRoleService.findByAuthUserAndAuthRole(authUser, authRole);
			if(authUserRole == null) {
				authUserRole = new AuthUserRole();
				authUserRole.setAuthUser(authUser);
				authUserRole.setAuthRole(authRole);
			}

			authUserRole = authUserRoleService.saveAndFlush(authUserRole);

			final Set<AuthUserRole> authUserRoles = new TreeSet<>();
			authUserRoles.add(authUserRole);

			authUser.setAuthUserRoles(authUserRoles);

			authUser = authUserService.saveAndFlush(authUser);

			final String encryptedPassword = passwordEncoder.encode(password1);

			AuthUserCredentials authUserCredentials = authUserCredentialsService.findByEmail(email);
			if(authUserCredentials == null) {
				authUserCredentials = new AuthUserCredentials();
				authUserCredentials.setEmail(email);
			}
			authUserCredentials.setEncryptedPassword(encryptedPassword);

			authUserCredentials = authUserCredentialsService.saveAndFlush(authUserCredentials);

			final String uuid = RandomUtils.UUID_GENERATOR.get();

			AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationService.findByEmail(email);
			if(authUserSignUpConfirmation == null) {
				authUserSignUpConfirmation = new AuthUserSignUpConfirmation();
				authUserSignUpConfirmation.setEmail(email);
			}
			authUserSignUpConfirmation.setUuid(uuid);
			authUserSignUpConfirmation = authUserSignUpConfirmationService.saveAndFlush(authUserSignUpConfirmation);

			emailService.sendSignUpConfirmationEmail(email, uuid, language);

			final String successMsg = I18nUtils.getInternationalizedMessage(language, "signupSave.successMsg", new Object[]{email});

			redirectAttributes.addFlashAttribute("authUserForSignupDTO", authUserForSignupDTO);
			redirectAttributes.addFlashAttribute("successMsg", successMsg);

			return ControllerNavigationUtils.getNextRedirect("/login", languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("authUserForSignupDTO", authUserForSignupDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextRedirect("/signup", languageCode);
		}
	}

	/**
	 * Method to confirm the signup in the application
	 */
	@GetMapping("/signup-confirmed")
	public String signupConfirmed(RedirectAttributes redirectAttributes,
			@RequestParam("email") String email, @RequestParam("uuid") String uuid, @RequestParam(name="languageParam", required=false) String languageCode) {

		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationService.findByEmailAndUuid(email, uuid);
		if(authUserSignUpConfirmation == null) {
			final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "signupConfirmed.wrongLink", new Object[]{email});

			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return ControllerNavigationUtils.getNextRedirect("/login", languageCode);
		} else {
			final LocalDateTime currentDateTime = LocalDateTime.now();
			final LocalDateTime entityLastDateTime = authUserSignUpConfirmation.getLastDateTime();
			final long signupConfirmationLinkExpirationHours = configPropertiesBean.getSignupConfirmationLinkExpirationHours();
			final LocalDateTime expirationDateTime = entityLastDateTime.plusHours(signupConfirmationLinkExpirationHours);
			if(currentDateTime.isAfter(expirationDateTime)) {
				//final Long authUserSignUpConfirmationId = authUserSignUpConfirmation.getId();
				//authUserSignUpConfirmationService.deleteByIdAndFlush(authUserSignUpConfirmationId);

				final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "signupConfirmed.expiredLink", new Object[]{email});

				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return ControllerNavigationUtils.getNextRedirect("/login", languageCode);
			}
		}

		AuthUser authUser = authUserService.findByEmail(email);
		authUser.setEnabled(Boolean.TRUE);

		authUser = authUserService.saveAndFlush(authUser);

		final Long authUserSignUpConfirmationId = authUserSignUpConfirmation.getId();
		authUserSignUpConfirmationService.deleteByIdAndFlush(authUserSignUpConfirmationId);

		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "signupConfirmed.successMsg", new Object[]{email});

		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirect("/login", languageCode);
	}

	/**
	 * Method to show the forgotten-password form
	 */
	@GetMapping("/forgotten-password")
	public String forgottenPasswordForm(HttpServletRequest httpServletRequest, Model model,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /forgotten-password";

		final AuthUserEmailDTO authUserEmailDTO = AuthUserEmailDTO.getNewInstance();

		model.addAttribute("authUserEmailDTO", authUserEmailDTO);

		return ControllerNavigationUtils.getNextView("forgottenPasswordForm.html", model, operation, languageCode);
	}

	/**
	 * Method to send an email to reset the password
	 */
	@PostMapping("/forgotten-password")
	public String forgottenPasswordSave(HttpServletRequest httpServletRequest, Model model, RedirectAttributes redirectAttributes,
			@Validated @NonNull AuthUserEmailDTO authUserEmailDTO, BindingResult bindingResult,
			@RequestParam(name="languageParam", required=false) String languageCode) throws MessagingException {
		try {
			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				redirectAttributes.addFlashAttribute("authUserEmailDTO", authUserEmailDTO);
				//redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.authUserEmailDTO", bindingResult);
				redirectAttributes.addFlashAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextRedirect("/forgotten-password", languageCode);
			}

			final String email = authUserEmailDTO.getEmail();

			final AuthUserCredentials authUserCredentials = authUserCredentialsService.findByEmail(email);
			if(authUserCredentials == null) {
				final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "forgottenPasswordSave.emailIncorrect", new Object[]{email});

				redirectAttributes.addFlashAttribute("authUserEmailDTO", authUserEmailDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return ControllerNavigationUtils.getNextRedirect("/forgotten-password", languageCode);
			}

			final String uuid = RandomUtils.UUID_GENERATOR.get();

			AuthUserResetPassword authUserResetPassword = authUserResetPasswordService.findByEmail(email);
			if(authUserResetPassword == null) {
				authUserResetPassword = new AuthUserResetPassword();
				authUserResetPassword.setEmail(email);
			}
			authUserResetPassword.setUuid(uuid);

			authUserResetPassword = authUserResetPasswordService.saveAndFlush(authUserResetPassword);

			final Language language = Language.findByCode(languageCode);

			emailService.sendResetPasswordEmail(email, uuid, language);

			final String successMsg = I18nUtils.getInternationalizedMessage(language, "forgottenPasswordSave.successMsg", new Object[]{email});

			redirectAttributes.addFlashAttribute("authUserEmailDTO", authUserEmailDTO);
			redirectAttributes.addFlashAttribute("successMsg", successMsg);

			return ControllerNavigationUtils.getNextRedirect("/login", languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("authUserEmailDTO", authUserEmailDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextRedirect("/forgotten-password", languageCode);
		}
	}

	/**
	 * Method to show the reset-password form
	 */
	@GetMapping("/reset-password")
	public String resetPasswordForm(HttpServletRequest httpServletRequest, Model model, RedirectAttributes redirectAttributes,
			@RequestParam("email") String email, @RequestParam("uuid") String uuid, @RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /reset-password";

		final AuthUserResetPassword authUserResetPassword = authUserResetPasswordService.findByEmailAndUuid(email, uuid);
		if(authUserResetPassword == null) {
			final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "resetPassword.wrongLink", new Object[]{email});

			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return ControllerNavigationUtils.getNextRedirect("/login", languageCode);
		} else {
			final LocalDateTime currentDateTime = LocalDateTime.now();
			final LocalDateTime entityLastDateTime = authUserResetPassword.getLastDateTime();
			final long resetPasswordLinkExpirationHours = configPropertiesBean.getResetPasswordLinkExpirationHours();
			final LocalDateTime expirationDateTime = entityLastDateTime.plusHours(resetPasswordLinkExpirationHours);
			if(currentDateTime.isAfter(expirationDateTime)) {
				//final Long authUserResetPasswordId = authUserResetPassword.getId();
				//authUserResetPasswordService.deleteByIdAndFlush(authUserResetPassword.getId());

				final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "resetPassword.expiredLink", new Object[]{email});

				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return ControllerNavigationUtils.getNextRedirect("/login", languageCode);
			}
		}

		final AuthUserResetPasswordDTO authUserResetPasswordDTO = AuthUserResetPasswordConverter.getInstance().convertEntityElement(authUserResetPassword);

		model.addAttribute("authUserResetPasswordDTO", authUserResetPasswordDTO);

		return ControllerNavigationUtils.getNextView("resetPasswordForm.html", model, operation, languageCode);
	}

	/**
	 * Method to reset the password
	 */
	@PostMapping("/reset-password")
	public String resetPasswordSave(HttpServletRequest httpServletRequest, Model model, RedirectAttributes redirectAttributes,
			@Validated @NonNull AuthUserResetPasswordDTO authUserResetPasswordDTO, BindingResult bindingResult,
			@RequestParam(name="languageParam", required=false) String languageCode) throws MessagingException {

		final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
		if(firstBindingErrorString != null) {
			redirectAttributes.addFlashAttribute("authUserResetPasswordDTO", authUserResetPasswordDTO);
			//redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.authUserResetPasswordDTO", bindingResult);
			redirectAttributes.addFlashAttribute("errorMsg", firstBindingErrorString);

			return ControllerNavigationUtils.getNextRedirect("/login", languageCode);
		}

		final String email = authUserResetPasswordDTO.getEmail();
		final String uuid = authUserResetPasswordDTO.getUuid();
		try {
			final String newPassword1 = authUserResetPasswordDTO.getNewPassword1();
			final String newPassword2 = authUserResetPasswordDTO.getNewPassword2();

			if(!newPassword1.equals(newPassword2)) {
				final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "saveNewPassword.newPasswordsDontMatch", null);

				redirectAttributes.addFlashAttribute("authUserResetPasswordDTO", authUserResetPasswordDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return ControllerNavigationUtils.getNextRedirect("/reset-password", languageCode, email, uuid);
			}

			AuthUserCredentials authUserCredentials = authUserCredentialsService.findByEmail(email);
			if(authUserCredentials == null) {
				final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "saveNewPassword.emailOrPasswordIncorrect", new Object[]{email});

				redirectAttributes.addFlashAttribute("authUserResetPasswordDTO", authUserResetPasswordDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return ControllerNavigationUtils.getNextRedirect("/reset-password", languageCode, email, uuid);
			}
			final String encryptedNewPassword = passwordEncoder.encode(newPassword1);
			authUserCredentials.setEncryptedPassword(encryptedNewPassword);

			authUserCredentials = authUserCredentialsService.saveAndFlush(authUserCredentials);

			final Long authUserResetPasswordId = authUserResetPasswordDTO.getId();
			authUserResetPasswordService.deleteByIdAndFlush(authUserResetPasswordId);

			final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "resetPassword.successMsg", new Object[]{email});

			redirectAttributes.addFlashAttribute("authUserResetPasswordDTO", authUserResetPasswordDTO);
			redirectAttributes.addFlashAttribute("successMsg", successMsg);

			return ControllerNavigationUtils.getNextRedirect("/login", languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("authUserResetPasswordDTO", authUserResetPasswordDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextRedirect("/reset-password", languageCode, email, uuid);
		}
	}

	/**
	 * Method to search job vacancies in the home page
	 */
	@GetMapping("/search")
	public String search(Model model,
			@RequestParam(name="description", required=false) String description, @RequestParam(name="jobCategoryId", required=false) Long jobCategoryId,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /search";

		try {
			final JobVacancy jobVacancySearch = new JobVacancy();
			jobVacancySearch.setDescription(description);
			jobVacancySearch.setStatus(JobVacancyStatus.APPROVED);

			if(jobCategoryId != null) {
				final JobCategory jobCategory = jobCategoryService.findByIdNotOptional(jobCategoryId);
				jobVacancySearch.setJobCategory(jobCategory);
			}

			final ExampleMatcher exampleMatcher = ExampleMatcher.matching()
					.withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains());

			final Example<JobVacancy> jobVacancyExample = Example.of(jobVacancySearch, exampleMatcher);
			final List<JobVacancy> approvedJobVacancies = jobVacancyService.findAll(jobVacancyExample);
			final List<JobVacancyDTO> approvedJobVacancyDTOs = JobVacancyConverter.getInstance().convertEntityList(approvedJobVacancies);

			model.addAttribute("approvedJobVacancyDTOs", approvedJobVacancyDTOs);

			return ControllerNavigationUtils.getNextViewWithHomeSearch("home.html", model, operation, description, jobCategoryId, languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextViewWithHomeSearch("home.html", model, operation, description, jobCategoryId, languageCode);
		}
	}

	/**
	 * Method to show information about the application
	 */
	@GetMapping("/about")
	public String about(Model model,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /about";

		return ControllerNavigationUtils.getNextView("about.html", model, operation, languageCode);
	}

	/**
	 * Method to show the login form
	 */
	@GetMapping("/login")
	public String login(Model model,
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="accountDeleted", required=false) Boolean accountDeleted) {
		final String operation = "GET /login";

		if(Boolean.TRUE.equals(accountDeleted)) {
			final String deleteAccountSuccessMessage = I18nUtils.getInternationalizedMessage(languageCode, Constants.DELETE_ACCOUNT_SUCCESS_MESSAGE_NAME, null);
			model.addAttribute(Constants.DELETE_ACCOUNT_SUCCESS_MESSAGE, deleteAccountSuccessMessage);
		}

		return ControllerNavigationUtils.getNextView("loginForm.html", model, operation, languageCode);
	}

	@ModelAttribute
	public void setGenerics(Model model) {
		final List<JobVacancy> approvedJobVacancies = jobVacancyService.findAllHighlighted();
		final List<JobVacancyDTO> approvedJobVacancyDTOs = JobVacancyConverter.getInstance().convertEntityList(approvedJobVacancies);
		model.addAttribute("approvedJobVacancyDTOs", approvedJobVacancyDTOs);

		final List<JobCategory> jobCategories = jobCategoryService.findAll();
		model.addAttribute("jobCategories", jobCategories);
	}

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}
}
