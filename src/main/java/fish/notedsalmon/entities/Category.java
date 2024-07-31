package fish.notedsalmon.entities;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "categories", uniqueConstraints = @UniqueConstraint(columnNames = "category"))
public class Category implements Comparable<Category> {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "category", nullable = false, length = 50, unique = true)
    private String category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int compareTo(Category other) {
        return this.category.compareTo(other.category);
    }

    @Override
    public String toString() {
        return this.category;
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        if (!(object instanceof Category)) return false;

        // Property checks.
        Category other = (Category) object;
        return Objects.equals(category, other.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category);
    }

}