package com.aliuken.jobvacanciesapp.model.entity.superinterface;

public interface AbstractEntityFieldsPrintable {
	public default String[] getGroupedFields() {
		final String[] result;
		if(this.isPrintableEntity()) {
			final String keyFields = this.getKeyFields();
			final String authUserFields = this.getAuthUserFields();
			final String commonFields = this.getCommonFields();
			final String otherFields = this.getOtherFields();
			result = new String[]{keyFields, authUserFields, commonFields, otherFields};
		} else {
			result = null;
		}
		return result;
	}

	public abstract boolean isPrintableEntity();
	public abstract String getKeyFields();
	public abstract String getAuthUserFields();
	public abstract String getCommonFields();
	public abstract String getOtherFields();
}
