package demo.entity.jooq;

import demo.entity.Customer;
import demo.entity.SimpleId;
import demo.generated.tables.records.CustomerRecord;
import org.jooq.*;
import static demo.generated.tables.CustomerTable.customerTable;

/**
 * Enables jooq to automatically convert database results to the Customer object, since it's built in mapper conflicts with the
 * requirements for the moxy json mapper. In a full production app, this should be an automatically generated file, to avoid
 * having to update many different places when a schema change happens.
 * @param <R>
 */
public class CustomerMapper<R extends Record> implements RecordMapper<R, Customer> {
	private final TableField<CustomerRecord, Integer> customerId;
	private final TableField<CustomerRecord, String>  firstName;
	private final TableField<CustomerRecord, String>  lastName;
	private final TableField<CustomerRecord, String>  phone;

	public CustomerMapper() {
		customerId = customerTable.customerId;
		firstName = customerTable.firstName;
		lastName = customerTable.lastName;
		phone = customerTable.phone;
	}

	@Override public Customer map(R record) {
		final Customer customer = Customer.make(
			SimpleId.make(record.getValue(customerId)),
			record.getValue(firstName),
			record.getValue(lastName),
			record.getValue(phone));
		return customer;
	}
}
