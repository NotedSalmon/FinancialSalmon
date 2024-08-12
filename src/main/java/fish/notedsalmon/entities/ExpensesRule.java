package fish.notedsalmon.entities;

import jakarta.persistence.*;
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
    private String expenseCategory;

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

    public String getExpenseCategory() {
        return expenseCategory;
    }

    public void setExpenseCategory(String expenseCategory) {
        this.expenseCategory = expenseCategory;
    }

/*
 TODO [Reverse Engineering] create field to map the 'isactive' column
 Available actions: Define target Java type | Uncomment as is | Remove column mapping
    @Column(name = "isactive", columnDefinition = "bit not null")
    private Object isactive;
*/
}