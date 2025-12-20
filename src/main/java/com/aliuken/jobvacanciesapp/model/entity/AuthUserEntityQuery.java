package com.aliuken.jobvacanciesapp.model.entity;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.EndpointType;
import com.aliuken.jobvacanciesapp.model.dto.PredefinedFilterDTO;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PredefinedFilterEntity;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableField;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableSortingDirection;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntityWithAuthUser;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.file.FileUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Entity
@Table(name = "auth_user_entity_query", indexes = {
		@Index(name = "auth_user_entity_query_key_1", columnList = "auth_user_id"),
		@Index(name = "auth_user_entity_query_key_2", columnList = "first_registration_auth_user_id"),
		@Index(name = "auth_user_entity_query_key_3", columnList = "last_modification_auth_user_id")})
@Getter
@Setter
public class AuthUserEntityQuery extends AbstractEntityWithAuthUser<AuthUserEntityQuery> {
	private static final long serialVersionUID = -2092511420655590241L;

	@NotNull
	@ManyToOne
	@JoinColumn(name="auth_user_id", nullable=false)
	private AuthUser authUser;

	@NotNull
	@Column(name = "page_entity", nullable = false)
	private PageEntityEnum pageEntity;

	@NotNull
	@Column(name = "initial_pdf_document_page_format", nullable = false)
	private PdfDocumentPageFormat initialPdfDocumentPageFormat;

	@NotNull
	@Column(name = "final_pdf_document_page_format", nullable = false)
	private PdfDocumentPageFormat finalPdfDocumentPageFormat;

	@NotNull
	@Column(name = "language", nullable = false)
	private Language language;

	@Column(name = "predefined_filter_name")
	private PredefinedFilterEntity predefinedFilterEntity;

	@Size(max = 255)
	@Column(name = "predefined_filter_value", length = 255)
	private String predefinedFilterValue;

	@Column(name = "filter_name")
	private TableField filterTableField;

	@Size(max = 255)
	@Column(name = "filter_value", length = 255)
	private String filterValue;

	@NotNull
	@Column(name = "sorting_field", nullable = false)
	private TableField tableSortingField;

	@NotNull
	@Column(name = "sorting_direction", nullable = false)
	private TableSortingDirection tableSortingDirection;

	@NotNull
	@Column(name = "page_size", nullable = false)
	private TablePageSize tablePageSize;

	@NotNull
	@Column(name = "page_number", nullable = false)
	private Integer pageNumber;

	@NotNull
	@Size(max = 2048)
	@Column(name = "query_url", length = 2048)
	private String queryUrl;

	@Size(max = 255)
	@Column(name = "final_result_file_name", length = 255)
	private String finalResultFileName;

	public AuthUserEntityQuery() {
		super();
	}

	public AuthUserEntityQuery(final AuthUser authUser, final PredefinedFilterDTO predefinedFilterDTO, final TableSearchDTO tableSearchDTO, final PageEntityEnum pageEntity, final String queryUrl) {
		this.setAuthUser(authUser);
		this.pageEntity = pageEntity;
		this.initialPdfDocumentPageFormat = authUser.getPdfDocumentPageFormat();
		this.finalPdfDocumentPageFormat = Constants.ENUM_UTILS.getFinalEnumElement(initialPdfDocumentPageFormat, ConfigPropertiesBean.CURRENT_DEFAULT_PDF_DOCUMENT_PAGE_FORMAT);
		this.language = Language.findByCode(tableSearchDTO.getLanguageParam());

		if(predefinedFilterDTO != null) {
			this.predefinedFilterEntity = predefinedFilterDTO.getPredefinedFilterEntity();
			this.predefinedFilterValue = predefinedFilterDTO.getPredefinedFilterValue();
		} else {
			this.predefinedFilterEntity = null;
			this.predefinedFilterValue = null;
		}

		this.filterTableField = tableSearchDTO.getFilterTableField();
		this.filterValue = tableSearchDTO.getFilterValue();
		this.tableSortingField = tableSearchDTO.getTableSortingField();
		this.tableSortingDirection = tableSearchDTO.getTableSortingDirection();
		this.tablePageSize = tableSearchDTO.getTablePageSize();
		this.pageNumber = tableSearchDTO.getPageNumber();
		this.queryUrl = queryUrl;
	}

	public String getOriginalResultFileName() {
		final String originalResultFileName = StringUtils.getStringJoined(pageEntity.getValue(), Constants.HYPHEN, this.getIdString(), FileUtils.PDF_EXTENSION);
		return originalResultFileName;
	}

	public String getFilePath() {
		final AuthUser authUser = this.getAuthUser();

		final String authUserIdString;
		if(authUser != null) {
			Long authUserId = authUser.getId();
			authUserIdString = (authUserId != null) ? authUserId.toString() : "temp";
		} else {
			authUserIdString = "temp";
		}

		final String filePath = StringUtils.getStringJoined(authUserIdString, "/", finalResultFileName);
		return filePath;
	}

	public String getInitialPdfDocumentPageFormatName() {
		final String initialPdfDocumentPageFormatName = Objects.toString(initialPdfDocumentPageFormat);
		return initialPdfDocumentPageFormatName;
	}

	public String getFinalPdfDocumentPageFormatName() {
		final String finalPdfDocumentPageFormatName = Objects.toString(finalPdfDocumentPageFormat);
		return finalPdfDocumentPageFormatName;
	}

	public String getLanguageName() {
		final String languageName = Objects.toString(language);
		return languageName;
	}

	public String getPredefinedFilterEntityName() {
		final String predefinedFilterEntityName;
		if(predefinedFilterEntity != null) {
			predefinedFilterEntityName = predefinedFilterEntity.getUpperCasedEntityName();
		} else {
			predefinedFilterEntityName = null;
		}
		return predefinedFilterEntityName;
	}

	public String getPredefinedFilterStringForWebPageField() {
		final String predefinedFilterString;
		if(predefinedFilterEntity != null && LogicalUtils.isNotNullNorEmptyString(predefinedFilterValue)) {
			final String predefinedFilterEntityName = predefinedFilterEntity.getUpperCasedEntityName();
			predefinedFilterString = StringUtils.getStringJoined(predefinedFilterEntityName, Constants.KEY_VALUE_SEPARATOR, predefinedFilterValue);
		} else {
			predefinedFilterString = Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD;
		}
		return predefinedFilterString;
	}

	public String getFilterTableFieldName() {
		final String filterTableFieldName;
		if(filterTableField != null) {
			filterTableFieldName = filterTableField.getCode();
		} else {
			filterTableFieldName = null;
		}
		return filterTableFieldName;
	}

	public String getFilterStringForWebPageField() {
		final String filterString;
		if(filterTableField != null && LogicalUtils.isNotNullNorEmptyString(filterValue)) {
			final String filterTableFieldName = filterTableField.getCode();
			filterString = StringUtils.getStringJoined(filterTableFieldName, Constants.KEY_VALUE_SEPARATOR, filterValue);
		} else {
			filterString = Constants.DEFAULT_VALUE_WHEN_SHOWING_NULL_TABLE_FIELD;
		}
		return filterString;
	}

	public String getTableSortingFieldName() {
		final String tableSortingFieldName = Objects.toString(tableSortingField);
		return tableSortingFieldName;
	}

	public String getTableSortingDirectionName() {
		final String tableSortingDirectionName = Objects.toString(tableSortingDirection);
		return tableSortingDirectionName;
	}

	public String getTablePageSizeName() {
		final String tablePageSizeName = Objects.toString(tablePageSize);
		return tablePageSizeName;
	}

	public String getPageNumberString() {
		final String pageNumberString = Objects.toString(pageNumber);
		return pageNumberString;
	}

	public Integer getRealPageNumber() {
		Integer realPageNumber;
		if(pageNumber != null) {
			realPageNumber = pageNumber + 1;
		} else {
			realPageNumber = null;
		}
		return realPageNumber;
	}

	public String getRealPageNumberString() {
		Integer realPageNumber = this.getRealPageNumber();
		final String pageNumberString = Objects.toString(realPageNumber);
		return pageNumberString;
	}

	public String getEndpointTypeString() {
		try {
			final String httpMethod = RequestMethod.GET.toString();
			final URI queryUri = new URI(queryUrl);
			final String queryUrlPath = StringUtils.getUrlPath(queryUri);
			final EndpointType endpointType = EndpointType.getInstance(httpMethod, queryUrlPath);
			final String endpointTypeString = (endpointType != null) ? endpointType.toString() : null;
			return endpointTypeString;
		} catch (final URISyntaxException e) {
			return null;
		}
	}

	@Override
	public boolean isPrintableEntity() {
		return false;
	}

	@Override
	public String getKeyFields() {
		final String idString = this.getIdString();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("id: "), idString);
		return result;
	}

	@Override
	public String getOtherFields() {
		final String initialPdfDocumentPageFormatName = this.getInitialPdfDocumentPageFormatName();
		final String finalPdfDocumentPageFormatName = this.getFinalPdfDocumentPageFormatName();
		final String languageName = this.getLanguageName();
		final String predefinedFilterEntityName = this.getPredefinedFilterEntityName();
		final String filterTableFieldName = this.getFilterTableFieldName();
		final String tableSortingFieldName = this.getTableSortingFieldName();
		final String tableSortingDirectionName = this.getTableSortingDirectionName();
		final String tablePageSizeName = this.getTablePageSizeName();
		final String pageNumberString = this.getPageNumberString();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("initialPdfDocumentPageFormat: "), initialPdfDocumentPageFormatName, Constants.NEWLINE,
			StyleApplier.getBoldString("finalPdfDocumentPageFormat: "), finalPdfDocumentPageFormatName, Constants.NEWLINE,
			StyleApplier.getBoldString("language: "), languageName, Constants.NEWLINE,
			StyleApplier.getBoldString("predefinedFilterEntity: "), predefinedFilterEntityName, Constants.NEWLINE,
			StyleApplier.getBoldString("predefinedFilterValue: "), predefinedFilterValue, Constants.NEWLINE,
			StyleApplier.getBoldString("filterTableField: "), filterTableFieldName, Constants.NEWLINE,
			StyleApplier.getBoldString("filterValue: "), filterValue, Constants.NEWLINE,
			StyleApplier.getBoldString("tableSortingField: "), tableSortingFieldName, Constants.NEWLINE,
			StyleApplier.getBoldString("tableSortingDirection: "), tableSortingDirectionName, Constants.NEWLINE,
			StyleApplier.getBoldString("tablePageSize: "), tablePageSizeName, Constants.NEWLINE,
			StyleApplier.getBoldString("pageNumber: "), pageNumberString, Constants.NEWLINE,
			StyleApplier.getBoldString("queryUrl: "), queryUrl, Constants.NEWLINE,
			StyleApplier.getBoldString("finalResultFileName: "), finalResultFileName);
		return result;
	}

	@Override
	public String toString() {
		final String idString = this.getIdString();
		final String authUserEmail = this.getAuthUserEmail();
		final String initialPdfDocumentPageFormatName = this.getInitialPdfDocumentPageFormatName();
		final String finalPdfDocumentPageFormatName = this.getFinalPdfDocumentPageFormatName();
		final String languageName = this.getLanguageName();
		final String predefinedFilterEntityName = this.getPredefinedFilterEntityName();
		final String filterTableFieldName = this.getFilterTableFieldName();
		final String tableSortingFieldName = this.getTableSortingFieldName();
		final String tableSortingDirectionName = this.getTableSortingDirectionName();
		final String tablePageSizeName = this.getTablePageSizeName();
		final String pageNumberString = this.getPageNumberString();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AbstractEntityQuery [id=", idString, ", authUser=", authUserEmail,
			", initialPdfDocumentPageFormat=", initialPdfDocumentPageFormatName, ", finalPdfDocumentPageFormat=", finalPdfDocumentPageFormatName, ", language=", languageName,
			", predefinedFilterEntity=", predefinedFilterEntityName, ", predefinedFilterValue=", predefinedFilterValue,
			", filterTableField=", filterTableFieldName, ", filterValue=", filterValue,
			", tableSortingField=", tableSortingFieldName, ", tableSortingDirection=", tableSortingDirectionName,
			", tablePageSize=", tablePageSizeName, ", pageNumber=", pageNumberString,
			", queryUrl=", queryUrl, ", finalResultFileName=", finalResultFileName,
			", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail,
			", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}
}
