package com.aliuken.jobvacanciesapp.util.persistence.pdf.event;

import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.PdfDocument;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfDocumentPageEventHelper extends PdfPageEventHelper {
	private static final int PDF_TEMPLATE_WIDTH = 100;
	private static final int PDF_TEMPLATE_HEIGHT = 100;
	private static final int FOOTER_FONT_SIZE = 12;
	private static final Font FOOTER_FONT = FontFactory.getFont(BaseFont.HELVETICA, BaseFont.WINANSI, false, FOOTER_FONT_SIZE);
	private static final String MAX_PAGE_NUMBER_ENDING = " / 999";
	private static final float RIGHT_FOOTER_X_OFFSET = FOOTER_FONT.getBaseFont().getWidthPoint(MAX_PAGE_NUMBER_ENDING, FOOTER_FONT_SIZE);

	private static final float DOC_BOTTOM_MARGIN = PdfDocument.cmToPoints(0.5f);

	private final String leftFooter;

	private PdfTemplate totalNumberOfPagesTemplate;

	public PdfDocumentPageEventHelper(final String leftFooter) {
		this.leftFooter = leftFooter;
	}

	@Override
	public void onOpenDocument(final PdfWriter pdfWriter, final Document document) {
		totalNumberOfPagesTemplate = pdfWriter.getDirectContent().createTemplate(PDF_TEMPLATE_WIDTH, PDF_TEMPLATE_HEIGHT);
	}

	@Override
	public void onStartPage(final PdfWriter pdfWriter, final Document document) {

	}

	@Override
	public void onEndPage(final PdfWriter pdfWriter, final Document document) {
		final PdfContentByte footer = pdfWriter.getDirectContent();

		// add left footer
		final Phrase leftFooterPhrase = new Phrase(leftFooter, FOOTER_FONT);
		final float leftFooterXPosition = document.left();
		final float leftFooterYPosition = PdfDocumentPageEventHelper.DOC_BOTTOM_MARGIN;
		final float leftFooterRotation = 0;

		ColumnText.showTextAligned(footer, Element.ALIGN_LEFT, leftFooterPhrase, leftFooterXPosition, leftFooterYPosition, leftFooterRotation);

		// add right footer
		final String currentPageNumber = PdfDocumentPageEventHelper.getCurrentPageNumberAsString(pdfWriter);
		final Phrase rightFooterPhrase = new Phrase(currentPageNumber, FOOTER_FONT);
		final float rightFooterXPosition = document.right() - RIGHT_FOOTER_X_OFFSET;
		final float rightFooterYPosition = PdfDocumentPageEventHelper.DOC_BOTTOM_MARGIN;
		final float rightFooterRotation = 0;

		ColumnText.showTextAligned(footer, Element.ALIGN_RIGHT, rightFooterPhrase, rightFooterXPosition, rightFooterYPosition, rightFooterRotation);

		footer.addTemplate(totalNumberOfPagesTemplate, rightFooterXPosition, rightFooterYPosition);
	}

	@Override
	public void onCloseDocument(final PdfWriter pdfWriter, final Document document) {
		final String currentPageNumber = PdfDocumentPageEventHelper.getCurrentPageNumberAsString(pdfWriter);

		totalNumberOfPagesTemplate.beginText();
		totalNumberOfPagesTemplate.setFontAndSize(FOOTER_FONT.getBaseFont(), FOOTER_FONT_SIZE);
		totalNumberOfPagesTemplate.setTextMatrix(0, 0);
		totalNumberOfPagesTemplate.showText(StringUtils.getStringJoined(" / ", currentPageNumber));
		totalNumberOfPagesTemplate.endText();
	}

	private static String getCurrentPageNumberAsString(final PdfWriter pdfWriter) {
		final int currentPageNumber = pdfWriter.getPageNumber();
		final String result = String.valueOf(currentPageNumber);
		return result;
	}
}
