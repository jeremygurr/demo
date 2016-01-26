package demo;

import demo.entity.Customer;
import demo.entity.SimpleId;

import javax.validation.constraints.NotNull;

/**
 * A simple abstraction causing database access to be centralized in one place. This makes it easy to provide different implementations of the
 * database operations for testing vs. production. It also makes it easy to migrate to different database technologies or frameworks as new ones
 * become available.
 */
public interface DataBridge {
	/**
	 * Retrieves the customer object which matches the given id from the database.
	 * @param id
	 * @return
	 */
	@NotNull DataResult<Customer> getCustomer(SimpleId id);

	/**
	 * creates the entity if it doesn't exist, updates it otherwise
	 * @param customer
	 * @return
	 */
	@NotNull DataResult<SimpleId> createOrUpdate(Customer customer);

	/**
	 * Delete the given customer from the database.
	 * @param id
	 * @return
	 */
	@NotNull DataResult deleteCustomer(SimpleId id);

	/**
	 * returns customer ids for each customer that is considered "similar" to the given customer.
	 * @param customerId
	 * @return
	 */
	@NotNull DataResult<SimpleId> findSimilarCustomers(SimpleId customerId);

	/**
	 * Will destroy all data in the database and recreate necessary tables.
	 */
	@NotNull DataResult initialize();

}
