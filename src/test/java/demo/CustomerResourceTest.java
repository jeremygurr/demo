package demo;

import demo.entity.Customer;
import demo.entity.SimpleId;
import org.glassfish.jersey.filter.LoggingFilter;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static javax.servlet.http.HttpServletResponse.*;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItems;

public class CustomerResourceTest extends JerseyTest {
	/**
	 * This must be static since the JerseyTest configure method, which requires this to be set,
	 * happens within the parent JerseyTest constructor, before the constructor on this class is called.
	 */
	private static DataBridge db;

	static {
		try {
			db = new SimpleDataBridge();
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, e.getMessage(), e);
		}
	}

	@Override protected Application configure() {
		final CustomerResource customerResource = new CustomerResource(db);
		final ResourceConfig config = new ResourceConfig()
			.register(customerResource);

		// to log web service requests for easier debugging.
		config.register(new LoggingFilter(Logger.getGlobal(), true));

		return config;
	}

	@Before
	public void prepEachTest() {
		final DataResult result = db.initialize();
		if(!result.successful) {
			throw new RuntimeException("Failed to initialize database", result.exception);
		}
	}

	@Test
	public void testCreate() {
		final Customer customerIn = Customer.make(SimpleId.empty, "Bob", "Tomato", "123-4567");
		final Entity<Customer> customerJson = Entity.json(customerIn);
		final Response response = target("customers").request(APPLICATION_JSON).buildPost(customerJson).invoke();

		assertThat("request failed", response.getStatus(), is(SC_CREATED));

		final SimpleId id = response.readEntity(SimpleId.class);
		assertThat("id is missing from response", id, is(notNullValue()));

		final DataResult<Customer> result = CustomerResourceTest.db.getCustomer(id);
		assertThat("customer isn't in db", result.exists, is(true));

		final Customer customerOut = result.entity;
		assertThat(customerOut.firstName, is("Bob"));
		assertThat(customerOut.lastName, is("Tomato"));
		assertThat(customerOut.phone, is("123-4567"));
	}

	@Test
	public void testUpdate() {
		final SimpleId customerId = SimpleId.make(1);
		final Customer customerOriginal = Customer.make(customerId, "Bob", "Tomato", "123-4567");
		db.createOrUpdate(customerOriginal);

		final Customer customerModified = customerOriginal.setFirstName("Bobby");
		final Entity<Customer> customerJson = Entity.json(customerModified);
		final Response response = target("customers/" + customerId.id).request(APPLICATION_JSON).buildPut(customerJson).invoke();
		assertThat("request failed", response.getStatus(), is(SC_OK));

		final DataResult<Customer> result = db.getCustomer(customerId);
		assertThat("customer is missing", result.exists, is(true));

		final Customer customerOut = result.entity;
		assertThat(customerOut.firstName, is("Bobby"));
		assertThat(customerOut.lastName, is("Tomato"));
		assertThat(customerOut.phone, is("123-4567"));
	}

	@Test
	public void testDelete() {
		final SimpleId customerId = createCustomer("Bob", "Tomato", "123-4567");

		final Response response = target("customers/" + customerId.id).request(APPLICATION_JSON).buildDelete().invoke();
		assertThat("request failed", response.getStatus(), is(SC_NO_CONTENT));

		final DataResult<Customer> result = db.getCustomer(customerId);
		assertThat("deleted record still exists", result.isEmpty(), is(true));
	}

	private SimpleId createCustomer(String firstName, String lastName, String phone) {
		final Customer customer = Customer.make(SimpleId.empty, firstName, lastName, phone);
		final DataResult<SimpleId> result = db.createOrUpdate(customer);
		return result.entity;
	}

	@Test
	public void testDuplicateFinder() {
		final SimpleId c1 = createCustomer("Bob", "Tomato", "123-4567");
		final SimpleId c2 = createCustomer("Larry", "Cucumber", "111-1111");
		final SimpleId c3 = createCustomer("Bobby", "Tomato", "");
		final SimpleId c4 = createCustomer("", "Tomato", "123-4567");
		final SimpleId c5 = createCustomer("", "", "123-4567");
		final SimpleId c6 = createCustomer("", "", "987-6543");

		final Response response = target("customers/" + c1.id + "/similarCustomers").request(APPLICATION_JSON).buildGet().invoke();
		assertThat("request failed", response.getStatus(), is(SC_OK));

		final List<SimpleId> customerIds = response.readEntity(new GenericType<List<SimpleId>>() { });
		assertThat("bad response, no id list found", customerIds, is(notNullValue()));
		assertThat("wrong number of ids returned", customerIds.size(), is(3));
		assertThat("wrong id set returned", customerIds, hasItems(c3, c4, c5));
	}

//	@Test
	public void testMatchValue() {
		final SimpleId c1 = createCustomer("Bob", "Tomato", "123-4567");
		final SimpleId c2 = createCustomer("Larry", "Cucumber", "111-1111");
		final SimpleId c3 = createCustomer("Bobby", "Tomato", "");
		final SimpleId c4 = createCustomer("", "Tomato", "123-4567");
		final SimpleId c5 = createCustomer("", "", "123-4567");
		final SimpleId c6 = createCustomer("", "", "987-6543");

		final Response response = target("customers/" + c1.id + "/matchValues").request(APPLICATION_JSON).buildGet().invoke();
		assertThat("request failed", response.getStatus(), is(SC_OK));

		final List<Integer> values = response.readEntity(new GenericType<List<Integer>>() { });
		assertThat("bad response, no id list found", values, is(notNullValue()));
		assertThat("wrong number of ids returned", values.size(), is(5));
	}

	@Test
	public void testAddNullCustomer() {
		final Entity<Customer> customerJson = Entity.json(null);
		final Response response = target("customers").request(APPLICATION_JSON).buildPost(customerJson).invoke();

		assertThat("request failed", response.getStatus(), is(SC_BAD_REQUEST));
	}

	@Test
	public void testAddCorruptCustomer() {
		final Entity<String> customerJson = Entity.json("I am corrupt");
		final Response response = target("customers").request(APPLICATION_JSON).buildPost(customerJson).invoke();

		assertThat("request failed", response.getStatus(), is(SC_BAD_REQUEST));
	}

}
