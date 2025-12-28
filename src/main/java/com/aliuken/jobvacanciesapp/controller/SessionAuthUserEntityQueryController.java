package com.aliuken.jobvacanciesapp.controller;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.controller.superclass.AbstractEntityControllerWithoutPredefinedFilter;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserEntityQuery;
import com.aliuken.jobvacanciesapp.service.AuthUserEntityQueryService;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.file.FileUtils;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;
import com.aliuken.jobvacanciesapp.util.spring.mvc.ControllerNavigationUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;

@Controller
@Slf4j
public class SessionAuthUserEntityQueryController extends AbstractEntityControllerWithoutPredefinedFilter<AuthUserEntityQuery> {

	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	@Autowired
	private AuthUserEntityQueryService authUserEntityQueryService;

	private static String authUserEntityQueryFilesPath;

	@PostConstruct
	private void postConstruct() {
		authUserEntityQueryFilesPath = configPropertiesBean.getAuthUserEntityQueryFilesPath();
	}

	/**
	 * Method to show the list of entity queries of the logged user with pagination
	 */
	@GetMapping("/my-user/auth-user-entity-queries")
	public String getAuthUserEntityQueries(HttpServletRequest httpServletRequest, Model model, Pageable pageable,
			@Validated TableSearchDTO tableSearchDTO, BindingResult bindingResult) {
		final String operation = "GET /my-user/auth-user-entity-queries";

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromHttpServletRequest(httpServletRequest);
		final Long sessionAuthUserId = sessionAuthUser.getId();
		final String sessionAuthUserEmail = sessionAuthUser.getEmail();

		try {
			if(tableSearchDTO == null || !tableSearchDTO.hasAllParameters()) {
				if(log.isDebugEnabled()) {
					final String tableSearchDtoString = String.valueOf(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<AuthUserEntityQuery> authUserEntityQueries = Page.empty();
				model.addAttribute("predefinedFilterField1", sessionAuthUserId);
				model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
				model.addAttribute("authUserEntityQueries", authUserEntityQueries);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return ControllerNavigationUtils.getNextViewWithTable("authUser/sessionAuthUserEntityQueries.html", model, operation, tableSearchDTO, false);
			}

			final String firstBindingErrorString = ControllerValidationUtils.getFirstBindingErrorString(bindingResult);
			if(firstBindingErrorString != null) {
				final Page<AuthUserEntityQuery> authUserEntityQueries = Page.empty();
				model.addAttribute("predefinedFilterField1", sessionAuthUserId);
				model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
				model.addAttribute("authUserEntityQueries", authUserEntityQueries);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				//model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);
				model.addAttribute("errorMsg", firstBindingErrorString);

				return ControllerNavigationUtils.getNextViewWithTable("authUser/sessionAuthUserEntityQueries.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<AuthUserEntityQuery> pageWithExceptionDTO = authUserEntityQueryService.getAuthUserEntityPage(sessionAuthUserId, tableSearchDTO, pageable);
			final Page<AuthUserEntityQuery> authUserEntityQueries = pageWithExceptionDTO.getPage();
			final Throwable throwable = pageWithExceptionDTO.getThrowable();

			model.addAttribute("predefinedFilterField1", sessionAuthUserId);
			model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
			model.addAttribute("authUserEntityQueries", authUserEntityQueries);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl());

			if(throwable != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(throwable);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return ControllerNavigationUtils.getNextViewWithTable("authUser/sessionAuthUserEntityQueries.html", model, operation,
					tableSearchDTO, true);

		} catch (final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<AuthUserEntityQuery> authUserEntityQueries = Page.empty();
			model.addAttribute("predefinedFilterField1", sessionAuthUserId);
			model.addAttribute("predefinedFilterField2", sessionAuthUserEmail);
			model.addAttribute("authUserEntityQueries", authUserEntityQueries);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return ControllerNavigationUtils.getNextViewWithTable("authUser/sessionAuthUserEntityQueries.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of user entity queries with pagination to pdf
	 */
	@GetMapping("/my-user/auth-user-entity-queries/export-to-pdf")
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

		final byte[] pdfByteArray = new byte[0];
		return pdfByteArray;
	}

	/**
	 * Method to show the detail of a entity query
	 */
	@GetMapping("/my-user/auth-user-entity-queries/view/{authUserEntityQueryId}")
	public String view(Model model, @PathVariable("authUserEntityQueryId") long authUserEntityQueryId,
			@RequestParam(name = "languageParam", required = false) String languageCode) {
		final String operation = "GET /my-user/auth-user-entity-queries/view/{authUserEntityQueryId}";

		final AuthUserEntityQuery authUserEntityQuery = authUserEntityQueryService.findByIdNotOptional(authUserEntityQueryId);
		model.addAttribute("authUserEntityQuery", authUserEntityQuery);

		return ControllerNavigationUtils.getNextView("authUserEntityQuery/authUserEntityQueryDetail.html", model, operation, languageCode);
	}

	/**
	 * Method to delete an entity query
	 */
	@GetMapping("/my-user/auth-user-entity-queries/delete/{authUserEntityQueryId}")
	public String delete(RedirectAttributes redirectAttributes, Authentication authentication,
			@PathVariable("authUserEntityQueryId") long authUserEntityQueryId,
			@RequestParam(name="languageParam", required=false) String languageCode,
			@RequestParam(name="filterName", required=false) String filterName,
			@RequestParam(name="filterValue", required=false) String filterValue,
			@RequestParam(name="sortingField", required=false) String sortingField,
			@RequestParam(name="sortingDirection", required=false) String sortingDirection,
			@RequestParam(name="pageSize", required=false) String pageSize,
			@RequestParam(name="pageNumber", required=false) String pageNumber) {

		final AuthUserEntityQuery authUserEntityQuery = authUserEntityQueryService.findByIdNotOptional(authUserEntityQueryId);
		if(authUserEntityQuery == null) {
			final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteUserEntityQuery.entityQueryDoesNotExist", null);
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return ControllerNavigationUtils.getNextRedirectWithTable("/my-user/auth-user-entity-queries", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		}

		final String sessionAuthUserEmail = authentication.getName();
		if(sessionAuthUserEmail == null) {
			final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteUserEntityQuery.entityQueryDoesNotBelongToUser", null);
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return ControllerNavigationUtils.getNextRedirectWithTable("/my-user/auth-user-entity-queries", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		}

		final AuthUser authUser = authUserEntityQuery.getAuthUser();
		if(authUser == null || !sessionAuthUserEmail.equals(authUser.getEmail())) {
			final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteUserEntityQuery.entityQueryDoesNotBelongToUser", null);
			redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

			return ControllerNavigationUtils.getNextRedirectWithTable("/my-user/auth-user-entity-queries", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
		}

		final String entityQueryFileName = authUserEntityQuery.getFinalResultFileName();

		authUserEntityQueryService.deleteByIdAndFlush(authUserEntityQueryId);

		final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromAuthentication(authentication);
		final String sessionAuthUserIdString = Long.toString(sessionAuthUser.getId());
		final Path finalFilePath = Path.of(authUserEntityQueryFilesPath, sessionAuthUserIdString, entityQueryFileName);
		FileUtils.deletePathRecursively(finalFilePath);

		final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteUserEntityQuery.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return ControllerNavigationUtils.getNextRedirectWithTable("/my-user/auth-user-entity-queries", languageCode, filterName, filterValue, sortingField, sortingDirection, pageSize, pageNumber);
	}

	@Override
	public String getPaginationUrl() {
		return "/my-user/auth-user-entity-queries";
	}

	@Override
	public String getExportToPdfUrl() {
		return EXPORT_TO_PDF_DISABLED_VALUE;
	}
}
