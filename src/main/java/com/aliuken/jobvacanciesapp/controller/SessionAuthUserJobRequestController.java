package com.aliuken.jobvacanciesapp.controller;

import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithoutPredefinedFilter;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PredefinedFilterEntity;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
					final String tableSearchDtoString = String.valueOf(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("predefinedFilterField1", sessionAuthUserId);
				model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return ControllerNavigationUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, false);
			}

			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("predefinedFilterField1", sessionAuthUserId);
				model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				//model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);
				model.addAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobRequest> pageWithExceptionDTO = jobRequestService.getAuthUserEntityPage(sessionAuthUserId, tableSearchDTO, pageable);
			final Page<JobRequest> jobRequests = pageWithExceptionDTO.getPage();
			final Throwable throwable = pageWithExceptionDTO.getThrowable();

			model.addAttribute("predefinedFilterField1", sessionAuthUserId);
			model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl());

			if(throwable != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(throwable);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return ControllerNavigationUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<JobRequest> jobRequests = Page.empty();
			model.addAttribute("predefinedFilterField1", sessionAuthUserId);
			model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of user job requests with pagination to pdf
	 */
	@GetMapping("/my-user/job-requests/export-to-pdf")
	@ResponseBody
	public byte[] exportJobRequestsToPdf(Model model, Pageable pageable,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) Integer pageSize,
			@RequestParam(name="pageNumber", required=false) Integer pageNumber) {

		final String predefinedFilterEntityName = PredefinedFilterEntity.AUTH_USER.getUpperCasedEntityName();
		final String sessionAuthUserIdString = SessionUtils.getSessionAuthUserIdStringFromHttpServletRequest(httpServletRequest);

		final PredefinedFilterDTO predefinedFilterDTO = new PredefinedFilterDTO(predefinedFilterEntityName, sessionAuthUserIdString);
		final TableSearchDTO tableSearchDTO = new TableSearchDTO(languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		final BindingResult bindingResult = null;

		this.getJobRequests(httpServletRequest, model, pageable, tableSearchDTO, bindingResult);
		final byte[] pdfByteArray = this.storeAndDownloadPdf(predefinedFilterDTO, tableSearchDTO, model, PageEntityEnum.JOB_REQUEST, httpServletRequest, httpServletResponse);
		return pdfByteArray;
	}

	@Override
	public String getPaginationUrl() {
		return "/my-user/job-requests";
	}
}
