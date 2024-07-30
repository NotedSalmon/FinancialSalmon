package fish.notedsalmon.services;

import fish.notedsalmon.entities.Expenses;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

@Named
@ApplicationScoped
public class ExpenseService {

    @PersistenceContext
    private EntityManager em;

    public List<Expenses> getExpensesList() {
        List<Expenses> expensesList;
        expensesList = em.createNamedQuery(Expenses.FIND_ALL, Expenses.class)
                .getResultList();
        return expensesList;
    }
}
