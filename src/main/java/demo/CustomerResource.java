
package demo;

import demo.entity.Customer;
import demo.entity.SimpleId;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.sql.SQLException;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;


@Path("customers")
public class CustomerResource {
	private final DataBridge db;

	public CustomerResource() throws SQLException, ClassNotFoundException {
		db = DataBridgeProvider.dataBridge;
	}

	public CustomerResource(final DataBridge db) {
		this.db = db;
	}

	@GET
	@Path("{id : \\d+}/similarCustomers")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSimilarCustomers(@PathParam("id") int id) {
		final DataResult<SimpleId> result = db.findSimilarCustomers(SimpleId.make(id));
		final GenericEntity<List<SimpleId>> genericEntity = new GenericEntity<List<SimpleId>>(result.entities) { };
		final Response response = Response
			.status(getResponseStatus(result, SC_OK))
			.entity(genericEntity)
			.build();
		return response;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addCustomer(@NotNull Customer customer) {
		final DataResult<SimpleId> result = db.createOrUpdate(customer);
		final Response response = Response.status(getResponseStatus(result, SC_CREATED)).entity(result.entity).build();
		return response;
	}

	@PUT
	@Path("{id : \\d+}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCustomer(@PathParam("id") int id, @NotNull Customer customer) {
		final DataResult result = db.createOrUpdate(customer);
		final Response response = Response.status(getResponseStatus(result, SC_OK)).entity(result.entity).build();
		return response;
	}

	@DELETE
	@Path("{id : \\d+}")
	public Response deleteCustomer(@PathParam("id") int id) {
		final DataResult result = db.deleteCustomer(SimpleId.make(id));
		final Response response = Response.status(getResponseStatus(result, SC_NO_CONTENT)).build();
		return response;
	}

	@GET
	@Path("test")
	@Produces(MediaType.TEXT_PLAIN)
	public Response test() {
		return Response.status(SC_OK).entity("It worked!").build();
	}

	@GET
	@Path("{id : \\d+}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCustomer(@PathParam("id") int customerId) {
		final DataResult<Customer> result = db.getCustomer(SimpleId.make(customerId));
		final Response response = Response.status(getResponseStatus(result, SC_OK)).entity(result.entity).build();
		return response;
	}

	private int getResponseStatus(@NotNull DataResult result, int successCode) {
		return result.successful ? successCode : SC_INTERNAL_SERVER_ERROR;
	}
}
