package com.aliuken.jobvacanciesapp.controller;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithoutPredefinedFilter;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserSignUpConfirmation;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.service.AuthUserCredentialsService;
import com.aliuken.jobvacanciesapp.service.AuthUserCurriculumService;
import com.aliuken.jobvacanciesapp.service.AuthUserRoleService;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.AuthUserSignUpConfirmationService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.file.FileUtils;
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

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

@Controller
@Slf4j
public class AuthUserController extends AbstractEntityControllerWithoutPredefinedFilter<AuthUser> {

	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private AuthUserSignUpConfirmationService authUserSignUpConfirmationService;

	@Autowired
	private AuthUserCredentialsService authUserCredentialsService;

	@Autowired
	private AuthUserRoleService authUserRoleService;

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
	 * Method to show the list of users with pagination
	 */
	@GetMapping("/auth-users/index")
	public String index(Model model, Pageable pageable,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /auth-users/index";

		try {
			if(tableSearchDTO == null || !tableSearchDTO.hasAllParameters()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDtoString = Objects.toString(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<AuthUser> authUsers = Page.empty();
				model.addAttribute("authUsers", authUsers);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return ControllerNavigationUtils.getNextViewWithTable("authUser/listAuthUsers.html", model, operation, tableSearchDTO, false);
			}

			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				final Page<AuthUser> authUsers = Page.empty();
				model.addAttribute("authUsers", authUsers);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				//model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);
				model.addAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextViewWithTable("authUser/listAuthUsers.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<AuthUser> pageWithExceptionDTO = authUserService.getEntityPage(tableSearchDTO, pageable);
			final Page<AuthUser> authUsers = pageWithExceptionDTO.getPage();
			final Throwable throwable = pageWithExceptionDTO.getThrowable();

			model.addAttribute("authUsers", authUsers);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl());

			if(throwable != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(throwable);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return ControllerNavigationUtils.getNextViewWithTable("authUser/listAuthUsers.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<AuthUser> authUsers = Page.empty();
			model.addAttribute("authUsers", authUsers);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextViewWithTable("authUser/listAuthUsers.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of users with pagination to pdf
	 */
	@GetMapping("/auth-users/index/export-to-pdf")
	@ResponseBody
	public byte[] exportToPdf(Model model, Pageable pageable,
			HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) Integer pageSize,
			@RequestParam(name="pageNumber", required=false) Integer pageNumber) {

		final PredefinedFilterDTO predefinedFilterDTO = null;
		final TableSearchDTO tableSearchDTO = new TableSearchDTO(languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		final BindingResult bindingResult = null;

		this.index(model, pageable, tableSearchDTO, bindingResult);
		final byte[] pdfByteArray = this.storeAndDownloadPdf(predefinedFilterDTO, tableSearchDTO, model, PageEntityEnum.AUTH_USER, httpServletRequest, httpServletResponse);
		return pdfByteArray;
	}

	/**
	 * Method to show the detail of a user
	 */
	@GetMapping("/auth-users/view/{authUserId}")
	public String view(Model model, @PathVariable("authUserId") long authUserId,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /auth-users/view/{authUserId}";

		final AuthUser authUser = authUserService.findByIdNotOptional(authUserId);
		model.addAttribute("authUser", authUser);

		return ControllerNavigationUtils.getNextView("authUser/authUserDetail.html", model, operation, languageCode);
	}

	/**
	 * Method to delete a user
	 */
	@GetMapping("/auth-users/delete/{authUserId}")
	public String deleteById(RedirectAttributes redirectAttributes, @PathVariable("authUserId") long authUserId,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) String pageSize,
			@RequestParam(name="pageNumber", required=false) String pageNumber) {

		this.deleteById(authUserId);

		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteAuthUser.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirectWithTable("/auth-users/index", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	public void deleteById(long authUserId) {
		final AuthUser authUser = authUserService.findByIdNotOptional(authUserId);

		final AuthUserSignUpConfirmation authUserSignUpConfirmation = authUserSignUpConfirmationService.findByEmail(authUser.getEmail());
		if(authUserSignUpConfirmation != null) {
			authUserSignUpConfirmationService.deleteByIdAndFlush(authUserSignUpConfirmation.getId());
		}

		final AuthUserCredentials authUserCredentials = authUserCredentialsService.findByEmail(authUser.getEmail());
		if(authUserCredentials != null) {
			authUserCredentialsService.deleteByIdAndFlush(authUserCredentials.getId());
		}

		final Set<Long> jobRequestIds = authUser.getJobRequestIds();
		for(final Long jobRequestId : jobRequestIds) {
			jobRequestService.deleteByIdAndFlush(jobRequestId);
		}

		final Set<Long> authUserCurriculumIds = authUser.getAuthUserCurriculumIds();
		for(final Long authUserCurriculumId : authUserCurriculumIds) {
			authUserCurriculumService.deleteByIdAndFlush(authUserCurriculumId);
		}

		final Set<Long> authUserRoleIds = authUser.getAuthUserRoleIds();
		for(final Long authUserRoleId : authUserRoleIds) {
			authUserRoleService.deleteByIdAndFlush(authUserRoleId);
		}

		authUserService.deleteByIdAndFlush(authUserId);

		final String authUserIdString = Long.toString(authUserId);
		final Path finalFilePath = Path.of(authUserCurriculumFilesPath, authUserIdString);
		FileUtils.deletePathRecursively(finalFilePath);
	}

	/**
	 * Method to lock a user
	 */
	@GetMapping("/auth-users/lock/{authUserId}")
	public String lock(RedirectAttributes redirectAttributes, @PathVariable("authUserId") long authUserId,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) String pageSize,
			@RequestParam(name="pageNumber", required=false) String pageNumber) {

		authUserService.lock(authUserId);
		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "lockAuthUser.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirectWithTable("/auth-users/index", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	/**
	 * Method to unlock a user
	 */
	@GetMapping("/auth-users/unlock/{authUserId}")
	public String unlock(RedirectAttributes redirectAttributes, @PathVariable("authUserId") long authUserId,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) String pageSize,
			@RequestParam(name="pageNumber", required=false) String pageNumber) {

		authUserService.unlock(authUserId);
		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "unlockAuthUser.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirectWithTable("/auth-users/index", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	@Override
	public String getPaginationUrl() {
		return "/auth-users/index";
	}
}
