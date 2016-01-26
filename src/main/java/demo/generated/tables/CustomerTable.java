/**
 * This class is generated by jOOQ
 */
package demo.generated.tables;


import demo.generated.Keys;
import demo.generated.Test;
import demo.generated.tables.records.CustomerRecord;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
	value = {
		"http://www.jooq.org",
		"jOOQ version:3.7.2"
	},
	comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CustomerTable extends TableImpl<CustomerRecord> {

	private static final long serialVersionUID = -1307528696;

	/**
	 * The reference instance of <code>test.customer</code>
	 */
	public static final CustomerTable customerTable = new CustomerTable();

	/**
	 * The class holding records for this type
	 */
	@Override
	public Class<CustomerRecord> getRecordType() {
		return CustomerRecord.class;
	}

	/**
	 * The column <code>test.customer.customerId</code>.
	 */
	public final TableField<CustomerRecord, Integer> customerId = createField("customerId", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

	/**
	 * The column <code>test.customer.firstName</code>.
	 */
	public final TableField<CustomerRecord, String> firstName = createField("firstName", org.jooq.impl.SQLDataType.VARCHAR.length(40).nullable(false), this, "");

	/**
	 * The column <code>test.customer.lastName</code>.
	 */
	public final TableField<CustomerRecord, String> lastName = createField("lastName", org.jooq.impl.SQLDataType.VARCHAR.length(40).nullable(false), this, "");

	/**
	 * The column <code>test.customer.phone</code>.
	 */
	public final TableField<CustomerRecord, String> phone = createField("phone", org.jooq.impl.SQLDataType.VARCHAR.length(20).nullable(false), this, "");

	/**
	 * Create a <code>test.customer</code> table reference
	 */
	public CustomerTable() {
		this("customer", null);
	}

	/**
	 * Create an aliased <code>test.customer</code> table reference
	 */
	public CustomerTable(String alias) {
		this(alias, customerTable);
	}

	private CustomerTable(String alias, Table<CustomerRecord> aliased) {
		this(alias, aliased, null);
	}

	private CustomerTable(String alias, Table<CustomerRecord> aliased, Field<?>[] parameters) {
		super(alias, Test.test, aliased, parameters, "");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Identity<CustomerRecord, Integer> getIdentity() {
		return Keys.IDENTITY_CUSTOMER;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UniqueKey<CustomerRecord> getPrimaryKey() {
		return Keys.KEY_CUSTOMER_PRIMARY;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UniqueKey<CustomerRecord>> getKeys() {
		return Arrays.<UniqueKey<CustomerRecord>>asList(Keys.KEY_CUSTOMER_PRIMARY);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerTable as(String alias) {
		return new CustomerTable(alias, this);
	}

	/**
	 * Rename this table
	 */
	public CustomerTable rename(String name) {
		return new CustomerTable(name, null);
	}
}
