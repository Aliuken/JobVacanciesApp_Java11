package com.aliuken.jobvacanciesapp.util.persistence.pdf.util;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;

import java.util.ArrayList;
import java.util.List;


public class StyleApplier {
	public static final String BOLD_START = "<bBb>";
	public static final String BOLD_END = "</bBb>";
	public static final int BOLD_START_LENGTH = BOLD_START.length();
	public static final int BOLD_END_LENGTH = BOLD_END.length();

	public static List<Chunk> applyStyles(final Font font, final String content) {
		final int start = content.indexOf(BOLD_START);
		final int end = content.indexOf(BOLD_END);

		final List<Chunk> chunks = new ArrayList<Chunk>();
		if(start >= 0 && end >= start + BOLD_START_LENGTH) {
			final Font fontNorm = new Font(font);
			fontNorm.setStyle(Font.NORMAL);

			final Font fontBold = new Font(font);
			fontBold.setStyle(Font.BOLD);

			final String normalFontContentLeft = content.substring(0, start);
			final String boldFontContent = content.substring(start + BOLD_START_LENGTH, end);
			final String normalFontContentRight = content.substring(end + BOLD_END_LENGTH, content.length());

			chunks.add(getChunk(normalFontContentLeft, fontNorm));
			chunks.add(getChunk(boldFontContent, fontBold));
			chunks.add(getChunk(normalFontContentRight, fontNorm));
		} else {
			final Font fontNorm = new Font(font);
			fontNorm.setStyle(Font.NORMAL);

			chunks.add(getChunk(content, fontNorm));
		}
		return chunks;
	}

	public static Chunk getChunk(final String content, final Font font) {
		final Chunk chunk = new Chunk(content);
		chunk.setSplitCharacter(new FixMaxCharsSplit());
		chunk.setFont(font);
		return chunk;
	}

	public static String getBoldString(String toBeBold) {
		final String result;
		if(toBeBold != null) {
			result = new StringBuilder(BOLD_START).append(toBeBold).append(BOLD_END).toString();
		} else {
			result = null;
		}
		return result;
	}
}
