// LoginBean.java
package fish.notedsalmon.beans;

import fish.notedsalmon.utils.PasswordHasher;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.security.Principal;

@Named
@SessionScoped
public class LoginBean implements Serializable {
    private String username;
    private String password;

    public String login() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.login(username, password);
            return "expenses.xhtml?faces-redirect=true";
        } catch (Exception e) {
            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Login Failed: "+e.getMessage(), "Invalid credentials"));
            return null;
        }
    }

    public String logout() {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        try {
            request.logout();
            context.getExternalContext().invalidateSession();
            return "index.xhtml?faces-redirect=true";
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *  GETTERS & SETTERS
     */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
