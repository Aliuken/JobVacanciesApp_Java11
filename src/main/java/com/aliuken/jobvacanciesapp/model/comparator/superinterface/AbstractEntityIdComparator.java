package com.aliuken.jobvacanciesapp.model.comparator.superinterface;

import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;

public interface AbstractEntityIdComparator {
	public default <T extends AbstractEntity<T>> int compareAbstractEntityIdDesc(final T abstractEntity1, final T abstractEntity2) {
		final Long abstractEntityId1;
		if(abstractEntity1 != null) {
			abstractEntityId1 = abstractEntity1.getId();
		} else {
			return 1;
		}

		final Long abstractEntityId2;
		if(abstractEntity2 != null) {
			abstractEntityId2 = abstractEntity2.getId();
		} else {
			return -1;
		}

		final int result = abstractEntityId2.compareTo(abstractEntityId1);
		return result;
	}
}