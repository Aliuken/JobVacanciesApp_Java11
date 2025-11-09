package com.aliuken.jobvacanciesapp.model.comparator.superinterface;

import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;

public interface AbstractEntityIdComparator {
	public default <T extends AbstractEntity<T>> int compareAbstractEntityIdDesc(final T abstractEntity1, final T abstractEntity2) {
		if(abstractEntity1 == null) {
			return -1;
		}
		if(abstractEntity2 == null) {
			return 1;
		}

		final int idCompareResult = AbstractEntityIdComparator.getIdCompareResult(abstractEntity1.getId(), abstractEntity2.getId());
		return idCompareResult;
	}

	private static int getIdCompareResult(Long abstractEntityId1, Long abstractEntityId2) {
		final int direction = -1;

		// In ascending order, entities with null ids are sorted last; in descending, first.
		final int idCompareResult;
		if (abstractEntityId1 == null && abstractEntityId2 == null) {
			idCompareResult = 0;
		} else if (abstractEntityId1 == null) {
			idCompareResult = -1;
		} else if (abstractEntityId2 == null) {
			idCompareResult = 1;
		} else {
			idCompareResult = direction * Long.compare(abstractEntityId2, abstractEntityId1);
		}
		return idCompareResult;
	}
}