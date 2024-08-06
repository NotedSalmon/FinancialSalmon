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
import java.util.ArrayList;
import java.util.List;

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

                CategorySum existingCategorySum = findCategorySumByName(categoryName);
                if (existingCategorySum != null) {
                    updateCategorySum(existingCategorySum, amount);
                } else {
                    summedCategories.add(new CategorySum(categoryName, amount));
                }
            }
        }
    }

    private CategorySum findCategorySumByName(String categoryName) {
        for (CategorySum categorySum : summedCategories) {
            if (categorySum.categoryName().equals(categoryName)) {
                return categorySum;
            }
        }
        return null;
    }

    private void updateCategorySum(CategorySum existingCategorySum, BigDecimal amount) {
        summedCategories.remove(existingCategorySum);
        BigDecimal newSum = existingCategorySum.sum().add(amount);
        summedCategories.add(new CategorySum(existingCategorySum.categoryName(), newSum));
    }

    public BigDecimal getSumForCategory(String categoryName) {
        CategorySum categorySum = findCategorySumByName(categoryName);
        return (categorySum != null) ? categorySum.sum() : BigDecimal.ZERO;
    }

    public List<CategorySum> getSummedCategories() {
        return summedCategories;
    }
}
