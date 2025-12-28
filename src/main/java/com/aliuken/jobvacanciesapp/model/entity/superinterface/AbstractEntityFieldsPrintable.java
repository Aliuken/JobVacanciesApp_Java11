package com.aliuken.jobvacanciesapp.model.entity.superinterface;

import org.jspecify.annotations.NonNull;

public interface AbstractEntityFieldsPrintable {
	public default @NonNull String[] getGroupedFields() {
		final String[] result;
		if(this.isPrintableEntity()) {
			final String keyFields = this.getKeyFields();
			final String authUserFields = this.getAuthUserFields();
			final String commonFields = this.getCommonFields();
			final String otherFields = this.getOtherFields();
			result = new String[]{keyFields, authUserFields, commonFields, otherFields};
		} else {
			result = new String[0];
		}
		return result;
	}

	public abstract boolean isPrintableEntity();
	public abstract @NonNull String getKeyFields();
	public abstract @NonNull String getAuthUserFields();
	public abstract @NonNull String getCommonFields();
	public abstract @NonNull String getOtherFields();
}
