package com.aliuken.jobvacanciesapp.enumtype;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

public enum RandomCharactersEnum implements Serializable {
	ALPHANUMERIC_CHARACTERS("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"),
	NUMERIC_CHARACTERS("0123456789");

	@NotEmpty
	private final String characters;

	private final int charactersSize;

	private RandomCharactersEnum(String characters) {
		this.characters = characters;
		this.charactersSize = characters.length();
	}

	public String getCharacters() {
		return characters;
	}

	public int getCharactersSize() {
		return charactersSize;
	}

}
