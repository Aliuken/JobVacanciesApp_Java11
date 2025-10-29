package com.aliuken.jobvacanciesapp.model.converter;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply=true)
public class PdfDocumentPageFormatConverter implements AttributeConverter<PdfDocumentPageFormat, String> {

	@Override
	public String convertToDatabaseColumn(final PdfDocumentPageFormat pdfDocumentPageFormat) {
		if(pdfDocumentPageFormat == null) {
			return PdfDocumentPageFormat.BY_DEFAULT.getCode();
		}

		final String pdfDocumentPageFormatCode = pdfDocumentPageFormat.getCode();
		return pdfDocumentPageFormatCode;
	}

	@Override
	public PdfDocumentPageFormat convertToEntityAttribute(final String pdfDocumentPageFormatCode) {
		final PdfDocumentPageFormat pdfDocumentPageFormat;
		if(pdfDocumentPageFormatCode != null) {
			pdfDocumentPageFormat = PdfDocumentPageFormat.findByCode(pdfDocumentPageFormatCode);
		} else {
			pdfDocumentPageFormat = PdfDocumentPageFormat.BY_DEFAULT;
		}

		return pdfDocumentPageFormat;
	}
}