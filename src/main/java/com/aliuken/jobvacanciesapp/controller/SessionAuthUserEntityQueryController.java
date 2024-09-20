package com.aliuken.jobvacanciesapp.controller;

import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.PostConstruct;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
import com.aliuken.jobvacanciesapp.util.web.MvcUtils;

import lombok.extern.slf4j.Slf4j;

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
					final String tableSearchDtoString = Objects.toString(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDtoString));
				}

				final Page<AuthUserEntityQuery> authUserEntityQueries = Page.empty();
				model.addAttribute("authUserId", sessionAuthUserId);
				model.addAttribute("authUserEmail", sessionAuthUserEmail);
				model.addAttribute("authUserEntityQueries", authUserEntityQueries);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

				return MvcUtils.getNextViewWithTable("authUser/sessionAuthUserEntityQueries.html", model, operation, tableSearchDTO, false);
			}

			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				final Page<AuthUserEntityQuery> authUserEntityQueries = Page.empty();
				model.addAttribute("authUserId", sessionAuthUserId);
				model.addAttribute("authUserEmail", sessionAuthUserEmail);
				model.addAttribute("authUserEntityQueries", authUserEntityQueries);
				model.addAttribute("paginationUrl", this.getPaginationUrl());
				model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);
				model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);

				return MvcUtils.getNextViewWithTable("authUser/sessionAuthUserEntityQueries.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<AuthUserEntityQuery> pageWithExceptionDTO = authUserEntityQueryService.getAuthUserEntityPage(sessionAuthUserId, tableSearchDTO, pageable);
			final Page<AuthUserEntityQuery> authUserEntityQueries = pageWithExceptionDTO.getPage();
			final Exception exception = pageWithExceptionDTO.getException();

			model.addAttribute("authUserId", sessionAuthUserId);
			model.addAttribute("authUserEmail", sessionAuthUserEmail);
			model.addAttribute("authUserEntityQueries", authUserEntityQueries);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", this.getExportToPdfUrl());

			if(exception != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return MvcUtils.getNextViewWithTable("authUser/sessionAuthUserEntityQueries.html", model, operation,
					tableSearchDTO, true);

		} catch (final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<AuthUserEntityQuery> authUserEntityQueries = Page.empty();
			model.addAttribute("authUserId", sessionAuthUserId);
			model.addAttribute("authUserEmail", sessionAuthUserEmail);
			model.addAttribute("authUserEntityQueries", authUserEntityQueries);
			model.addAttribute("paginationUrl", this.getPaginationUrl());
			model.addAttribute("exportToPdfUrl", EXPORT_TO_PDF_DISABLED_VALUE);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextViewWithTable("authUser/sessionAuthUserEntityQueries.html", model, operation, tableSearchDTO, true);
		}
	}

	/**
	 * Method to export the list of user entity queries with pagination to pdf
	 */
	@GetMapping("/my-user/auth-user-entity-queries/export-to-pdf")
	@ResponseBody
	public byte[] exportToPdf(Model model, Pageable pageable, @Validated TableSearchDTO tableSearchDTO,
			BindingResult bindingResult, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
			@RequestParam(name = "languageParam", required = false) String languageCode,
			@RequestParam(name = "tableFieldCode", required = false) String tableFieldCode,
			@RequestParam(name = "tableFieldValue", required = false) String tableFieldValue,
			@RequestParam(name = "tableOrderCode", required = false) String tableOrderCode,
			@RequestParam(name = "pageSize", required = false) Integer pageSize,
			@RequestParam(name = "pageNumber", required = false) Integer pageNumber) {

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

		return MvcUtils.getNextView("authUserEntityQuery/authUserEntityQueryDetail.html", model, operation, languageCode);
	}

	/**
	 * Method to delete an entity query
	 */
	@GetMapping("/my-user/auth-user-entity-queries/delete/{authUserEntityQueryId}")
	public String delete(RedirectAttributes redirectAttributes, Authentication authentication,
			@PathVariable("authUserEntityQueryId") long authUserEntityQueryId,
			@RequestParam(name = "languageParam", required = false) String languageCode,
			@RequestParam(name = "tableFieldCode", required = false) String tableFieldCode,
			@RequestParam(name = "tableFieldValue", required = false) String tableFieldValue,
			@RequestParam(name = "tableOrderCode", required = false) String tableOrderCode,
			@RequestParam(name = "pageSize", required = false) String pageSize,
			@RequestParam(name = "pageNumber", required = false) String pageNumber) {
		final String sessionAuthUserEmail = authentication.getName();
		final AuthUserEntityQuery authUserEntityQuery = authUserEntityQueryService.findByIdNotOptional(authUserEntityQueryId);

		if(sessionAuthUserEmail != null && authUserEntityQuery != null) {
			final AuthUser authUser = authUserEntityQuery.getAuthUser();
			if(authUser != null && sessionAuthUserEmail.equals(authUser.getEmail())) {
				final String entityQueryFileName = authUserEntityQuery.getFinalResultFileName();

				authUserEntityQueryService.deleteByIdAndFlush(authUserEntityQueryId);

				final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromAuthentication(authentication);
				final String sessionAuthUserIdString = Long.toString(sessionAuthUser.getId());
				final Path finalFilePath = Path.of(authUserEntityQueryFilesPath, sessionAuthUserIdString, entityQueryFileName);
				FileUtils.deletePathRecursively(finalFilePath);

				final String successMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteUserEntityQuery.successMsg", null);
				redirectAttributes.addFlashAttribute("successMsg", successMsg);

				return MvcUtils.getNextRedirectWithTable("/my-user/auth-user-entity-queries", languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
			}
		}

		final String errorMsg = I18nUtils.getInternationalizedMessage(languageCode, "deleteUserEntityQuery.entityQueryDoesNotBelongToUser", null);
		redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

		return MvcUtils.getNextRedirectWithTable("/my-user/auth-user-entity-queries", languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
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
