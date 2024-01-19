package com.aliuken.jobvacanciesapp.controller;

import java.util.Objects;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.JobCompany;
import com.aliuken.jobvacanciesapp.model.entity.JobCompanyLogo;
import com.aliuken.jobvacanciesapp.service.JobCompanyLogoService;
import com.aliuken.jobvacanciesapp.service.JobCompanyService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.web.MvcUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class JobCompanyJobCompanyLogoController {

	@Autowired
	private JobCompanyService jobCompanyService;

	@Autowired
	private JobCompanyLogoService jobCompanyLogoService;

	/**
	 * Method to show the list of job vacancies of a company with pagination
	 */
	@GetMapping("/job-companies/job-company-logos/{jobCompanyId}")
	public String getJobCompanyLogos(Model model, Pageable pageable, @PathVariable("jobCompanyId") long jobCompanyId,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /job-companies/job-company-logos/{jobCompanyId}";

		final JobCompany jobCompany = jobCompanyService.findByIdNotOptional(jobCompanyId);
		final String jobCompanyName = (jobCompany != null) ? jobCompany.getName() : null;

		try {
			if(tableSearchDTO == null || tableSearchDTO.hasEmptyAttribute()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDTOString = Objects.toString(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDTOString));
				}

				final Page<JobCompanyLogo> jobCompanyLogos = Page.empty();
				model.addAttribute("jobCompanyId", jobCompanyId);
				model.addAttribute("jobCompanyName", jobCompanyName);
				model.addAttribute("jobCompanyLogos", jobCompanyLogos);

				return MvcUtils.getNextViewWithTable("jobCompany/jobCompanyJobCompanyLogos.html", model, operation, tableSearchDTO, false);
			}

			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				final Page<JobCompanyLogo> jobCompanyLogos = Page.empty();
				model.addAttribute("jobCompanyId", jobCompanyId);
				model.addAttribute("jobCompanyName", jobCompanyName);
				model.addAttribute("jobCompanyLogos", jobCompanyLogos);
				model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);

				return MvcUtils.getNextViewWithTable("jobCompany/jobCompanyJobCompanyLogos.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobCompanyLogo> pageWithExceptionDTO = jobCompanyLogoService.getJobCompanyJobCompanyLogosPage(jobCompanyId, tableSearchDTO, pageable);
			final Page<JobCompanyLogo> jobCompanyLogos = pageWithExceptionDTO.getPage();
			final Exception exception = pageWithExceptionDTO.getException();

			model.addAttribute("jobCompanyId", jobCompanyId);
			model.addAttribute("jobCompanyName", jobCompanyName);
			model.addAttribute("jobCompanyLogos", jobCompanyLogos);

			if(exception != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return MvcUtils.getNextViewWithTable("jobCompany/jobCompanyJobCompanyLogos.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<JobCompanyLogo> jobCompanyLogos = Page.empty();
			model.addAttribute("jobCompanyId", jobCompanyId);
			model.addAttribute("jobCompanyName", jobCompanyName);
			model.addAttribute("jobCompanyLogos", jobCompanyLogos);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextViewWithTable("jobCompany/jobCompanyJobCompanyLogos.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to delete a job vacancy of a company
	 */
	@GetMapping("/job-companies/job-company-logos/delete/{jobCompanyId}/{jobCompanyLogoId}")
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("jobCompanyId") long jobCompanyId, @PathVariable("jobCompanyLogoId") long jobCompanyLogoId,
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="tableFieldCode", required=false) String tableFieldCode, @RequestParam(name="tableFieldValue", required=false) String tableFieldValue, @RequestParam(name="tableOrderCode", required=false) String tableOrderCode, @RequestParam(name="pageSize", required=false) String pageSize, @RequestParam(name="pageNumber", required=false) String pageNumber) {

		jobCompanyLogoService.deleteByIdAndFlush(jobCompanyLogoId);

		final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "deleteJobCompanyLogo.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return MvcUtils.getNextRedirectWithTable("/job-companies/job-company-logos/" + jobCompanyId, languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
	}

}
