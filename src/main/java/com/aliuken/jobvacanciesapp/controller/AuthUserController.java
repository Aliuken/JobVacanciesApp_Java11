package com.aliuken.jobvacanciesapp.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliuken.jobvacanciesapp.controller.superinterface.GenericControllerInterface;
import com.aliuken.jobvacanciesapp.model.AbstractEntityPageWithException;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.model.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.service.AuthUserConfirmationService;
import com.aliuken.jobvacanciesapp.service.AuthUserCredentialsService;
import com.aliuken.jobvacanciesapp.service.AuthUserCurriculumService;
import com.aliuken.jobvacanciesapp.service.AuthUserRoleService;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@Controller
public class AuthUserController implements GenericControllerInterface {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private AuthUserConfirmationService authUserConfirmationService;

	@Autowired
	private AuthUserCredentialsService authUserCredentialsService;

	@Autowired
	private AuthUserRoleService authUserRoleService;

	@Autowired
	private AuthUserCurriculumService authUserCurriculumService;

	@Autowired
	private JobRequestService jobRequestService;

	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * Metodo que muestra la lista de usuarios sin paginacion
	 */
	@GetMapping("/auth-users/index")
	public String index(Model model, @ModelAttribute("tableSearchDTO") TableSearchDTO tableSearchDTO, BindingResult bindingResult, Pageable pageable, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /auth-users/index";

		if (bindingResult.hasErrors()) {
			final Page<AuthUser> authUsers = Page.empty();
			model.addAttribute("authUsers", authUsers);
			model.addAttribute("tableSearchDTO", tableSearchDTO);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "authUser/listAuthUsers.html");
		}

		final AbstractEntityPageWithException<AuthUser> pageWithException = authUserService.getEntityPage(tableSearchDTO, pageable);
		final Page<AuthUser> authUsers = pageWithException.getPage();
		final Exception exception = pageWithException.getException();

		model.addAttribute("authUsers", authUsers);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("pageNumber", 0);

		if(exception != null) {
			String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);
		}

		return this.getNextView(model, operation, language, "authUser/listAuthUsers.html");
	}

	/**
	 * Método para renderizar la vista de los Detalles para un determinado usuario
	 */
	@GetMapping("/auth-users/view/{authUserId}")
	public String view(Model model, @PathVariable("authUserId") long authUserId, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /auth-users/view/{authUserId}";

		final AuthUser authUser = authUserService.findByIdNotOptional(authUserId);
		model.addAttribute("authUser", authUser);

		return this.getNextView(model, operation, language, "authUser/authUserDetail.html");
	}

	/**
	 * Método para eliminar un usuario de la base de datos.
	 */
	@GetMapping("/auth-users/delete/{authUserId}")
	public String deleteById(@PathVariable("authUserId") long authUserId, RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		AuthUser authUser = authUserService.findByIdNotOptional(authUserId);

		AuthUserConfirmation authUserConfirmation = authUserConfirmationService.findByEmail(authUser.getEmail());
		if(authUserConfirmation != null) {
			authUserConfirmationService.deleteByIdAndFlush(authUserConfirmation.getId());
		}

		AuthUserCredentials authUserCredentials = authUserCredentialsService.findByEmail(authUser.getEmail());
		if(authUserCredentials != null) {
			authUserCredentialsService.deleteByIdAndFlush(authUserCredentials.getId());
		}

		Set<Long> jobRequestIds = authUser.getJobRequestIds();
		for(Long jobRequestId : jobRequestIds) {
			jobRequestService.deleteByIdAndFlush(jobRequestId);
		}

		Set<Long> authUserCurriculumIds = authUser.getAuthUserCurriculumIds();
		for(Long authUserCurriculumId : authUserCurriculumIds) {
			authUserCurriculumService.deleteByIdAndFlush(authUserCurriculumId);
		}

		Set<Long> authUserRoleIds = authUser.getAuthUserRoleIds();
		for(Long authUserRoleId : authUserRoleIds) {
			authUserRoleService.deleteByIdAndFlush(authUserRoleId);
		}

		authUserService.deleteByIdAndFlush(authUserId);

		String successMsg = this.getInternationalizedMessage(language, "deleteAuthUser.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/auth-users/index");
	}

	/**
	 * Método para bloquear un usuario
	 */
	@GetMapping("/auth-users/lock/{authUserId}")
	public String lock(@PathVariable("authUserId") long authUserId, RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		authUserService.lock(authUserId);
		String successMsg = this.getInternationalizedMessage(language, "lockAuthUser.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/auth-users/index");
	}

	/**
	 * Método para desbloquear un usuario
	 */
	@GetMapping("/auth-users/unlock/{authUserId}")
	public String unlock(@PathVariable("authUserId") long authUserId, RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		authUserService.unlock(authUserId);
		String successMsg = this.getInternationalizedMessage(language, "unlockAuthUser.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/auth-users/index");
	}

	/**
	 * Método para desbloquear un usuario
	 */
	@GetMapping("/auth-users/job-requests/{authUserId}")
	public String getJobRequests(Model model, @PathVariable("authUserId") long authUserId, @ModelAttribute("tableSearchDTO") TableSearchDTO tableSearchDTO, BindingResult bindingResult, Pageable pageable, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /auth-users/job-requests/{authUserId}";

		final AuthUser authUser = authUserService.findByIdNotOptional(authUserId);
		final String authUserEmail = (authUser != null) ? authUser.getEmail() : null;

		if (bindingResult.hasErrors()) {
			final Page<JobRequest> jobRequests = Page.empty();
			model.addAttribute("authUserId", authUserId);
			model.addAttribute("authUserEmail", authUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("tableSearchDTO", tableSearchDTO);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "authUser/authUserJobRequests.html");
		}

		final AbstractEntityPageWithException<JobRequest> pageWithException = jobRequestService.getAuthUserEntityPage(authUserId, tableSearchDTO, pageable);
		final Page<JobRequest> jobRequests = pageWithException.getPage();
		final Exception exception = pageWithException.getException();

		model.addAttribute("authUserId", authUserId);
		model.addAttribute("authUserEmail", authUserEmail);
		model.addAttribute("jobRequests", jobRequests);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("pageNumber", 0);

		if(exception != null) {
			String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);
		}

		return this.getNextView(model, operation, language, "authUser/authUserJobRequests.html");
	}

}
