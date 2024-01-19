package com.aliuken.jobvacanciesapp.controller;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.FileType;
import com.aliuken.jobvacanciesapp.model.dto.AuthUserCurriculumDTO;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserCurriculum;
import com.aliuken.jobvacanciesapp.model.entity.JobRequest;
import com.aliuken.jobvacanciesapp.service.AuthUserCurriculumService;
import com.aliuken.jobvacanciesapp.service.AuthUserService;
import com.aliuken.jobvacanciesapp.service.JobRequestService;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.FileUtils;
import com.aliuken.jobvacanciesapp.util.security.SessionUtils;
import com.aliuken.jobvacanciesapp.util.web.MvcUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class SessionAuthUserCurriculumController {

	@Autowired
	private ConfigPropertiesBean configPropertiesBean;

	@Autowired
	private AuthUserService authUserService;

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
	 * Method to show the detail of a curriculum
	 */
	@GetMapping("/my-user/auth-user-curriculums/view/{authUserCurriculumId}")
	public String view(Model model, @PathVariable("authUserCurriculumId") long authUserCurriculumId,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /my-user/auth-user-curriculums/view/{authUserCurriculumId}";

		final AuthUserCurriculum authUserCurriculum = authUserCurriculumService.findByIdNotOptional(authUserCurriculumId);
		model.addAttribute("authUserCurriculum", authUserCurriculum);

		return MvcUtils.getNextView("authUserCurriculum/authUserCurriculumDetail.html", model, operation, languageCode);
	}

	/**
	 * Method to show the creation form of a curriculum
	 */
	@GetMapping("/my-user/auth-user-curriculums/create")
	public String create(HttpServletRequest httpServletRequest, Model model, Authentication authentication,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		final String operation = "GET /my-user/auth-user-curriculums/create";

		final Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(httpServletRequest);

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

		return MvcUtils.getNextView("authUserCurriculum/sessionAuthUserCurriculumForm.html", model, operation, languageCode);
	}

	/**
	 * Method to save a curriculum in the database
	 */
	@PostMapping("/my-user/auth-user-curriculums/save")
	public String save(RedirectAttributes redirectAttributes, Authentication authentication,
			@Validated AuthUserCurriculumDTO authUserCurriculumDTO, BindingResult bindingResult,
			@RequestParam(name="languageParam", required=false) String languageCode) {
		try {
			if(bindingResult.hasErrors()) {
				final String errors = bindingResult.getAllErrors().toString();

				if(log.isErrorEnabled()) {
					log.error(StringUtils.getStringJoined("Some errors were detected when binding some parameters to a model object: ", errors));
				}

				redirectAttributes.addFlashAttribute("authUserCurriculumDTO", authUserCurriculumDTO);
				redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.authUserCurriculumDTO", bindingResult);

				return MvcUtils.getNextRedirect("/my-user/auth-user-curriculums/create", languageCode);
			}

			Long id = authUserCurriculumDTO.getId();
			final MultipartFile multipartFile = authUserCurriculumDTO.getCurriculumFile();
			final String description = authUserCurriculumDTO.getDescription();

			final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromAuthentication(authentication, authUserService);

			final String authUserId = Long.toString(sessionAuthUser.getId());
			final String pathWithAuthUserId = StringUtils.getStringJoined(authUserCurriculumFilesPath, authUserId, "/");
			final List<String> savedFileNames = FileUtils.uploadAndOptionallyUnzipFile(multipartFile, pathWithAuthUserId, FileType.CURRICULUM);

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

			final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "saveUserCurriculum.successMsg", null);
			redirectAttributes.addFlashAttribute("successMsg", successMsg);

			return MvcUtils.getNextRedirect("/my-user/auth-user-curriculums/view/" + authUserCurriculum.getId(), languageCode);

		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("An exception happened when executing a controller method. Exception: ", stackTrace));
			}

			final String rootCauseMessage = ThrowableUtils.getRootCauseMessage(exception);

			redirectAttributes.addFlashAttribute("authUserCurriculumDTO", authUserCurriculumDTO);
			redirectAttributes.addFlashAttribute("errorMsg", rootCauseMessage);

			return MvcUtils.getNextRedirect("/my-user/auth-user-curriculums/create", languageCode);
		}
	}

	/**
	 * Method to delete a curriculum
	 */
	@GetMapping("/my-user/auth-user-curriculums/delete/{authUserCurriculumId}")
	public String delete(RedirectAttributes redirectAttributes, Authentication authentication, @PathVariable("authUserCurriculumId") long authUserCurriculumId,
			@RequestParam(name="languageParam", required=false) String languageCode, @RequestParam(name="tableFieldCode", required=false) String tableFieldCode, @RequestParam(name="tableFieldValue", required=false) String tableFieldValue, @RequestParam(name="tableOrderCode", required=false) String tableOrderCode, @RequestParam(name="pageSize", required=false) String pageSize, @RequestParam(name="pageNumber", required=false) String pageNumber) {
		final String sessionAuthUserEmail = authentication.getName();
		final AuthUserCurriculum authUserCurriculum = authUserCurriculumService.findByIdNotOptional(authUserCurriculumId);

		if(sessionAuthUserEmail != null && authUserCurriculum != null) {
			final AuthUser authUser = authUserCurriculum.getAuthUser();
			if(authUser != null && sessionAuthUserEmail.equals(authUser.getEmail())) {
				final String curriculumFileName = authUserCurriculum.getFileName();
				final List<JobRequest> jobRequests = jobRequestService.findByAuthUserAndCurriculumFileName(authUser, curriculumFileName);
				if(jobRequests != null) {
					for(final JobRequest jobRequest : jobRequests) {
						jobRequestService.deleteByIdAndFlush(jobRequest.getId());
					}
				}

				authUserCurriculumService.deleteByIdAndFlush(authUserCurriculumId);

				final AuthUser sessionAuthUser = SessionUtils.getSessionAuthUserFromAuthentication(authentication, authUserService);
				final String authUserId = Long.toString(sessionAuthUser.getId());
				final Path finalFilePath = Path.of(authUserCurriculumFilesPath, authUserId, curriculumFileName);
				FileUtils.deletePathRecursively(finalFilePath);

				final String successMsg = StringUtils.getInternationalizedMessage(languageCode, "deleteUserCurriculum.successMsg", null);
				redirectAttributes.addFlashAttribute("successMsg", successMsg);

				return MvcUtils.getNextRedirectWithTable("/my-user/auth-users/auth-user-curriculums", languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
			}
		}

		final String errorMsg = StringUtils.getInternationalizedMessage(languageCode, "deleteUserCurriculum.curriculumDoesNotBelongToUser", null);
		redirectAttributes.addFlashAttribute("errorMsg", errorMsg);

		return MvcUtils.getNextRedirectWithTable("/my-user/auth-users/auth-user-curriculums", languageCode, tableFieldCode, tableFieldValue, tableOrderCode, pageSize, pageNumber);
	}

}
