package com.aliuken.jobvacanciesapp.util.persistence.pdf;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.PdfDocumentPageFormat;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.componentbuilder.SearchCriteriaTableBuilder;
import com.aliuken.jobvacanciesapp.util.persistence.pdf.event.PdfDocumentPageEventHelper;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class PdfDocument extends Document implements AutoCloseable {
	private static final float POINTS = 72f;
	private static final float CENTIMETER = 2.54f;
	private static final float POINTS_PER_CENTIMETER = POINTS / CENTIMETER;

	private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
	private static final Font BOLD_FONT = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);

	private static final String DEFAULT_CREATOR = "Aliuken Team";
	private static final String DEFAULT_KEYWORDS = "JobVacanciesApp";
	private static final String DEFAULT_SUBJECT = "JobVacanciesApp Query Report";

	private static final float DOC_LEFT_MARGIN = PdfDocument.cmToPoints(0.5f);
	private static final float DOC_RIGHT_MARGIN = PdfDocument.cmToPoints(0.5f);
	private static final float DOC_TOP_MARGIN = PdfDocument.cmToPoints(0.5f);
	private static final float DOC_BOTTOM_MARGIN = PdfDocument.cmToPoints(1f);

	private final PdfDocumentPageFormat initialPdfDocumentPageFormat;
	private final PdfDocumentPageFormat finalPdfDocumentPageFormat;
	private final PdfWriter pdfWriter;

	public PdfDocument(final PdfDocumentPageFormat initialPdfDocumentPageFormat, final PdfDocumentPageFormat finalPdfDocumentPageFormat, final ByteArrayOutputStream byteArrayOutputStream) throws DocumentException, IOException {
		this.initialPdfDocumentPageFormat = initialPdfDocumentPageFormat;
		this.finalPdfDocumentPageFormat = finalPdfDocumentPageFormat;
		this.pdfWriter = this.init(byteArrayOutputStream);
	}

	private PdfWriter init(final OutputStream outputStream) throws DocumentException {
		this.initDocumentAttributes();

		final PdfWriter pdfWriter = PdfWriter.getInstance(this, outputStream);
		return pdfWriter;
	}

	private void initDocumentAttributes() {
		final Rectangle rectangle = finalPdfDocumentPageFormat.getRectangle();
		this.setPageSize(rectangle);

		this.addTitle(DEFAULT_SUBJECT);
		this.addSubject(DEFAULT_SUBJECT);
		this.addAuthor(DEFAULT_CREATOR);
		this.addCreator(DEFAULT_CREATOR);
		this.addKeywords(DEFAULT_KEYWORDS);
		this.setMargins(DOC_LEFT_MARGIN, DOC_RIGHT_MARGIN, DOC_TOP_MARGIN, DOC_BOTTOM_MARGIN);
	}

	protected void addPageEventHelper() {
		final String leftFooter = this.getLeftFooter();
		final PdfDocumentPageEventHelper pageEventHelper = new PdfDocumentPageEventHelper(leftFooter);
		pdfWriter.setPageEvent(pageEventHelper);
	}

	protected abstract String getLeftFooter();

	protected PdfDocumentPageFormat getInitialPdfDocumentPageFormat() {
		return initialPdfDocumentPageFormat;
	}

	protected PdfDocumentPageFormat getFinalPdfDocumentPageFormat() {
		return finalPdfDocumentPageFormat;
	}

	protected PdfWriter getPdfWriter() {
		return pdfWriter;
	}

	@Override
	public void open() {
		if(!isOpen()) {
			super.open();
		}
	}

	@Override
	public void close() {
		if(isOpen()) {
			pdfWriter.flush();
			pdfWriter.close();
			super.close();
		}
	}

	public static float cmToPoints(final float centimeters) {
		final float points = centimeters * POINTS_PER_CENTIMETER;
		return points;
	}

	protected static void addCellWithPhrase(final List<PdfPCell> pdfPCellList, final String fieldName, final String fieldValue) {
		final Phrase phrase;
		if(fieldName != null && !fieldName.isEmpty()) {
			phrase = new Phrase();
			phrase.add(new Phrase(StringUtils.getStringJoined(fieldName, Constants.FIELD_NAME_VALUE_SEPARATOR), PdfDocument.BOLD_FONT));
			phrase.add(new Phrase(fieldValue, PdfDocument.NORMAL_FONT));
		} else {
			phrase = new Phrase(Constants.SPACE, PdfDocument.NORMAL_FONT);
		}

		final PdfPCell cell = new PdfPCell(phrase);
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setVerticalAlignment(PdfPTable.ALIGN_TOP);

		pdfPCellList.add(cell);
	}

	protected static PdfPTable createSearchCriteriaTable(String title, List<PdfPCell> contents) throws DocumentException {
		final List<Element> elementList = PdfDocument.getElementList(contents);

		final SearchCriteriaTableBuilder searchCriteriaTableBuilder = new SearchCriteriaTableBuilder(title, elementList);

		final PdfPTable searchCriteriaTable = searchCriteriaTableBuilder.build();
		searchCriteriaTable.setWidthPercentage(100);
		searchCriteriaTable.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		searchCriteriaTable.getDefaultCell().setVerticalAlignment(PdfPTable.ALIGN_TOP);

		return searchCriteriaTable;
	}

	private static List<Element> getElementList(List<PdfPCell> contents) {
		final PdfPTable pdfTable = new PdfPTable(1);
		pdfTable.setWidthPercentage(100);

		final PdfPCell defaultCell = pdfTable.getDefaultCell();
		defaultCell.setBorder(PdfPCell.NO_BORDER);
		defaultCell.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
		defaultCell.setVerticalAlignment(PdfPTable.ALIGN_TOP);

		for(final PdfPCell content : contents) {
			content.setBorder(PdfPCell.NO_BORDER);
			content.setVerticalAlignment(PdfPTable.ALIGN_TOP);
			pdfTable.addCell(content);
		}

		final List<Element> listElems = new ArrayList<Element>();
		listElems.add(pdfTable);

		return listElems;
	}
}
