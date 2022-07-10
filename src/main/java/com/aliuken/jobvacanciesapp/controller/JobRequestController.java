package com.aliuken.jobvacanciesapp.controller;

import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliuken.jobvacanciesapp.controller.superinterface.GenericControllerInterface;
import com.aliuken.jobvacanciesapp.model.AbstractEntityPageWithException;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@Controller
public class JobRequestController implements GenericControllerInterface {

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JobRequestService jobRequestService;

	@Autowired
	private JobVacancyService jobVacancyService;

	@Autowired
	private AuthUserService authUserService;

	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * Metodo que muestra la lista de solicitudes con paginacion
	 */
	@GetMapping("/job-requests/index")
	public String index(Model model, @ModelAttribute("tableSearchDTO") TableSearchDTO tableSearchDTO, BindingResult bindingResult, Pageable pageable, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-requests/index";

		if (bindingResult.hasErrors()) {
			final Page<JobRequest> jobRequests = Page.empty();
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("tableSearchDTO", tableSearchDTO);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "jobRequest/listJobRequests.html");
		}

		final AbstractEntityPageWithException<JobRequest> pageWithException = jobRequestService.getEntityPage(tableSearchDTO, pageable);
		final Page<JobRequest> jobRequests = pageWithException.getPage();
		final Exception exception = pageWithException.getException();

		model.addAttribute("jobRequests", jobRequests);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("pageNumber", 0);

		if(exception != null) {
			String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);
		}

		return this.getNextView(model, operation, language, "jobRequest/listJobRequests.html");
	}

	/**
	 * Método para renderizar la vista de los Detalles para un determinado usuario
	 */
	@GetMapping("/job-requests/view/{jobRequestId}")
	public String view(Model model, @PathVariable("jobRequestId") long jobRequestId, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-requests/view/{jobRequestId}";

		final JobRequest jobRequest = jobRequestService.findByIdNotOptional(jobRequestId);
		model.addAttribute("jobRequest", jobRequest);

		return this.getNextView(model, operation, language, "jobRequest/jobRequestDetail.html");
	}

	/**
	 * Método para renderizar el formulario para aplicar para una Oferta
	 */
	@GetMapping("/job-requests/create/{jobVacancyId}")
	public String create(Model model, @PathVariable Long jobVacancyId, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-requests/create/{jobVacancyId}";

		final JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);
		final JobRequest jobRequest = new JobRequest();
		jobRequest.setJobVacancy(jobVacancy);

		model.addAttribute("jobVacancy", jobVacancy);
		model.addAttribute("jobRequest", jobRequest);

		return this.getNextView(model, operation, language, "jobRequest/jobRequestForm.html");
	}

	/**
	 * Método que guarda la solicitud enviada por el usuario en la base de datos
	 */
	@PostMapping("/job-requests/save")
	public String save(Model model, JobRequest jobRequest, BindingResult bindingResult, HttpSession httpSession,
			RedirectAttributes redirectAttributes, Authentication authentication, @RequestParam(name="lang", required=false) String language) {

		final String operation = "POST /job-requests/save";

		if (bindingResult.hasErrors()) {
			model.addAttribute("jobRequest", jobRequest);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "jobRequest/jobRequestForm.html");
		}

		final String email = authentication.getName();
		final AuthUser authUser = authUserService.findByEmail(email);
		jobRequest.setAuthUser(authUser);

		final Long jobVacancyId = jobRequest.getJobVacancy().getId();
		final JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);
		jobRequest.setJobVacancy(jobVacancy);

		jobRequest = jobRequestService.saveAndFlush(jobRequest);

		String successMsg = this.getInternationalizedMessage(language, "saveJobRequest.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		// return "redirect:/job-requests/index";
		return this.getNextRedirect(language, "/");
	}

	/**
	 * Método para eliminar una solicitud
	 */
	@GetMapping("/job-requests/delete/{jobRequestId}")
	public String delete(@PathVariable("jobRequestId") long jobRequestId, RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		jobRequestService.deleteByIdAndFlush(jobRequestId);

		String successMsg = this.getInternationalizedMessage(language, "deleteJobRequest.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/job-requests/index");
	}

	/**
	 * Metodo que agrega al modelo datos genéricos para todo el controlador
	 */
	@ModelAttribute
	public void setGenerics(Model model, Authentication authentication) {
		final String email = authentication.getName();
		final AuthUser authUser = authUserService.findByEmail(email);
		final Set<AuthUserCurriculum> authUserCurriculums = authUser.getAuthUserCurriculums();
		model.addAttribute("authUserCurriculums", authUserCurriculums);
	}

}
