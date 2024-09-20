package com.aliuken.jobvacanciesapp.controller;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithoutPredefinedFilter;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;
import com.aliuken.jobvacanciesapp.util.web.MvcUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SessionAuthUserJobRequestController extends AbstractEntityControllerWithoutPredefinedFilter<JobRequest> {

	@Autowired
	private JobRequestService jobRequestService;

	/**
	 * Method to show the list of job requests of the logged user with pagination
	 */
	@GetMapping("/my-user/job-requests")
	public String getJobRequests(HttpServletRequest httpServletRequest, Model model, Pageable pageable,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /my-user/job-requests";

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);
		final Long sessionAuthUserId = sessionAuthUser.getId();
		final String sessionAuthUserEmail = sessionAuthUser.getEmail();

		try {
			if(tableSearchDTO == null || !tableSearchDTO.hasAllParameters()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDtoString = Objects.toString(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("authUserId", sessionAuthUserId);
				model.addAttribute("authUserEmail", sessionAuthUserEmail);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return MvcUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, false);
			}

			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("authUserId", sessionAuthUserId);
				model.addAttribute("authUserEmail", sessionAuthUserEmail);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);

				return MvcUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobRequest> pageWithExceptionDTO = jobRequestService.getAuthUserEntityPage(sessionAuthUserId, tableSearchDTO, pageable);
			final Page<JobRequest> jobRequests = pageWithExceptionDTO.getPage();
			final Exception exception = pageWithExceptionDTO.getException();

			model.addAttribute("authUserId", sessionAuthUserId);
			model.addAttribute("authUserEmail", sessionAuthUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl());

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
			model.addAttribute("authUserId", sessionAuthUserId);
			model.addAttribute("authUserEmail", sessionAuthUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of user job requests with pagination to pdf
	 */
	@GetMapping("/my-user/job-requests/export-to-pdf")
	@ResponseBody
	public byte[] exportJobRequestsToPdf(Model model, Pageable pageable,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="tableFieldCode", required=false) String tableFieldCode,
			@RequestParam(name="tableFieldValue", required=false) String tableFieldValue,
			@RequestParam(name="tableOrderCode", required=false) String tableOrderCode,
			@RequestParam(name="pageSize", required=false) Integer pageSize,
			@RequestParam(name="pageNumber", required=false) Integer pageNumber) {

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);
		final Long sessionAuthUserId = sessionAuthUser.getId();
		final String sessionAuthUserIdString = String.valueOf(sessionAuthUserId);

		tableSearchDTO = new TableSearchDTO(languageCode, Constants.AUTH_USER_PREDEFINED_FILTER_NAME, sessionAuthUserIdString, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);

		this.getJobRequests(httpServletRequest, model, pageable, tableSearchDTO, bindingResult);
		final byte[] pdfByteArray = this.storeAndDownloadPdf(tableSearchDTO, model, PageEntityEnum.JOB_REQUEST, httpServletRequest, httpServletResponse);
		return pdfByteArray;
	}

	@Override
	public String getPaginationUrl() {
		return "/my-user/job-requests";
	}
}
