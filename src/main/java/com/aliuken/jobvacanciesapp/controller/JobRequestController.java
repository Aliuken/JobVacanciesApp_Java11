package com.aliuken.jobvacanciesapp.controller;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithoutPredefinedFilter;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobRequestDTO;
import com.aliuken.jobvacanciesapp.model.dto.JobVacancyDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.dto.converter.JobVacancyConverter;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.JobVacancy;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.service.JobVacancyService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.web.MvcUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class JobRequestController extends AbstractEntityControllerWithoutPredefinedFilter<JobRequest> {

	@Autowired
	private JobRequestService jobRequestService;

	@Autowired
	private JobVacancyService jobVacancyService;

	@Autowired
	private AuthUserService authUserService;

	/**
	 * Method to show the list of job requests with pagination
	 */
	@GetMapping("/job-requests/index")
	public String index(Model model, Pageable pageable,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /job-requests/index";

		try {
			if(tableSearchDTO == null || !tableSearchDTO.hasAllParameters()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDtoString = Objects.toString(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return MvcUtils.getNextViewWithTable("jobRequest/listJobRequests.html", model, operation, tableSearchDTO, false);
			}

			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				final Page<JobRequest> jobRequests = Page.empty();
				model.addAttribute("jobRequests", jobRequests);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);

				return MvcUtils.getNextViewWithTable("jobRequest/listJobRequests.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<JobRequest> pageWithExceptionDTO = jobRequestService.getEntityPage(tableSearchDTO, pageable);
			final Page<JobRequest> jobRequests = pageWithExceptionDTO.getPage();
			final Exception exception = pageWithExceptionDTO.getException();

			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl());

			if(exception != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return MvcUtils.getNextViewWithTable("jobRequest/listJobRequests.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<JobRequest> jobRequests = Page.empty();
			model.addAttribute("jobRequests", jobRequests);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextViewWithTable("jobRequest/listJobRequests.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of job requests with pagination to pdf
	 */
	@GetMapping("/job-requests/index/export-to-pdf")
	@ResponseBody
	public byte[] exportToPdf(Model model, Pageable pageable,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="tableFieldCode", required=false) String tableFieldCode,
			@RequestParam(name="tableFieldValue", required=false) String tableFieldValue,
			@RequestParam(name="tableOrderCode", required=false) String tableOrderCode,
			@RequestParam(name="pageSize", required=false) Integer pageSize,
			@RequestParam(name="pageNumber", required=false) Integer pageNumber) {

		tableSearchDTO = new TableSearchDTO(languageCode, null, null, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);

		this.index(model, pageable, tableSearchDTO, bindingResult);
		final byte[] pdfByteArray = this.storeAndDownloadPdf(tableSearchDTO, model, PageEntityEnum.JOB_REQUEST, httpServletRequest, httpServletResponse);
		return pdfByteArray;
	}

	/**
	 * Method to show the detail of a job request
	 */
	@GetMapping("/job-requests/view/{jobRequestId}")
	public String view(Model model, @PathVariable("jobRequestId") long jobRequestId,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /job-requests/view/{jobRequestId}";

		final JobRequest jobRequest = jobRequestService.findByIdNotOptional(jobRequestId);
		model.addAttribute("jobRequest", jobRequest);

		return MvcUtils.getNextView("jobRequest/jobRequestDetail.html", model, operation, languageCode);
	}

	/**
	 * Method to show the creation form of a job request
	 */
	@GetMapping("/job-requests/create/{jobVacancyId}")
	public String create(HttpServletRequest httpServletRequest, Model model, @PathVariable long jobVacancyId,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /job-requests/create/{jobVacancyId}";

		final JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);

		final JobVacancyDTO jobVacancyDTO = JobVacancyConverter.getInstance().convertEntityElement(jobVacancy);

		final Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(httpServletRequest);

		JobRequestDTO jobRequestDTO;
		if(inputFlashMap != null) {
			jobRequestDTO = (JobRequestDTO) inputFlashMap.get("jobRequestDTO");
			if(jobRequestDTO == null) {
				jobRequestDTO = JobRequestDTO.getNewInstance();
			}
		} else {
			jobRequestDTO = JobRequestDTO.getNewInstance();
		}

		jobRequestDTO = JobRequestDTO.getNewInstance(jobRequestDTO, jobVacancyDTO);

		model.addAttribute("jobRequestDTO", jobRequestDTO);

		return MvcUtils.getNextView("jobRequest/jobRequestForm.html", model, operation, languageCode);
	}

	/**
	 * Method to save a job request in the database
	 */
	@PostMapping("/job-requests/save")
	public String save(RedirectAttributes redirectAttributes, Authentication authentication,
			@Validated JobRequestDTO jobRequestDTO, BindingResult bindingResult,
			@RequestParam(name="jobVacancyId", required=false) Long jobVacancyId, @RequestParam(name="languageParam", required=false) String languageCode) {
		try {
			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				redirectAttributes.addFlashAttribute("jobRequestDTO", jobRequestDTO);
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.jobRequestDTO", bindingResult);

				return MvcUtils.getNextRedirect("/job-requests/create/" + jobVacancyId, languageCode);
			}

			final Long id = jobRequestDTO.getId();
			final String comments = jobRequestDTO.getComments();
			final String curriculumFileName = jobRequestDTO.getCurriculumFileName();

			JobRequest jobRequest = jobRequestService.findByIdOrNewEntity(id);

			final String email = authentication.getName();
			final AuthUser authUser = authUserService.findByEmail(email);
			jobRequest.setAuthUser(authUser);

			final JobVacancy jobVacancy = jobVacancyService.findByIdNotOptional(jobVacancyId);
			jobRequest.setJobVacancy(jobVacancy);

			jobRequest.setComments(comments);
			jobRequest.setCurriculumFileName(curriculumFileName);

			jobRequest = jobRequestService.saveAndFlush(jobRequest);

			final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "saveJobRequest.successMsg", null);
			redirectAttributes.addFlashAttribute("successMsg", successMsg);

			return MvcUtils.getNextRedirect("/job-requests/view/" + jobRequest.getId(), languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("jobRequestDTO", jobRequestDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextRedirect("/job-requests/create/" + jobVacancyId, languageCode);
		}
	}

	/**
	 * Method to delete a job request
	 */
	@GetMapping("/job-requests/delete/{jobRequestId}")
	public String delete(RedirectAttributes redirectAttributes, @PathVariable("jobRequestId") long jobRequestId,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="tableFieldCode", required=false) String tableFieldCode,
			@RequestParam(name="tableFieldValue", required=false) String tableFieldValue,
			@RequestParam(name="tableOrderCode", required=false) String tableOrderCode,
			@RequestParam(name="pageSize", required=false) String pageSize,
			@RequestParam(name="pageNumber", required=false) String pageNumber) {

		jobRequestService.deleteByIdAndFlush(jobRequestId);

		final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "deleteJobRequest.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return MvcUtils.getNextRedirectWithTable("/job-requests/index", languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
	}

	/**
	 * Metodo que agrega al modelo datos genéricos para todo el controlador
	 */
	@ModelAttribute
	public void setGenerics(Model model, Authentication authentication) {
		final Set<AuthUserCurriculum> authUserCurriculums;
		if(authentication != null) {
			final String email = authentication.getName();
			final AuthUser authUser = authUserService.findByEmail(email);
			authUserCurriculums = authUser.getAuthUserCurriculums();
		} else {
			authUserCurriculums = null;
		}

		model.addAttribute("authUserCurriculums", authUserCurriculums);
	}

	@Override
	public String getPaginationUrl() {
		return "/job-requests/index";
	}
}
