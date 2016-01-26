package demo;

import demo.entity.Customer;
import demo.entity.SimpleId;
import demo.entity.jooq.CustomerMapper;
import demo.generated.tables.records.CustomerRecord;
import org.jooq.*;
import org.jooq.impl.*;

import javax.validation.constraints.NotNull;
import java.sql.*;
import java.util.List;
import java.util.stream.Collectors;

import static demo.generated.tables.CustomerTable.customerTable;
import static org.jooq.impl.DSL.when;

/**
 */
public class SimpleDataBridge implements DataBridge {

	private final @NotNull Connection connection;
	private final @NotNull DSLContext db;

	private final TableField<CustomerRecord, Integer> customerId;
	private final TableField<CustomerRecord, String>  firstName;
	private final TableField<CustomerRecord, String>  lastName;
	private final TableField<CustomerRecord, String>  phone;

	public SimpleDataBridge() throws ClassNotFoundException, SQLException {
		connection = getConnection();

		final RecordMapperProvider mapperProvider = getRecordMapperProvider();
		db = DSL.using(getJooqConfiguration(mapperProvider));

		customerId = customerTable.customerId;
		firstName = customerTable.firstName;
		lastName = customerTable.lastName;
		phone = customerTable.phone;
	}

	private Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("org.h2.Driver");
		return DriverManager.getConnection("jdbc:h2:mem:test"
		                                   + ";DB_CLOSE_DELAY=-1"
		                                   + ";DATABASE_TO_UPPER=false"
		                                   + ";INIT=CREATE SCHEMA IF NOT EXISTS test"
//		                                   + ";TRACE_LEVEL_SYSTEM_OUT=2"
		                                  );
	}

	private Configuration getJooqConfiguration(RecordMapperProvider mapperProvider) {
		return new DefaultConfiguration()
			.set(connection)
			.set(SQLDialect.H2)
			.set(mapperProvider);
	}

	private RecordMapperProvider getRecordMapperProvider() {
		return new RecordMapperProvider() {
			@Override public <R extends Record, E> RecordMapper<R, E> provide(RecordType<R> recordType, Class<? extends E> type) {
				final RecordMapper<R, E> result;
				if (type == Customer.class) {
					result = new CustomerMapper();
				} else {
					result = new DefaultRecordMapper(recordType, type);
				}
				return result;
			}
		};
	}

	@Override @NotNull public DataResult<demo.entity.Customer> getCustomer(@NotNull SimpleId id) {
		DataResult<demo.entity.Customer> result;
		final Record4<Integer, String, String, String> record = db.select(customerId, firstName, lastName, phone)
		                                                          .from(customerTable)
		                                                          .where(customerId.equal(id.id))
		                                                          .fetchAny();
		if (record == null) {
			result = DataResult.successfulButEmpty;
		} else {
			final Customer existingCustomer = record.into(Customer.class);
			result = DataResult.make(existingCustomer, null);
		}
		return result;
	}

	@Override @NotNull public DataResult<SimpleId> createOrUpdate(Customer newCustomer) {
		final DataResult<demo.entity.Customer> oldCustomerResult = getCustomer(newCustomer.customerId);

		DataResult<SimpleId> result;
		if (oldCustomerResult.exists) {
			db.update(customerTable)
			  .set(firstName, newCustomer.firstName)
			  .set(lastName, newCustomer.lastName)
			  .set(phone, newCustomer.phone)
			  .where(customerId.equal(newCustomer.customerId.id))
			  .execute();
			result = DataResult.successfulButEmpty;
		} else {
			final SimpleId newId = SimpleId.make(db.insertInto(customerTable)
			                                       .set(firstName, newCustomer.firstName)
			                                       .set(lastName, newCustomer.lastName)
			                                       .set(phone, newCustomer.phone)
			                                       .returning(customerId)
			                                       .fetchOne().getCustomerid());
			result = DataResult.make(newId, null);
		}

		return result;
	}

	@Override @NotNull public DataResult deleteCustomer(SimpleId id) {
		db.delete(customerTable).where(customerId.equal(id.id)).execute();
		DataResult<Customer> result = DataResult.successfulButEmpty;

		return result;
	}

	/**
	 * This uses a very simple soundex based matching query to return customers that are similar to the given customer. There
	 * are many things which could be done to speed this up and make it more accurate.
	 *
	 * @param baseId
	 * @return
	 */
	@Override @NotNull public DataResult<SimpleId> findSimilarCustomers(SimpleId baseId) {
		DataResult<SimpleId> result;
		final Customer baseCustomer = db.select()
		                                .from(customerTable)
		                                .where(customerId.equal(baseId.id))
		                                .fetchAny().into(Customer.class);

		final List<SimpleId> similarCustomers = db.select(customerId)
		                                          .from(customerTable)
		                                          .where(getSimilarityComputation(baseCustomer)
			                                                 .greaterOrEqual(3)
			                                                 .and(customerId.notEqual(baseCustomer.customerId.id))
		                                                )
		                                          .fetch().stream()
		                                          .map(
			                                          record -> SimpleId.make((Integer) record.getValue(customerId))
		                                              )
		                                          .collect(Collectors.toList());

		result = DataResult.make(similarCustomers, null);
		return result;
	}

	private Field<Integer> getSimilarityComputation(Customer baseCustomer) {
		return ifThen(soundex(firstName).equal(soundex(baseCustomer.firstName)), 1, 0)
			.add(
				ifThen(soundex(lastName).equal(soundex(baseCustomer.lastName)), 2, 0)
			    )
			.add(
				ifThen(phone.equal(baseCustomer.phone), 5, 0)
			    );
	}

	private Field<Integer> ifThen(Condition condition, int trueCase, int falseCase) {
		return when(condition, trueCase).otherwise(falseCase);
	}

	private Field<String> soundex(TableField<CustomerRecord, String> field) {
		return DSL.field("soundex({0})", String.class, field);
	}

	private Field<String> soundex(String field) {
		return DSL.field("soundex({0})", String.class, field);
	}

	/**
	 * ideally an incremental database updating framework would be use to manage the database schema, but for simplicity for this demo,
	 * we are taking some shortcuts here.
	 * @return
	 */
	@Override public DataResult initialize() {
		DataResult result;
		try {
			execute("set schema test");
			execute("drop table if exists customer");
			execute("create table customer (" +
			        "customerId long auto_increment not null primary key" +
			        ", firstName varchar(40) not null" +
			        ", lastName varchar(40) not null" +
			        ", phone varchar(20) not null" +
			        ")");
			/**
			 * These aren't going to be used for this demo, but would likely be used in a real world application, so I included them anyway.
			 */
			execute("create index firstNameIndex on customer (firstName, lastName)");
			execute("create index lastNameIndex on customer (lastName)");
			execute("create index phoneIndex on customer (phone)");

			result = DataResult.successfulButEmpty;
		} catch (SQLException e) {
			result = DataResult.make(e);
		}
		return result;
	}

	private void execute(String sql) throws SQLException {
		final Statement statement = connection.createStatement();
		statement.executeUpdate(sql);
	}

}
