package com.aliuken.jobvacanciesapp.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliuken.jobvacanciesapp.config.JobVacanciesAppConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.controller.superinterface.GenericControllerInterface;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.dto.FileType;
import com.aliuken.jobvacanciesapp.service.AuthUserCurriculumService;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.util.FileUtils;

@Controller
public class SessionAuthUserCurriculumController implements GenericControllerInterface {
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private JobVacanciesAppConfigPropertiesBean jobVacanciesAppConfigPropertiesBean;
	
	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private AuthUserCurriculumService authUserCurriculumService;
	
	@Autowired
	private JobRequestService jobRequestService;
	
	private static String authUserCurriculumFilesPath;

	@PostConstruct
	private void postConstruct() {
		authUserCurriculumFilesPath = jobVacanciesAppConfigPropertiesBean.getAuthUserCurriculumFilesPath();
	}
	
	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}
	
	/**
	 * Método para renderizar el formulario para crear un Currículum
	 */
	@GetMapping("/my-user/auth-user-curriculums/create")
	public String create(Model model, Authentication authentication, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /my-user/auth-user-curriculums/create";
		
		final AuthUser sessionAuthUser = this.getRefreshedSessionAuthUser(authentication);
		AuthUserCurriculum authUserCurriculum = new AuthUserCurriculum();
		authUserCurriculum.setAuthUser(sessionAuthUser);
		
		model.addAttribute("authUserCurriculum", authUserCurriculum);

		return this.getNextView(model, operation, language, "authUserCurriculum/sessionAuthUserCurriculumForm.html");
	}
	
	/**
	 * Método que guarda el currículum enviado por el usuario en la base de datos
	 */
	@PostMapping("/my-user/auth-user-curriculums/save")
	public String save(Model model, AuthUserCurriculum authUserCurriculum, BindingResult bindingResult, HttpSession httpSession,
			@RequestParam("curriculumFile") MultipartFile multipartFile, RedirectAttributes redirectAttributes,
			Authentication authentication, @RequestParam(name="lang", required=false) String language) {

		final String operation = "POST /my-user/auth-user-curriculums/save";

		if (bindingResult.hasErrors()) {
			model.addAttribute("authUserCurriculum", authUserCurriculum);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "authUserCurriculum/sessionAuthUserCurriculumForm.html");
		}

		final AuthUser sessionAuthUser = this.getRefreshedSessionAuthUser(authentication);
		authUserCurriculum.setAuthUser(sessionAuthUser);

		Long authUserId = sessionAuthUser.getId();
		final String curriculumFileName = FileUtils.saveFile(multipartFile, authUserCurriculumFilesPath + authUserId + "/", FileType.CURRICULUM);
		authUserCurriculum.setFileName(curriculumFileName);

		authUserCurriculumService.save(authUserCurriculum);

		String successMsg = this.getInternationalizedMessage(language, "saveUserCurriculum.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/my-user/auth-users/auth-user-curriculums");
	}
	
	/**
	 * Método para eliminar una solicitud
	 */
	@GetMapping("/my-user/auth-user-curriculums/delete/{authUserCurriculumId}")
	public String delete(@PathVariable("authUserCurriculumId") long authUserCurriculumId, Authentication authentication, RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		final String sessionAuthUserEmail = authentication.getName();
		final AuthUserCurriculum authUserCurriculum = authUserCurriculumService.findById(authUserCurriculumId);
		
		if(sessionAuthUserEmail != null && authUserCurriculum != null) {
			final AuthUser authUser = authUserCurriculum.getAuthUser();
			if(authUser != null && sessionAuthUserEmail.equals(authUser.getEmail())) {
				final String curriculumFileName = authUserCurriculum.getFileName();
				final List<JobRequest> jobRequests = jobRequestService.findByAuthUserAndCurriculumFileName(authUser, curriculumFileName);
				if(jobRequests != null) {
					for(JobRequest jobRequest : jobRequests) {
						jobRequestService.deleteById(jobRequest.getId());
					}
				}
				
				authUserCurriculumService.deleteById(authUserCurriculumId);
				String successMsg = this.getInternationalizedMessage(language, "deleteUserCurriculum.successMsg", null);
				redirectAttributes.addFlashAttribute("successMsg", successMsg);
				
				return this.getNextRedirect(language, "/my-user/auth-users/auth-user-curriculums");
			}
		}
		
		String errorMsg = this.getInternationalizedMessage(language, "deleteUserCurriculum.curriculumDoesNotBelongToUser", null);
		redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

		return this.getNextRedirect(language, "/my-user/auth-users/auth-user-curriculums");
	}
	
//	private AuthUser getRefreshedSessionAuthUser(HttpServletRequest httpServletRequest) {
//		AuthUser sessionAuthUser = (AuthUser) httpServletRequest.getSession().getAttribute(Constants.SESSION_AUTH_USER);
//		sessionAuthUser = abstractEntityService.refreshEntity(sessionAuthUser);
//		return sessionAuthUser;
//	}
	
	private AuthUser getRefreshedSessionAuthUser(Authentication authentication) {
		final String sessionAuthUserEmail = authentication.getName();
		final AuthUser sessionAuthUser = authUserService.findByEmail(sessionAuthUserEmail);
		return sessionAuthUser;
	}

}
