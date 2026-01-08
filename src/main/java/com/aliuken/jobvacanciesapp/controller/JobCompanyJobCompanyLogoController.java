package com.aliuken.jobvacanciesapp.controller;

import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithPredefinedFilter;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PredefinedFilterEntity;
import com.aliuken.jobvacanciesapp.service.JobCompanyLogoService;
import com.aliuken.jobvacanciesapp.service.JobCompanyService;
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

@Controller
@Slf4j
public class JobCompanyJobCompanyLogoController extends AbstractEntityControllerWithPredefinedFilter<JobCompanyLogo> {

	@Autowired
	private JobCompanyService jobCompanyService;

	@Autowired
	private JobCompanyLogoService jobCompanyLogoService;

	/**
	 * Method to show the list of job vacancies of a company with pagination
	 */
	@GetMapping("/job-companies/job-company-logos/{jobCompanyId}")
	public String getJobCompanyLogos(Model model, @NonNull Pageable pageable, @PathVariable("jobCompanyId") long jobCompanyId,
									 @Validated @NonNull TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /job-companies/job-company-logos/{jobCompanyId}";

		final JobCompany jobCompany = jobCompanyService.findByIdNotOptional(jobCompanyId);
		final String jobCompanyName = (jobCompany != null) ? jobCompany.getName() : null;

		try {
			if(tableSearchDTO == null || !tableSearchDTO.hasAllParameters()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDtoString = String.valueOf(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<JobCompanyLogo> jobCompanyLogos = Page.empty();
				model.addAttribute("predefinedFilterField1", jobCompanyId);
				model.addAttribute("predefinedFilterField2", jobCompanyName);
				model.addAttribute("jobCompanyLogos", jobCompanyLogos);
				model.addAttribute("paginationUrl", this.getPaginationUrl(jobCompanyId));
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return ControllerNavigationUtils.getNextViewWithTable("jobCompany/jobCompanyJobCompanyLogos.html", model, operation, tableSearchDTO, false);
			}

			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				final Page<JobCompanyLogo> jobCompanyLogos = Page.empty();
				model.addAttribute("predefinedFilterField1", jobCompanyId);
				model.addAttribute("predefinedFilterField2", jobCompanyName);
				model.addAttribute("jobCompanyLogos", jobCompanyLogos);
				model.addAttribute("paginationUrl", this.getPaginationUrl(jobCompanyId));
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				//model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);
				model.addAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextViewWithTable("jobCompany/jobCompanyJobCompanyLogos.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobCompanyLogo> pageWithExceptionDTO = jobCompanyLogoService.getJobCompanyJobCompanyLogosPage(jobCompanyId, tableSearchDTO, pageable);
			final Page<JobCompanyLogo> jobCompanyLogos = pageWithExceptionDTO.getPage();
			final Throwable throwable = pageWithExceptionDTO.getThrowable();

			model.addAttribute("predefinedFilterField1", jobCompanyId);
			model.addAttribute("predefinedFilterField2", jobCompanyName);
			model.addAttribute("jobCompanyLogos", jobCompanyLogos);
			model.addAttribute("paginationUrl", this.getPaginationUrl(jobCompanyId));
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl(jobCompanyId));

			if(throwable != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(throwable);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return ControllerNavigationUtils.getNextViewWithTable("jobCompany/jobCompanyJobCompanyLogos.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<JobCompanyLogo> jobCompanyLogos = Page.empty();
			model.addAttribute("predefinedFilterField1", jobCompanyId);
			model.addAttribute("predefinedFilterField2", jobCompanyName);
			model.addAttribute("jobCompanyLogos", jobCompanyLogos);
			model.addAttribute("paginationUrl", this.getPaginationUrl(jobCompanyId));
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextViewWithTable("jobCompany/jobCompanyJobCompanyLogos.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of company logos with pagination to pdf
	 */
	@GetMapping("/job-companies/job-company-logos/{jobCompanyId}/exportToPdf")
	@ResponseBody
	public byte[] exportToPdf(Model model, @NonNull Pageable pageable, @PathVariable("jobCompanyId") long jobCompanyId,
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

		this.getJobCompanyLogos(model, pageable, jobCompanyId, tableSearchDTO, bindingResult);
		final byte[] pdfByteArray = this.storeAndDownloadPdf(predefinedFilterDTO, tableSearchDTO, model, PageEntityEnum.JOB_COMPANY_LOGO, httpServletRequest, httpServletResponse);
		return pdfByteArray;
	}

	/**
	 * Method to delete a job vacancy of a company
	 */
	@GetMapping("/job-companies/job-company-logos/delete/{jobCompanyId}/{jobCompanyLogoId}")
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("jobCompanyId") long jobCompanyId, @PathVariable("jobCompanyLogoId") long jobCompanyLogoId,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) String pageSize,
			@RequestParam(name="pageNumber", required=false) String pageNumber) {

		jobCompanyLogoService.deleteByIdAndFlush(jobCompanyLogoId);

		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteJobCompanyLogo.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirectWithTable("/job-companies/job-company-logos/" + jobCompanyId, languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	@Override
	public String getPaginationUrl(final String predefinedFilterIdString) {
		return StringUtils.getStringJoined("/job-companies/job-company-logos/", predefinedFilterIdString);
	}
}
