package com.aliuken.jobvacanciesapp.controller;

import java.util.Objects;
import java.util.Set;

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
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.JobVacancyStatus;
import com.aliuken.jobvacanciesapp.service.JobCompanyService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.web.MvcUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class JobCompanyJobVacancyController {

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
			if(tableSearchDTO == null || tableSearchDTO.hasEmptyAttribute()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDTOString = Objects.toString(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDTOString));
				}

				final Page<JobVacancy> jobVacancies = Page.empty();
				model.addAttribute("jobCompanyId", jobCompanyId);
				model.addAttribute("jobCompanyName", jobCompanyName);
				model.addAttribute("jobVacancies", jobVacancies);

				return MvcUtils.getNextViewWithTable("jobCompany/jobCompanyJobVacancies.html", model, operation, tableSearchDTO, false);
			}

			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				final Page<JobVacancy> jobVacancies = Page.empty();
				model.addAttribute("jobCompanyId", jobCompanyId);
				model.addAttribute("jobCompanyName", jobCompanyName);
				model.addAttribute("jobVacancies", jobVacancies);
				model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);

				return MvcUtils.getNextViewWithTable("jobCompany/jobCompanyJobVacancies.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobVacancy> pageWithExceptionDTO = jobVacancyService.getJobCompanyJobVacanciesPage(jobCompanyId, tableSearchDTO, pageable);
			final Page<JobVacancy> jobVacancies = pageWithExceptionDTO.getPage();
			final Exception exception = pageWithExceptionDTO.getException();

			model.addAttribute("jobCompanyId", jobCompanyId);
			model.addAttribute("jobCompanyName", jobCompanyName);
			model.addAttribute("jobVacancies", jobVacancies);

			if(exception != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return MvcUtils.getNextViewWithTable("jobCompany/jobCompanyJobVacancies.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<JobVacancy> jobVacancies = Page.empty();
			model.addAttribute("jobCompanyId", jobCompanyId);
			model.addAttribute("jobCompanyName", jobCompanyName);
			model.addAttribute("jobVacancies", jobVacancies);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextViewWithTable("jobCompany/jobCompanyJobVacancies.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to delete a job vacancy of a company
	 */
	@GetMapping("/job-companies/job-vacancies/delete/{jobCompanyId}/{jobVacancyId}")
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("jobCompanyId") long jobCompanyId, @PathVariable("jobVacancyId") long jobVacancyId,
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="tableFieldCode", required=false) String tableFieldCode, @RequestParam(name="tableFieldValue", required=false) String tableFieldValue, @RequestParam(name="tableOrderCode", required=false) String tableOrderCode, @RequestParam(name="pageSize", required=false) String pageSize, @RequestParam(name="pageNumber", required=false) String pageNumber) {
		final JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);

		final Set<Long> jobRequestIds = jobVacancy.getJobRequestIds();
		for(final Long jobRequestId : jobRequestIds) {
			jobRequestService.deleteByIdAndFlush(jobRequestId);
		}

		jobVacancyService.deleteByIdAndFlush(jobVacancyId);

		final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "deleteJobVacancy.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return MvcUtils.getNextRedirectWithTable("/job-companies/job-vacancies/" + jobCompanyId, languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
	}

	/**
	 * Method to verify a job vacancy of a company
	 */
	@GetMapping("/job-companies/job-vacancies/verify/{jobCompanyId}/{jobVacancyId}")
	public String verify(RedirectAttributes redirectAttributes, @PathVariable("jobCompanyId") long jobCompanyId, @PathVariable("jobVacancyId") long jobVacancyId,
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="tableFieldCode", required=false) String tableFieldCode, @RequestParam(name="tableFieldValue", required=false) String tableFieldValue, @RequestParam(name="tableOrderCode", required=false) String tableOrderCode, @RequestParam(name="pageSize", required=false) String pageSize, @RequestParam(name="pageNumber", required=false) String pageNumber) {
		JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);
		if(!jobVacancy.isVerifiable()) {
			final String errorMsg = StringUtils.getInternationalizedMessage(languageCode, "verifyJobVacancy.notVerifiable", null);
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return MvcUtils.getNextRedirectWithTable("/job-companies/job-vacancies/" + jobCompanyId, languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
		}

		jobVacancy.setStatus(JobVacancyStatus.APPROVED);
		jobVacancy.setHighlighted(Boolean.TRUE);
		jobVacancy = jobVacancyService.saveAndFlush(jobVacancy);

		final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "verifyJobVacancy.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return MvcUtils.getNextRedirectWithTable("/job-companies/job-vacancies/" + jobCompanyId, languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
	}

}
