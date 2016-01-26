/**
 * This class is generated by jOOQ
 */
package demo.generated.tables.records;


import demo.generated.tables.CustomerTable;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


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
public class CustomerRecord extends UpdatableRecordImpl<CustomerRecord> implements Record4<Integer, String, String, String> {

	private static final long serialVersionUID = -629768908;

	/**
	 * Setter for <code>test.customer.customerId</code>.
	 */
	public void setCustomerid(Integer value) {
		setValue(0, value);
	}

	/**
	 * Getter for <code>test.customer.customerId</code>.
	 */
	public Integer getCustomerid() {
		return (Integer) getValue(0);
	}

	/**
	 * Setter for <code>test.customer.firstName</code>.
	 */
	public void setFirstname(String value) {
		setValue(1, value);
	}

	/**
	 * Getter for <code>test.customer.firstName</code>.
	 */
	public String getFirstname() {
		return (String) getValue(1);
	}

	/**
	 * Setter for <code>test.customer.lastName</code>.
	 */
	public void setLastname(String value) {
		setValue(2, value);
	}

	/**
	 * Getter for <code>test.customer.lastName</code>.
	 */
	public String getLastname() {
		return (String) getValue(2);
	}

	/**
	 * Setter for <code>test.customer.phone</code>.
	 */
	public void setPhone(String value) {
		setValue(3, value);
	}

	/**
	 * Getter for <code>test.customer.phone</code>.
	 */
	public String getPhone() {
		return (String) getValue(3);
	}

	// -------------------------------------------------------------------------
	// Primary key information
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Record1<Integer> key() {
		return (Record1) super.key();
	}

	// -------------------------------------------------------------------------
	// Record4 type implementation
	// -------------------------------------------------------------------------

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Integer, String, String, String> fieldsRow() {
		return (Row4) super.fieldsRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Row4<Integer, String, String, String> valuesRow() {
		return (Row4) super.valuesRow();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<Integer> field1() {
		return CustomerTable.customerTable.customerId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field2() {
		return CustomerTable.customerTable.firstName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field3() {
		return CustomerTable.customerTable.lastName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Field<String> field4() {
		return CustomerTable.customerTable.phone;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Integer value1() {
		return getCustomerid();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value2() {
		return getFirstname();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value3() {
		return getLastname();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String value4() {
		return getPhone();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerRecord value1(Integer value) {
		setCustomerid(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerRecord value2(String value) {
		setFirstname(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerRecord value3(String value) {
		setLastname(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerRecord value4(String value) {
		setPhone(value);
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public CustomerRecord values(Integer value1, String value2, String value3, String value4) {
		value1(value1);
		value2(value2);
		value3(value3);
		value4(value4);
		return this;
	}

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Create a detached CustomerRecord
	 */
	public CustomerRecord() {
		super(CustomerTable.customerTable);
	}

	/**
	 * Create a detached, initialised CustomerRecord
	 */
	public CustomerRecord(Integer customerid, String firstname, String lastname, String phone) {
		super(CustomerTable.customerTable);

		setValue(0, customerid);
		setValue(1, firstname);
		setValue(2, lastname);
		setValue(3, phone);
	}
}