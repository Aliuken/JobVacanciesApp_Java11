package com.aliuken.jobvacanciesapp.util.persistence.pdf.componentbuilder;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
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

public class GenericTableHeaderCellBuilder implements PdfPCellEvent {
	private static final Font FONT = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
	private static final BaseColor LIGHTER_GRAY = new BaseColor(215, 215, 215);
	private static final BaseColor DARKER_GRAY = new BaseColor(160, 160, 160);
	private static final float LINE_WIDTH = 1f;
	private static final float FULL_WIDTH = -1f;
	private static final float HEIGHT = 15;
	private static final float TEXT_PADDING = 5f;
	private static final int ARC_CORNER_RADIUS = 7;

	private final Paragraph paragraph;
	private final BaseColor backgroundColor;
	private final float width;
	private final PdfPCell defaultCell;

	public GenericTableHeaderCellBuilder(String paragraphText, final boolean isHeaderWithContent, final PdfPCell defaultCell) {
		if(isHeaderWithContent) {
			this.paragraph = new Paragraph(paragraphText, FONT);
			this.paragraph.setAlignment(Element.ALIGN_CENTER);
			this.backgroundColor = LIGHTER_GRAY;
			this.width = GenericTableHeaderCellBuilder.getWidth(paragraph);
		} else {
			paragraphText = StringUtils.getStringJoined(Constants.SPACE, paragraphText);
			this.paragraph = new Paragraph(paragraphText, FONT);
			this.paragraph.setAlignment(Element.ALIGN_LEFT);
			this.backgroundColor = DARKER_GRAY;
			this.width = FULL_WIDTH;
		}
		this.defaultCell = defaultCell;
	}

	private static float getWidth(Paragraph paragraph) {
		float width = 0f;
		for(final Element element : paragraph) {
			for(final Chunk chunk : element.getChunks()) {
				float textWidth = chunk.getWidthPoint();
				width = textWidth + 2 * TEXT_PADDING;
				break;
			}
			break;
		}
		return width;
	}

	@Override
	public final void cellLayout(final PdfPCell cell, final Rectangle position, final PdfContentByte[] canvases) {
		final PdfContentByte canvas = canvases[PdfPTable.LINECANVAS];
		canvas.saveState();
		canvas.setColorFill(backgroundColor);
		canvas.setLineWidth(LINE_WIDTH);

		final float x;
		final float width;
		if(this.width != FULL_WIDTH) {
			x = (position.getLeft() + position.getRight() - this.width) / 2;
			width = this.width;
		} else {
			x = position.getLeft();
			width = position.getRight() - position.getLeft();
		}

		final float y = position.getTop() - HEIGHT - TEXT_PADDING + LINE_WIDTH;
		canvas.roundRectangle(x, y, width, HEIGHT, ARC_CORNER_RADIUS);

		canvas.fillStroke();
		canvas.restoreState();
	}

	public PdfPCell build() {
		final PdfPCell cell = new PdfPCell(defaultCell);
		cell.addElement(paragraph);
		cell.setCellEvent(this);
		cell.setFixedHeight(HEIGHT + TEXT_PADDING);
		cell.setPaddingTop(TEXT_PADDING / 2);
		cell.setBorder(0);
		return cell;
	}
}
