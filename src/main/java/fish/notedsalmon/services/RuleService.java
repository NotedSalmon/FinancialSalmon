package fish.notedsalmon.services;

import fish.notedsalmon.entities.Category;
import fish.notedsalmon.entities.ExpensesRule;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

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

}
