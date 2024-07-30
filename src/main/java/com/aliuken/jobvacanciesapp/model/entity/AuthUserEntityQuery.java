package com.aliuken.jobvacanciesapp.model.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.model.dto.TableSearchDTO;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PageEntityEnum;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TableOrder;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.TablePageSize;
import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntityWithAuthUser;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.file.FileUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;

import lombok.Data;

@Entity
@Table(name = "auth_user_entity_query", indexes = {
		@Index(name = "auth_user_entity_query_key_1", columnList = "auth_user_id"),
		@Index(name = "auth_user_entity_query_key_2", columnList = "first_registration_auth_user_id"),
		@Index(name = "auth_user_entity_query_key_3", columnList = "last_modification_auth_user_id")})
@Data
public class AuthUserEntityQuery extends AbstractEntityWithAuthUser {
	private static final long serialVersionUID = -2092511420655590241L;

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

	@Size(max = 255)
	@Column(name = "predefined_filter_name", length = 255)
	private String predefinedFilterName;

	@Size(max = 255)
	@Column(name = "predefined_filter_value", length = 255)
	private String predefinedFilterValue;

	@Size(max = 255)
	@Column(name = "filter_name", length = 255)
	private String filterName;

	@Size(max = 255)
	@Column(name = "filter_value", length = 255)
	private String filterValue;

	@NotNull
	@Column(name = "table_order", nullable = false)
	private TableOrder tableOrder;

	@NotNull
	@Column(name = "table_page_size", nullable = false)
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

	public AuthUserEntityQuery(final AuthUser authUser, final TableSearchDTO tableSearchDTO, final PageEntityEnum pageEntity, final String queryUrl) {
		this.setAuthUser(authUser);
		this.pageEntity = pageEntity;
		this.initialPdfDocumentPageFormat = authUser.getPdfDocumentPageFormat();
		this.finalPdfDocumentPageFormat = Constants.ENUM_UTILS.getFinalEnumElement(initialPdfDocumentPageFormat, ConfigPropertiesBean.CURRENT_DEFAULT_PDF_DOCUMENT_PAGE_FORMAT);
		this.language = Language.findByCode(tableSearchDTO.getLanguageParam());
		this.predefinedFilterName = tableSearchDTO.getPredefinedFilterName();
		this.predefinedFilterValue = tableSearchDTO.getPredefinedFilterValue();
		this.filterName = tableSearchDTO.getTableFieldCode();
		this.filterValue = tableSearchDTO.getTableFieldValue();
		this.tableOrder = TableOrder.findByCode(tableSearchDTO.getTableOrderCode());
		this.tablePageSize = TablePageSize.findByValue(tableSearchDTO.getPageSize());
		this.pageNumber = tableSearchDTO.getPageNumber();
		this.queryUrl= queryUrl;
	}

	public String getOriginalResultFileName() {
		final String originalResultFileName = StringUtils.getStringJoined(pageEntity.getValue(), "-", this.getIdString(), FileUtils.PDF_EXTENSION);
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

	public String getTableOrderName() {
		final String tableOrderName = Objects.toString(tableOrder);
		return tableOrderName;
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
		final String tableOrderName = this.getTableOrderName();
		final String tablePageSizeName = this.getTablePageSizeName();
		final String pageNumberString = this.getPageNumberString();

		final String result = StringUtils.getStringJoined(
			StyleApplier.getBoldString("initialPdfDocumentPageFormat: "), initialPdfDocumentPageFormatName, "\n",
			StyleApplier.getBoldString("finalPdfDocumentPageFormat: "), finalPdfDocumentPageFormatName, "\n",
			StyleApplier.getBoldString("language: "), languageName, "\n",
			StyleApplier.getBoldString("predefinedFilterName: "), predefinedFilterName, "\n",
			StyleApplier.getBoldString("predefinedFilterValue: "), predefinedFilterValue, "\n",
			StyleApplier.getBoldString("filterName: "), filterName, "\n",
			StyleApplier.getBoldString("filterValue: "), filterValue, "\n",
			StyleApplier.getBoldString("tableOrder: "), tableOrderName, "\n",
			StyleApplier.getBoldString("tablePageSize: "), tablePageSizeName, "\n",
			StyleApplier.getBoldString("pageNumber: "), pageNumberString, "\n",
			StyleApplier.getBoldString("queryUrl: "), queryUrl, "\n",
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
		final String tableOrderName = this.getTableOrderName();
		final String tablePageSizeName = this.getTablePageSizeName();
		final String pageNumberString = this.getPageNumberString();
		final String firstRegistrationDateTimeString = this.getFirstRegistrationDateTimeString();
		final String firstRegistrationAuthUserEmail = this.getFirstRegistrationAuthUserEmail();
		final String lastModificationDateTimeString = this.getLastModificationDateTimeString();
		final String lastModificationAuthUserEmail = this.getLastModificationAuthUserEmail();

		final String result = StringUtils.getStringJoined("AbstractEntityQuery [id=", idString, ", authUser=", authUserEmail,
				", initialPdfDocumentPageFormat=", initialPdfDocumentPageFormatName, ", finalPdfDocumentPageFormat=", finalPdfDocumentPageFormatName, ", language=", languageName,
				", predefinedFilterName=", predefinedFilterName, ", predefinedFilterValue=", predefinedFilterValue,
				", filterName=", filterName, ", filterValue=", filterValue,
				", tableOrder=", tableOrderName, ", tablePageSize=", tablePageSizeName, ", pageNumber=", pageNumberString,
				", queryUrl=", queryUrl, ", finalResultFileName=", finalResultFileName,
				", firstRegistrationDateTime=", firstRegistrationDateTimeString, ", firstRegistrationAuthUser=", firstRegistrationAuthUserEmail,
				", lastModificationDateTime=", lastModificationDateTimeString, ", lastModificationAuthUser=", lastModificationAuthUserEmail, "]");

		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ Objects.hash(initialPdfDocumentPageFormat, finalPdfDocumentPageFormat, language, predefinedFilterName, predefinedFilterValue, filterName, filterValue, tableOrder, tablePageSize, pageNumber, queryUrl, finalResultFileName);

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if(!super.equals(obj)) {
			return false;
		}
		AuthUserEntityQuery other = (AuthUserEntityQuery) obj;
		return Objects.equals(initialPdfDocumentPageFormat, other.initialPdfDocumentPageFormat) && Objects.equals(finalPdfDocumentPageFormat, other.finalPdfDocumentPageFormat) && Objects.equals(language, other.language)
				&& Objects.equals(predefinedFilterName, other.predefinedFilterName) && Objects.equals(predefinedFilterValue, other.predefinedFilterValue)
				&& Objects.equals(filterName, other.filterName) && Objects.equals(filterValue, other.filterValue)
				&& Objects.equals(tableOrder, other.tableOrder) && Objects.equals(tablePageSize, other.tablePageSize) && Objects.equals(pageNumber, other.pageNumber)
				&& Objects.equals(queryUrl, other.queryUrl) && Objects.equals(finalResultFileName, other.finalResultFileName);
	}
}
