package com.aliuken.jobvacanciesapp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.JobVacanciesAppConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.controller.superinterface.GenericControllerWithJobCompanyLogoInterface;
import com.aliuken.jobvacanciesapp.model.AbstractEntityPageWithException;
import com.aliuken.jobvacanciesapp.model.AuthUser;
import com.aliuken.jobvacanciesapp.model.JobCompany;
import com.aliuken.jobvacanciesapp.model.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.dto.FileType;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobCompanyLogoDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobCompanyLogoService;
import com.aliuken.jobvacanciesapp.service.JobCompanyService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;
import com.aliuken.jobvacanciesapp.util.FileUtils;

@Controller
public class JobCompanyController implements GenericControllerWithJobCompanyLogoInterface {
	
	@Autowired
	private JobVacanciesAppConfigPropertiesBean jobVacanciesAppConfigPropertiesBean;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JobCompanyService jobCompanyService;
	
	@Autowired
	private JobCompanyLogoService jobCompanyLogoService;
	
	@Autowired
	private JobVacancyService jobVacancyService;
	
	@Autowired
	private AuthUserService authUserService;

	private static String jobCompanyLogosPath;
	private static boolean useAjaxToRefreshJobCompanyLogos;

	@PostConstruct
	private void postConstruct() {
		jobCompanyLogosPath = jobVacanciesAppConfigPropertiesBean.getJobCompanyLogosPath();
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
	 * Metodo que muestra la lista de companies con paginacion
	 */
	@GetMapping("/job-companies/index")
	public String index(Model model, @ModelAttribute("tableSearchDTO") TableSearchDTO tableSearchDTO, BindingResult bindingResult, Pageable pageable, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-companies/index";

		if (bindingResult.hasErrors()) {
			final Page<JobCompany> jobCompanies = Page.empty();
			model.addAttribute("jobCompanies", jobCompanies);
			model.addAttribute("tableSearchDTO", tableSearchDTO);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "jobCompany/listJobCompanies.html");
		}

		final AbstractEntityPageWithException<JobCompany> pageWithException = jobCompanyService.getEntityPage(tableSearchDTO, pageable);
		final Page<JobCompany> jobCompanies = pageWithException.getPage();
		final Exception exception = pageWithException.getException();

		model.addAttribute("jobCompanies", jobCompanies);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("pageNumber", 0);

		if(exception != null) {
			model.addAttribute("errorMsg", exception.getMessage());
		}

		return this.getNextView(model, operation, language, "jobCompany/listJobCompanies.html");
	}
	
	/**
	 * Método para renderizar la vista de los Detalles para una determinada empresa
	 */
	@GetMapping("/job-companies/view/{jobCompanyId}")
	public String view(Model model, @PathVariable("jobCompanyId") long jobCompanyId, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-companies/view/{jobCompanyId}";

		final JobCompany jobCompany = jobCompanyService.findById(jobCompanyId);
		model.addAttribute("jobCompany", jobCompany);

		return this.getNextView(model, operation, language, "jobCompany/jobCompanyDetail.html");
	}

	/**
	 * Método para renderizar el formulario para crear una nueva Empresa
	 */
	@GetMapping("/job-companies/create")
	public String create(Model model, @RequestParam(name="lang", required=false) String language, @RequestParam(name="jobCompanyLogo", required=false) String jobCompanyLogoUrlParam) {
		final String operation = "GET /job-companies/create";
		
		JobCompanyDTO jobCompanyDTO = new JobCompanyDTO();
		
		if(!useAjaxToRefreshJobCompanyLogos) {
			this.setSelectedJobCompanyLogoForJobCompanyForm(jobCompanyDTO, jobCompanyLogoUrlParam, true);
		}
		
		model.addAttribute("jobCompanyDTO", jobCompanyDTO);
		model.addAttribute("jobCompanyLogo", jobCompanyDTO.getSelectedLogoId());
		model.addAttribute("useAjaxToRefreshJobCompanyLogos", useAjaxToRefreshJobCompanyLogos);

		return this.getNextView(model, operation, language, "jobCompany/jobCompanyForm.html");
	}
	
	/**
	 * Método para renderizar el formulario para editar una Empresa
	 */
	@GetMapping("/job-companies/edit/{jobCompanyId}")
	public String edit(Model model, @PathVariable("jobCompanyId") long jobCompanyId, @RequestParam(name="lang", required=false) String language, @RequestParam(name="jobCompanyLogo", required=false) String jobCompanyLogoUrlParam) {
		final String operation = "GET /job-companies/edit/{jobCompanyId}";

		final JobCompany jobCompany = jobCompanyService.findById(jobCompanyId);
		final JobCompanyDTO jobCompanyDTO = new JobCompanyDTO(jobCompany);

		if(!useAjaxToRefreshJobCompanyLogos) {
			this.setSelectedJobCompanyLogoForJobCompanyForm(jobCompanyDTO, jobCompanyLogoUrlParam, false);
		}

		model.addAttribute("jobCompanyDTO", jobCompanyDTO);
		model.addAttribute("jobCompanyLogo", jobCompanyDTO.getSelectedLogoId());
		model.addAttribute("useAjaxToRefreshJobCompanyLogos", useAjaxToRefreshJobCompanyLogos);

		return this.getNextView(model, operation, language, "jobCompany/jobCompanyForm.html");
	}
	
	/**
	 * Método para refrescar el logo de la empresa seleccionada
	 */
	@GetMapping("/job-companies/refresh-logo")
	public String refreshLogo(Model model, @RequestParam(name="jobCompanyId", required=false) Long jobCompanyId, @RequestParam(name="jobCompanyLogo", required=false) String jobCompanyLogoUrlParam) {
		final JobCompanyDTO jobCompanyDTO;
		if(jobCompanyId != null) {
			final JobCompany jobCompany = jobCompanyService.findById(jobCompanyId);
			jobCompanyDTO = new JobCompanyDTO(jobCompany);
		} else {
			jobCompanyDTO = new JobCompanyDTO();
		}

		this.setSelectedJobCompanyLogoForJobCompanyForm(jobCompanyDTO, jobCompanyLogoUrlParam, false);
		
		model.addAttribute("jobCompanyDTO", jobCompanyDTO);
		model.addAttribute("isJobCompanyForm", true);

		return "fragments/jobCompanyLogoFragment :: jobCompanyLogoFragment";
	}

	/**
	 * Método para guardar una Empresa en la base de datos
	 */
	@PostMapping("/job-companies/save")
	public String save(Model model, @ModelAttribute("jobCompanyDTO") JobCompanyDTO jobCompanyDTO, BindingResult bindingResult, 
			@RequestParam(name="jobCompanySelectedLogoFile", required=false) MultipartFile multipartFile, RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		final String operation = "POST /job-companies/save";

		if (bindingResult.hasErrors()) {
			model.addAttribute("jobCompanyDTO", jobCompanyDTO);
			model.addAttribute("jobCompanyLogo", jobCompanyDTO.getSelectedLogoId());
			model.addAttribute("useAjaxToRefreshJobCompanyLogos", useAjaxToRefreshJobCompanyLogos);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "jobCompany/jobCompanyForm.html");
		}
		
		Long id = jobCompanyDTO.getId();
		final String name = jobCompanyDTO.getName();
		final String description = jobCompanyDTO.getDescription();
		final Long selectedLogoId = jobCompanyDTO.getSelectedLogoId();
		final String firstRegistrationAuthUserEmail = jobCompanyDTO.getFirstRegistrationAuthUserEmail();
		final Set<JobCompanyLogoDTO> jobCompanyLogoDTOs = jobCompanyDTO.getJobCompanyLogos();

		JobCompany jobCompany;
		final String savedJobCompanyLogoFileName;
		if(id != null) {
			jobCompany = jobCompanyService.findById(id);
			savedJobCompanyLogoFileName = FileUtils.saveFile(multipartFile, jobCompanyLogosPath + id + "/", FileType.JOB_VACANCY_LOGO);
		} else {
			jobCompany = new JobCompany();
			savedJobCompanyLogoFileName = FileUtils.saveFile(multipartFile, jobCompanyLogosPath, FileType.JOB_VACANCY_LOGO);
		}
		jobCompany.setName(name);
		jobCompany.setDescription(description);

		if(selectedLogoId != null) {
			if(!Constants.NO_SELECTED_LOGO_ID.equals(selectedLogoId)) {
				final JobCompanyLogo selectedLogo = jobCompanyLogoService.findById(selectedLogoId);
				final String selectedLogoFileName = selectedLogo.getFileName();
				jobCompany.setSelectedLogoFileName(selectedLogoFileName);
			} else {
				jobCompany.setSelectedLogoFileName(null);
			}
		} else {
			jobCompany.setSelectedLogoFileName(savedJobCompanyLogoFileName);
		}

		if(firstRegistrationAuthUserEmail != null) {
			AuthUser firstRegistrationAuthUser = authUserService.findByEmail(firstRegistrationAuthUserEmail);
			jobCompany.setFirstRegistrationAuthUser(firstRegistrationAuthUser);
		}

		final Set<JobCompanyLogo> jobCompanyLogos = new LinkedHashSet<>();
		if(jobCompanyLogoDTOs != null) {
			for(JobCompanyLogoDTO jobCompanyDTOLogo : jobCompanyLogoDTOs) {
				final JobCompanyLogo jobCompanyLogo;
				if(jobCompanyDTOLogo.getId() != null) {
					jobCompanyLogo = jobCompanyLogoService.findById(id);
				} else {
					jobCompanyLogo = new JobCompanyLogo();
				}
				jobCompanyLogo.setJobCompany(jobCompany);
				jobCompanyLogo.setFileName(jobCompanyDTOLogo.getFileName());
				
				jobCompanyLogos.add(jobCompanyLogo);
			}
		}
		
		JobCompanyLogo jobCompanyLogo = null;
		if(savedJobCompanyLogoFileName != null) {
			jobCompanyLogo = new JobCompanyLogo();
			jobCompanyLogo.setJobCompany(jobCompany);
			jobCompanyLogo.setFileName(savedJobCompanyLogoFileName);
			
			jobCompanyLogos.add(jobCompanyLogo);
		}

		jobCompany.setJobCompanyLogos(jobCompanyLogos);

		jobCompanyService.save(jobCompany);

		jobCompany = jobCompanyService.findByName(name);

		if(jobCompanyLogo != null) {
			jobCompanyLogo.setJobCompany(jobCompany);
			jobCompanyLogoService.save(jobCompanyLogo);
		}

		if(id == null && savedJobCompanyLogoFileName != null) {
			id = jobCompany.getId();
			try {
				Files.createDirectories(Paths.get(jobCompanyLogosPath + id));
				Files.move(Paths.get(jobCompanyLogosPath + savedJobCompanyLogoFileName), Paths.get(jobCompanyLogosPath + id + "/" + savedJobCompanyLogoFileName), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				model.addAttribute("jobCompanyDTO", jobCompanyDTO);
				model.addAttribute("jobCompanyLogo", jobCompanyDTO.getSelectedLogoId());
				model.addAttribute("useAjaxToRefreshJobCompanyLogos", useAjaxToRefreshJobCompanyLogos);
				model.addAttribute("errorMsg", e.getMessage());

				return this.getNextView(model, operation, language, "jobCompany/jobCompanyForm.html");
			}
		}

		String successMsg = this.getInternationalizedMessage(language, "saveJobCompany.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/job-companies/index");
	}

	/**
	 * Método para eliminar una Empresa de la base de datos
	 */
	@GetMapping("/job-companies/delete/{jobCompanyId}")
	public String delete(@PathVariable("jobCompanyId") long jobCompanyId, RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		JobCompany jobCompany = jobCompanyService.findById(jobCompanyId);

		Set<JobCompanyLogo> jobCompanyLogos = jobCompany.getJobCompanyLogos();
		if(jobCompanyLogos != null) {
			for(JobCompanyLogo jobCompanyLogo : jobCompanyLogos) {
				Long jobCompanyLogoId = jobCompanyLogo.getId();
				jobCompanyLogoService.deleteById(jobCompanyLogoId);
			}
		}

		Set<JobVacancy> jobVacancies = jobCompany.getJobVacancies();
		if(jobVacancies != null) {
			for(JobVacancy jobVacancy : jobVacancies) {
				Long jobVacancyId = jobVacancy.getId();
				jobVacancyService.deleteById(jobVacancyId);
			}
		}

		jobCompanyService.deleteById(jobCompanyId);
		
		String successMsg = this.getInternationalizedMessage(language, "deleteJobCompany.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/job-companies/index");
	}
	
	/**
	 * Metodo que muestra la lista de ofertas con paginacion
	 */
	@GetMapping("/job-companies/job-vacancies/{jobCompanyId}")
	public String getJobVacancies(@PathVariable("jobCompanyId") long jobCompanyId, Model model, @ModelAttribute("tableSearchDTO") TableSearchDTO tableSearchDTO, BindingResult bindingResult, Pageable pageable, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-companies/job-vacancies/{jobCompanyId}";
		
		final JobCompany jobCompany = jobCompanyService.findById(jobCompanyId);

		if (bindingResult.hasErrors()) {
			final Page<JobVacancy> jobVacancies = Page.empty();
			model.addAttribute("jobCompanyId", jobCompany.getId());
			model.addAttribute("jobCompanyName", jobCompany.getName());
			model.addAttribute("jobVacancies", jobVacancies);
			model.addAttribute("tableSearchDTO", tableSearchDTO);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "jobCompany/jobCompanyJobVacancies.html");
		}

		final AbstractEntityPageWithException<JobVacancy> pageWithException = jobVacancyService.getJobCompanyJobVacanciesPage(jobCompanyId, tableSearchDTO, pageable);
		final Page<JobVacancy> jobVacancies = pageWithException.getPage();
		final Exception exception = pageWithException.getException();

		model.addAttribute("jobCompanyId", jobCompany.getId());
		model.addAttribute("jobCompanyName", jobCompany.getName());
		model.addAttribute("jobVacancies", jobVacancies);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("pageNumber", 0);

		if(exception != null) {
			model.addAttribute("errorMsg", exception.getMessage());
		}

		return this.getNextView(model, operation, language, "jobCompany/jobCompanyJobVacancies.html");
	}

}
