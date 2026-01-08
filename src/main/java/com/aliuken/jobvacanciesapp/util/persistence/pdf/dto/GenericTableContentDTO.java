package com.aliuken.jobvacanciesapp.util.persistence.pdf.dto;

import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import lombok.Data;

import java.io.Serializable;

@Data
public class GenericTableContentDTO implements Serializable {
	private static final long serialVersionUID = 7050201715198718369L;

	private final int cellHorizontalAlignment;
	private final Font cellFont;
	private final String[][] contentArray;
	private final boolean alternateRowColor;
	private final boolean drawBorders;
}
