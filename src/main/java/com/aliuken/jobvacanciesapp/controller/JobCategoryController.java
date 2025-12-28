package com.aliuken.jobvacanciesapp.controller;

import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithoutPredefinedFilter;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobCategoryDTO;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.JobCategoryConverter;
import com.aliuken.jobvacanciesapp.model.entity.JobCategory;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.service.JobCategoryService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerNavigationUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerServletUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Controller
@Slf4j
public class JobCategoryController extends AbstractEntityControllerWithoutPredefinedFilter<JobCategory> {

	@Autowired
	private JobCategoryService jobCategoryService;

	@Autowired
	private JobVacancyService jobVacancyService;

	@Autowired
	private JobRequestService jobRequestService;

	/**
	 * Method to show the list of job categories with pagination
	 */
	@GetMapping("/job-categories/index")
	public String index(Model model, Pageable pageable,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /job-categories/index";

		try {
			if(tableSearchDTO == null || !tableSearchDTO.hasAllParameters()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDtoString = String.valueOf(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<JobCategory> jobCategories = Page.empty();
				model.addAttribute("jobCategories", jobCategories);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return ControllerNavigationUtils.getNextViewWithTable("jobCategory/listJobCategories.html", model, operation, tableSearchDTO, false);
			}

			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				final Page<JobCategory> jobCategories = Page.empty();
				model.addAttribute("jobCategories", jobCategories);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				//model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);
				model.addAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextViewWithTable("jobCategory/listJobCategories.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobCategory> pageWithExceptionDTO = jobCategoryService.getEntityPage(tableSearchDTO, pageable);
			final Page<JobCategory> jobCategories = pageWithExceptionDTO.getPage();
			final Throwable throwable = pageWithExceptionDTO.getThrowable();

			model.addAttribute("jobCategories", jobCategories);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl());

			if(throwable != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(throwable);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return ControllerNavigationUtils.getNextViewWithTable("jobCategory/listJobCategories.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<JobCategory> jobCategories = Page.empty();
			model.addAttribute("jobCategories", jobCategories);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextViewWithTable("jobCategory/listJobCategories.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of job categories with pagination to pdf
	 */
	@GetMapping("/job-categories/index/export-to-pdf")
	@ResponseBody
	public byte[] exportToPdf(Model model, Pageable pageable,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) Integer pageSize,
			@RequestParam(name="pageNumber", required=false) Integer pageNumber) {

		final PredefinedFilterDTO predefinedFilterDTO = null;
		final TableSearchDTO tableSearchDTO = new TableSearchDTO(languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		final BindingResult bindingResult = null;

		this.index(model, pageable, tableSearchDTO, bindingResult);
		final byte[] pdfByteArray = this.storeAndDownloadPdf(predefinedFilterDTO, tableSearchDTO, model, PageEntityEnum.JOB_CATEGORY, httpServletRequest, httpServletResponse);
		return pdfByteArray;
	}

	/**
	 * Method to show the detail of a job category
	 */
	@GetMapping("/job-categories/view/{jobCategoryId}")
	public String view(Model model, @PathVariable("jobCategoryId") long jobCategoryId,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /job-categories/view/{jobCategoryId}";

		final JobCategory jobCategory = jobCategoryService.findByIdNotOptional(jobCategoryId);
		model.addAttribute("jobCategory", jobCategory);

		return ControllerNavigationUtils.getNextView("jobCategory/jobCategoryDetail.html", model, operation, languageCode);
	}

	/**
	 * Method to show the creation form of a job category
	 */
	@GetMapping("/job-categories/create")
	public String create(HttpServletRequest httpServletRequest, Model model,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /job-categories/create";

		final Map<String, ?> inputFlashMap = ControllerServletUtils.getInputFlashMap(httpServletRequest);

		JobCategoryDTO jobCategoryDTO;
		if(inputFlashMap != null) {
			jobCategoryDTO = (JobCategoryDTO) inputFlashMap.get("jobCategoryDTO");
			if(jobCategoryDTO == null) {
				jobCategoryDTO = JobCategoryDTO.getNewInstance();
			}
		} else {
			jobCategoryDTO = JobCategoryDTO.getNewInstance();
		}

		model.addAttribute("jobCategoryDTO", jobCategoryDTO);

		return ControllerNavigationUtils.getNextView("jobCategory/jobCategoryForm.html", model, operation, languageCode);
	}

	/**
	 * Method to show the edition form of a job category
	 */
	@GetMapping("/job-categories/edit/{jobCategoryId}")
	public String edit(HttpServletRequest httpServletRequest, Model model, @PathVariable("jobCategoryId") long jobCategoryId,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /job-categories/edit/{jobCategoryId}";

		final Map<String, ?> inputFlashMap = ControllerServletUtils.getInputFlashMap(httpServletRequest);

		JobCategoryDTO jobCategoryDTO;
		if(inputFlashMap != null) {
			jobCategoryDTO = (JobCategoryDTO) inputFlashMap.get("jobCategoryDTO");
		} else {
			jobCategoryDTO = null;
		}

		if(jobCategoryDTO == null) {
			final JobCategory jobCategory = jobCategoryService.findByIdNotOptional(jobCategoryId);
			jobCategoryDTO = JobCategoryConverter.getInstance().convertEntityElement(jobCategory);
		}

		model.addAttribute("jobCategoryDTO", jobCategoryDTO);

		return ControllerNavigationUtils.getNextView("jobCategory/jobCategoryForm.html", model, operation, languageCode);
	}

	/**
	 * Method to save a job category in the database
	 */
	@PostMapping("/job-categories/save")
	public String save(RedirectAttributes redirectAttributes,
			@Validated JobCategoryDTO jobCategoryDTO, BindingResult bindingResult,
			@RequestParam(name="id", required=false) Long id, @RequestParam(name="languageParam", required=false) String languageCode) {
		try {
			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				redirectAttributes.addFlashAttribute("jobCategoryDTO", jobCategoryDTO);
				//redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.jobCategoryDTO", bindingResult);
				redirectAttributes.addFlashAttribute("errorMsg", firstBindingErrorString);

				if(id == null) {
					return ControllerNavigationUtils.getNextRedirect("/job-categories/create", languageCode);
				} else {
					return ControllerNavigationUtils.getNextRedirect("/job-categories/edit/" + id, languageCode);
				}
			}

			final String name = jobCategoryDTO.getName();
			final String description = jobCategoryDTO.getDescription();
			final Set<Long> jobVacancyIds = jobCategoryDTO.getJobVacancyIds();

			JobCategory jobCategory = jobCategoryService.findByIdOrNewEntity(id);
			jobCategory.setName(name);
			jobCategory.setDescription(description);

			final Set<JobVacancy> jobVacancies = new LinkedHashSet<>();
			if(jobVacancyIds != null) {
				for(final Long jobVacancyId : jobVacancyIds) {
					final JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);
					jobVacancies.add(jobVacancy);
				}
			}

			jobCategory.setJobVacancies(jobVacancies);

			jobCategory = jobCategoryService.saveAndFlush(jobCategory);

			final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "saveJobCategory.successMsg", null);
			redirectAttributes.addFlashAttribute("successMsg", successMsg);

			return ControllerNavigationUtils.getNextRedirect("/job-categories/view/" + jobCategory.getId(), languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("jobCategoryDTO", jobCategoryDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			if(id == null) {
				return ControllerNavigationUtils.getNextRedirect("/job-categories/create", languageCode);
			} else {
				return ControllerNavigationUtils.getNextRedirect("/job-categories/edit/" + id, languageCode);
			}
		}
	}

	/**
	 * Method to delete a job category
	 */
	@GetMapping("/job-categories/delete/{jobCategoryId}")
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("jobCategoryId") long jobCategoryId,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) String pageSize,
			@RequestParam(name="pageNumber", required=false) String pageNumber) {

		final JobCategory jobCategory = jobCategoryService.findByIdNotOptional(jobCategoryId);

		final Set<Long> jobVacancyIds = jobCategory.getJobVacancyIds();
		for(final Long jobVacancyId : jobVacancyIds) {
			final JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);

			final Set<Long> jobRequestIds = jobVacancy.getJobRequestIds();
			for(final Long jobRequestId : jobRequestIds) {
				jobRequestService.deleteByIdAndFlush(jobRequestId);
			}

			jobVacancyService.deleteByIdAndFlush(jobVacancyId);
		}

		jobCategoryService.deleteByIdAndFlush(jobCategoryId);

		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteJobCategory.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirectWithTable("/job-categories/index", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	@Override
	public String getPaginationUrl() {
		return "/job-categories/index";
	}
}
