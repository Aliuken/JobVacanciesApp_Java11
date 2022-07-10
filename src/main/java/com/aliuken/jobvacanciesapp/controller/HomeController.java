package com.aliuken.jobvacanciesapp.controller;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliuken.jobvacanciesapp.controller.superinterface.GenericControllerInterface;
import com.aliuken.jobvacanciesapp.model.AuthRole;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.model.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.AuthUserLanguage;
import com.aliuken.jobvacanciesapp.model.AuthUserRole;
import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserForSignupDTO;
import com.aliuken.jobvacanciesapp.service.AuthRoleService;
import com.aliuken.jobvacanciesapp.service.AuthUserConfirmationService;
import com.aliuken.jobvacanciesapp.service.AuthUserCredentialsService;
import com.aliuken.jobvacanciesapp.service.AuthUserRoleService;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.EmailService;
import com.aliuken.jobvacanciesapp.service.JobCategoryService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;
import com.aliuken.jobvacanciesapp.util.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController implements GenericControllerInterface {

	@Autowired
	private MessageSource messageSource;

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

	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	@GetMapping("/")
	public String home(Model model, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /";

		return this.getNextView(model, operation, language, "home.html");
	}

	/**
	 * Método que esta mapeado al botón Ingresar en el menú
	 */
	@GetMapping("/index")
	public String index(HttpServletRequest httpServletRequest, @RequestParam(name="lang", required=false) String language) {
		if(log.isInfoEnabled()) {
			log.info(StringUtils.getStringJoined("index language: ", language));
		}

		return this.getNextRedirect(language, "/");
	}

	/**
	 * Método que muestra el formulario para que se registren nuevos usuarios.
	 */
	@GetMapping("/signup")
	public String signupForm(Model model, AuthUserForSignupDTO authUserForSignupDTO, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /signup";

		return this.getNextView(model, operation, language, "signupForm.html");
	}

	/**
	 * Método que guarda en la base de datos el usuario registrado pendiente de confirmar via email
	 */
	@PostMapping("/signup")
	public String signupSave(Model model, AuthUserForSignupDTO authUserForSignupDTO, RedirectAttributes redirectAttributes) throws MessagingException {
		final String operation = "POST /signup";

		final String email = authUserForSignupDTO.getEmail();
		final String password1 = authUserForSignupDTO.getPassword1();
		final String password2 = authUserForSignupDTO.getPassword2();
		final String name = authUserForSignupDTO.getName();
		final String surnames = authUserForSignupDTO.getSurnames();
		final String language = authUserForSignupDTO.getLanguage();

		if(!password1.equals(password2)) {
			String errorMsg = this.getInternationalizedMessage(language, "signupSave.passwordsDontMatch", null);
			model.addAttribute("errorMsg", errorMsg);

			return this.getNextView(model, operation, language, "signupForm.html");
		}

		final AuthUserLanguage authUserLanguage = AuthUserLanguage.findByCode(language);

		AuthUser authUser = authUserService.findByEmail(email);
		if(authUser != null) {
			if(Boolean.TRUE.equals(authUser.getEnabled())) {
				String errorMsg = this.getInternationalizedMessage(authUserLanguage, "signupSave.emailAlreadyExists", null);
				model.addAttribute("errorMsg", errorMsg);

				return this.getNextView(model, operation, language, "signupForm.html");
			}
		} else {
			authUser = new AuthUser();
			authUser.setEmail(email);
		}
		authUser.setName(name);
		authUser.setSurnames(surnames);
		authUser.setLanguage(authUserLanguage);
		authUser.setEnabled(Boolean.FALSE);

		authUser = authUserService.saveAndFlush(authUser);

		final AuthRole authRole = authRoleService.findByName(AuthRole.USER);

		AuthUserRole authUserRole = authUserRoleService.findByAuthUserAndAuthRole(authUser, authRole);
		if(authUserRole == null) {
			authUserRole = new AuthUserRole();
			authUserRole.setAuthUser(authUser);
			authUserRole.setAuthRole(authRole);
		}

		authUserRole = authUserRoleService.saveAndFlush(authUserRole);

		Set<AuthUserRole> authUserRoles = new TreeSet<>();
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

		final String appUrl = this.getAppUrl();
		final String link = StringUtils.getStringJoined(appUrl, "/signup-confirmed?email=", email, "&uuid=", uuid);

		final String subject;
		final String textTitle;
		final String textBody;
		if(AuthUserLanguage.SPANISH.equals(authUserLanguage)) {
			subject = "¡Confirma tu cuenta de JobVacanciesApp!";
			textTitle = "Activación de cuenta de JobVacanciesApp requerida";
			textBody = StringUtils.getStringJoined(
				"<p>Haz clic en el siguiente enlace para activar tu cuenta de JobVacanciesApp:</p><p><a href=\"", link, "\">", link, "</a></p><p>Tienes 24 horas para usar este enlace. Después de ese tiempo, tendrás que registrarte de nuevo y activar la cuenta por email para entrar al sitio web.</p><p>Un saludo,</p> <p>el equipo de JobVacanciesApp</p>");
    	} else {
    		subject = "Confirm your JobVacanciesApp account!";
    		textTitle = "JobVacanciesApp account activation required";
    		textBody = StringUtils.getStringJoined(
    			"<p>Click in the following link to activate your JobVacanciesApp account:</p><p><a href=\"", link, "\">", link, "</a></p><p>You have 24 hours to use the link. After that time, you'll have to register again and activate the account by email to enter in the website.</p><p>Regards,</p> <p>the JobVacanciesApp team</p>");
    	}

		emailService.sendComplexMessage(email, subject, textTitle, textBody, authUserLanguage, true, null);

		String successMsg = this.getInternationalizedMessage(authUserLanguage, "signupSave.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/login");
	}

	/**
	 * Método que guarda en la base de datos la confirmación de la creación del usuario via email
	 */
	@GetMapping("/signup-confirmed")
	public String signupConfirmed(@RequestParam("email") String email, @RequestParam("uuid") String uuid, RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		final AuthUserConfirmation authUserConfirmation = authUserConfirmationService.findByEmailAndUuid(email, uuid);

		if(authUserConfirmation != null) {
			AuthUser authUser = authUserService.findByEmail(email);
			authUser.setEnabled(Boolean.TRUE);

			final Long authUserConfirmationId = authUserConfirmation.getId();

			authUser = authUserService.saveAndFlush(authUser);
			authUserConfirmationService.deleteByIdAndFlush(authUserConfirmationId);
		}

		String successMsg = this.getInternationalizedMessage(language, "signupConfirmed.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/login");
	}

	/**
	 * Método para realizar búsquedas desde el formulario de búsqueda del HomePage
	 */
	@GetMapping("/search")
	public String search(Model model, @ModelAttribute("jobVacancySearch") JobVacancy jobVacancySearch, BindingResult bindingResult, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /search";

		if (bindingResult.hasErrors()) {
			model.addAttribute("jobVacancySearch", jobVacancySearch);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "home.html");
		}

		/**
		 * La busqueda de ofertas desde el formulario debera de ser únicamente en
		 * Ofertas con estatus "Aprobada". Entonces forzamos ese filtrado.
		 */
		jobVacancySearch.setStatus(JobVacancyStatus.APPROVED);

		// Personalizamos el tipo de busqueda...
		final ExampleMatcher exampleMatcher = ExampleMatcher.matching().
				withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains());

		final Example<JobVacancy> jobVacancyExample = Example.of(jobVacancySearch, exampleMatcher);
		final List<JobVacancy> jobVacancies = jobVacancyService.findAll(jobVacancyExample);
		model.addAttribute("jobVacancies", jobVacancies);

		return this.getNextView(model, operation, language, "home.html");
	}

	/**
	 * Metodo que muestra la vista de la pagina de Acerca
	 */
	@GetMapping("/about")
	public String about(Model model, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /about";

		return this.getNextView(model, operation, language, "about.html");
	}

	/**
	 * Método que muestra el formulario de login personalizado.
	 */
	@GetMapping("/login")
	public String login(Model model, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /login";

		return this.getNextView(model, operation, language, "loginForm.html");
	}

	/**
	 * Método personalizado para cerrar la sesión del usuario
	 */
	@GetMapping("/logout")
	public String logout(@RequestParam(name="lang", required=false) String language) {
		final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
		logoutHandler.logout(httpServletRequest, null, null);

		return this.getNextRedirect(language, "/");
	}

	/**
	 * Metodo que agrega al modelo datos genéricos para todo el controlador
	 */
	@ModelAttribute
	public void setGenerics(Model model) {
		final JobVacancy jobVacancySearch = new JobVacancy();
		model.addAttribute("jobVacancySearch", jobVacancySearch);

		final List<JobVacancy> jobVacancies = jobVacancyService.findAllHighlighted();
		model.addAttribute("jobVacancies", jobVacancies);

		final List<JobCategory> jobCategories = jobCategoryService.findAll();
		model.addAttribute("jobCategories", jobCategories);
	}

	/**
	 * InitBinder para Strings si los detecta vacios en el Data Binding los settea a null
	 */
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
