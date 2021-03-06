/**
 * This class is generated by jOOQ
 */
package demo.generated.tables.pojos;


import java.io.Serializable;

import javax.annotation.Generated;


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
public class Customer implements Serializable {

	private static final long serialVersionUID = -972076801;

	private final Integer customerid;
	private final String  firstname;
	private final String  lastname;
	private final String  phone;

	public Customer(Customer value) {
		this.customerid = value.customerid;
		this.firstname = value.firstname;
		this.lastname = value.lastname;
		this.phone = value.phone;
	}

	public Customer(
		Integer customerid,
		String  firstname,
		String  lastname,
		String  phone
	) {
		this.customerid = customerid;
		this.firstname = firstname;
		this.lastname = lastname;
		this.phone = phone;
	}

	public Integer getCustomerid() {
		return this.customerid;
	}

	public String getFirstname() {
		return this.firstname;
	}

	public String getLastname() {
		return this.lastname;
	}

	public String getPhone() {
		return this.phone;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Customer (");

		sb.append(customerid);
		sb.append(", ").append(firstname);
		sb.append(", ").append(lastname);
		sb.append(", ").append(phone);

		sb.append(")");
		return sb.toString();
	}
}
