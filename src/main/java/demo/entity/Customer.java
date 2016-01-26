package demo.entity;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import java.beans.ConstructorProperties;

/**
 * The Customer immutable pojo. Unlike normal pojos, the setters here will not change the base object, but instead create a new one with the
 * changes applied. To construct a new instance, the static make method is used, instead of exposing the constructors. This enables lower cost
 * of change in the future should we want to return a subclass from the factory method.
 */
@Immutable
@XmlRootElement
public class Customer {
	public static final Customer empty = new Customer();

	public static Customer make(SimpleId id, String firstName, String lastName, String phone) {
		return new Customer(id, firstName, lastName, phone);
	}

	@NotNull
	public final SimpleId customerId;

	@NotNull
	public final String firstName;

	@NotNull
	public final String lastName;

	@NotNull
	public final String phone;

	/**
	 * This should not normally be used. It is provided only for auto json binding libraries.
	 */
	private Customer() {
		firstName = "";
		phone = "";
		lastName = "";
		customerId = SimpleId.empty;
	}

	private Customer(SimpleId customerId, String firstName, String lastName, String phone) {
		this.customerId = customerId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.phone = phone;
	}

	public Customer setFirstName(String firstName) {
		return make(customerId, firstName, lastName, phone);
	}

	public Customer setLastName(String lastName) {
		return make(customerId, firstName, lastName, phone);
	}

	public Customer setPhone(String phone) {
		return make(customerId, firstName, lastName, phone);
	}

	public Customer setCustomerId(SimpleId customerId) {
		return make(customerId, firstName, lastName, phone);
	}

	@Override public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		Customer customer = (Customer) o;

		if (firstName != null ? !firstName.equals(customer.firstName) : customer.firstName != null) { return false; }
		if (lastName != null ? !lastName.equals(customer.lastName) : customer.lastName != null) { return false; }
		return phone != null ? phone.equals(customer.phone) : customer.phone == null;

	}

	@Override public int hashCode() {
		int result = firstName != null ? firstName.hashCode() : 0;
		result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
		result = 31 * result + (phone != null ? phone.hashCode() : 0);
		return result;
	}

	@Override public String toString() {
		final StringBuilder sb = new StringBuilder("Customer{");
		sb.append("customerId=").append(customerId);
		sb.append(", firstName='").append(firstName).append('\'');
		sb.append(", lastName='").append(lastName).append('\'');
		sb.append(", phone='").append(phone).append('\'');
		sb.append('}');
		return sb.toString();
	}
}
