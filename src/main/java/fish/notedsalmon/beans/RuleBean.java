package fish.notedsalmon.beans;

import fish.notedsalmon.entities.Category;
import fish.notedsalmon.entities.Expenses;
import fish.notedsalmon.entities.ExpensesRule;
import fish.notedsalmon.services.ExpenseService;
import fish.notedsalmon.services.RuleService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.primefaces.event.CellEditEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class RuleBean implements Serializable {

    private String expenseName;
    private String expenseCategory;
    private Integer category_id;
    private List<ExpensesRule> expensesRules;

    @Inject
    ExpenseService expenseService;

    @EJB
    RuleService ruleService;

    @PostConstruct
    public void init() {
        expensesRules = ruleService.getRulesList();
    }

    public List<String> autoExpenseDescription(String query) {
        String queryLowerCase = query.toLowerCase();
        Set<String> expenseSetNames = new TreeSet<>();
        List<Expenses> expenses = expenseService.getExpensesList();
        for (Expenses expense : expenses) {
            expenseSetNames.add(expense.getDescription());
        }

        List<String> filteredExpenseNames = expenseSetNames.stream()
                .filter(t -> t.toLowerCase().startsWith(queryLowerCase))
                .collect(Collectors.toList());

        return filteredExpenseNames;
    }

    public List<String> autoCategory(String query) {
        String queryLowerCase = query.toLowerCase();
        List<String> categoriesList = new ArrayList<>();
        List<Category> category = expenseService.getCategoriesList();
        for (Category categories : category) {
            categoriesList.add(categories.getCategory());
        }

        List<String> filteredExpenseNames = categoriesList.stream()
                .filter(t -> t.toLowerCase().startsWith(queryLowerCase))
                .collect(Collectors.toList());

        return filteredExpenseNames;
    }

    public void createRule() {
        category_id = ruleService.findCategoryByName(expenseCategory);
        if (ruleService.addRule(expenseName, category_id)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Successful", "Rule created"));
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Failed", "Rule cannot be created"));
        }
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            int rowIndex = event.getRowIndex();
            ExpensesRule rule = expensesRules.get(rowIndex);
            rule.setExpenseCategory((Integer) newValue);

            ruleService.updateRule(rule);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Expense Changed", "Old: " + oldValue + ", New: " + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    /**
     * -----------------------GETTERS and SETTERS-----------------------------------
     */

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

    public ExpenseService getExpenseService() {
        return expenseService;
    }

    public void setExpenseService(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    public RuleService getRuleService() {
        return ruleService;
    }

    public void setRuleService(RuleService ruleService) {
        this.ruleService = ruleService;
    }

    public List<ExpensesRule> getExpensesRules() {
        return expensesRules;
    }

    public void setExpensesRules(List<ExpensesRule> expensesRules) {
        this.expensesRules = expensesRules;
    }

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }
}
