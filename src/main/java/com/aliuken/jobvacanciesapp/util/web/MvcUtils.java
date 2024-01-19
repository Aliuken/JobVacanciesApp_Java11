package com.aliuken.jobvacanciesapp.util.web;

import org.springframework.ui.Model;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.AnonymousAccessPermission;
import com.aliuken.jobvacanciesapp.enumtype.UserInterfaceFramework;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MvcUtils {

	public static String getNextView(final String nextView, final Model model, final String operation, final String languageCode) {
		model.addAttribute("operation", operation);
		model.addAttribute("language", languageCode);

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Going to the view '", nextView, "' with the following attributes: ", model.asMap().entrySet().toString()));
		}
		return nextView;
	}

	public static String getNextViewWithTable(final String nextView, final Model model, final String operation, final TableSearchDTO tableSearchDTO, final boolean showBindingResultErrors) {
		model.addAttribute("operation", operation);
		model.addAttribute("tableSearchDTO", tableSearchDTO);
		model.addAttribute("showBindingResultErrors", showBindingResultErrors);

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Going to the view '", nextView, "' with the following attributes: ", model.asMap().entrySet().toString()));
		}
		return nextView;
	}

	public static String getNextViewWithHomeSearch(final String nextView, final Model model, final String operation, final String description, final Long jobCategoryId, final String languageCode) {
		model.addAttribute("operation", operation);
		model.addAttribute("description", description);
		model.addAttribute("jobCategoryId", jobCategoryId);
		model.addAttribute("language", languageCode);

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Going to the view '", nextView, "' with the following attributes: ", model.asMap().entrySet().toString()));
		}
		return nextView;
	}

	public static String getNextRedirect(final String nextRedirectPath, String languageCode) {
		if(languageCode == null) {
			languageCode = Constants.EMPTY_STRING;
		}

		final String nextRedirect = StringUtils.getStringJoined("redirect:", nextRedirectPath, "?languageParam=", languageCode);

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Redirecting to the path '", nextRedirect));
		}
		return nextRedirect;
	}

	public static String getNextRedirect(final String nextRedirectPath, String languageCode, Language nextDefaultLanguage, AnonymousAccessPermission nextAnonymousAccessPermission, TablePageSize nextDefaultInitialTablePageSize, ColorMode nextDefaultColorMode, UserInterfaceFramework nextUserInterfaceFramework) {
		if(languageCode == null) {
			languageCode = Constants.EMPTY_STRING;
		}

		if(nextDefaultLanguage == null) {
			nextDefaultLanguage = Language.ENGLISH;
		}

		if(nextAnonymousAccessPermission == null) {
			nextAnonymousAccessPermission = AnonymousAccessPermission.ACCESS_BY_PROPERTY;
		}

		if(nextDefaultInitialTablePageSize == null) {
			nextDefaultInitialTablePageSize = TablePageSize.BY_DEFAULT;
		}

		if(nextDefaultColorMode == null) {
			nextDefaultColorMode = ColorMode.BY_DEFAULT;
		}

		if(nextUserInterfaceFramework == null) {
			nextUserInterfaceFramework = UserInterfaceFramework.MATERIAL_DESIGN;
		}

		final String nextRedirect = StringUtils.getStringJoined("redirect:", nextRedirectPath, "?languageParam=", languageCode,
				"&nextDefaultLanguageCode=", nextDefaultLanguage.getCode(),
				"&nextAnonymousAccessPermissionName=", nextAnonymousAccessPermission.name(),
				"&nextDefaultInitialTablePageSizeValue=", String.valueOf(nextDefaultInitialTablePageSize.getValue()),
				"&nextDefaultColorModeCode=", nextDefaultColorMode.getCode(),
				"&nextUserInterfaceFrameworkCode=", nextUserInterfaceFramework.getCode());

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Redirecting to the path '", nextRedirect));
		}
		return nextRedirect;
	}

	public static String getNextRedirectWithTable(final String nextRedirectPath, String languageCode, String tableFieldCode, String tableFieldValue, String tableOrderCode, String pageSize, String pageNumber) {
		if(languageCode == null) {
			languageCode = Constants.EMPTY_STRING;
		}

		if(tableFieldCode == null) {
			tableFieldCode = Constants.EMPTY_STRING;
		}

		if(tableFieldValue == null) {
			tableFieldValue = Constants.EMPTY_STRING;
		}

		if(tableOrderCode == null) {
			tableOrderCode = Constants.EMPTY_STRING;
		}

		if(pageSize == null) {
			pageSize = Constants.EMPTY_STRING;
		}

		if(pageNumber == null) {
			pageNumber = Constants.EMPTY_STRING;
		}

		final String nextRedirect = StringUtils.getStringJoined("redirect:", nextRedirectPath, "?languageParam=", languageCode, "&tableFieldCode=", tableFieldCode, "&tableFieldValue=", tableFieldValue, "&tableOrderCode=", tableOrderCode, "&pageSize=", pageSize, "&pageNumber=", pageNumber);

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Redirecting to the path '", nextRedirect));
		}
		return nextRedirect;
	}

}
