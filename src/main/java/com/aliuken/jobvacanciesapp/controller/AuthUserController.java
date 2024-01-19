package com.aliuken.jobvacanciesapp.controller;

import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;

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

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.AbstractEntityPageWithExceptionDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserConfirmation;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCredentials;
import com.aliuken.jobvacanciesapp.service.AuthUserConfirmationService;
import com.aliuken.jobvacanciesapp.service.AuthUserCredentialsService;
import com.aliuken.jobvacanciesapp.service.AuthUserCurriculumService;
import com.aliuken.jobvacanciesapp.service.AuthUserRoleService;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.FileUtils;
import com.aliuken.jobvacanciesapp.util.web.MvcUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class AuthUserController {

	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	@Autowired
	private AuthUserService authUserService;

	@Autowired
	private AuthUserConfirmationService authUserConfirmationService;

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
			if(tableSearchDTO == null || tableSearchDTO.hasEmptyAttribute()) {
				if(log.isDebugEnabled()) {
					String tableSearchDTOString = Objects.toString(tableSearchDTO);
					log.debug(StringUtils.getStringJoined("Some table search parameters were empty: ", tableSearchDTOString));
				}

				final Page<AuthUser> authUsers = Page.empty();
				model.addAttribute("authUsers", authUsers);

				return MvcUtils.getNextViewWithTable("authUser/listAuthUsers.html", model, operation, tableSearchDTO, false);
			}

			if(bindingResult.hasErrors()) {
				String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				final Page<AuthUser> authUsers = Page.empty();
				model.addAttribute("authUsers", authUsers);
				model.addAttribute("org.springframework.validation.BindingResult.tableSearchDTO", bindingResult);

				return MvcUtils.getNextViewWithTable("authUser/listAuthUsers.html", model, operation, tableSearchDTO, true);
			}

			final AbstractEntityPageWithExceptionDTO<AuthUser> pageWithExceptionDTO = authUserService.getEntityPage(tableSearchDTO, pageable);
			final Page<AuthUser> authUsers = pageWithExceptionDTO.getPage();
			final Exception exception = pageWithExceptionDTO.getException();

			model.addAttribute("authUsers", authUsers);

			if(exception != null) {
				final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
				model.addAttribute("errorMsg", rootCauseMessage);
			}

			return MvcUtils.getNextViewWithTable("authUser/listAuthUsers.html", model, operation, tableSearchDTO, true);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final Page<AuthUser> authUsers = Page.empty();
			model.addAttribute("authUsers", authUsers);

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);
			model.addAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextViewWithTable("authUser/listAuthUsers.html", model, operation, tableSearchDTO, true);
		}
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

		return MvcUtils.getNextView("authUser/authUserDetail.html", model, operation, languageCode);
	}

	/**
	 * Method to delete a user
	 */
	@GetMapping("/auth-users/delete/{authUserId}")
	public String deleteById(RedirectAttributes redirectAttributes, @PathVariable("authUserId") long authUserId,
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="tableFieldCode", required=false) String tableFieldCode, @RequestParam(name="tableFieldValue", required=false) String tableFieldValue, @RequestParam(name="tableOrderCode", required=false) String tableOrderCode, @RequestParam(name="pageSize", required=false) String pageSize, @RequestParam(name="pageNumber", required=false) String pageNumber) {
		this.deleteById(authUserId);

		final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "deleteAuthUser.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return MvcUtils.getNextRedirectWithTable("/auth-users/index", languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
	}

	public void deleteById(long authUserId) {
		final AuthUser authUser = authUserService.findByIdNotOptional(authUserId);

		final AuthUserConfirmation authUserConfirmation = authUserConfirmationService.findByEmail(authUser.getEmail());
		if(authUserConfirmation != null) {
			authUserConfirmationService.deleteByIdAndFlush(authUserConfirmation.getId());
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
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="tableFieldCode", required=false) String tableFieldCode, @RequestParam(name="tableFieldValue", required=false) String tableFieldValue, @RequestParam(name="tableOrderCode", required=false) String tableOrderCode, @RequestParam(name="pageSize", required=false) String pageSize, @RequestParam(name="pageNumber", required=false) String pageNumber) {
		authUserService.lock(authUserId);
		final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "lockAuthUser.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return MvcUtils.getNextRedirectWithTable("/auth-users/index", languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
	}

	/**
	 * Method to unlock a user
	 */
	@GetMapping("/auth-users/unlock/{authUserId}")
	public String unlock(RedirectAttributes redirectAttributes, @PathVariable("authUserId") long authUserId,
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="tableFieldCode", required=false) String tableFieldCode, @RequestParam(name="tableFieldValue", required=false) String tableFieldValue, @RequestParam(name="tableOrderCode", required=false) String tableOrderCode, @RequestParam(name="pageSize", required=false) String pageSize, @RequestParam(name="pageNumber", required=false) String pageNumber) {
		authUserService.unlock(authUserId);
		final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "unlockAuthUser.successMsg", null);
		redirectAttributes.addFlashAttribute("successMsg", successMsg);

		return MvcUtils.getNextRedirectWithTable("/auth-users/index", languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
	}

}
