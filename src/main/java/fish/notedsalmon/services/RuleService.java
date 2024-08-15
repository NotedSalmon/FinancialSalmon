package fish.notedsalmon.services;

import fish.notedsalmon.entities.Category;
import fish.notedsalmon.entities.ExpensesRule;
import jakarta.ejb.Stateless;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
public class RuleService {

    @PersistenceContext
    private EntityManager em;


    public Boolean addRule(String expenseName, Integer category) {
        try {
            ExpensesRule rule = new ExpensesRule();
            rule.setExpenseDescription(expenseName);
            rule.setExpenseCategory(category);
            rule.setIsactive(1);
            em.persist(rule);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public Integer findRuleByExpenseName(String expenseName) {
        try {
            ExpensesRule rule = em.createQuery("SELECT r FROM ExpensesRule r WHERE r.expenseDescription = :expenseName", ExpensesRule.class)
                    .setParameter("expenseName", expenseName)
                    .getSingleResult();
            return rule.getExpenseCategory();
        } catch (Exception e) {
            return null;
        }
    }

    public Integer findCategoryByName(String categoryName) {
        try {
            Category category = em.createQuery("SELECT c FROM Category c WHERE c.category = :categoryName", Category.class)
                    .setParameter("categoryName", categoryName)
                    .getSingleResult();
            return category.getId();
        } catch (Exception e) {
            return null;
        }
    }

    @Transactional
    public void updateRule(ExpensesRule rule) {
        try {
            em.merge(rule);
        } catch (Exception e) {
            FacesMessage message = new FacesMessage("Disaster", " Failed to merge");
            FacesContext.getCurrentInstance().addMessage(null, message);
            throw new RuntimeException(e);
        }
    }

    public List<ExpensesRule> getRulesList() {
        return em.createQuery("SELECT r FROM ExpensesRule r", ExpensesRule.class).getResultList();
    }

}
