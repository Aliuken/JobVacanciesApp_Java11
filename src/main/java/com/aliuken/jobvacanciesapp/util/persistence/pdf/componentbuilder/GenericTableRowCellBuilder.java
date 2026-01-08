package com.aliuken.jobvacanciesapp.util.persistence.pdf.componentbuilder;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.util.StyleApplier;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

import java.util.List;

public class GenericTableRowCellBuilder implements PdfPCellEvent {
	private static final int CELL_VERTICAL_ALIGNMENT = Element.ALIGN_MIDDLE;
	private static final float CELL_HEIGHT = 17f;

	private final PdfPTable fullTable;
	private final PdfPCell fullTableDefaultCell;
	private final String[] row;
	private final BaseColor backgroundColor;
	private final int cellHorizontalAlignment;
	private final Font cellFont;

	public GenericTableRowCellBuilder(final PdfPTable fullTable, final String[] row, final BaseColor backgroundColor,
			final int cellHorizontalAlignment, final Font cellFont) {
		this.fullTable = fullTable;
		this.fullTableDefaultCell = fullTable.getDefaultCell();
		this.row = row;
		this.backgroundColor = backgroundColor;
		this.cellHorizontalAlignment = cellHorizontalAlignment;
		this.cellFont = cellFont;
	}

	@Override
	public final void cellLayout(final PdfPCell cell, final Rectangle position, final PdfContentByte[] canvases) {
		final float x1 = position.getLeft();
		final float x2 = position.getRight();
		final float y1 = position.getTop();
		final float y2 = position.getBottom();

		final PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
		canvas.saveState();
		canvas.setColorFill(backgroundColor);
		canvas.setColorStroke(backgroundColor);
		canvas.rectangle(x1, y1, x2 - x1, y2 - y1);
		canvas.fillStroke();
		canvas.restoreState();
	}

	public PdfPTable build() {
		final int maxNumberOfLinesInRow = GenericTableRowCellBuilder.getMaxNumberOfLinesInRow(row);

		for(final String cellContent : row) {
			final PdfPTable innerTable = GenericTableRowCellBuilder.getCellTable(fullTableDefaultCell, maxNumberOfLinesInRow,
					this, cellContent, cellHorizontalAlignment, cellFont);
			fullTable.addCell(innerTable);
		}
		return fullTable;
	}

	private static int getMaxNumberOfLinesInRow(final String[] row) {
		int maxNumberOfLinesInRow = 0;
		for(final String cellContent : row) {
			if(cellContent != null) {
				final String[] cellTextLines = cellContent.split(Constants.NEWLINE);
				final int numberOfCellTextLines = cellTextLines.length;
				if (numberOfCellTextLines > maxNumberOfLinesInRow) {
					maxNumberOfLinesInRow = numberOfCellTextLines;
				}
			}
		}
		return maxNumberOfLinesInRow;
	}

	private static PdfPTable getCellTable(final PdfPCell defaultCell, final int maxNumberOfLinesInRow, final PdfPCellEvent cellEvent,
			final String cellContent, final int cellHorizontalAlignment, final Font cellFont) {

		final String[] cellTextLines = cellContent != null ? cellContent.split(Constants.NEWLINE) : new String[0];

		final PdfPTable innerTable = new PdfPTable(1);
		innerTable.setWidthPercentage(100f);

		final PdfPCell innerTableDefaultCell = innerTable.getDefaultCell();
		innerTableDefaultCell.setBorderWidth(0f);
		innerTableDefaultCell.setPadding(0f);
		innerTableDefaultCell.setUseAscender(true);
		innerTableDefaultCell.setUseDescender(true);
		innerTableDefaultCell.setVerticalAlignment(Element.ALIGN_TOP);
		innerTableDefaultCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		innerTableDefaultCell.setFixedHeight(maxNumberOfLinesInRow * CELL_HEIGHT);

		for(int lineIndex = 0; lineIndex < maxNumberOfLinesInRow; lineIndex++) {
			final String cellTextLine;
			if(lineIndex < cellTextLines.length) {
				cellTextLine = cellTextLines[lineIndex];
			} else {
				cellTextLine = Constants.SPACE;
			}

			final PdfPCell cell = GenericTableRowCellBuilder.getCellTableLine(defaultCell, cellTextLine, cellEvent,
					cellHorizontalAlignment, CELL_VERTICAL_ALIGNMENT, CELL_HEIGHT, cellFont);

			innerTable.addCell(cell);
		}

		return innerTable;
	}

	private static PdfPCell getCellTableLine(final PdfPCell defaultCell, final String content, final PdfPCellEvent cellEvent,
			final int horizontalAlignment, final int verticalAlignment, final float height, final Font font) {

		final List<Chunk> chunks = StyleApplier.applyStyles(font, content);

		final Paragraph paragraph = new Paragraph();
		paragraph.setAlignment(horizontalAlignment);
		for(final Chunk chunk : chunks) {
			paragraph.add(chunk);
		}

		final PdfPCell cell = new PdfPCell(defaultCell);
		cell.addElement(paragraph);
		cell.setPadding(1f);
		cell.setPaddingRight(2f);
		cell.setPaddingLeft(2f);
		cell.setVerticalAlignment(verticalAlignment);
		cell.setCellEvent(cellEvent);
		cell.setFixedHeight(height);
		return cell;
	}
}
