package com.aliuken.jobvacanciesapp.controller;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliuken.jobvacanciesapp.controller.superinterface.GenericControllerInterface;
import com.aliuken.jobvacanciesapp.model.AbstractEntityPageWithException;
import com.aliuken.jobvacanciesapp.model.JobCategory;
import com.aliuken.jobvacanciesapp.model.JobVacancy;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.service.JobCategoryService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;

@Controller
public class JobCategoryController implements GenericControllerInterface {
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private JobCategoryService jobCategoryService;
	
	@Autowired
	private JobVacancyService jobVacancyService;
	
	@Override
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * Metodo que muestra la lista de categories con paginacion
	 */
	@GetMapping("/job-categories/index")
	public String index(Model model, @ModelAttribute("tableSearchDTO") TableSearchDTO tableSearchDTO, BindingResult bindingResult, Pageable pageable, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-categories/index";

		if (bindingResult.hasErrors()) {
			final Page<JobCategory> jobCategories = Page.empty();
			model.addAttribute("jobCategories", jobCategories);
			model.addAttribute("tableSearchDTO", tableSearchDTO);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "jobCategory/listJobCategories.html");
		}

		final AbstractEntityPageWithException<JobCategory> pageWithException = jobCategoryService.getEntityPage(tableSearchDTO, pageable);
		final Page<JobCategory> jobCategories = pageWithException.getPage();
		final Exception exception = pageWithException.getException();

		model.addAttribute("jobCategories", jobCategories);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("pageNumber", 0);

		if(exception != null) {
			model.addAttribute("errorMsg", exception.getMessage());
		}

		return this.getNextView(model, operation, language, "jobCategory/listJobCategories.html");
	}
	
	/**
	 * Método para renderizar la vista de los Detalles para una determinada categoría
	 */
	@GetMapping("/job-categories/view/{jobCategoryId}")
	public String view(Model model, @PathVariable("jobCategoryId") long jobCategoryId, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-categories/view/{jobCategoryId}";

		final JobCategory jobCategory = jobCategoryService.findById(jobCategoryId);
		model.addAttribute("jobCategory", jobCategory);

		return this.getNextView(model, operation, language, "jobCategory/jobCategoryDetail.html");
	}

	/**
	 * Método para renderizar el formulario para crear una nueva Categoría
	 */
	@GetMapping("/job-categories/create")
	public String create(Model model, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-categories/create";
		
		JobCategory jobCategory = new JobCategory();
		
		model.addAttribute("jobCategory", jobCategory);

		return this.getNextView(model, operation, language, "jobCategory/jobCategoryForm.html");
	}

	/**
	 * Método para renderizar el formulario para editar una Categoría
	 */
	@GetMapping("/job-categories/edit/{jobCategoryId}")
	public String edit(Model model, @PathVariable("jobCategoryId") long jobCategoryId, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-categories/edit/{jobCategoryId}";

		final JobCategory jobCategory = jobCategoryService.findById(jobCategoryId);
		model.addAttribute("jobCategory", jobCategory);

		return this.getNextView(model, operation, language, "jobCategory/jobCategoryForm.html");
	}
	
	/**
	 * Método para guardar una Categoría en la base de datos
	 */
	@PostMapping("/job-categories/save")
	public String save(Model model, JobCategory jobCategory, BindingResult bindingResult, 
			RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		final String operation = "POST /job-categories/save";

		if (bindingResult.hasErrors()) {
			model.addAttribute("jobCategory", jobCategory);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "jobCategory/jobCategoryForm.html");
		}

		jobCategoryService.save(jobCategory);
		
		String successMsg = this.getInternationalizedMessage(language, "saveJobCategory.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/job-categories/index");
	}

	/**
	 * Método para eliminar una Categoría de la base de datos
	 */
	@GetMapping("/job-categories/delete/{id}")
	public String delete(@PathVariable("jobCategoryId") long jobCategoryId, RedirectAttributes redirectAttributes, @RequestParam(name="lang", required=false) String language) {
		jobCategoryService.deleteById(jobCategoryId);
		
		String successMsg = this.getInternationalizedMessage(language, "deleteJobCategory.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return this.getNextRedirect(language, "/job-categories/index");
	}
	
	/**
	 * Metodo que muestra la lista de ofertas con paginacion
	 */
	@GetMapping("/job-categories/job-vacancies/{jobCategoryId}")
	public String getJobVacancies(@PathVariable("jobCategoryId") long jobCategoryId, Model model, @ModelAttribute("tableSearchDTO") TableSearchDTO tableSearchDTO, BindingResult bindingResult, Pageable pageable, @RequestParam(name="lang", required=false) String language) {
		final String operation = "GET /job-categories/job-vacancies/{jobCategoryId}";
		
		final JobCategory jobCategory = jobCategoryService.findById(jobCategoryId);

		if (bindingResult.hasErrors()) {
			final Page<JobVacancy> jobVacancies = Page.empty();
			model.addAttribute("jobCategoryId", jobCategory.getId());
			model.addAttribute("jobCategoryName", jobCategory.getName());
			model.addAttribute("jobVacancies", jobVacancies);
			model.addAttribute("tableSearchDTO", tableSearchDTO);
			model.addAttribute("errorMsg", bindingResult.getAllErrors().toString());

			return this.getNextView(model, operation, language, "jobCategory/jobCategoryJobVacancies.html");
		}

		final AbstractEntityPageWithException<JobVacancy> pageWithException = jobVacancyService.getJobCategoryJobVacanciesPage(jobCategoryId, tableSearchDTO, pageable);
		final Page<JobVacancy> jobVacancies = pageWithException.getPage();
		final Exception exception = pageWithException.getException();

		model.addAttribute("jobCategoryId", jobCategory.getId());
		model.addAttribute("jobCategoryName", jobCategory.getName());
		model.addAttribute("jobVacancies", jobVacancies);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("pageNumber", 0);

		if(exception != null) {
			model.addAttribute("errorMsg", exception.getMessage());
		}

		return this.getNextView(model, operation, language, "jobCategory/jobCategoryJobVacancies.html");
	}

}
