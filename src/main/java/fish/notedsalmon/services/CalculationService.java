package fish.notedsalmon.services;

import fish.notedsalmon.entities.Category;
import fish.notedsalmon.entities.Expenses;
import fish.notedsalmon.records.CategorySum;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
public class CalculationService {

    @PersistenceContext
    private EntityManager em;

    @Inject
    ExpenseService expenseService;

    private List<Expenses> expensesList;
    private List<CategorySum> summedCategories;

    @PostConstruct
    public void init(){
        expensesList = expenseService.getExpensesList();
        summedCategories = new ArrayList<>();
        sumExpensesByCategory();
    }

    private void sumExpensesByCategory() {
        for (Expenses expense : expensesList) {
            Category category = expense.getCategory();
            if (category != null) {
                String categoryName = category.getCategory();
                BigDecimal amount = expense.getAmount();
                Integer red = category.getColourRed();
                Integer green = category.getColourGreen();
                Integer blue = category.getColourBlue();

                CategorySum existingCategorySum = findCategorySumByName(categoryName);
                if (existingCategorySum != null) {
                    updateCategorySum(existingCategorySum, amount, red, green, blue);
                } else {
                    summedCategories.add(new CategorySum(categoryName, amount,red,green,blue));
                }
            }
        }
    }

    public Map<YearMonth, BigDecimal> getMonthlyExpenses() {
        Map<YearMonth, BigDecimal> monthlyExpenses = new HashMap<>();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), month);
            monthlyExpenses.put(yearMonth, BigDecimal.ZERO);
        }

        for (Expenses expense : expensesList) {
            LocalDateTime expenseDate = expense.getExpense_date();
            YearMonth yearMonth = YearMonth.of(expenseDate.getYear(), expenseDate.getMonthValue());

            BigDecimal currentTotal = monthlyExpenses.getOrDefault(yearMonth, BigDecimal.ZERO);
            monthlyExpenses.put(yearMonth, currentTotal.add(expense.getAmount()));
        }
        return monthlyExpenses;
    }

    private CategorySum findCategorySumByName(String categoryName) {
        for (CategorySum categorySum : summedCategories) {
            if (categorySum.categoryName().equals(categoryName)) {
                return categorySum;
            }
        }
        return null;
    }

    private void updateCategorySum(CategorySum existingCategorySum, BigDecimal amount, Integer red, Integer green, Integer blue) {

        summedCategories.remove(existingCategorySum);
        BigDecimal newSum = existingCategorySum.sum().add(amount);
        summedCategories.add(new CategorySum(existingCategorySum.categoryName(), newSum, red,green,blue));
    }

    public BigDecimal getSumForCategory(String categoryName) {
        CategorySum categorySum = findCategorySumByName(categoryName);
        return (categorySum != null) ? categorySum.sum() : BigDecimal.ZERO;
    }

    public List<CategorySum> getSummedCategories() {
        return summedCategories;
    }
}
