package com.aliuken.jobvacanciesapp.util.persistence.pdf.componentbuilder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;

public class GenericTableBuilder implements PdfPTableEvent {
	private static final BaseColor BORDER_COLOR = BaseColor.GRAY;
	private static final BaseColor ALTERNATIVE_ROW_COLOR = new BaseColor(242, 242, 242);

	private final String[] columnNames;
	private final float[] columnWidths;
	private final int cellHorizontalAlignment;
	private final Font cellFont;
	private final String[][] contentArray;
	private final BaseColor firstRowColor;
	private final BaseColor secondRowColor;
	private final boolean drawBorders;

	public GenericTableBuilder(final String[] columnNames, final float[] columnWidths, final int cellHorizontalAlignment,
			final Font cellFont, final String[][] contentArray, final boolean alternateRowColor, final boolean drawBorders) throws DocumentException {
		this.columnNames = columnNames;
		this.columnWidths = columnWidths;
		this.cellHorizontalAlignment = cellHorizontalAlignment;
		this.cellFont = cellFont;
		this.cellFont.setColor(new BaseColor(2, 2, 2));
		this.contentArray = contentArray;

		if(alternateRowColor) {
			this.firstRowColor = BaseColor.WHITE;
			this.secondRowColor = ALTERNATIVE_ROW_COLOR;
		} else {
			this.firstRowColor = BaseColor.WHITE;
			this.secondRowColor = BaseColor.WHITE;
		}

		this.drawBorders = drawBorders;
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

			// draw external rectangle
			canvas.rectangle(x1, y1, x2 - x1, y2 - y1);

			// draw column line separators
			for(int i = 1; i < headerWidths.length - 1; i++) {
				canvas.moveTo(headerWidths[i], y1);
				canvas.lineTo(headerWidths[i], y2);
			}

			// draw row line separators
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

		final boolean isHeaderWithContent = this.contentArray != null;

		GenericTableBuilder.addHeaderRow(fullTable, columnNames, isHeaderWithContent);

		final String[][] contentArray;
		if(isHeaderWithContent) {
			contentArray = this.contentArray;
		} else {
			contentArray = new String[][]{{""}};
		}

		for(int rowIndex = 0; rowIndex < contentArray.length; rowIndex++) {
			final String[] row = contentArray[rowIndex];

			final BaseColor backgroundColor = (rowIndex % 2 == 0) ? firstRowColor : secondRowColor;

			final GenericTableRowCellBuilder genericTableRowCellBuilder = new GenericTableRowCellBuilder(fullTable, row,
					backgroundColor, cellHorizontalAlignment, cellFont);

			genericTableRowCellBuilder.build();
		}

		return fullTable;
	}

	private static void addHeaderRow(final PdfPTable pdfPTable, final String[] columnNames, final boolean isHeaderWithContent) {
		if(columnNames != null) {
			pdfPTable.setHeaderRows(1);

			final PdfPCell defaultCell = pdfPTable.getDefaultCell();
			for(final String columnName : columnNames) {
				final GenericTableHeaderCellBuilder genericTableHeaderCellBuilder = new GenericTableHeaderCellBuilder(columnName, isHeaderWithContent, defaultCell);
				final PdfPCell genericTableHeaderCell = genericTableHeaderCellBuilder.build();
				pdfPTable.addCell(genericTableHeaderCell);
			}
		} else {
			pdfPTable.setHeaderRows(0);
		}
	}
}
