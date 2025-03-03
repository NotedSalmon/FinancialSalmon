package fish.notedsalmon.services;

import fish.notedsalmon.beans.ImportBean;
import fish.notedsalmon.entities.Category;
import fish.notedsalmon.entities.Expenses;
import fish.notedsalmon.exceptions.NotFoundException;
import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManagedExecutorService;
import jakarta.enterprise.concurrent.ManagedThreadFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import javax.xml.crypto.Data;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Named
@Transactional
@ApplicationScoped
public class ExpenseService {

    private static final Random random = new Random();

    @Resource
    private ManagedExecutorService managedExecutorService;

    @Resource
    ManagedThreadFactory managedThreadFactory ;

    private static final Log log = LogFactory.getLog(ExpenseService.class);

    @Inject
    private ImportBean importBean;
    @PersistenceContext
    private EntityManager em;

    private static final String[] SAMPLE_DESCRIPTIONS = {
            "Restaurant", "Rent", "Electricity bill",
            "Groceries", "Cinema", "Gym",
            "Clothes shopping", "Internet", "UBER",
            "Coffee", "Games", "Health Supplements",
            "Netflix"
    };

    public ExpenseService() {

    }


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

    public void generateExampleExpensesParallel() {
        List< Future<?> > future = new ArrayList<>();
        for (int i=0; i < 5; i++){
            System.out.println("Scheduling task number: " + i);
            future.add(managedExecutorService.submit(()->{
                generateExampleExpenses();
            }));
        }
        System.out.println("Waiting for results");
        for (Future<?> f : future){
            try {
                f.get();
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void generateExampleExpensesSequential() {
        for (int i=0; i < 30; i++){
            generateExampleExpenses();
        }
    }

    private void generateExampleExpenses() {
        System.out.println("Start of generateExampleExpenses");
        List<Category> categories = em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
        if (categories.isEmpty()) {
            throw new RuntimeException("Error: No categories found");
        }

        for (int i=0; i < 500; i++) {
            Expenses expense = new Expenses();
            expense.setAmount(generateRandomAmount());
            expense.setCategory(randomCategory(categories));
            expense.setDescription(randomDescription());
            expense.setExpense_date(generateRandomDate());

            saveExpense(expense);
        }
        System.out.println(new Date());
    }

    public void saveExpense(Expenses expense) {
        em.persist(expense);
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

    public void deleteExpense(int id) throws NotFoundException {
        Expenses expenses = em.find(Expenses.class, id);
        if (expenses == null) {
            throw new NotFoundException("Expense: "+ id+ " not found");
        }
        em.remove(expenses);
    }

    public Expenses findExpenseById(Integer id) {
        return em.find(Expenses.class, id);
    }
}
