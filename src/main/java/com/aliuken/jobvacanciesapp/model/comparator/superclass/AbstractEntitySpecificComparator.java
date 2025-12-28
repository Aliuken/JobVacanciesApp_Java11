package com.aliuken.jobvacanciesapp.model.comparator.superclass;

import com.aliuken.jobvacanciesapp.model.entity.superclass.AbstractEntity;
import org.jspecify.annotations.NonNull;

import java.util.function.Function;

public abstract class AbstractEntitySpecificComparator<T extends AbstractEntity<T>, U extends Comparable<U>> extends AbstractEntityComparator<T> {
	public abstract @NonNull Function<T, U> getFirstCompareFieldFunction();
	public abstract boolean getIsDescendingOrder();

	/**
	 * Defines an ordering among entities.
	 * <p>
	 * Entities are ordered by:
	 * <ul>
	 *   <li>Class name</li>
	 *   <li>A first compare field</li>
	 *   <li>ID value</li>
	 * </ul>
	 */
	@Override
	public final int compare(final T entity1, final T entity2) {
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

		final int firstCompareFieldResult = this.getFirstCompareFieldResult(entity1, entity2);
		if(firstCompareFieldResult != ENTITIES_EQUAL) {
			return direction * firstCompareFieldResult;
		}

		final int idCompareResult = this.getIdCompareResult(entity1, entity2);
		return direction * idCompareResult;
	}

	// Ascending order where entities are sorted by their first compare field (where null values are sorted last).
	private int getFirstCompareFieldResult(final @NonNull T entity1, final @NonNull T entity2) {
		final Function<T, U> firstCompareFieldFunction = this.getFirstCompareFieldFunction();
		final U firstCompareFieldValue1 = firstCompareFieldFunction.apply(entity1);
		final U firstCompareFieldValue2 = firstCompareFieldFunction.apply(entity2);

		final int firstCompareFieldResult = AbstractEntityComparator.getCompareResult(firstCompareFieldValue1, firstCompareFieldValue2); //CONTINUE IF ZERO
		return firstCompareFieldResult;
	}
}