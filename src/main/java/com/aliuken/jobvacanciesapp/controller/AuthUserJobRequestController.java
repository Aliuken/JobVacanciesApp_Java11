package com.aliuken.jobvacanciesapp.controller;

import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithPredefinedFilter;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PredefinedFilterEntity;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
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
public class AuthUserJobRequestController extends AbstractEntityControllerWithPredefinedFilter<JobRequest> {

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private JobRequestService jobRequestService;

	/**
	 * Method to show the list of request of a user with pagination
	 */
	@GetMapping("/auth-users/job-requests/{authUserId}")
	public String getJobRequests(final @NonNull Model model, final @NonNull Pageable pageable, @PathVariable("authUserId") long authUserId,
								 @Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /auth-users/job-requests/{authUserId}";

		final AuthUser authUser = authUserService.findByIdNotOptional(authUserId);
		final String authUserEmail = (authUser != null) ? authUser.getEmail() : null;

		try {
			if(!this.hasExportToPdfEnabled(tableSearchDTO)) {
				if(log.isDebugEnabled()) {
					final String tableSearchDtoString = String.valueOf(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("predefinedFilterField1", authUserId);
				model.addAttribute("predefinedFilterField2", authUserEmail);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", this.getPaginationUrl(authUserId));
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return ControllerNavigationUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, false);
			}

			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("predefinedFilterField1", authUserId);
				model.addAttribute("predefinedFilterField2", authUserEmail);
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", this.getPaginationUrl(authUserId));
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				//model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);
				model.addAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobRequest> pageWithExceptionDTO = jobRequestService.getAuthUserEntityPage(authUserId, tableSearchDTO, pageable);
			final Page<JobRequest> jobRequests = pageWithExceptionDTO.getPage();
			final Throwable throwable = pageWithExceptionDTO.getThrowable();

			model.addAttribute("predefinedFilterField1", authUserId);
			model.addAttribute("predefinedFilterField2", authUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", this.getPaginationUrl(authUserId));
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl(authUserId));

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
			model.addAttribute("predefinedFilterField1", authUserId);
			model.addAttribute("predefinedFilterField2", authUserEmail);
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", this.getPaginationUrl(authUserId));
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextViewWithTable("authUser/authUserJobRequests.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of user job requests with pagination to pdf
	 */
	@GetMapping("/auth-users/job-requests/{authUserId}/exportToPdf")
	@ResponseBody
	public byte[] exportToPdf(final @NonNull Model model, final @NonNull Pageable pageable, @PathVariable("authUserId") long authUserId,
			final @NonNull HttpServletRequest httpServletRequest, final @NonNull HttpServletResponse httpServletResponse,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) Integer pageSize,
			@RequestParam(name="pageNumber", required=false) Integer pageNumber) {

		final String predefinedFilterEntityName = PredefinedFilterEntity.AUTH_USER.getUpperCasedEntityName();
		final String authUserIdString = String.valueOf(authUserId);

		final PredefinedFilterDTO predefinedFilterDTO = new PredefinedFilterDTO(predefinedFilterEntityName, authUserIdString);
		final TableSearchDTO tableSearchDTO = new TableSearchDTO(httpServletRequest, languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		final BindingResult bindingResult = null;

		this.getJobRequests(model, pageable, authUserId, tableSearchDTO, bindingResult);
		final byte[] pdfByteArray = this.storeAndDownloadPdf(predefinedFilterDTO, tableSearchDTO, model, PageEntityEnum.JOB_REQUEST, httpServletRequest, httpServletResponse);
		return pdfByteArray;
	}

	/**
	 * Method to delete a job request of a user
	 */
	@GetMapping("/auth-users/job-requests/delete/{authUserId}/{jobRequestId}")
	public String delete(final @NonNull RedirectAttributes redirectAttributes, @PathVariable("authUserId") long authUserId, @PathVariable("jobRequestId") long jobRequestId,
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

		return ControllerNavigationUtils.getNextRedirectWithTable("/auth-users/job-requests/" + authUserId, languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	@Override
	public String getPaginationUrl(final String predefinedFilterIdString) {
		return StringUtils.getStringJoined("/auth-users/job-requests/", predefinedFilterIdString);
	}
}
