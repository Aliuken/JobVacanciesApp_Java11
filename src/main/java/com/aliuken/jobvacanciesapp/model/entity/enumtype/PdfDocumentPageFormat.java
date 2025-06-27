package com.aliuken.jobvacanciesapp.model.entity.enumtype;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.config.ConfigPropertiesBean;
import com.aliuken.jobvacanciesapp.enumtype.superinterface.ConfigurableEnum;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;

public enum PdfDocumentPageFormat implements ConfigurableEnum<PdfDocumentPageFormat> {
	BY_DEFAULT   ("---", "pdfDocumentPageFormat.byDefault",    null),
	A3_VERTICAL  ("A3V", "pdfDocumentPageFormat.verticalA3",   PageSize.A3),
	A3_HORIZONTAL("A3H", "pdfDocumentPageFormat.horizontalA3", PageSize.A3.rotate()),
	A4_VERTICAL  ("A4V", "pdfDocumentPageFormat.verticalA4",   PageSize.A4),
	A4_HORIZONTAL("A4H", "pdfDocumentPageFormat.horizontalA4", PageSize.A4.rotate());

	private final String code;
	private final String messageName;
	private final Rectangle rectangle;

	private PdfDocumentPageFormat(final String code, final String messageName, final Rectangle rectangle) {
		this.code = code;
		this.messageName = messageName;
		this.rectangle = rectangle;
	}

	public String getCode() {
		return code;
	}

	@Override
	public String getMessageName() {
		return messageName;
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public static PdfDocumentPageFormat findByCode(final String code) {
		final PdfDocumentPageFormat pdfDocumentPageFormat;
		if(code != null) {
			pdfDocumentPageFormat = Constants.PARALLEL_STREAM_UTILS.ofEnum(PdfDocumentPageFormat.class)
				.filter(pdfDocumentPageFormatAux -> code.equals(pdfDocumentPageFormatAux.code))
				.findFirst()
				.orElse(null);
		} else {
			pdfDocumentPageFormat = null;
		}

		return pdfDocumentPageFormat;
	}

	@Override
	public ConfigurableEnum<PdfDocumentPageFormat> getOverwrittenEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final PdfDocumentPageFormat pdfDocumentPageFormat = configPropertiesBean.getDefaultPdfDocumentPageFormatOverwritten();
		return pdfDocumentPageFormat;
	}


	@Override
	public ConfigurableEnum<PdfDocumentPageFormat> getOverwritableEnumElement(ConfigPropertiesBean configPropertiesBean) {
		final PdfDocumentPageFormat pdfDocumentPageFormat = configPropertiesBean.getDefaultPdfDocumentPageFormat();
		return pdfDocumentPageFormat;
	}

	@Override
	public ConfigurableEnum<PdfDocumentPageFormat> getFinalDefaultEnumElement() {
		return PdfDocumentPageFormat.A4_VERTICAL;
	}
}
