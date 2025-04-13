package com.aliuken.jobvacanciesapp.enumtype;

import java.io.Serializable;

public enum PdfTableHeaderFieldType implements Serializable {
	URL_FIELD_WITHOUT_CONTENT,
	NOT_URL_FIELD_WITHOUT_CONTENT,
	FIELD_WITH_CONTENT;

	private static final String URL_FIELD_NAME = "URL";

	public static PdfTableHeaderFieldType getPdfTableHeaderFieldType(final boolean isHeaderWithContent, String paragraphBoldText) {
		final PdfTableHeaderFieldType pdfTableHeaderFieldType;
		if(isHeaderWithContent) {
			pdfTableHeaderFieldType = FIELD_WITH_CONTENT;
		} else {
			final boolean isUrlField = URL_FIELD_NAME.equals(paragraphBoldText);
			if(isUrlField) {
				pdfTableHeaderFieldType = URL_FIELD_WITHOUT_CONTENT;
			} else {
				pdfTableHeaderFieldType = NOT_URL_FIELD_WITHOUT_CONTENT;
			}
		}
		return pdfTableHeaderFieldType;
	}
}
