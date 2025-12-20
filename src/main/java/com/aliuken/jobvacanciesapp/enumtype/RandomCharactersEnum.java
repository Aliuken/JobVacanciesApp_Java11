package com.aliuken.jobvacanciesapp.enumtype;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public enum RandomCharactersEnum implements Serializable {
	ALPHANUMERIC_CHARACTERS("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"),
	NUMERIC_CHARACTERS("0123456789");

	@Getter
	@NotEmpty
	private final String characters;

	@Getter
	private final int charactersSize;

	private RandomCharactersEnum(String characters) {
		this.characters = characters;
		this.charactersSize = characters.length();
	}
}
