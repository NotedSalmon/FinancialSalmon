package fish.notedsalmon.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

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

    @Column(name = "colour_red")
    private Integer colourRed;

    @Column(name = "colour_green")
    private Integer colourGreen;

    @Column(name = "colour_blue")
    private Integer colourBlue;

    public Integer getColourBlue() {
        return colourBlue;
    }

    public void setColourBlue(Integer colourBlue) {
        this.colourBlue = colourBlue;
    }

    public Integer getColourGreen() {
        return colourGreen;
    }

    public void setColourGreen(Integer colourGreen) {
        this.colourGreen = colourGreen;
    }

    public Integer getColourRed() {
        return colourRed;
    }

    public void setColourRed(Integer colourRed) {
        this.colourRed = colourRed;
    }

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