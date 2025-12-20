package com.aliuken.jobvacanciesapp.util.persistence.pdf.componentbuilder;

import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.javase.LogicalUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.dto.GenericTableContentDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;

public class GenericTableBuilder implements PdfPTableEvent {
	private static final float[] NO_HEADER_WIDTH = new float[]{1f};
	private static final BaseColor BORDER_COLOR = BaseColor.GRAY;
	private static final BaseColor ALTERNATIVE_ROW_COLOR = new BaseColor(242, 242, 242);

	//Header fields
	private final Language queryLanguage;
	private final String[] columnNames;
	private final String[] columnValues;
	private final float[] columnWidths;

	//Content fields
	private final boolean isHeaderWithContent;
	private final int cellHorizontalAlignment;
	private final Font cellFont;
	private final String[][] contentArray;
	private final BaseColor firstRowColor;
	private final BaseColor secondRowColor;
	private final boolean drawBorders;

	//Single-text header constructor
	public GenericTableBuilder(final Language queryLanguage, final String columnName, final String columnValue, final float columnWidth) throws DocumentException {
		this(queryLanguage, new String[]{columnName}, new String[]{columnValue}, new float[]{columnWidth}, null);
	}

	//Single-text content constructor
	public GenericTableBuilder(final Language queryLanguage, final Font cellFont, final String contentArrayValue) throws DocumentException {
		this(queryLanguage, null, null, NO_HEADER_WIDTH, new GenericTableContentDTO(Element.ALIGN_CENTER, cellFont, new String[][]{{contentArrayValue}}, false, false));
	}

	public GenericTableBuilder(final Language queryLanguage, final String[] columnNames, final String[] columnValues, final float[] columnWidths, final GenericTableContentDTO genericTableContentDTO) throws DocumentException {
		this.queryLanguage = queryLanguage;

		if(columnNames != null) {
			this.columnNames = columnNames;
		} else {
			this.columnNames = new String[0];
		}

		if(columnValues != null) {
			this.columnValues = columnValues;
		} else {
			this.columnValues = new String[this.columnNames.length];
		}

		this.columnWidths = columnWidths;

		this.isHeaderWithContent = genericTableContentDTO != null;

		if(this.isHeaderWithContent) {
			this.cellHorizontalAlignment = genericTableContentDTO.getCellHorizontalAlignment();
			this.cellFont = genericTableContentDTO.getCellFont();
			this.cellFont.setColor(new BaseColor(2, 2, 2));
			this.contentArray = genericTableContentDTO.getContentArray();

			if(genericTableContentDTO.isAlternateRowColor()) {
				this.firstRowColor = BaseColor.WHITE;
				this.secondRowColor = ALTERNATIVE_ROW_COLOR;
			} else {
				this.firstRowColor = BaseColor.WHITE;
				this.secondRowColor = BaseColor.WHITE;
			}

			this.drawBorders = genericTableContentDTO.isDrawBorders();
		} else {
			this.cellHorizontalAlignment = Element.ALIGN_LEFT;
			this.cellFont = null;
			this.contentArray = null;
			this.firstRowColor = null;
			this.secondRowColor = null;
			this.drawBorders = false;
		}
	}

	@Override
	public void tableLayout(final PdfPTable table, final float[][] widths, final float[] heights, final int headerRows, final int rowStart, final PdfContentByte[] canvases) {
		final PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
		canvas.saveState();

		if(drawBorders) {
			final float[] headerWidths = widths[0];
			final float x1 = headerWidths[0];
			final float x2 = headerWidths[headerWidths.length - 1];
			final float y1 = heights[headerRows];
			final float y2 = heights[heights.length - 1];

			canvas.setColorStroke(BORDER_COLOR);

			//Draw external rectangle
			canvas.rectangle(x1, y1, x2 - x1, y2 - y1);

			//Draw column line separators
			for(int i = 1; i < headerWidths.length - 1; i++) {
				canvas.moveTo(headerWidths[i], y1);
				canvas.lineTo(headerWidths[i], y2);
			}

			//Draw row line separators
			for(int i = 1; i < heights.length - 1; i++) {
				canvas.moveTo(x1, heights[i]);
				canvas.lineTo(x2, heights[i]);
			}
		}

		canvas.stroke();
		canvas.restoreState();
	}

	public PdfPTable build() throws DocumentException {
		final PdfPTable fullTable = new PdfPTable(columnWidths.length);
		fullTable.setWidthPercentage(100);
		fullTable.setWidths(columnWidths);
		fullTable.setTableEvent(this);

		final PdfPCell fullTableDefaultCell = fullTable.getDefaultCell();
		fullTableDefaultCell.setBorderWidth(0f);
		fullTableDefaultCell.setPadding(0f);
		fullTableDefaultCell.setUseAscender(true);
		fullTableDefaultCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		fullTableDefaultCell.setHorizontalAlignment(Element.ALIGN_CENTER);

		this.addHeaderRow(fullTable);
		this.addContentRows(fullTable);

		return fullTable;
	}

	private void addHeaderRow(final PdfPTable pdfPTable) {
		if(LogicalUtils.isNotNullNorEmpty(columnNames)) {
			pdfPTable.setHeaderRows(1);

			final PdfPCell defaultCell = pdfPTable.getDefaultCell();
			for(int i = 0; i < columnNames.length; i++) {
				final String columnName = columnNames[i];
				final String columnValue = columnValues[i];
				final GenericTableHeaderCellBuilder genericTableHeaderCellBuilder = new GenericTableHeaderCellBuilder(queryLanguage, columnName, columnValue, isHeaderWithContent, defaultCell);
				final PdfPCell genericTableHeaderCell = genericTableHeaderCellBuilder.build();
				pdfPTable.addCell(genericTableHeaderCell);
			}
		} else {
			pdfPTable.setHeaderRows(0);
		}
	}

	private void addContentRows(final PdfPTable pdfPTable) {
		if(isHeaderWithContent) {
			for(int rowIndex = 0; rowIndex < contentArray.length; rowIndex++) {
				final String[] row = contentArray[rowIndex];

				final BaseColor backgroundColor = (rowIndex % 2 == 0) ? firstRowColor : secondRowColor;

				final GenericTableRowCellBuilder genericTableRowCellBuilder = new GenericTableRowCellBuilder(pdfPTable, row,
						backgroundColor, cellHorizontalAlignment, cellFont);

				genericTableRowCellBuilder.build();
			}
		} else {
			final PdfPTable innerTable = new PdfPTable(1);
			pdfPTable.addCell(innerTable);
		}
	}
}
