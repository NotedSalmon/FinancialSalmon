package fish.notedsalmon.services;

import fish.notedsalmon.entities.Category;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jdk.jfr.Name;

import java.util.List;

@Named
@ApplicationScoped
public class EditService {

    @PersistenceContext
    private EntityManager em;

    public List<Category> getCategoriesList() {
        return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    }

    @Transactional
    public void addCategoryIfNotExists(String categoryName) {
        try {
            Category existingCategory = em.createQuery("SELECT c FROM Category c WHERE c.category = :category", Category.class)
                    .setParameter("category", categoryName)
                    .getSingleResult();
        } catch (NoResultException e) {
            Category newCategory = new Category();
            newCategory.setCategory(categoryName);
            em.persist(newCategory);
        }
    }

    public Category findCategoryById(Integer id) {
        return em.find(Category.class, id);
    }
}
