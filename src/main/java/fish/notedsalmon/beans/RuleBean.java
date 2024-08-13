package fish.notedsalmon.beans;

import fish.notedsalmon.entities.Category;
import fish.notedsalmon.entities.Expenses;
import fish.notedsalmon.services.ExpenseService;
import fish.notedsalmon.services.RuleService;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class RuleBean implements Serializable {

    private static final Log log = LogFactory.getLog(RuleBean.class);
    private String expenseName;
    private String expenseCategory;
    private Integer category_id;

    @Inject
    ExpenseService expenseService;

    @EJB
    RuleService ruleService;

    public List<String> autoExpenseDescription(String query) {
        String queryLowerCase = query.toLowerCase();
        Set<String> expenseSetNames = new TreeSet<>();
        List<Expenses> expenses = expenseService.getExpensesList();
        for (Expenses expense : expenses) {
            expenseSetNames.add(expense.getDescription());
            log.info("expense description: " + expense.getDescription());
        }

        List<String> filteredExpenseNames = expenseSetNames.stream()
                .filter(t -> t.toLowerCase().startsWith(queryLowerCase))
                .collect(Collectors.toList());

        log.info("Filtered List Size: " + filteredExpenseNames.size());
        return filteredExpenseNames;
    }

    public List<String> autoCategory(String query) {
        String queryLowerCase = query.toLowerCase();
        List<String> categoriesList = new ArrayList<>();
        List<Category> category = expenseService.getCategoriesList();
        for (Category categories : category) {
            categoriesList.add(categories.getCategory());
            log.info("Category is: " + categories.getCategory());
        }

        List<String> filteredExpenseNames = categoriesList.stream()
                .filter(t -> t.toLowerCase().startsWith(queryLowerCase))
                .collect(Collectors.toList());

        log.info("Filtered List Size: " + filteredExpenseNames.size());
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

    public Integer getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Integer category_id) {
        this.category_id = category_id;
    }
}
