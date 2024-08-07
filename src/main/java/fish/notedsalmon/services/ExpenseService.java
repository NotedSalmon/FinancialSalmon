package fish.notedsalmon.services;

import fish.notedsalmon.entities.Category;
import fish.notedsalmon.entities.Expenses;
import jakarta.ejb.Stateless;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@Named
@ApplicationScoped
public class ExpenseService {

    private static final Log log = LogFactory.getLog(ExpenseService.class);
    @PersistenceContext
    private EntityManager em;

    public List<Expenses> getExpensesList() {
        List<Expenses> expensesList;
        expensesList = em.createNamedQuery(Expenses.FIND_ALL, Expenses.class)
                .getResultList();
        return expensesList;
    }


    public List<Category> getCategoriesList() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    @Transactional
    public void updateExpense(Expenses expense) {
        try {
            em.merge(expense);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("Disaster", " Failed to merge");
            FacesContext.getCurrentInstance().addMessage(null, message);
            throw new RuntimeException(e);
        }
    }

    public Expenses findExpenseById(Integer id) {
        return em.find(Expenses.class, id);
    }
}
