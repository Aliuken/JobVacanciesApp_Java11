package com.aliuken.jobvacanciesapp.controller;

import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithPredefinedFilter;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.JobCategory;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PredefinedFilterEntity;
import com.aliuken.jobvacanciesapp.service.JobCategoryService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerNavigationUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

@Controller
@Slf4j
public class JobCategoryJobVacancyController extends AbstractEntityControllerWithPredefinedFilter<JobVacancy> {

	@Autowired
	private JobCategoryService jobCategoryService;

	@Autowired
	private JobVacancyService jobVacancyService;

	@Autowired
	private JobRequestService jobRequestService;

	/**
	 * Method to show the list of job vacancies of a category with pagination
	 */
	@GetMapping("/job-categories/job-vacancies/{jobCategoryId}")
	public String getJobVacancies(Model model, @NonNull Pageable pageable, @PathVariable("jobCategoryId") long jobCategoryId,
								  @Validated @NonNull TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /job-categories/job-vacancies/{jobCategoryId}";

		final JobCategory jobCategory = jobCategoryService.findByIdNotOptional(jobCategoryId);
		final String jobCategoryName = (jobCategory != null) ? jobCategory.getName() : null;

		try {
			if(tableSearchDTO == null || !tableSearchDTO.hasAllParameters()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDtoString = String.valueOf(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<JobVacancy> jobVacancies = Page.empty();
				model.addAttribute("predefinedFilterField1", jobCategoryId);
				model.addAttribute("predefinedFilterField2", jobCategoryName);
				model.addAttribute("jobVacancies", jobVacancies);
				model.addAttribute("paginationUrl", this.getPaginationUrl(jobCategoryId));
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return ControllerNavigationUtils.getNextViewWithTable("jobCategory/jobCategoryJobVacancies.html", model, operation, tableSearchDTO, false);
			}

			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				final Page<JobVacancy> jobVacancies = Page.empty();
				model.addAttribute("predefinedFilterField1", jobCategoryId);
				model.addAttribute("predefinedFilterField2", jobCategoryName);
				model.addAttribute("jobVacancies", jobVacancies);
				model.addAttribute("paginationUrl", this.getPaginationUrl(jobCategoryId));
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				//model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);
				model.addAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextViewWithTable("jobCategory/jobCategoryJobVacancies.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobVacancy> pageWithExceptionDTO = jobVacancyService.getJobCategoryJobVacanciesPage(jobCategoryId, tableSearchDTO, pageable);
			final Page<JobVacancy> jobVacancies = pageWithExceptionDTO.getPage();
			final Throwable throwable = pageWithExceptionDTO.getThrowable();

			model.addAttribute("predefinedFilterField1", jobCategoryId);
			model.addAttribute("predefinedFilterField2", jobCategoryName);
			model.addAttribute("jobVacancies", jobVacancies);
			model.addAttribute("paginationUrl", this.getPaginationUrl(jobCategoryId));
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl(jobCategoryId));

			if(throwable != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(throwable);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return ControllerNavigationUtils.getNextViewWithTable("jobCategory/jobCategoryJobVacancies.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<JobVacancy> jobVacancies = Page.empty();
			model.addAttribute("predefinedFilterField1", jobCategoryId);
			model.addAttribute("predefinedFilterField2", jobCategoryName);
			model.addAttribute("jobVacancies", jobVacancies);
			model.addAttribute("paginationUrl", this.getPaginationUrl(jobCategoryId));
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextViewWithTable("jobCategory/jobCategoryJobVacancies.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of job category vacancies with pagination to pdf
	 */
	@GetMapping("/job-categories/job-vacancies/{jobCategoryId}/exportToPdf")
	@ResponseBody
	public byte[] exportToPdf(Model model, @NonNull Pageable pageable, @PathVariable("jobCategoryId") long jobCategoryId,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) Integer pageSize,
			@RequestParam(name="pageNumber", required=false) Integer pageNumber) {

		final String predefinedFilterEntityName = PredefinedFilterEntity.JOB_CATEGORY.getUpperCasedEntityName();
		final String jobCategoryIdString = String.valueOf(jobCategoryId);

		final PredefinedFilterDTO predefinedFilterDTO = new PredefinedFilterDTO(predefinedFilterEntityName, jobCategoryIdString);
		final TableSearchDTO tableSearchDTO = new TableSearchDTO(languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		final BindingResult bindingResult = null;

		this.getJobVacancies(model, pageable, jobCategoryId, tableSearchDTO, bindingResult);
		final byte[] pdfByteArray = this.storeAndDownloadPdf(predefinedFilterDTO, tableSearchDTO, model, PageEntityEnum.JOB_VACANCY, httpServletRequest, httpServletResponse);
		return pdfByteArray;
	}

	/**
	 * Method to delete a job vacancy of a job category
	 */
	@GetMapping("/job-categories/job-vacancies/delete/{jobCategoryId}/{jobVacancyId}")
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("jobCategoryId") long jobCategoryId, @PathVariable("jobVacancyId") long jobVacancyId,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) String pageSize,
			@RequestParam(name="pageNumber", required=false) String pageNumber) {

		final JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);

		final Set<Long> jobRequestIds = jobVacancy.getJobRequestIds();
		for(final Long jobRequestId : jobRequestIds) {
			jobRequestService.deleteByIdAndFlush(jobRequestId);
		}

		jobVacancyService.deleteByIdAndFlush(jobVacancyId);

		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteJobVacancy.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirectWithTable("/job-categories/job-vacancies/" + jobCategoryId, languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	/**
	 * Method to verify a job vacancy of a job category
	 */
	@GetMapping("/job-categories/job-vacancies/verify/{jobCategoryId}/{jobVacancyId}")
	public String verify(RedirectAttributes redirectAttributes, @PathVariable("jobCategoryId") long jobCategoryId, @PathVariable("jobVacancyId") long jobVacancyId,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) String pageSize,
			@RequestParam(name="pageNumber", required=false) String pageNumber) {

		JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);
		if(!jobVacancy.isVerifiable()) {
			final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "verifyJobVacancy.notVerifiable", null);
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return ControllerNavigationUtils.getNextRedirectWithTable("/job-categories/job-vacancies/" + jobCategoryId, languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		}

		jobVacancy.setStatus(JobVacancyStatus.APPROVED);
		jobVacancy.setHighlighted(Boolean.TRUE);
		jobVacancy = jobVacancyService.saveAndFlush(jobVacancy);

		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "verifyJobVacancy.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirectWithTable("/job-categories/job-vacancies/" + jobCategoryId, languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	@Override
	public String getPaginationUrl(final String predefinedFilterIdString) {
		return StringUtils.getStringJoined("/job-categories/job-vacancies/", predefinedFilterIdString);
	}
}
