package com.aliuken.jobvacanciesapp.model.comparator.superclass;

import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import com.aliuken.jobvacanciesapp.util.javase.GenericsUtils;

import java.util.function.Function;

public abstract class AbstractEntitySpecificComparator<T extends AbstractEntity<T>, U extends Comparable<U>> extends AbstractEntityComparator<T> {
	public abstract Function<T, U> getFirstCompareFieldFunction();
	public abstract boolean getIsDescendingOrder();

	/**
	 * Defines an ordering among entities.
	 * <p>
	 * Entities are ordered by:
	 * <ul>
	 *   <li>Class name (lexicographically ascending)</li>
	 *   <li>A first compare field (ascending)</li>
	 *   <li>ID value (ascending)</li>
	 * </ul>
	 */
	@Override
	public final int compare(final AbstractEntity<T> abstractEntity1, final AbstractEntity<T> abstractEntity2) {
		final T entity1 = GenericsUtils.cast(abstractEntity1);
		final T entity2 = GenericsUtils.cast(abstractEntity2);

		final boolean isDescendingOrder = this.getIsDescendingOrder();
		final int direction = isDescendingOrder ? -1 : 1;

		final Integer nullCompareResult = this.getNullCompareResult(entity1, entity2);
		if(nullCompareResult != null) {
			return direction * nullCompareResult;
		}

		final Integer classCompareResult = this.getClassCompareResult(entity1, entity2);
		if(classCompareResult != null) {
			return direction * classCompareResult;
		}

		final Integer firstCompareFieldResult = this.getFirstCompareFieldResult(entity1, entity2);
		if(firstCompareFieldResult != null && firstCompareFieldResult != ENTITIES_EQUAL) {
			return direction * firstCompareFieldResult;
		}

		final int idCompareResult = this.getIdCompareResult(entity1, entity2);
		return direction * idCompareResult;
	}

	// Ascending order where different classes are sorted by their names (including packages).
	private Integer getFirstCompareFieldResult(final T entity1, final T entity2) {
		final Function<T, U> firstCompareFieldFunction = this.getFirstCompareFieldFunction();

		final Integer firstCompareFieldResult;
		if(firstCompareFieldFunction != null) {
			final U firstCompareFieldValue1 = firstCompareFieldFunction.apply(entity1);
			final U firstCompareFieldValue2 = firstCompareFieldFunction.apply(entity2);
			firstCompareFieldResult = this.getCompareResult(firstCompareFieldValue1, firstCompareFieldValue2); //CONTINUE IF ZERO
		} else {
			firstCompareFieldResult = null; //CONTINUE
		}
		return firstCompareFieldResult;
	}
}