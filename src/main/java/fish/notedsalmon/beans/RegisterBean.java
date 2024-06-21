package fish.notedsalmon.beans;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;

@Named
@RequestScoped
public class RegisterBean {

    private String username;
    private String email;
    private String password;
    private String fullName;

    public String register() {
        return "success";
    }


    /**
     * Getters and setters
     */

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return fullName;
    }

    public void setFirstName(String firstName) {
        this.fullName = firstName;
    }

}
