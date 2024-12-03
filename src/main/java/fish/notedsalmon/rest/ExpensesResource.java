package fish.notedsalmon.rest;

import fish.notedsalmon.entities.Expenses;
import fish.notedsalmon.exceptions.NotFoundException;
import fish.notedsalmon.services.ExpenseService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/expenses")
@RequestScoped
public class ExpensesResource {

    @Inject
    ExpenseService expensesService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createExpense(Expenses expense) {
        expense.setId(null);
        expensesService.saveExpense(expense);
        return Response.created(URI.create("api/expenses/" + expense.getId())).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllExpenses() {
        List<Expenses> expensesList = expensesService.getExpensesList();
        return Response.ok(expensesList).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findExpense(@PathParam("id") int id) {
        Expenses expense = expensesService.findExpenseById(id);
        return Response.ok(expense).build();
    }

    @DELETE
    @Path("{id}")
    //@RolesAllowed({Constants.ROLE_REFERENT, Constants.ROLE_ADMIN, Constants.ROLE_SUPERADMIN})
    public Response deleteExpense(@PathParam("id") int id) {
        try {
            expensesService.deleteExpense(id);
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()).build();
        }

        return Response.noContent().build();
    }

    @PUT
    @Path("{id}")
    public Response updateExpense(@PathParam("id") int id, Expenses expense) {
        expense.setId(id);
        expensesService.updateExpense(expense);
        return Response.noContent().build();
    }
}
