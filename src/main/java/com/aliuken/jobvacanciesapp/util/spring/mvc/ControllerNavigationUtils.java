package com.aliuken.jobvacanciesapp.util.spring.mvc;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.AnonymousAccessPermission;
import com.aliuken.jobvacanciesapp.enumtype.UserInterfaceFramework;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.ColorMode;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.ui.Model;

@Slf4j
public class ControllerNavigationUtils {

	private ControllerNavigationUtils() throws InstantiationException {
		final String className = this.getClass().getName();
		throw new InstantiationException(StringUtils.getStringJoined(Constants.INSTANTIATION_NOT_ALLOWED, className));
	}

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

	public static @NonNull String getNextRedirect(final @NonNull String nextRedirectPath, String languageCode) {
		if(languageCode == null) {
			languageCode = Constants.EMPTY_STRING;
		}

		final String nextRedirect = StringUtils.getStringJoined("redirect:", nextRedirectPath, "?languageParam=", languageCode);

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Redirecting to the path '", nextRedirect));
		}
		return nextRedirect;
	}

	public static @NonNull String getNextRedirect(final @NonNull String nextRedirectPath, String languageCode, final String email, final String uuid) {
		if(languageCode == null) {
			languageCode = Constants.EMPTY_STRING;
		}

		final String nextRedirect = StringUtils.getStringJoined("redirect:", nextRedirectPath, "?languageParam=", languageCode,
				"&email=", email,
				"&uuid=", uuid);

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Redirecting to the path '", nextRedirect));
		}
		return nextRedirect;
	}

	public static @NonNull String getNextRedirect(final @NonNull String nextRedirectPath, String languageCode,
				Language nextDefaultLanguage, AnonymousAccessPermission nextDefaultAnonymousAccessPermission,
				TableSortingDirection nextDefaultInitialTableSortingDirection, TablePageSize nextDefaultInitialTablePageSize,
				ColorMode nextDefaultColorMode, UserInterfaceFramework nextDefaultUserInterfaceFramework, PdfDocumentPageFormat nextDefaultPdfDocumentPageFormat) {
		if(languageCode == null) {
			languageCode = Constants.EMPTY_STRING;
		}

		if(nextDefaultLanguage == null) {
			nextDefaultLanguage = Language.ENGLISH;
		}

		if(nextDefaultAnonymousAccessPermission == null) {
			nextDefaultAnonymousAccessPermission = AnonymousAccessPermission.BY_DEFAULT;
		}

		if(nextDefaultInitialTableSortingDirection == null) {
			nextDefaultInitialTableSortingDirection = TableSortingDirection.BY_DEFAULT;
		}

		if(nextDefaultInitialTablePageSize == null) {
			nextDefaultInitialTablePageSize = TablePageSize.BY_DEFAULT;
		}

		if(nextDefaultColorMode == null) {
			nextDefaultColorMode = ColorMode.BY_DEFAULT;
		}

		if(nextDefaultUserInterfaceFramework == null) {
			nextDefaultUserInterfaceFramework = UserInterfaceFramework.MATERIAL_DESIGN;
		}

		if(nextDefaultPdfDocumentPageFormat == null) {
			nextDefaultPdfDocumentPageFormat = PdfDocumentPageFormat.BY_DEFAULT;
		}

		final String nextRedirect = StringUtils.getStringJoined("redirect:", nextRedirectPath, "?languageParam=", languageCode,
				"&nextDefaultLanguageCode=", nextDefaultLanguage.getCode(),
				"&nextDefaultAnonymousAccessPermissionValue=", nextDefaultAnonymousAccessPermission.getValue(),
				"&nextDefaultInitialTableSortingDirectionCode=", String.valueOf(nextDefaultInitialTableSortingDirection.getCode()),
				"&nextDefaultInitialTablePageSizeValue=", String.valueOf(nextDefaultInitialTablePageSize.getValue()),
				"&nextDefaultColorModeCode=", nextDefaultColorMode.getCode(),
				"&nextDefaultUserInterfaceFrameworkCode=", nextDefaultUserInterfaceFramework.getCode(),
				"&nextDefaultPdfDocumentPageFormatCode=", nextDefaultPdfDocumentPageFormat.getCode());

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Redirecting to the path '", nextRedirect));
		}
		return nextRedirect;
	}

	public static String getNextRedirectWithTable(final String nextRedirectPath, String languageCode, String filterName, String filterValue, String sortingField, String sortingDirection, String pageSize, String pageNumber) {
		if(languageCode == null) {
			languageCode = Constants.EMPTY_STRING;
		}

		if(filterName == null) {
			filterName = Constants.EMPTY_STRING;
		}

		if(filterValue == null) {
			filterValue = Constants.EMPTY_STRING;
		}

		if(sortingField == null) {
			sortingField = Constants.EMPTY_STRING;
		}

		if(sortingDirection == null) {
			sortingDirection = Constants.EMPTY_STRING;
		}

		if(pageSize == null) {
			pageSize = Constants.EMPTY_STRING;
		}

		if(pageNumber == null) {
			pageNumber = Constants.EMPTY_STRING;
		}

		final String nextRedirect = StringUtils.getStringJoined("redirect:", nextRedirectPath, "?languageParam=", languageCode, "&filterName=", filterName, "&filterValue=", filterValue, "&sortingField=", sortingField, "&sortingDirection=", sortingDirection, "&pageSize=", pageSize, "&pageNumber=", pageNumber);

		if(log.isDebugEnabled()) {
			log.debug(StringUtils.getStringJoined("Redirecting to the path '", nextRedirect));
		}
		return nextRedirect;
	}
}
