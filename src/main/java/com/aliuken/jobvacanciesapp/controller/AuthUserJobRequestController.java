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
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.web.MvcUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthUserJobRequestController {

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private JobRequestService jobRequestService;

	/**
	 * Method to show the list of request of a user with pagination
	 */
	@GetMapping("/auth-users/job-requests/{authUserId}")
	public String getJobRequests(Model model, Pageable pageable, @PathVariable("authUserId") long authUserId,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /auth-users/job-requests/{authUserId}";

		final AuthUser authUser = authUserService.findByIdNotOptional(authUserId);
		final String authUserEmail = (authUser != null) ? authUser.getEmail() : null;

		try {
			if(tableSearchDTO == null || tableSearchDTO.hasEmptyAttribute()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDTOString = Objects.toString(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDTOString));
				}

				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("authUserId", authUserId);
				model.addAttribute("authUserEmail", authUserEmail);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", "/auth-users/job-requests/" + authUserId);

				return MvcUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, false);
			}

			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("authUserId", authUserId);
				model.addAttribute("authUserEmail", authUserEmail);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", "/auth-users/job-requests/" + authUserId);
				model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);

				return MvcUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobRequest> pageWithExceptionDTO = jobRequestService.getAuthUserEntityPage(authUserId, tableSearchDTO, pageable);
			final Page<JobRequest> jobRequests = pageWithExceptionDTO.getPage();
			final Exception exception = pageWithExceptionDTO.getException();

			model.addAttribute("authUserId", authUserId);
			model.addAttribute("authUserEmail", authUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", "/auth-users/job-requests/" + authUserId);

			if(exception != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return MvcUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<JobRequest> jobRequests = Page.empty();
			model.addAttribute("authUserId", authUserId);
			model.addAttribute("authUserEmail", authUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", "/auth-users/job-requests/" + authUserId);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to delete a job request of a user
	 */
	@GetMapping("/auth-users/job-requests/delete/{authUserId}/{jobRequestId}")
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("authUserId") long authUserId, @PathVariable("jobRequestId") long jobRequestId,
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="tableFieldCode", required=false) String tableFieldCode, @RequestParam(name="tableFieldValue", required=false) String tableFieldValue, @RequestParam(name="tableOrderCode", required=false) String tableOrderCode, @RequestParam(name="pageSize", required=false) String pageSize, @RequestParam(name="pageNumber", required=false) String pageNumber) {
		jobRequestService.deleteByIdAndFlush(jobRequestId);

		final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "deleteJobRequest.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return MvcUtils.getNextRedirectWithTable("/auth-users/job-requests/" + authUserId, languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
	}

}
