package com.aliuken.jobvacanciesapp.util.persistence.pdf;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.AuthRole;
import com.aliuken.jobvacanciesapp.model.entity.AuthUser;
import com.aliuken.jobvacanciesapp.model.entity.AuthUserEntityQuery;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.javase.ThrowableUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.componentbuilder.GenericTableBuilder;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.dto.GenericTableContentDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class AuthUserQueryReport<T extends AbstractEntity<T>> extends PdfDocument {
	private static final Font TITLE_CELL_FONT = new Font(FontFamily.COURIER, 18, Font.BOLD);

	private static final float URL_TABLE_COLUMN_WIDTH = 20f;
	private static final String[] URL_TABLE_COLUMN_MESSAGE_NAMES = new String[]{"queryReport.url"};

	private static final Font EMPTY_RESULT_CELL_FONT = new Font(FontFamily.HELVETICA, 10, Font.NORMAL);

	private static final float[] RESULT_TABLE_COLUMN_WIDTHS = new float[]{5, 5, 5, 5};
	private static final String[] RESULT_TABLE_COLUMN_MESSAGE_NAMES = new String[]{"queryReport.tableHeader.keys", "queryReport.tableHeader.userFields", "queryReport.tableHeader.commonFields", "queryReport.tableHeader.otherFields"};
	private static final int RESULT_TABLE_CELL_HORIZONTAL_ALIGNMENT = Element.ALIGN_LEFT;
	private static final Font RESULT_TABLE_CELL_FONT = new Font(FontFamily.HELVETICA, 7, Font.NORMAL);

	private final AuthUserEntityQuery authUserEntityQuery;
	private final String[][] contentArray;

	public static <U extends AbstractEntity<U>> AuthUserQueryReport<U> generatePdfDocument(final ByteArrayOutputStream byteArrayOutputStream, final AuthUserEntityQuery authUserEntityQuery, final Page<U> entityPage) {
		AuthUserQueryReport<U> result;
		try(final AuthUserQueryReport<U> authUserQueryReport = new AuthUserQueryReport<>(byteArrayOutputStream, authUserEntityQuery, entityPage)) {
			authUserQueryReport.open();
			authUserQueryReport.createContent();
			result = authUserQueryReport;
		} catch(final Exception exception) {
			if(log.isErrorEnabled()) {
				final String authUserEntityQueryString = String.valueOf(authUserEntityQuery);
				final String stackTrace = ThrowableUtils.getStackTrace(exception);
				log.error(StringUtils.getStringJoined("Error when saving an AuthUserQueryReport with authUserEntityQuery \"", authUserEntityQueryString, "\". Exception: ", stackTrace));
			}
			result = null;
		}
		return result;
	}

	private AuthUserQueryReport(final ByteArrayOutputStream byteArrayOutputStream, final AuthUserEntityQuery authUserEntityQuery, final Page<T> entityPage) throws DocumentException, IOException {
		super(authUserEntityQuery.getInitialPdfDocumentPageFormat(), authUserEntityQuery.getFinalPdfDocumentPageFormat(), byteArrayOutputStream);
		this.authUserEntityQuery = authUserEntityQuery;
		this.contentArray = this.createContentArrayFromPage(entityPage);
		this.addPageEventHelper();
	}

	private String[][] createContentArrayFromPage(final Page<T> entityPage) {
		final List<AbstractEntity> entityList = GenericsUtils.cast(entityPage.getContent());

		final String[][] contentArray;
		if(entityList != null && !entityList.isEmpty()) {
			final AbstractEntity<?> abstractEntity = entityList.get(0);
			if(abstractEntity.isPrintableEntity()) {
				final List<String[]> contentList = Constants.PARALLEL_STREAM_UTILS.convertList(entityList,
						entity -> entity.getGroupedFields(), AbstractEntity.class, String[].class);
				contentArray = contentList.toArray(new String[][]{});
			} else {
				contentArray = new String[0][0];
			}
		} else {
			contentArray = new String[0][0];
		}
		return contentArray;
	}

	@Override
	public String getLeftFooter() {
		final String leftFooter;
		if(authUserEntityQuery != null) {
			final AuthUser authUser = authUserEntityQuery.getAuthUser();

			final String authUserId;
			if(authUser != null) {
				authUserId = authUser.getIdString();
			} else {
				authUserId = null;
			}

			final String queryId = authUserEntityQuery.getIdString();
			final LocalDateTime queryDateTime = authUserEntityQuery.getFirstRegistrationDateTime();
			final String queryDateTimeString = Constants.DATE_TIME_UTILS.convertToString(queryDateTime);

			leftFooter = StringUtils.getStringJoined("[", authUserId, ", ", queryId, "] - ", queryDateTimeString);
		} else {
			leftFooter = null;
		}

		return leftFooter;
	}

	private void createContent() throws DocumentException {
		final PdfWriter pdfWriter = getPdfWriter();
		addDocListener(pdfWriter);

		final Chapter chapter = new Chapter(0);
		chapter.setNumberDepth(0);
		chapter.setTriggerNewPage(false);

		final PdfPTable firstPageTitleTable = this.createFirstPageTitleTable();
		chapter.add(firstPageTitleTable);

		final Paragraph separatorParagraph1 = new Paragraph(Chunk.NEWLINE);
		chapter.add(separatorParagraph1);

		final PdfPTable userAndQueryTable = this.createUserAndQueryTable();
		chapter.add(userAndQueryTable);

		final Paragraph separatorParagraph2 = new Paragraph(Chunk.NEWLINE);
		chapter.add(separatorParagraph2);

		final PdfPTable queryUrlTable = this.createQueryUrlTable();
		chapter.add(queryUrlTable);

		final Paragraph separatorParagraph3 = new Paragraph(Chunk.NEWLINE);
		chapter.add(separatorParagraph3);

		final PdfPTable queryResultTable = this.createQueryResultTable();
		chapter.add(queryResultTable);

		add(chapter);
	}

	private PdfPTable createFirstPageTitleTable() throws DocumentException {
		final Language queryLanguage = authUserEntityQuery.getLanguage();
		final String firstPageTitle = getFirstPageTitle();
		final GenericTableBuilder titleTableBuilder = new GenericTableBuilder(queryLanguage, TITLE_CELL_FONT, firstPageTitle);
		final PdfPTable titleTable = titleTableBuilder.build();

		return titleTable;
	}

	private String getFirstPageTitle() {
		final Language queryLanguage = this.getQueryLanguage();
		final String firstPageTitle = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.title", null);
		return firstPageTitle;
	}

	private PdfPTable createUserAndQueryTable() throws DocumentException {
		final PdfPTable userAndQueryTable = new PdfPTable(3);
		userAndQueryTable.setWidths(new int[]{3, 1, 3});
		userAndQueryTable.setWidthPercentage(90);
		userAndQueryTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		userAndQueryTable.getDefaultCell().setVerticalAlignment(PdfPTable.ALIGN_TOP);
		userAndQueryTable.getDefaultCell().setBorderColor(BaseColor.WHITE);

		final PdfPTable userTable = this.createUserTable();
		userAndQueryTable.addCell(userTable);

		final PdfPCell separatorCell = this.createSeparatorCell();
		userAndQueryTable.addCell(separatorCell);

		final PdfPTable queryTable = this.createQueryTable();
		userAndQueryTable.addCell(queryTable);

		return userAndQueryTable;
	}

	private PdfPTable createUserTable() throws DocumentException {
		final String userTitle;
		final List<PdfPCell> userInfo;
		if(authUserEntityQuery != null) {
			final Language queryLanguage = authUserEntityQuery.getLanguage();
			final AuthUser authUser = authUserEntityQuery.getAuthUser();

			userTitle = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.user", null);
			userInfo = this.getUserInfo(queryLanguage, authUser);
		} else {
			userTitle = I18nUtils.getInternationalizedMessage(Language.ENGLISH, "queryReport.user", null);
			userInfo = new ArrayList<>();
		}

		final PdfPTable userTable = PdfDocument.createSearchCriteriaTable(userTitle, userInfo);
		return userTable;
	}

	private List<PdfPCell> getUserInfo(final Language queryLanguage, final AuthUser authUser) {
		final List<PdfPCell> userInfo = new ArrayList<>();
		if(authUser != null) {
			final Language userLanguage = authUser.getLanguage();

			final String idField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.user.id", null);
			final String nameField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.user.name", null);
			final String emailField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.user.email", null);
			final String roleField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.user.role", null);
			final String languageField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.user.language", null);
			final String pdfDocumentPageFormatField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.user.pdfDocumentPageFormat", null);

			final AuthRole maxPriorityAuthRole = authUser.getMaxPriorityAuthRole();
			final String maxPriorityAuthRoleMessage = (maxPriorityAuthRole != null) ? maxPriorityAuthRole.getMessage(queryLanguage) : null;

			final String userLanguageMessage = userLanguage.getMessage(queryLanguage);

			final PdfDocumentPageFormat initialPdfDocumentPageFormat = this.getInitialPdfDocumentPageFormat();
			final PdfDocumentPageFormat finalPdfDocumentPageFormat = this.getFinalPdfDocumentPageFormat();
			final String pdfDocumentPageFormatMessage = Constants.ENUM_UTILS.getConfigurableEnumMessage(initialPdfDocumentPageFormat, finalPdfDocumentPageFormat, PdfDocumentPageFormat.class, queryLanguage);

			PdfDocument.addCellWithPhrase(userInfo, idField, authUser.getIdString());
			PdfDocument.addCellWithPhrase(userInfo, nameField, authUser.getFullName());
			PdfDocument.addCellWithPhrase(userInfo, emailField, authUser.getEmail());
			PdfDocument.addCellWithPhrase(userInfo, roleField, maxPriorityAuthRoleMessage);
			PdfDocument.addCellWithPhrase(userInfo, languageField, userLanguageMessage);
			PdfDocument.addCellWithPhrase(userInfo, pdfDocumentPageFormatField, pdfDocumentPageFormatMessage);
			PdfDocument.addCellWithPhrase(userInfo, null, null);
			PdfDocument.addCellWithPhrase(userInfo, null, null);
			PdfDocument.addCellWithPhrase(userInfo, null, null);

			final String predefinedFilterEntityName = authUserEntityQuery.getPredefinedFilterEntityName();
			final String predefinedFilterValue = authUserEntityQuery.getPredefinedFilterValue();
			if(LogicalUtils.isNotNullNorEmptyString(predefinedFilterEntityName) && LogicalUtils.isNotNullNorEmptyString(predefinedFilterValue)) {
				PdfDocument.addCellWithPhrase(userInfo, null, null);
			}

			final String filterName = authUserEntityQuery.getFilterTableFieldName();
			final String filterValue = authUserEntityQuery.getFilterValue();
			if(LogicalUtils.isNotNullNorEmptyString(filterName) && LogicalUtils.isNotNullNorEmptyString(filterValue)) {
				PdfDocument.addCellWithPhrase(userInfo, null, null);
			}
		}
		return userInfo;
	}

	private PdfPCell createSeparatorCell() {
		final PdfPCell separatorCell = new PdfPCell();
		separatorCell.setBorder(PdfPCell.NO_BORDER);
		separatorCell.setVerticalAlignment(PdfPTable.ALIGN_TOP);
		return separatorCell;
	}

	private PdfPTable createQueryTable() throws DocumentException {
		final String queryTitle;
		final List<PdfPCell> queryInfo;
		if(authUserEntityQuery != null) {
			final Language queryLanguage = authUserEntityQuery.getLanguage();
			final TableField sortingTableField = authUserEntityQuery.getSortingTableField();
			final TableSortingDirection tableSortingDirection = authUserEntityQuery.getTableSortingDirection();
			final TablePageSize tablePageSize = authUserEntityQuery.getTablePageSize();

			queryTitle = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.query", null);
			queryInfo = this.getQueryInfo(queryLanguage, sortingTableField, tableSortingDirection, tablePageSize);
		} else {
			queryTitle = I18nUtils.getInternationalizedMessage(Language.ENGLISH, "queryReport.query", null);
			queryInfo = new ArrayList<>();
		}

		final PdfPTable queryTable = PdfDocument.createSearchCriteriaTable(queryTitle, queryInfo);
		return queryTable;
	}

	private List<PdfPCell> getQueryInfo(final Language queryLanguage, final TableField sortingTableField, final TableSortingDirection tableSortingDirection, final TablePageSize tablePageSize) {
		final List<PdfPCell> queryInfo = new ArrayList<>();

		final String idField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.query.id", null);
		final String typeField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.query.type", null);
		final String dateField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.query.date", null);
		final String sortingFieldField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.query.sortingField", null);
		final String sortingDirectionField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.query.sortingDirection", null);
		final String pageSizeField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.query.pageSize", null);
		final String languageField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.query.language", null);
		final String pageNumberField = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.query.pageNumber", null);
		final String filterFields = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.query.filters", null);

		final String sortingTableFieldMessage = sortingTableField.getMessage(queryLanguage);
		final String tableDirectionFieldMessage = tableSortingDirection.getMessage(queryLanguage);
		final String tablePageSizeMessage = tablePageSize.getMessage(queryLanguage);
		final String queryLanguageMessage = queryLanguage.getMessage(queryLanguage);

		PdfDocument.addCellWithPhrase(queryInfo, idField, authUserEntityQuery.getIdString());
		PdfDocument.addCellWithPhrase(queryInfo, typeField, authUserEntityQuery.getEndpointTypeString());
		PdfDocument.addCellWithPhrase(queryInfo, dateField, authUserEntityQuery.getFirstRegistrationDateTimeString());
		PdfDocument.addCellWithPhrase(queryInfo, sortingFieldField, sortingTableFieldMessage);
		PdfDocument.addCellWithPhrase(queryInfo, sortingDirectionField, tableDirectionFieldMessage);
		PdfDocument.addCellWithPhrase(queryInfo, pageSizeField, tablePageSizeMessage);
		PdfDocument.addCellWithPhrase(queryInfo, languageField, queryLanguageMessage);
		PdfDocument.addCellWithPhrase(queryInfo, pageNumberField, authUserEntityQuery.getRealPageNumberString());

		final String predefinedFilterEntityName = authUserEntityQuery.getPredefinedFilterEntityName();
		final String predefinedFilterValue = authUserEntityQuery.getPredefinedFilterValue();
		final boolean mustAddPredefinedFilter = (LogicalUtils.isNotNullNorEmptyString(predefinedFilterEntityName) && LogicalUtils.isNotNullNorEmptyString(predefinedFilterValue));

		final String filterName = authUserEntityQuery.getFilterTableFieldName();
		final String filterValue = authUserEntityQuery.getFilterValue();
		final boolean mustAddFilter = (LogicalUtils.isNotNullNorEmptyString(filterName) && LogicalUtils.isNotNullNorEmptyString(filterValue));

		if(!mustAddPredefinedFilter && !mustAddFilter) {
			final String none = I18nUtils.getInternationalizedMessage(queryLanguage, "filters.none", null);
			PdfDocument.addCellWithPhrase(queryInfo, filterFields, none);
		} else {
			PdfDocument.addCellWithPhrase(queryInfo, filterFields, null);

			if(mustAddPredefinedFilter) {
				PdfDocument.addCellWithPhrase(queryInfo, StringUtils.getStringJoined(" - ", predefinedFilterEntityName), predefinedFilterValue);
			}

			if(mustAddFilter) {
				PdfDocument.addCellWithPhrase(queryInfo, StringUtils.getStringJoined(" - ", filterName), filterValue);
			}
		}

		return queryInfo;
	}

	private PdfPTable createQueryUrlTable() throws DocumentException {
		final Language queryLanguage = authUserEntityQuery.getLanguage();
		final String[] columnNames = this.getColumnNames(URL_TABLE_COLUMN_MESSAGE_NAMES);
		final String columnName = columnNames[0];
		final String columnValue = authUserEntityQuery.getQueryUrl();
		final GenericTableBuilder searchUrlTableBuilder = new GenericTableBuilder(queryLanguage, columnName, columnValue, URL_TABLE_COLUMN_WIDTH);
		final PdfPTable searchResultTable = searchUrlTableBuilder.build();
		return searchResultTable;
	}

	private PdfPTable createQueryResultTable() throws DocumentException {
		final Language queryLanguage = authUserEntityQuery.getLanguage();

		final PdfPTable searchResultTable;
		if(LogicalUtils.isNotNullNorEmpty(contentArray)) {
			final String[] columnNames = this.getColumnNames(RESULT_TABLE_COLUMN_MESSAGE_NAMES);
			final GenericTableContentDTO genericTableContentDTO = new GenericTableContentDTO(RESULT_TABLE_CELL_HORIZONTAL_ALIGNMENT, RESULT_TABLE_CELL_FONT, contentArray, true, true);
			final GenericTableBuilder searchResultTableBuilder = new GenericTableBuilder(queryLanguage, columnNames, null, RESULT_TABLE_COLUMN_WIDTHS, genericTableContentDTO);
			searchResultTable = searchResultTableBuilder.build();
		} else {
			final String emptyResultText = getEmptyResultText();
			final GenericTableBuilder titleTableBuilder = new GenericTableBuilder(queryLanguage, EMPTY_RESULT_CELL_FONT, emptyResultText);
			searchResultTable = titleTableBuilder.build();
		}
		return searchResultTable;
	}

	private String[] getColumnNames(final String[] columnMessageNames) {
		final String[] columnNames;
		if(columnMessageNames != null) {
			final Language queryLanguage = this.getQueryLanguage();
			columnNames = new String[columnMessageNames.length];
			int columnIndex = 0;
			for(final String columnMessageName : columnMessageNames) {
				final String columnName = I18nUtils.getInternationalizedMessage(queryLanguage, columnMessageName, null);
				columnNames[columnIndex] = columnName;
				columnIndex++;
			}
		} else {
			columnNames = new String[0];
		}

		return columnNames;
	}

	private String getEmptyResultText() {
		final Language queryLanguage = this.getQueryLanguage();
		final String emptyResultText = I18nUtils.getInternationalizedMessage(queryLanguage, "queryReport.emptyResultText", null);
		return emptyResultText;
	}

	private Language getQueryLanguage() {
		final Language queryLanguage;
		if(authUserEntityQuery != null) {
			queryLanguage = authUserEntityQuery.getLanguage();
		} else {
			queryLanguage = Language.ENGLISH;
		}
		return queryLanguage;
	}
}
