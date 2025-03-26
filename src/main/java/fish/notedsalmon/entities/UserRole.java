package fish.notedsalmon.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "user_role")
public class UserRole {
    @Size(max = 32)
    @NotNull
    @Column(name = "user_id", nullable = false, length = 32)
    private String userId;

    @Size(max = 32)
    @NotNull
    @Column(name = "role_id", nullable = false, length = 32)
    private String roleId;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}