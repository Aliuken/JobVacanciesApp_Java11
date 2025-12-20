package com.aliuken.jobvacanciesapp.util.persistence.pdf.componentbuilder;

import com.aliuken.jobvacanciesapp.Constants;
import com.aliuken.jobvacanciesapp.enumtype.PdfTableHeaderFieldType;
import com.aliuken.jobvacanciesapp.model.entity.enumtype.Language;
import com.aliuken.jobvacanciesapp.util.i18n.I18nUtils;
import com.aliuken.jobvacanciesapp.util.javase.StringUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;

import java.net.URI;
import java.net.URISyntaxException;

public class GenericTableHeaderCellBuilder implements PdfPCellEvent {
	private static final Font NORMAL_FONT = new Font(FontFamily.HELVETICA, 7, Font.NORMAL, BaseColor.BLACK);
	private static final Font BOLD_FONT = new Font(FontFamily.HELVETICA, 7, Font.BOLD, BaseColor.BLACK);
	private static final BaseColor LIGHTER_GRAY = new BaseColor(215, 215, 215);
	private static final BaseColor DARKER_GRAY = new BaseColor(160, 160, 160);
	private static final float LINE_WIDTH = 1f;
	private static final float FULL_WIDTH = -1f;
	private static final float TEXT_PADDING = 5f;
	private static final int ARC_CORNER_RADIUS = 7;
	private static final Phrase NEWLINE_PHRASE = GenericTableHeaderCellBuilder.getNewLinePhrase();

	private final Paragraph paragraph;
	private final BaseColor backgroundColor;
	private final float width;
	private final float height;
	private final PdfPCell defaultCell;

	public GenericTableHeaderCellBuilder(final Language queryLanguage, final String paragraphBoldText, final String paragraphNormalText, final boolean isHeaderWithContent, final PdfPCell defaultCell) {
		final PdfTableHeaderFieldType pdfTableHeaderFieldType = PdfTableHeaderFieldType.getPdfTableHeaderFieldType(isHeaderWithContent, paragraphBoldText);
		this.paragraph = GenericTableHeaderCellBuilder.getParagraph(pdfTableHeaderFieldType, queryLanguage, paragraphBoldText, paragraphNormalText);

		switch(pdfTableHeaderFieldType) {
			case URL_FIELD_WITHOUT_CONTENT: {
				this.paragraph.setAlignment(Element.ALIGN_LEFT);
				this.backgroundColor = DARKER_GRAY;
				this.width = FULL_WIDTH;
				this.height = 45;
				break;
			}
			case NOT_URL_FIELD_WITHOUT_CONTENT: {
				this.paragraph.setAlignment(Element.ALIGN_LEFT);
				this.backgroundColor = DARKER_GRAY;
				this.width = FULL_WIDTH;
				this.height = 15;
				break;
			}
			case FIELD_WITH_CONTENT: {
				this.paragraph.setAlignment(Element.ALIGN_CENTER);
				this.backgroundColor = LIGHTER_GRAY;
				this.width = GenericTableHeaderCellBuilder.getWidth(paragraph);
				this.height = 15;
				break;
			}
			default: {
				throw new IllegalArgumentException(StringUtils.getStringJoined("PdfTableHeaderFieldType '", pdfTableHeaderFieldType.name(), "' not supported"));
			}
		}

		this.defaultCell = defaultCell;
	}

	private static Paragraph getParagraph(final PdfTableHeaderFieldType pdfTableHeaderFieldType, final Language queryLanguage, String paragraphBoldText, final String paragraphNormalText) {
		final Paragraph paragraph = new Paragraph();
		switch(pdfTableHeaderFieldType) {
			case URL_FIELD_WITHOUT_CONTENT: {
				String queryUrlBeginning;
				String queryUrlParams;
				String queryUrl;
				try {
					final URI queryUri = new URI(paragraphNormalText);
					queryUrlBeginning = StringUtils.getUrlWithoutParametersAndFragment(queryUri);
					queryUrlParams = StringUtils.getUrlParameters(queryUri);
					queryUrl = StringUtils.getStringJoined(queryUrlBeginning, queryUrlParams);
				} catch (final URISyntaxException e) {
					queryUrlBeginning = null;
					queryUrlParams = null;
					queryUrl = null;
				}

				final Phrase urlLinkPhraseTitle = GenericTableHeaderCellBuilder.getUrlPhraseTitle(queryLanguage, "queryReport.urlLink");
				paragraph.add(urlLinkPhraseTitle);
				final Phrase urlLinkPhraseValue = GenericTableHeaderCellBuilder.getUrlPhraseValue("[ LINK ]", queryUrl);
				paragraph.add(urlLinkPhraseValue);

				paragraph.add(NEWLINE_PHRASE);

				final Phrase urlBeginningPhraseTitle = GenericTableHeaderCellBuilder.getUrlPhraseTitle(queryLanguage, "queryReport.urlBeginning");
				paragraph.add(urlBeginningPhraseTitle);
				final Phrase urlBeginningPhraseValue = GenericTableHeaderCellBuilder.getUrlPhraseValue(queryUrlBeginning, null);
				paragraph.add(urlBeginningPhraseValue);

				paragraph.add(NEWLINE_PHRASE);

				final Phrase urlParamsPhraseTitle = GenericTableHeaderCellBuilder.getUrlPhraseTitle(queryLanguage, "queryReport.urlParams");
				paragraph.add(urlParamsPhraseTitle);
				final Phrase urlParamsPhraseValue = GenericTableHeaderCellBuilder.getUrlPhraseValue(queryUrlParams, null);
				paragraph.add(urlParamsPhraseValue);
				break;
			}
			case NOT_URL_FIELD_WITHOUT_CONTENT: {
				final String titlePhraseText = StringUtils.getStringJoined(Constants.SPACE, paragraphBoldText, Constants.FIELD_NAME_VALUE_SEPARATOR);
				final Phrase titlePhrase = new Phrase(titlePhraseText, BOLD_FONT);
				paragraph.add(titlePhrase);
				final Phrase valuePhrase = new Phrase(paragraphNormalText, NORMAL_FONT);
				paragraph.add(valuePhrase);
				break;
			}
			case FIELD_WITH_CONTENT: {
				final Phrase titlePhrase = new Phrase(paragraphBoldText, BOLD_FONT);
				paragraph.add(titlePhrase);
				break;
			}
			default: {
				throw new IllegalArgumentException(StringUtils.getStringJoined("PdfTableHeaderFieldType '", pdfTableHeaderFieldType.name(), "' not supported"));
			}
		}
		return paragraph;
	}

	private static Phrase getUrlPhraseTitle(final Language queryLanguage, final String titleMessageName) {
		final String title = I18nUtils.getInternationalizedMessage(queryLanguage, titleMessageName, null);
		final String phraseTitle = StringUtils.getStringJoined(Constants.SPACE, title, Constants.FIELD_NAME_VALUE_SEPARATOR);
		final Phrase phrase = new Phrase(phraseTitle, BOLD_FONT);
		return phrase;
	}

	private static Phrase getUrlPhraseValue(final String valueContent, final String valueAnchor) {
		final Phrase phrase = new Phrase();
		final Chunk chunk = new Chunk(valueContent, NORMAL_FONT);
		if(valueAnchor != null) {
			chunk.setAnchor(valueAnchor);
		}
		phrase.add(chunk);
		return phrase;
	}

	private static Phrase getNewLinePhrase() {
		final Phrase phrase = new Phrase();
		phrase.add(Chunk.NEWLINE);
		return phrase;
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

		final float y = position.getTop() - height - TEXT_PADDING + LINE_WIDTH;
		canvas.roundRectangle(x, y, width, height, ARC_CORNER_RADIUS);

		canvas.fillStroke();
		canvas.restoreState();
	}

	public PdfPCell build() {
		final PdfPCell cell = new PdfPCell(defaultCell);
		cell.addElement(paragraph);
		cell.setCellEvent(this);
		cell.setFixedHeight(height + TEXT_PADDING);
		cell.setPaddingTop(TEXT_PADDING / 2);
		cell.setBorder(0);
		return cell;
	}
}
