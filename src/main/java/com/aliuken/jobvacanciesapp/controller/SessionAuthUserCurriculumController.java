package com.aliuken.jobvacanciesapp.controller;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithoutPredefinedFilter;
import com.aliuken.jobvacanciesapp.enumtype.FileType;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserCurriculumDTO;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PredefinedFilterEntity;
import com.aliuken.jobvacanciesapp.service.AuthUserCurriculumService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.file.FileUtils;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerNavigationUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerServletUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class SessionAuthUserCurriculumController extends AbstractEntityControllerWithoutPredefinedFilter<AuthUserCurriculum> {

	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	@Autowired
	private AuthUserCurriculumService authUserCurriculumService;

	@Autowired
	private JobRequestService jobRequestService;

	private static String authUserCurriculumFilesPath;

	@PostConstruct
	private void postConstruct() {
		authUserCurriculumFilesPath = configPropertiesBean.getAuthUserCurriculumFilesPath();
	}

	/**
	 * Method to show the list of curriculums of the logged user with pagination
	 */
	@GetMapping("/my-user/auth-user-curriculums")
	public String getAuthUserCurriculums(HttpServletRequest httpServletRequest, Model model, Pageable pageable,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /my-user/auth-user-curriculums";

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);
		final Long sessionAuthUserId = sessionAuthUser.getId();
		final String sessionAuthUserEmail = sessionAuthUser.getEmail();

		try {
			if(tableSearchDTO == null || !tableSearchDTO.hasAllParameters()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDtoString = String.valueOf(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<AuthUserCurriculum> authUserCurriculums = Page.empty();
				model.addAttribute("predefinedFilterField1", sessionAuthUserId);
				model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
				model.addAttribute("authUserCurriculums", authUserCurriculums);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return ControllerNavigationUtils.getNextViewWithTable("authUser/sessionAuthUserCurriculums.html", model, operation, tableSearchDTO, false);
			}

			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				final Page<AuthUserCurriculum> authUserCurriculums = Page.empty();
				model.addAttribute("predefinedFilterField1", sessionAuthUserId);
				model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
				model.addAttribute("authUserCurriculums", authUserCurriculums);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				//model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);
				model.addAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextViewWithTable("authUser/sessionAuthUserCurriculums.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<AuthUserCurriculum> pageWithExceptionDTO = authUserCurriculumService.getAuthUserEntityPage(sessionAuthUserId, tableSearchDTO, pageable);
			final Page<AuthUserCurriculum> authUserCurriculums = pageWithExceptionDTO.getPage();
			final Throwable throwable = pageWithExceptionDTO.getThrowable();

			model.addAttribute("predefinedFilterField1", sessionAuthUserId);
			model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
			model.addAttribute("authUserCurriculums", authUserCurriculums);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl());

			if(throwable != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(throwable);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return ControllerNavigationUtils.getNextViewWithTable("authUser/sessionAuthUserCurriculums.html", model, operation, tableSearchDTO, true);

		} catch (final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<AuthUserCurriculum> authUserCurriculums = Page.empty();
			model.addAttribute("predefinedFilterField1", sessionAuthUserId);
			model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
			model.addAttribute("authUserCurriculums", authUserCurriculums);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextViewWithTable("authUser/sessionAuthUserCurriculums.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of user curriculums with pagination to pdf
	 */
	@GetMapping("/my-user/auth-user-curriculums/export-to-pdf")
	@ResponseBody
	public byte[] exportCurriculumsToPdf(Model model, Pageable pageable,
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

		this.getAuthUserCurriculums(httpServletRequest, model, pageable, tableSearchDTO, bindingResult);
		final byte[] pdfByteArray = this.storeAndDownloadPdf(predefinedFilterDTO, tableSearchDTO, model, PageEntityEnum.AUTH_USER_CURRICULUM, httpServletRequest, httpServletResponse);
		return pdfByteArray;
	}

	/**
	 * Method to show the detail of a curriculum
	 */
	@GetMapping("/my-user/auth-user-curriculums/view/{authUserCurriculumId}")
	public String view(Model model, @PathVariable("authUserCurriculumId") long authUserCurriculumId,
			@RequestParam(name = "languageParam", required = false) String languageCode) {
		final String operation = "GET /my-user/auth-user-curriculums/view/{authUserCurriculumId}";

		final AuthUserCurriculum authUserCurriculum = authUserCurriculumService.findByIdNotOptional(authUserCurriculumId);
		model.addAttribute("authUserCurriculum", authUserCurriculum);

		return ControllerNavigationUtils.getNextView("authUserCurriculum/authUserCurriculumDetail.html", model, operation, languageCode);
	}

	/**
	 * Method to show the creation form of a curriculum
	 */
	@GetMapping("/my-user/auth-user-curriculums/create")
	public String create(HttpServletRequest httpServletRequest, Model model, Authentication authentication,
			@RequestParam(name = "languageParam", required = false) String languageCode) {
		final String operation = "GET /my-user/auth-user-curriculums/create";

		final Map<String, ?> inputFlashMap = ControllerServletUtils.getInputFlashMap(httpServletRequest);

		AuthUserCurriculumDTO authUserCurriculumDTO;
		if(inputFlashMap != null) {
			authUserCurriculumDTO = (AuthUserCurriculumDTO) inputFlashMap.get("authUserCurriculumDTO");
			if(authUserCurriculumDTO == null) {
				authUserCurriculumDTO = AuthUserCurriculumDTO.getNewInstance();
			}
		} else {
			authUserCurriculumDTO = AuthUserCurriculumDTO.getNewInstance();
		}

		model.addAttribute("authUserCurriculumDTO", authUserCurriculumDTO);

		return ControllerNavigationUtils.getNextView("authUserCurriculum/sessionAuthUserCurriculumForm.html", model, operation, languageCode);
	}

	/**
	 * Method to save a curriculum in the database
	 */
	@PostMapping("/my-user/auth-user-curriculums/save")
	public String save(RedirectAttributes redirectAttributes, Authentication authentication,
			@Validated AuthUserCurriculumDTO authUserCurriculumDTO, BindingResult bindingResult,
			@RequestParam(name = "languageParam", required = false) String languageCode) {
		try {
			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				redirectAttributes.addFlashAttribute("authUserCurriculumDTO", authUserCurriculumDTO);
				//redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.authUserCurriculumDTO", bindingResult);
				redirectAttributes.addFlashAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextRedirect("/my-user/auth-user-curriculums/create", languageCode);
			}

			Long id = authUserCurriculumDTO.getId();
			final MultipartFile multipartFile = authUserCurriculumDTO.getCurriculumFile();
			final String description = authUserCurriculumDTO.getDescription();

			final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromAuthentication(authentication);

			final String sessionAuthUserIdString = Long.toString(sessionAuthUser.getId());
			final String pathWithAuthUserId = StringUtils.getStringJoined(authUserCurriculumFilesPath, sessionAuthUserIdString, "/");
			final List<String> savedFileNames = FileUtils.uploadAndOptionallyUnzipFile(multipartFile, pathWithAuthUserId, FileType.USER_CURRICULUM);

			List<AuthUserCurriculum> authUserCurriculumList;
			if(savedFileNames != null) {
				authUserCurriculumList = new ArrayList<>();
				if(savedFileNames.size() == 1) {
					AuthUserCurriculum authUserCurriculum = authUserCurriculumService.findByIdOrNewEntity(id);
					authUserCurriculum.setAuthUser(sessionAuthUser);
					authUserCurriculum.setFileName(savedFileNames.get(0));
					authUserCurriculum.setDescription(description);
					authUserCurriculum = authUserCurriculumService.saveAndFlush(authUserCurriculum);

					authUserCurriculumList.add(authUserCurriculum);
				} else {
					int i = 1;
					for(final String savedFileName : savedFileNames) {
						id = null;
						AuthUserCurriculum authUserCurriculum = authUserCurriculumService.findByIdOrNewEntity(id);
						authUserCurriculum.setAuthUser(sessionAuthUser);
						authUserCurriculum.setFileName(savedFileName);
						authUserCurriculum.setDescription(StringUtils.getStringJoined(description, " [", String.valueOf(i), "]"));
						authUserCurriculum = authUserCurriculumService.saveAndFlush(authUserCurriculum);

						authUserCurriculumList.add(authUserCurriculum);
						i++;
					}
				}
			} else {
				authUserCurriculumList = null;
			}

			final AuthUserCurriculum authUserCurriculum = (LogicalUtils.isNotNullNorEmpty(authUserCurriculumList)) ? authUserCurriculumList.get(0) : null;

			final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "saveUserCurriculum.successMsg", null);
			redirectAttributes.addFlashAttribute("successMsg", successMsg);

			return ControllerNavigationUtils.getNextRedirect("/my-user/auth-user-curriculums/view/" + authUserCurriculum.getId(), languageCode);

		} catch (final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("authUserCurriculumDTO", authUserCurriculumDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextRedirect("/my-user/auth-user-curriculums/create", languageCode);
		}
	}

	/**
	 * Method to delete a curriculum
	 */
	@GetMapping("/my-user/auth-user-curriculums/delete/{authUserCurriculumId}")
	public String delete(RedirectAttributes redirectAttributes, Authentication authentication,
			@PathVariable("authUserCurriculumId") long authUserCurriculumId,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) String pageSize,
			@RequestParam(name="pageNumber", required=false) String pageNumber) {

		final AuthUserCurriculum authUserCurriculum = authUserCurriculumService.findByIdNotOptional(authUserCurriculumId);
		if(authUserCurriculum == null) {
			final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteUserCurriculum.curriculumDoesNotExist", null);
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return ControllerNavigationUtils.getNextRedirectWithTable("/my-user/auth-user-curriculums", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		}

		final String sessionAuthUserEmail = authentication.getName();
		if(sessionAuthUserEmail == null) {
			final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteUserCurriculum.curriculumDoesNotBelongToUser", null);
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return ControllerNavigationUtils.getNextRedirectWithTable("/my-user/auth-user-curriculums", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		}

		final AuthUser authUser = authUserCurriculum.getAuthUser();
		if(authUser == null || !sessionAuthUserEmail.equals(authUser.getEmail())) {
			final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteUserCurriculum.curriculumDoesNotBelongToUser", null);
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return ControllerNavigationUtils.getNextRedirectWithTable("/my-user/auth-user-curriculums", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		}

		final String curriculumFileName = authUserCurriculum.getFileName();
		final List<JobRequest> jobRequests = jobRequestService.findByAuthUserAndCurriculumFileName(authUser, curriculumFileName);
		if(jobRequests != null) {
			for(final JobRequest jobRequest : jobRequests) {
				jobRequestService.deleteByIdAndFlush(jobRequest.getId());
			}
		}

		authUserCurriculumService.deleteByIdAndFlush(authUserCurriculumId);

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromAuthentication(authentication);
		final String sessionAuthUserIdString = Long.toString(sessionAuthUser.getId());
		final Path finalFilePath = Path.of(authUserCurriculumFilesPath, sessionAuthUserIdString, curriculumFileName);
		FileUtils.deletePathRecursively(finalFilePath);

		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteUserCurriculum.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirectWithTable("/my-user/auth-user-curriculums", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	@Override
	public String getPaginationUrl() {
		return "/my-user/auth-user-curriculums";
	}
}
