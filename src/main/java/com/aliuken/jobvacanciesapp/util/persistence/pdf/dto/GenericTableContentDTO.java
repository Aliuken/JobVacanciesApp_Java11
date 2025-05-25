package com.aliuken.jobvacanciesapp.util.persistence.pdf.dto;

import java.io.Serializable;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;

import lombok.Data;

@Data
public class GenericTableContentDTO implements Serializable {
	private static final long serialVersionUID = 7050201715198718369L;

	private static final GenericTableContentDTO NO_ARGS_INSTANCE = new GenericTableContentDTO(Element.ALIGN_LEFT, null, null, false, false);

	private final int cellHorizontalAlignment;
	private final Font cellFont;
	private final String[][] contentArray;
	private final boolean alternateRowColor;
	private final boolean drawBorders;

	public static GenericTableContentDTO getNewInstance() {
		return NO_ARGS_INSTANCE;
	}
}
