package fish.notedsalmon.services;


import fish.notedsalmon.entities.User;
import fish.notedsalmon.entities.UserRole;
import fish.notedsalmon.utils.PasswordHasher;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
@LocalBean
public class RegisterService {

    @PersistenceContext
    private EntityManager em;

    public Boolean createNewUser(String firstName, String lastName, String username, String email, String password) {
        if (!checkUniqueUser(username)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Username Taken", "Registration Failed"));
            return false;
        }
        try {
            String hashedPassword = PasswordHasher.hashPassword(password);
            User createNewUser = new User(firstName,lastName,username,email,hashedPassword);
            em.persist(createNewUser);

            UserRole role = new UserRole();
            role.setUserId(username);
            role.setRoleId("USER");
            em.persist(role);

        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean checkUniqueUser(String username){
        try {
            User user = em.createNamedQuery(User.FIND_BY_USERNAME, User.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return false;
        } catch (jakarta.persistence.NoResultException e) {
            return true;
        }
    }
}
