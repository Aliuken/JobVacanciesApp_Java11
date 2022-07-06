package com.aliuken.jobvacanciesapp.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.controller.superinterface.GenericControllerInterface;
import com.aliuken.jobvacanciesapp.model.AbstractEntityPageWithException;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.AuthUserLanguage;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserCredentialsDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.service.AbstractEntityService;
import com.aliuken.jobvacanciesapp.service.AuthUserCredentialsService;
import com.aliuken.jobvacanciesapp.service.AuthUserCurriculumService;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;

@Controller
public class SessionAuthUserController implements GenericControllerInterface {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private AbstractEntityService<AuthUser> abstractEntityService;

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private AuthUserCredentialsService authUserCredentialsService;

	@Autowired
	private JobRequestService jobRequestService;
	
	@Autowired
	private AuthUserCurriculumService authUserCurriculumService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * Método para renderizar el formulario para editar un Usuario
	 */
	@GetMapping("/my-user/auth-users")
	public String editUserForm(HttpServletRequest httpServletRequest, Model model, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /my-user/auth-users";

		final AuthUser sessionAuthUser = this.getRefreshedSessionAuthUser(httpServletRequest);
		final AuthUserDTO authUserDTO = new AuthUserDTO(sessionAuthUser);
		model.addAttribute("authUserDTO", authUserDTO);

		return this.getNextView(model, operation, language, "authUser/sessionAuthUserForm.html");
	}

	/**
	 * Método que guarda en la base de datos el usuario editado
	 */
	@PostMapping("/my-user/auth-users")
	public String saveUser(HttpServletRequest httpServletRequest, Model model, @ModelAttribute("authUserDTO") AuthUserDTO authUserDTO, BindingResult bindingResult, @RequestParam(name="lang", required=false) String language, RedirectAttributes redirectAttributes) throws MessagingException {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextRedirect(language, "/my-user/auth-users");
		}

		final String name = authUserDTO.getName();
		final String surnames = authUserDTO.getSurnames();
		language = authUserDTO.getLanguage();
		final boolean enabled = authUserDTO.isEnabled();

		final AuthUserLanguage authUserLanguage = AuthUserLanguage.findByCode(language);

		final AuthUser sessionAuthUser = this.getRefreshedSessionAuthUser(httpServletRequest);
		sessionAuthUser.setName(name);
		sessionAuthUser.setSurnames(surnames);
		sessionAuthUser.setLanguage(authUserLanguage);
		sessionAuthUser.setEnabled(enabled);
		authUserService.save(sessionAuthUser);
		
		httpServletRequest.getSession().setAttribute(Constants.SESSION_AUTH_USER, sessionAuthUser);

		String successMsg = this.getInternationalizedMessage(language, "saveUser.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/my-user/auth-users");
	}

	/**
	 * Método para renderizar el formulario para modificar la contraseña del Usuario
	 */
	@GetMapping("/my-user/auth-users/change-password")
	public String changePasswordForm(HttpServletRequest httpServletRequest, Model model, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /my-user/auth-users/change-password";

		final AuthUser sessionAuthUser = this.getRefreshedSessionAuthUser(httpServletRequest);
		final AuthUserCredentials authUserCredentials = authUserCredentialsService.findByEmail(sessionAuthUser.getEmail());
		final AuthUserCredentialsDTO authUserCredentialsDTO = new AuthUserCredentialsDTO(authUserCredentials);
		model.addAttribute("authUserCredentialsDTO", authUserCredentialsDTO);

		return this.getNextView(model, operation, language, "authUser/sessionAuthUserChangePassword.html");
	}

	/**
	 * Método que guarda en la base de datos la contraseña modificada
	 */
	@PostMapping("/my-user/auth-users/change-password")
	public String saveNewPassword(HttpServletRequest httpServletRequest, Model model, AuthUserCredentialsDTO authUserCredentialsDTO, BindingResult bindingResult, @RequestParam(name="lang", required=false) String language, RedirectAttributes redirectAttributes) throws MessagingException {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextRedirect(language, "/my-user/auth-users/change-password");
		}

		final AuthUser sessionAuthUser = this.getRefreshedSessionAuthUser(httpServletRequest);
		final String email = sessionAuthUser.getEmail();
//		final String email = authUserCredentialsDTO.getEmail();
		final String password = authUserCredentialsDTO.getPassword();
		final String newPassword1 = authUserCredentialsDTO.getNewPassword1();
		final String newPassword2 = authUserCredentialsDTO.getNewPassword2();

		if(!newPassword1.equals(newPassword2)) {
			String errorMsg = this.getInternationalizedMessage(language, "saveNewPassword.newPasswordsDontMatch", null);
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return this.getNextRedirect(language, "/my-user/auth-users/change-password");
		}
		if(password.equals(newPassword1)) {
			String errorMsg = this.getInternationalizedMessage(language, "saveNewPassword.newPasswordUnchanged", null);
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return this.getNextRedirect(language, "/my-user/auth-users/change-password");
		}

		AuthUserCredentials authUserCredentials = authUserCredentialsService.findByEmail(email);
		if(authUserCredentials == null) {
			String errorMsg = this.getInternationalizedMessage(language, "saveNewPassword.emailOrPasswordIncorrect", new Object[]{email});
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return this.getNextRedirect(language, "/my-user/auth-users/change-password");
		}
		if(!passwordEncoder.matches(password, authUserCredentials.getEncryptedPassword())) {
			String errorMsg = this.getInternationalizedMessage(language, "saveNewPassword.emailOrPasswordIncorrect", new Object[]{email});
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return this.getNextRedirect(language, "/my-user/auth-users/change-password");
		}
		final String encryptedNewPassword = passwordEncoder.encode(newPassword1);
		authUserCredentials.setEncryptedPassword(encryptedNewPassword);
		authUserCredentialsService.save(authUserCredentials);

		String successMsg = this.getInternationalizedMessage(language, "saveNewPassword.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/my-user/auth-users/change-password");
	}

	/**
	 * Método para renderizar la tabla para consultar las solicitudes de un usuario
	 */
	@GetMapping("/my-user/auth-users/job-requests")
	public String getJobRequests(HttpServletRequest httpServletRequest, Model model, @ModelAttribute("tableSearchDTO") TableSearchDTO tableSearchDTO, BindingResult bindingResult, Pageable pageable, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /my-user/auth-users/job-requests";

		final AuthUser sessionAuthUser = this.getRefreshedSessionAuthUser(httpServletRequest);
		Long sessionAuthUserId = sessionAuthUser.getId();
		String sessionAuthUserEmail = sessionAuthUser.getEmail();
		
		if (bindingResult.hasErrors()) {
			final Page<JobRequest> jobRequests = Page.empty();
			model.addAttribute("authUserId", sessionAuthUserId);
			model.addAttribute("authUserEmail", sessionAuthUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("tableSearchDTO", tableSearchDTO);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "authUser/authUserJobRequests.html");
		}

		final AbstractEntityPageWithException<JobRequest> pageWithException = jobRequestService.getAuthUserEntityPage(sessionAuthUserId, tableSearchDTO, pageable);
		final Page<JobRequest> jobRequests = pageWithException.getPage();
		final Exception exception = pageWithException.getException();

		model.addAttribute("authUserId", sessionAuthUserId);
		model.addAttribute("authUserEmail", sessionAuthUserEmail);
		model.addAttribute("jobRequests", jobRequests);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("pageNumber", 0);

		if(exception != null) {
			model.addAttribute("errorMsg", exception.getMessage());
		}

		return this.getNextView(model, operation, language, "authUser/authUserJobRequests.html");
	}
	
	/**
	 * Método para renderizar la tabla para consultar los currículums de un usuario
	 */
	@GetMapping("/my-user/auth-users/auth-user-curriculums")
	public String getAuthUserCurriculums(HttpServletRequest httpServletRequest, Model model, @ModelAttribute("tableSearchDTO") TableSearchDTO tableSearchDTO, BindingResult bindingResult, Pageable pageable, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /my-user/auth-users/auth-user-curriculums";

		final AuthUser sessionAuthUser = this.getRefreshedSessionAuthUser(httpServletRequest);
		Long sessionAuthUserId = sessionAuthUser.getId();
		String sessionAuthUserEmail = sessionAuthUser.getEmail();
		
		if (bindingResult.hasErrors()) {
			final Page<AuthUserCurriculum> authUserCurriculums = Page.empty();
			model.addAttribute("authUserId", sessionAuthUserId);
			model.addAttribute("authUserEmail", sessionAuthUserEmail);
			model.addAttribute("authUserCurriculums", authUserCurriculums);
			model.addAttribute("tableSearchDTO", tableSearchDTO);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "authUser/sessionAuthUserCurriculums.html");
		}

		final AbstractEntityPageWithException<AuthUserCurriculum> pageWithException = authUserCurriculumService.getAuthUserEntityPage(sessionAuthUserId, tableSearchDTO, pageable);
		final Page<AuthUserCurriculum> authUserCurriculums = pageWithException.getPage();
		final Exception exception = pageWithException.getException();

		model.addAttribute("authUserId", sessionAuthUserId);
		model.addAttribute("authUserEmail", sessionAuthUserEmail);
		model.addAttribute("authUserCurriculums", authUserCurriculums);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("pageNumber", 0);

		if(exception != null) {
			model.addAttribute("errorMsg", exception.getMessage());
		}

		return this.getNextView(model, operation, language, "authUser/sessionAuthUserCurriculums.html");
	}
	
	private AuthUser getRefreshedSessionAuthUser(HttpServletRequest httpServletRequest) {
		AuthUser sessionAuthUser = (AuthUser) httpServletRequest.getSession().getAttribute(Constants.SESSION_AUTH_USER);
		sessionAuthUser = abstractEntityService.refreshEntity(sessionAuthUser);
		return sessionAuthUser;
	}
	
//	private AuthUser getRefreshedSessionAuthUser(Authentication authentication) {
//		final String sessionAuthUserEmail = authentication.getName();
//		final AuthUser sessionAuthUser = authUserService.findByEmail(sessionAuthUserEmail);
//		return sessionAuthUser;
//	}

}
