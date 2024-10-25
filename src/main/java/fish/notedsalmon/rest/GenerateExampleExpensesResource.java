package fish.notedsalmon.rest;

import fish.notedsalmon.services.ExpenseService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.UriInfo;

@Path("/generateExampleExpenses")
@RequestScoped
public class GenerateExampleExpensesResource {

    @Context
    private UriInfo uriInfo;

    @Inject
    ExpenseService expenseService;

    public GenerateExampleExpensesResource() {}

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String generateExampleExpenses() {
        expenseService.generateExampleExpenses();
        return "Successful generated Expenses";
    }

    // TODO create CRUD actions within th rest api
}