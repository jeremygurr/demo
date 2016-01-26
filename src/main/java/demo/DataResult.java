package demo;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

/**
 * Generic structure for data results, which may be generated by any data service, such as a web service request or direct database access.
 * Note: checked exceptions are specifically avoided here, instead favoring a strategy of checking whether an object exists with a separate method,
 * before fetching the actual data. If this pattern isn't followed, and an attempt is made to call getFirstEntity() on a result that doesn't
 * have any entities, an unchecked exception will be thrown, indicating a failure to use the API correctly. Because this is a lower level object,
 * that is utilized frequently, this avoids a lot of exceptions being thrown over the normal operation of an application, improving debugability
 * and making it easier to identify real issues.
 * @param <T> the entity class this result is composed of
 */
public class DataResult<T> {
	/**
	 *
	 */
	public static final DataResult successfulButEmpty = new DataResult(Collections.emptyList(), null, null);

	public static <T> DataResult<T> make(String errorMessage) {
		return new DataResult<>(Collections.emptyList(), errorMessage, null);
	}

	public static <T> DataResult<T> make(@NotNull T entity, String errorMessage) {
		return new DataResult<>(Collections.singletonList(entity), errorMessage, null);
	}

	public static <T> DataResult<T> make(@NotNull List<T> entities, String errorMessage) {
		return new DataResult<>(entities, errorMessage, null);
	}

	public static DataResult make(@NotNull Throwable throwable) {
		return new DataResult(Collections.emptyList(), throwable.getMessage(), throwable);
	}

	/**
	 * Returns a list of all entities that belong to these results.
	 * Will return an empty list if there was an error, or if nothing matched.
	 */
	@NotNull
	public final List<T>               entities;

	/**
	 * Null if there were no results or if there was an error
	 */
	public final T entity;

	/**
	 * abbreviation for entities.size() > 0
	 */
	public final boolean exists;

	/**
	 * the message describing error(s) that have happened as a result of the request which generated this object. If null, then the request
	 * generating this result was serviced successfully.
	 */
	public final String errorMessage;

	/**
	 * an exception thrown by a data storage framework which gives further details as to what went wrong.
	 */
	public final Throwable exception;

	/**
	 * abbreviation for errorMessage != null
	 */
	public final boolean successful;

	private DataResult(@NotNull List<T> entities, String errorMessage, Throwable exception) {
		this.entities = entities;
		this.errorMessage = errorMessage;
		this.exception = exception;

		this.successful = errorMessage == null;
		this.exists = entities.size() > 0;
		if(entities.size() > 0) {
			this.entity = entities.get(0);
		} else {
			this.entity = null;
		}
	}

	public boolean isEmpty() {
		return equals(successfulButEmpty);
	}
}