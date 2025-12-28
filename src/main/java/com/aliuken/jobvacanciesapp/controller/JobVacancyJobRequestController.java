package com.aliuken.jobvacanciesapp.controller;

import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithPredefinedFilter;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PredefinedFilterEntity;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerNavigationUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class JobVacancyJobRequestController extends AbstractEntityControllerWithPredefinedFilter<JobRequest> {

	@Autowired
	private JobVacancyService jobVacancyService;

	@Autowired
	private JobRequestService jobRequestService;

	/**
	 * Method to show the list of job requests of a job vacancy with pagination
	 */
	@GetMapping("/job-vacancies/job-requests/{jobVacancyId}")
	public String getJobRequests(Model model, Pageable pageable, @PathVariable("jobVacancyId") long jobVacancyId,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /job-vacancies/job-requests/{jobVacancyId}";

		final JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);
		final String jobVacancyName = (jobVacancy != null) ? jobVacancy.getName() : null;

		try {
			if(tableSearchDTO == null || !tableSearchDTO.hasAllParameters()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDtoString = String.valueOf(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("predefinedFilterField1", jobVacancyId);
				model.addAttribute("predefinedFilterField2", jobVacancyName);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", this.getPaginationUrl(jobVacancyId));
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return ControllerNavigationUtils.getNextViewWithTable("jobVacancy/jobVacancyJobRequests.html", model, operation, tableSearchDTO, false);
			}

			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("predefinedFilterField1", jobVacancyId);
				model.addAttribute("predefinedFilterField2", jobVacancyName);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", this.getPaginationUrl(jobVacancyId));
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				//model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);
				model.addAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextViewWithTable("jobVacancy/jobVacancyJobRequests.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobRequest> pageWithExceptionDTO = jobRequestService.getJobVacancyJobRequestsPage(jobVacancyId, tableSearchDTO, pageable);
			final Page<JobRequest> jobRequests = pageWithExceptionDTO.getPage();
			final Throwable throwable = pageWithExceptionDTO.getThrowable();

			model.addAttribute("predefinedFilterField1", jobVacancyId);
			model.addAttribute("predefinedFilterField2", jobVacancyName);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", this.getPaginationUrl(jobVacancyId));
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl(jobVacancyId));

			if(throwable != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(throwable);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return ControllerNavigationUtils.getNextViewWithTable("jobVacancy/jobVacancyJobRequests.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<JobRequest> jobRequests = Page.empty();
			model.addAttribute("predefinedFilterField1", jobVacancyId);
			model.addAttribute("predefinedFilterField2", jobVacancyName);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", this.getPaginationUrl(jobVacancyId));
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextViewWithTable("jobVacancy/jobVacancyJobRequests.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of job vacancy requests with pagination to pdf
	 */
	@GetMapping("/job-vacancies/job-requests/{jobVacancyId}/export-to-pdf")
	@ResponseBody
	public byte[] exportToPdf(Model model, Pageable pageable, @PathVariable("jobVacancyId") long jobVacancyId,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) Integer pageSize,
			@RequestParam(name="pageNumber", required=false) Integer pageNumber) {

		final String predefinedFilterEntityName = PredefinedFilterEntity.JOB_VACANCY.getUpperCasedEntityName();
		final String jobVacancyIdString = String.valueOf(jobVacancyId);

		final PredefinedFilterDTO predefinedFilterDTO = new PredefinedFilterDTO(predefinedFilterEntityName, jobVacancyIdString);
		final TableSearchDTO tableSearchDTO = new TableSearchDTO(languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		final BindingResult bindingResult = null;

		this.getJobRequests(model, pageable, jobVacancyId, tableSearchDTO, bindingResult);
		final byte[] pdfByteArray = this.storeAndDownloadPdf(predefinedFilterDTO, tableSearchDTO, model, PageEntityEnum.JOB_REQUEST, httpServletRequest, httpServletResponse);
		return pdfByteArray;
	}

	/**
	 * Method to delete a job request of a job vacancy
	 */
	@GetMapping("/job-vacancies/job-requests/delete/{jobVacancyId}/{jobRequestId}")
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("jobVacancyId") long jobVacancyId, @PathVariable("jobRequestId") long jobRequestId,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) String pageSize,
			@RequestParam(name="pageNumber", required=false) String pageNumber) {

		jobRequestService.deleteByIdAndFlush(jobRequestId);

		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteJobRequest.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirectWithTable("/job-vacancies/job-requests/" + jobVacancyId, languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	@Override
	public String getPaginationUrl(final String predefinedFilterIdString) {
		return StringUtils.getStringJoined("/job-vacancies/job-requests/", predefinedFilterIdString);
	}
}
