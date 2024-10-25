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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@Named
@Transactional
@ApplicationScoped
public class ExpenseService {

    private static final Random random = new Random();

    private static final Log log = LogFactory.getLog(ExpenseService.class);
    @PersistenceContext
    private EntityManager em;

    private static final String[] SAMPLE_DESCRIPTIONS = {
            "Restaurant", "Rent", "Electricity bill",
            "Groceries", "Cinema", "Gym",
            "Clothes shopping", "Internet", "UBER",
            "Coffee", "Games", "Health Supplements",
            "Netflix"
    };


    public List<Expenses> getExpensesList() {
        List<Expenses> expensesList;
        expensesList = em.createNamedQuery(Expenses.FIND_ALL, Expenses.class)
                .getResultList();
        return expensesList;
    }


    public List<Category> getCategoriesList() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    public void updateExpense(Expenses expense) {
        try {
            em.merge(expense);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("Disaster", " Failed to merge");
            FacesContext.getCurrentInstance().addMessage(null, message);
            throw new RuntimeException(e);
        }
    }

    public void truncateExpenses() {
        em.createNativeQuery("TRUNCATE EXPENSES").executeUpdate();
    }

    public double getTotalExpenses() {
        BigDecimal total = em.createQuery("SELECT SUM(e.amount) FROM Expenses e", BigDecimal.class)
                .getSingleResult();
        return total != null ? total.doubleValue() : 0.0;
    }

    public void generateExampleExpenses() {
        List<Category> categories = em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        if (categories.isEmpty()) {
            throw new RuntimeException("Error: No categories found");
        }

        for (int i=0; i < random.nextInt(51) + 50; i++) {
            Expenses expense = new Expenses();
            expense.setAmount(generateRandomAmount());
            expense.setCategory(randomCategory(categories));
            expense.setDescription(randomDescription());
            expense.setExpense_date(generateRandomDate());

            em.persist(expense);
        }
    }

    private BigDecimal generateRandomAmount() {
        double min = 1.00;
        double max = 500.00;
        double amount = min + (max - min) * random.nextDouble();
        return BigDecimal.valueOf(amount);
    }

    private Category randomCategory(List<Category> categories) {
        return categories.get(random.nextInt(categories.size()));
    }

    private String randomDescription() {
        return SAMPLE_DESCRIPTIONS[random.nextInt(SAMPLE_DESCRIPTIONS.length)];
    }

    private LocalDateTime generateRandomDate() {
        int year = LocalDateTime.now().getYear();
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        return LocalDateTime.of(year, month, day, random.nextInt(24), random.nextInt(60));
    }

    public Expenses findExpenseById(Integer id) {
        return em.find(Expenses.class, id);
    }
}
