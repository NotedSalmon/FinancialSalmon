package fish.notedsalmon.rest;

import fish.notedsalmon.services.ExpenseService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

@Path("/services")
@RequestScoped
public class ServiceResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    ExpenseService expenseService;

    public ServiceResource() {}

    @GET
    @Path("/generateExampleExpenses")
    @Produces(MediaType.APPLICATION_JSON)
    public String generateExampleExpenses() {
        expenseService.generateExampleExpensesSequential();
        return "Successful generated Expenses Sequential";
    }

    @GET
    @Path("/generateExampleExpensesParallel")
    @Produces(MediaType.APPLICATION_JSON)
    public String generateExampleExpensesParallel() {
        expenseService.generateExampleExpensesParallel();
        return "Successful generated Expenses Parallel";
    }

}