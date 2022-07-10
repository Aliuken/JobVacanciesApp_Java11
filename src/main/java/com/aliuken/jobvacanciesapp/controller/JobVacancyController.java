package com.aliuken.jobvacanciesapp.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

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

import com.aliuken.jobvacanciesapp.config.JobVacanciesAppConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.controller.superinterface.GenericControllerWithJobCompanyLogoInterface;
import com.aliuken.jobvacanciesapp.model.AbstractEntityPageWithException;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.JobRequest;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.model.dto.JobCategoryDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobVacancyDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobCategoryService;
import com.aliuken.jobvacanciesapp.service.JobCompanyLogoService;
import com.aliuken.jobvacanciesapp.service.JobCompanyService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;
import com.aliuken.jobvacanciesapp.util.ThrowableUtils;

@Controller
public class JobVacancyController implements GenericControllerWithJobCompanyLogoInterface {

	@Autowired
	private JobVacanciesAppConfigPropertiesBean jobVacanciesAppConfigPropertiesBean;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JobVacancyService jobVacancyService;

	@Autowired
	private JobCategoryService jobCategoryService;

	@Autowired
	private JobCompanyService jobCompanyService;

	@Autowired
	private JobCompanyLogoService jobCompanyLogoService;

	@Autowired
	private JobRequestService jobRequestService;

	@Autowired
	private AuthUserService authUserService;

	private static boolean useAjaxToRefreshJobCompanyLogos;

	@PostConstruct
	private void postConstruct() {
		useAjaxToRefreshJobCompanyLogos = jobVacanciesAppConfigPropertiesBean.isUseAjaxToRefreshJobCompanyLogos();
	}

	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	@Override
	public JobCompanyLogoService getJobCompanyLogoService() {
		return jobCompanyLogoService;
	}

	/**
	 * Metodo que muestra la lista de ofertas con paginacion
	 */
	@GetMapping("/job-vacancies/index")
	public String index(Model model, @ModelAttribute("tableSearchDTO") TableSearchDTO tableSearchDTO, BindingResult bindingResult, Pageable pageable, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-vacancies/index";

		if (bindingResult.hasErrors()) {
			final Page<JobVacancy> jobVacancies = Page.empty();
			model.addAttribute("jobVacancies", jobVacancies);
			model.addAttribute("tableSearchDTO", tableSearchDTO);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "jobVacancy/listJobVacancies.html");
		}

		final AbstractEntityPageWithException<JobVacancy> pageWithException = jobVacancyService.getEntityPage(tableSearchDTO, pageable);
		final Page<JobVacancy> jobVacancies = pageWithException.getPage();
		final Exception exception = pageWithException.getException();

		model.addAttribute("jobVacancies", jobVacancies);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("pageNumber", 0);

		if(exception != null) {
			String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);
		}

		return this.getNextView(model, operation, language, "jobVacancy/listJobVacancies.html");
	}

	/**
	 * Método que muestra el formulario para crear una nueva Oferta
	 */
	@GetMapping("/job-vacancies/create")
	public String create(Model model, @RequestParam(name="lang", required=false) String language, @RequestParam(name="jobCompanyLogo", required=false) String jobCompanyLogoUrlParam) {
		final String operation = "GET /job-vacancies/create";

		JobCompanyDTO jobCompanyDTO = new JobCompanyDTO();

		if(!useAjaxToRefreshJobCompanyLogos) {
			this.setSelectedJobCompanyLogoForJobVacancyForm(jobCompanyDTO, jobCompanyLogoUrlParam, true);
		}

		JobVacancyDTO jobVacancyDTO = new JobVacancyDTO();
		jobVacancyDTO.setJobCompany(jobCompanyDTO);

		model.addAttribute("jobVacancyDTO", jobVacancyDTO);
		model.addAttribute("jobCompanyLogo", jobVacancyDTO.getJobCompany().getSelectedLogoId());
		model.addAttribute("useAjaxToRefreshJobCompanyLogos", useAjaxToRefreshJobCompanyLogos);

		return this.getNextView(model, operation, language, "jobVacancy/jobVacancyForm.html");
	}


	/**
	 * Método que renderiza el formulario HTML para editar una oferta
	 */
	@GetMapping("/job-vacancies/edit/{jobVacancyId}")
	public String edit(Model model, @PathVariable("jobVacancyId") long jobVacancyId, @RequestParam(name="lang", required=false) String language, @RequestParam(name="jobCompanyLogo", required=false) String jobCompanyLogoUrlParam) {
		final String operation = "GET /job-vacancies/edit/{jobVacancyId}";

		final JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);
		final JobVacancyDTO jobVacancyDTO = new JobVacancyDTO(jobVacancy);

		JobCompanyDTO jobCompanyDTO = jobVacancyDTO.getJobCompany();

		if(!useAjaxToRefreshJobCompanyLogos) {
			this.setSelectedJobCompanyLogoForJobVacancyForm(jobCompanyDTO, jobCompanyLogoUrlParam, false);
		}

		jobVacancyDTO.setJobCompany(jobCompanyDTO);

		model.addAttribute("jobVacancyDTO", jobVacancyDTO);
		model.addAttribute("jobCompanyLogo", jobVacancyDTO.getJobCompany().getSelectedLogoId());
		model.addAttribute("useAjaxToRefreshJobCompanyLogos", useAjaxToRefreshJobCompanyLogos);

		return this.getNextView(model, operation, language, "jobVacancy/jobVacancyForm.html");
	}

	/**
	 * Método para refrescar el logo de la empresa seleccionada
	 */
	@GetMapping("/job-vacancies/refresh-logo")
	public String refreshLogo(Model model, @RequestParam(name="jobCompanyId", required=false) Long jobCompanyId, @RequestParam(name="jobCompanyLogo", required=false) String jobCompanyLogoUrlParam) {
		final JobCompanyDTO jobCompanyDTO;
		if(jobCompanyId != null) {
			final JobCompany jobCompany = jobCompanyService.findByIdNotOptional(jobCompanyId);
			jobCompanyDTO = new JobCompanyDTO(jobCompany);
		} else {
			jobCompanyDTO = new JobCompanyDTO();
		}

		this.setSelectedJobCompanyLogoForJobVacancyForm(jobCompanyDTO, jobCompanyLogoUrlParam, false);

		model.addAttribute("jobCompanyDTO", jobCompanyDTO);
		model.addAttribute("isJobCompanyForm", false);

		return "fragments/jobCompanyLogoFragment :: jobCompanyLogoFragment";
	}

	/**
	 * Método que guarda la Oferta en la base de datos
	 */
	@PostMapping("/job-vacancies/save")
	public String save(Model model, @ModelAttribute JobVacancyDTO jobVacancyDTO, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {

		final String operation = "POST /job-vacancies/save";

		if (bindingResult.hasErrors()) {
			model.addAttribute("jobVacancyDTO", jobVacancyDTO);
			model.addAttribute("jobCompanyLogo", jobVacancyDTO.getJobCompany().getSelectedLogoId());
			model.addAttribute("useAjaxToRefreshJobCompanyLogos", useAjaxToRefreshJobCompanyLogos);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "jobVacancy/jobVacancyForm.html");
		}

		Long id = jobVacancyDTO.getId();
		String name = jobVacancyDTO.getName();
		String description = jobVacancyDTO.getDescription();
		JobCategoryDTO jobCategoryDTO = jobVacancyDTO.getJobCategory();
		JobCompanyDTO jobCompanyDTO = jobVacancyDTO.getJobCompany();
		String statusName = jobVacancyDTO.getStatus();
		LocalDateTime publicationDateTime = jobVacancyDTO.getPublicationDateTime();
		BigDecimal salary = jobVacancyDTO.getSalary();
		Boolean highlighted = jobVacancyDTO.getHighlighted();
		String details = jobVacancyDTO.getDetails();
		LocalDateTime firstRegistrationDateTime = jobVacancyDTO.getFirstRegistrationDateTime();
		String firstRegistrationAuthUserEmail = jobVacancyDTO.getFirstRegistrationAuthUserEmail();

		JobCategory jobCategory;
		if(jobCategoryDTO != null) {
			Long jobCategoryDTOId = jobCategoryDTO.getId();
			if(jobCategoryDTOId != null) {
				jobCategory = jobCategoryService.findByIdNotOptional(jobCategoryDTOId);
			} else {
				jobCategory = null;
			}
		} else {
			jobCategory = null;
		}

		JobCompany jobCompany;
		if(jobCompanyDTO != null) {
			Long jobCompanyDTOId = jobCompanyDTO.getId();
			if(jobCompanyDTOId != null) {
				jobCompany = jobCompanyService.findByIdNotOptional(jobCompanyDTOId);
			} else {
				jobCompany = null;
			}
		} else {
			jobCompany = null;
		}

		JobVacancyStatus status = JobVacancyStatus.valueOf(statusName);

		JobVacancy jobVacancy = new JobVacancy();
		jobVacancy.setId(id);
		jobVacancy.setName(name);
		jobVacancy.setDescription(description);
		jobVacancy.setJobCategory(jobCategory);
		jobVacancy.setJobCompany(jobCompany);
		jobVacancy.setStatus(status);
		jobVacancy.setPublicationDateTime(publicationDateTime);
		jobVacancy.setSalary(salary);
		jobVacancy.setHighlighted(highlighted);
		jobVacancy.setDetails(details);
		jobVacancy.setFirstRegistrationDateTime(firstRegistrationDateTime);

		if(firstRegistrationAuthUserEmail != null) {
			AuthUser firstRegistrationAuthUser = authUserService.findByEmail(firstRegistrationAuthUserEmail);
			jobVacancy.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		}

		jobVacancy = jobVacancyService.saveAndFlush(jobVacancy);

		String successMsg = this.getInternationalizedMessage(language, "saveJobVacancy.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/job-vacancies/index");
	}

	/**
	 * Método para renderizar la vista de los Detalles para una determinada Oferta
	 */
	@GetMapping("/job-vacancies/view/{jobVacancyId}")
	public String view(Model model, @PathVariable("jobVacancyId") long jobVacancyId, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-vacancies/view/{jobVacancyId}";

		final JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);
		model.addAttribute("jobVacancy", jobVacancy);

		return this.getNextView(model, operation, language, "jobVacancy/jobVacancyDetail.html");
	}

	/**
	 * Método que elimina una Oferta de la base de datos
	 */
	@GetMapping("/job-vacancies/delete/{jobVacancyId}")
	public String delete(@PathVariable("jobVacancyId") long jobVacancyId, RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);

		Set<Long> jobRequestIds = jobVacancy.getJobRequestIds();
		for(Long jobRequestId : jobRequestIds) {
			jobRequestService.deleteByIdAndFlush(jobRequestId);
		}

		jobVacancyService.deleteByIdAndFlush(jobVacancyId);

		String successMsg = this.getInternationalizedMessage(language, "deleteJobVacancy.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/job-vacancies/index");
	}

	/**
	 * Método que verifica una Oferta de la base de datos
	 */
	@GetMapping("/job-vacancies/verify/{jobVacancyId}")
	public String verify(@PathVariable("jobVacancyId") long jobVacancyId, RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);
		if(!jobVacancy.isVerifiable()) {
			String errorMsg = this.getInternationalizedMessage(language, "verifyJobVacancy.notVerifiable", null);
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return this.getNextRedirect(language, "/job-vacancies/index");
		}

		jobVacancy.setStatus(JobVacancyStatus.APPROVED);
		jobVacancy.setHighlighted(Boolean.TRUE);
		jobVacancy = jobVacancyService.saveAndFlush(jobVacancy);

		String successMsg = this.getInternationalizedMessage(language, "verifyJobVacancy.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/job-vacancies/index");
	}

	/**
	 * Método para renderizar el formulario para consultar las solicitudes de una oferta de trabajo
	 */
	@GetMapping("/job-vacancies/job-requests/{jobVacancyId}")
	public String getJobRequests(@PathVariable("jobVacancyId") long jobVacancyId, Model model, @ModelAttribute("tableSearchDTO") TableSearchDTO tableSearchDTO, BindingResult bindingResult, Pageable pageable, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-vacancies/job-requests/{jobVacancyId}";

		JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);

		if (bindingResult.hasErrors()) {
			final Page<JobRequest> jobRequests = Page.empty();
			model.addAttribute("jobVacancyId", jobVacancy.getId());
			model.addAttribute("jobVacancyName", jobVacancy.getName());
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("tableSearchDTO", tableSearchDTO);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "jobVacancy/jobVacancyJobRequests.html");
		}

		final AbstractEntityPageWithException<JobRequest> pageWithException = jobRequestService.getJobVacancyJobRequestsPage(jobVacancyId, tableSearchDTO, pageable);
		final Page<JobRequest> jobRequests = pageWithException.getPage();
		final Exception exception = pageWithException.getException();

		model.addAttribute("jobVacancyId", jobVacancy.getId());
		model.addAttribute("jobVacancyName", jobVacancy.getName());
		model.addAttribute("jobRequests", jobRequests);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("pageNumber", 0);

		if(exception != null) {
			String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);
		}

		return this.getNextView(model, operation, language, "jobVacancy/jobVacancyJobRequests.html");
	}

	/**
	 * Agregamos al Model la lista de Categories: De esta forma nos evitamos
	 * agregarlos en los metodos crear y editar.
	 */
	@ModelAttribute
	public void setGenerics(Model model, Authentication authentication) {
		final List<JobCategory> jobCategories = jobCategoryService.findAll();
		model.addAttribute("jobCategories", jobCategories);

		final List<JobCompany> jobCompanies = jobCompanyService.findAll();
		model.addAttribute("jobCompanies", jobCompanies);

		final String email = authentication.getName();
		final AuthUser authUser = authUserService.findByEmail(email);
		final Set<Long> authUserJobVacancyIds = authUser.getJobVacancyIds();
		model.addAttribute("authUserJobVacancyIds", authUserJobVacancyIds);
	}

}
