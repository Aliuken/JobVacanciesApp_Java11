package com.aliuken.jobvacanciesapp.util.persistence.pdf.util;

import com.itextpdf.text.SplitCharacter;
import com.itextpdf.text.pdf.PdfChunk;

public class FixMaxCharsSplit implements SplitCharacter {

	@Override
	public boolean isSplitCharacter(final int start, final int current, final int end, final char[] cc, final PdfChunk[] ck) {
		final char c;
		if(ck == null) {
			c = cc[current];
		} else {
			final int ckIndex = Math.min(current, ck.length - 1);
			c = (char) ck[ckIndex].getUnicodeEquivalent(cc[current]);
		}
		if(c < 0x2e80) {
			return false;
		}
		return c >= 0x2e80 && c < 0xd7a0 || c >= 0xf900 && c < 0xfb00 || c >= 0xfe30 && c < 0xfe50 || c >= 0xff61 && c < 0xffa0;
	}
}
