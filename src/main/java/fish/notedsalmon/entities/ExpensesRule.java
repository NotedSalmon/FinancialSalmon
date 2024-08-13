package fish.notedsalmon.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "expenses_rules")
public class ExpensesRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "expense_description", nullable = false)
    private String expenseDescription;

    @NotNull
    @Column(name = "expense_category", nullable = false)
    private Integer expenseCategory;

    @Column(name = "isactive")
    private Integer isactive;

    public Integer getIsactive() {
        return isactive;
    }

    public void setIsactive(Integer isactive) {
        this.isactive = isactive;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExpenseDescription() {
        return expenseDescription;
    }

    public void setExpenseDescription(String expenseDescription) {
        this.expenseDescription = expenseDescription;
    }

    public Integer getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(Integer expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

}