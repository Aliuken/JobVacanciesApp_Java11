package com.aliuken.jobvacanciesapp.controller;

import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithPredefinedFilter;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PredefinedFilterEntity;
import com.aliuken.jobvacanciesapp.service.JobCompanyService;
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
import java.util.Objects;
import java.util.Set;

@Controller
@Slf4j
public class JobCompanyJobVacancyController extends AbstractEntityControllerWithPredefinedFilter<JobVacancy> {

	@Autowired
	private JobCompanyService jobCompanyService;

	@Autowired
	private JobVacancyService jobVacancyService;

	@Autowired
	private JobRequestService jobRequestService;

	/**
	 * Method to show the list of job vacancies of a company with pagination
	 */
	@GetMapping("/job-companies/job-vacancies/{jobCompanyId}")
	public String getJobVacancies(Model model, Pageable pageable, @PathVariable("jobCompanyId") long jobCompanyId,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /job-companies/job-vacancies/{jobCompanyId}";

		final JobCompany jobCompany = jobCompanyService.findByIdNotOptional(jobCompanyId);
		final String jobCompanyName = (jobCompany != null) ? jobCompany.getName() : null;

		try {
			if(tableSearchDTO == null || !tableSearchDTO.hasAllParameters()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDtoString = Objects.toString(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<JobVacancy> jobVacancies = Page.empty();
				model.addAttribute("predefinedFilterField1", jobCompanyId);
				model.addAttribute("predefinedFilterField2", jobCompanyName);
				model.addAttribute("jobVacancies", jobVacancies);
				model.addAttribute("paginationUrl", this.getPaginationUrl(jobCompanyId));
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return ControllerNavigationUtils.getNextViewWithTable("jobCompany/jobCompanyJobVacancies.html", model, operation, tableSearchDTO, false);
			}

			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				final Page<JobVacancy> jobVacancies = Page.empty();
				model.addAttribute("predefinedFilterField1", jobCompanyId);
				model.addAttribute("predefinedFilterField2", jobCompanyName);
				model.addAttribute("jobVacancies", jobVacancies);
				model.addAttribute("paginationUrl", this.getPaginationUrl(jobCompanyId));
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				//model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);
				model.addAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextViewWithTable("jobCompany/jobCompanyJobVacancies.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobVacancy> pageWithExceptionDTO = jobVacancyService.getJobCompanyJobVacanciesPage(jobCompanyId, tableSearchDTO, pageable);
			final Page<JobVacancy> jobVacancies = pageWithExceptionDTO.getPage();
			final Throwable throwable = pageWithExceptionDTO.getThrowable();

			model.addAttribute("predefinedFilterField1", jobCompanyId);
			model.addAttribute("predefinedFilterField2", jobCompanyName);
			model.addAttribute("jobVacancies", jobVacancies);
			model.addAttribute("paginationUrl", this.getPaginationUrl(jobCompanyId));
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl(jobCompanyId));

			if(throwable != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(throwable);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return ControllerNavigationUtils.getNextViewWithTable("jobCompany/jobCompanyJobVacancies.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<JobVacancy> jobVacancies = Page.empty();
			model.addAttribute("predefinedFilterField1", jobCompanyId);
			model.addAttribute("predefinedFilterField2", jobCompanyName);
			model.addAttribute("jobVacancies", jobVacancies);
			model.addAttribute("paginationUrl", this.getPaginationUrl(jobCompanyId));
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextViewWithTable("jobCompany/jobCompanyJobVacancies.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of company job vacancies with pagination to pdf
	 */
	@GetMapping("/job-companies/job-vacancies/{jobCompanyId}/export-to-pdf")
	@ResponseBody
	public byte[] exportToPdf(Model model, Pageable pageable, @PathVariable("jobCompanyId") long jobCompanyId,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) Integer pageSize,
			@RequestParam(name="pageNumber", required=false) Integer pageNumber) {

		final String predefinedFilterEntityName = PredefinedFilterEntity.JOB_COMPANY.getUpperCasedEntityName();
		final String jobCompanyIdString = String.valueOf(jobCompanyId);

		final PredefinedFilterDTO predefinedFilterDTO = new PredefinedFilterDTO(predefinedFilterEntityName, jobCompanyIdString);
		final TableSearchDTO tableSearchDTO = new TableSearchDTO(languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		final BindingResult bindingResult = null;

		this.getJobVacancies(model, pageable, jobCompanyId, tableSearchDTO, bindingResult);
		final byte[] pdfByteArray = this.storeAndDownloadPdf(predefinedFilterDTO, tableSearchDTO, model, PageEntityEnum.JOB_VACANCY, httpServletRequest, httpServletResponse);
		return pdfByteArray;
	}

	/**
	 * Method to delete a job vacancy of a company
	 */
	@GetMapping("/job-companies/job-vacancies/delete/{jobCompanyId}/{jobVacancyId}")
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("jobCompanyId") long jobCompanyId, @PathVariable("jobVacancyId") long jobVacancyId,
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

		return ControllerNavigationUtils.getNextRedirectWithTable("/job-companies/job-vacancies/" + jobCompanyId, languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	/**
	 * Method to verify a job vacancy of a company
	 */
	@GetMapping("/job-companies/job-vacancies/verify/{jobCompanyId}/{jobVacancyId}")
	public String verify(RedirectAttributes redirectAttributes, @PathVariable("jobCompanyId") long jobCompanyId, @PathVariable("jobVacancyId") long jobVacancyId,
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

			return ControllerNavigationUtils.getNextRedirectWithTable("/job-companies/job-vacancies/" + jobCompanyId, languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		}

		jobVacancy.setStatus(JobVacancyStatus.APPROVED);
		jobVacancy.setHighlighted(Boolean.TRUE);
		jobVacancy = jobVacancyService.saveAndFlush(jobVacancy);

		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "verifyJobVacancy.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirectWithTable("/job-companies/job-vacancies/" + jobCompanyId, languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	@Override
	public String getPaginationUrl(final String predefinedFilterIdString) {
		return StringUtils.getStringJoined("/job-companies/job-vacancies/", predefinedFilterIdString);
	}
}
