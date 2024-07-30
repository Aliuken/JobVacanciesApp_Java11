package com.aliuken.jobvacanciesapp.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.servlet.support.RequestContextUtils;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserForSignupDTO;
import com.aliuken.jobvacanciesapp.model.dto.EmailDataDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobVacancyDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.JobVacancyConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserRole;
import com.aliuken.jobvacanciesapp.model.entity.JobCategory;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.service.AuthRoleService;
import com.aliuken.jobvacanciesapp.service.AuthUserConfirmationService;
import com.aliuken.jobvacanciesapp.service.AuthUserCredentialsService;
import com.aliuken.jobvacanciesapp.service.AuthUserRoleService;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.EmailService;
import com.aliuken.jobvacanciesapp.service.JobCategoryService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.web.MvcUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

	@Autowired
	private HttpServletRequest httpServletRequest;

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
	private AuthUserConfirmationService authUserConfirmationService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private EmailService emailService;

	/**
	 * Method to show the first page of the application
	 */
	@GetMapping("/")
	public String home(Model model,
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="accountDeleted", required=false) Boolean accountDeleted) {
		final String operation = "GET /";

		if(Boolean.TRUE.equals(accountDeleted)) {
			final String deleteAccountSuccessMessage = StringUtils.getInternationalizedMessage(languageCode, Constants.DELETE_ACCOUNT_SUCCESS_MESSAGE_NAME, null);
			model.addAttribute(Constants.DELETE_ACCOUNT_SUCCESS_MESSAGE, deleteAccountSuccessMessage);
		}

		return MvcUtils.getNextView("home.html", model, operation, languageCode);
	}

	/**
	 * Method to show the signup form
	 */
	@GetMapping("/signup")
	public String signupForm(HttpServletRequest httpServletRequest, Model model,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /signup";

		final Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(httpServletRequest);

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

		return MvcUtils.getNextView("signupForm.html", model, operation, languageCode);
	}

	/**
	 * Method to send an email to signup in the application
	 */
	@PostMapping("/signup")
	public String signupSave(RedirectAttributes redirectAttributes,
			@Validated AuthUserForSignupDTO authUserForSignupDTO, BindingResult bindingResult,
			@RequestParam(name="languageParam", required=false) String languageCode) throws MessagingException {
		try {
			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				redirectAttributes.addFlashAttribute("authUserForSignupDTO", authUserForSignupDTO);
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.authUserForSignupDTO", bindingResult);

				return MvcUtils.getNextRedirect("/signup", languageCode);
			}

			final String email = authUserForSignupDTO.getEmail();
			final String password1 = authUserForSignupDTO.getPassword1();
			final String password2 = authUserForSignupDTO.getPassword2();
			final String name = authUserForSignupDTO.getName();
			final String surnames = authUserForSignupDTO.getSurnames();

			if(!password1.equals(password2)) {
				String errorMsg = StringUtils.getInternationalizedMessage(languageCode, "signupSave.passwordsDontMatch", null);

				redirectAttributes.addFlashAttribute("authUserForSignupDTO", authUserForSignupDTO);
				redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

				return MvcUtils.getNextRedirect("/signup", languageCode);
			}

			final Language language = Language.findByCode(languageCode);

			AuthUser authUser = authUserService.findByEmail(email);
			if(authUser != null) {
				if(Boolean.TRUE.equals(authUser.getEnabled())) {
					final String errorMsg = StringUtils.getInternationalizedMessage(language, "signupSave.emailAlreadyExists", null);

					redirectAttributes.addFlashAttribute("authUserForSignupDTO", authUserForSignupDTO);
					redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

					return MvcUtils.getNextRedirect("/signup", languageCode);
				}
			} else {
				authUser = new AuthUser();
				authUser.setEmail(email);
			}
			authUser.setName(name);
			authUser.setSurnames(surnames);
			authUser.setLanguage(language);
			authUser.setEnabled(Boolean.FALSE);
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

			final String uuid = UUID.randomUUID().toString();

			AuthUserConfirmation authUserConfirmation = authUserConfirmationService.findByEmail(email);
			if(authUserConfirmation == null) {
				authUserConfirmation = new AuthUserConfirmation();
				authUserConfirmation.setEmail(email);
			}
			authUserConfirmation.setUuid(uuid);
			authUserConfirmation = authUserConfirmationService.saveAndFlush(authUserConfirmation);

			this.sendAccountConfirmationEmail(email, uuid, language);

			final String successMsg = StringUtils.getInternationalizedMessage(language, "signupSave.successMsg", null);
			redirectAttributes.addFlashAttribute("successMsg", successMsg);

			return MvcUtils.getNextRedirect("/login", languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("authUserForSignupDTO", authUserForSignupDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextRedirect("/signup", languageCode);
		}
	}

	private void sendAccountConfirmationEmail(final String destinationEmailAddress, final String uuid, final Language language) throws MessagingException {
		final String accountActivationLink = this.getAccountActivationLink(destinationEmailAddress, uuid, language);
		final EmailDataDTO emailDataDTO = emailService.getAccountConfirmationEmailData(destinationEmailAddress, accountActivationLink, language);

		if(log.isInfoEnabled()) {
			log.info(StringUtils.getStringJoined("Trying to send an email to '", destinationEmailAddress, "' with the account activation link '", accountActivationLink, "'"));
		}
		emailService.sendMail(emailDataDTO);
	}

	private String getAccountActivationLink(final String destinationEmailAddress, final String uuid, final Language language) {
		final String appUrl = this.getAppUrl();
		final String languageCode = language.getCode();

		final String accountActivationLink = StringUtils.getStringJoined(appUrl, "/signup-confirmed?email=", destinationEmailAddress, "&uuid=", uuid, "&languageParam=", languageCode);
		return accountActivationLink;
	}

	/**
	 * Method to confirm the signup in the application
	 */
	@GetMapping("/signup-confirmed")
	public String signupConfirmed(RedirectAttributes redirectAttributes,
			@RequestParam("email") String email, @RequestParam("uuid") String uuid, @RequestParam(name="languageParam", required=false) String languageCode) {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationService.findByEmailAndUuid(email, uuid);

		if(authUserConfirmation != null) {
			AuthUser authUser = authUserService.findByEmail(email);
			authUser.setEnabled(Boolean.TRUE);

			final Long authUserConfirmationId = authUserConfirmation.getId();

			authUser = authUserService.saveAndFlush(authUser);
			authUserConfirmationService.deleteByIdAndFlush(authUserConfirmationId);
		}

		final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "signupConfirmed.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return MvcUtils.getNextRedirect("/login", languageCode);
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
			final List<JobVacancy> jobVacancies = jobVacancyService.findAll(jobVacancyExample);
			final List<JobVacancyDTO> jobVacancyDTOs = JobVacancyConverter.getInstance().convertEntityList(jobVacancies);

			model.addAttribute("jobVacancyDTOs", jobVacancyDTOs);

			return MvcUtils.getNextViewWithHomeSearch("home.html", model, operation, description, jobCategoryId, languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextViewWithHomeSearch("home.html", model, operation, description, jobCategoryId, languageCode);
		}
	}

	/**
	 * Method to show information about the application
	 */
	@GetMapping("/about")
	public String about(Model model,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /about";

		return MvcUtils.getNextView("about.html", model, operation, languageCode);
	}

	/**
	 * Method to show the login form
	 */
	@GetMapping("/login")
	public String login(Model model,
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="accountDeleted", required=false) Boolean accountDeleted) {
		final String operation = "GET /login";

		if(Boolean.TRUE.equals(accountDeleted)) {
			final String deleteAccountSuccessMessage = StringUtils.getInternationalizedMessage(languageCode, Constants.DELETE_ACCOUNT_SUCCESS_MESSAGE_NAME, null);
			model.addAttribute(Constants.DELETE_ACCOUNT_SUCCESS_MESSAGE, deleteAccountSuccessMessage);
		}

		return MvcUtils.getNextView("loginForm.html", model, operation, languageCode);
	}

	@ModelAttribute
	public void setGenerics(Model model) {
		final List<JobVacancy> jobVacancies = jobVacancyService.findAllHighlighted();
		final List<JobVacancyDTO> jobVacancyDTOs = JobVacancyConverter.getInstance().convertEntityList(jobVacancies);
		model.addAttribute("jobVacancyDTOs", jobVacancyDTOs);

		final List<JobCategory> jobCategories = jobCategoryService.findAll();
		model.addAttribute("jobCategories", jobCategories);
	}

	@InitBinder
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
	}

	private String getAppUrl() {
		final StringBuffer url = httpServletRequest.getRequestURL();
		final String uri = httpServletRequest.getRequestURI();
		final String host = url.substring(0, url.indexOf(uri));

		final String appUrl = host + httpServletRequest.getContextPath();
		return appUrl;
	}
}
