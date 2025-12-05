package com.aliuken.jobvacanciesapp.util.persistence.pdf.componentbuilder;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPTableEvent;

import java.util.List;

public class SearchCriteriaTableBuilder implements PdfPTableEvent {
	private static final float TITLE_PADDING = 5f;
	private static final int ARC_CORNER_RADIUS = 7;

	private static final int TITLE_FONT_SIZE = 10;
	private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED, TITLE_FONT_SIZE, Font.BOLD, BaseColor.WHITE);
	private static final BaseFont TITLE_BASE_FONT = TITLE_FONT.getBaseFont();

	private final String title;
	private final List<Element> contentList;

	public SearchCriteriaTableBuilder(final String title, final List<Element> contentList) throws DocumentException {
		this.title = title;
		this.contentList = contentList;
	}

	@Override
	public void tableLayout(final PdfPTable table, final float[][] widths, final float[] heights, final int headerRows, final int rowStart, final PdfContentByte[] canvases) {
		final float[] headerWidths = widths[0];
		final PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
		this.drawTableBorder(canvas, headerWidths, heights);
		this.drawTab(canvas, headerWidths, heights);
	}

	private void drawTableBorder(final PdfContentByte canvas, final float[] headerWidths, final float[] heights) {
		final float coordX1 = headerWidths[0];
		final float coordX2 = headerWidths[headerWidths.length - 1];
		final float coordY1 = heights[1];
		final float coordY2 = heights[heights.length - 1];

		canvas.saveState();
		canvas.setColorStroke(BaseColor.BLACK);
		canvas.setLineWidth(1f);

		// draw left border
		canvas.moveTo(coordX1, coordY1 + ARC_CORNER_RADIUS);
		canvas.lineTo(coordX1, coordY2);

		// draw bottom border
		canvas.lineTo(coordX2, coordY2);

		// draw right border
		canvas.lineTo(coordX2, coordY1);

		// draw top border
		canvas.lineTo(coordX1 + ARC_CORNER_RADIUS, coordY1);

		canvas.stroke();
		canvas.restoreState();
	}

	private void drawTab(final PdfContentByte canvas, final float[] headerWidths, final float[] heights) {
		final float coordX1 = headerWidths[0];
		final float coordY1 = heights[0];
		final float coordY2 = heights[1];

		canvas.saveState();
		canvas.setColorStroke(BaseColor.BLACK);
		canvas.setColorFill(BaseColor.BLACK);

		final float tabWidth = SearchCriteriaTableBuilder.getTabWitdh(title);
		canvas.roundRectangle(coordX1, coordY1, tabWidth, coordY2 - coordY1, ARC_CORNER_RADIUS);

		canvas.fillStroke();
		canvas.restoreState();
	}

	private static float getTabWitdh(String title) {
		final float titleWidth = TITLE_BASE_FONT.getWidthPoint(title, TITLE_FONT_SIZE);
		final float tabWidth = titleWidth + 2 * TITLE_PADDING;
		return tabWidth;
	}

	public PdfPTable build() {
		final PdfPTable table = new PdfPTable(1);
		table.setTableEvent(this);
		table.setHeaderRows(1);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.getDefaultCell().setPadding(TITLE_PADDING);
		table.getDefaultCell().setUseAscender(true);
		table.getDefaultCell().setVerticalAlignment(Element.ALIGN_MIDDLE);

		final PdfPCell tabCell = new PdfPCell(table.getDefaultCell());
		final Chunk tabChunk = new Chunk(title, TITLE_FONT);
		tabCell.addElement(tabChunk);
		tabCell.setNoWrap(true);
		table.addCell(tabCell);

		for(final Element content : contentList) {
			final PdfPCell contentCell = new PdfPCell(table.getDefaultCell());
			contentCell.addElement(content);
			table.addCell(contentCell);
		}

		table.setSplitLate(false);
		table.setSplitRows(true);
		return table;
	}
}
