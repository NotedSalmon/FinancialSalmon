package fish.notedsalmon.beans;

import fish.notedsalmon.entities.Category;
import fish.notedsalmon.entities.Expenses;
import fish.notedsalmon.exceptions.NotFoundException;
import fish.notedsalmon.services.BankParserService;
import fish.notedsalmon.services.EditService;
import fish.notedsalmon.services.ExpenseService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.el.MethodExpression;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ImportBean implements Serializable {

    @EJB
    private BankParserService bankParserService;

    @Inject
    ExpenseService expenseService;

    @Inject
    EditService editService;

    private UploadedFile file;
    private List<Expenses> expensesList;
    private List<Category> categoriesList;
    private int rowCount = 100;
    private int firstResult = 0;
    private long totalExpenseCount;

    @PostConstruct
    public void init() {
        expensesList = expenseService.getExpensesListPaginated();
        categoriesList = expenseService.getCategoriesList();
        totalExpenseCount = expenseService.countExpenses();
        addDefaultCategories();
    }


    /**
     * Creates default classes with colours
     */
    private void addDefaultCategories() {
        Object[][] defaultCategories = {
                {"ENTERTAINMENT", 128, 0, 128},  // Purple
                {"UTILITIES", 255, 255, 0},      // Yellow
                {"FOOD", 0, 128, 0},             // Green
                {"SHOPPING", 173, 216, 230},     // Light Blue
                {"EMPTY", 255, 0, 0}             // Red
        };

        for (Object[] category : defaultCategories) {
            editService.addCategoryIfNotExists((String) category[0], (int) category[1], (int) category[2], (int) category[3]);
        }
        categoriesList = expenseService.getCategoriesList();
    }

    public void upload() {
        if (file != null) {
            try (InputStream inputStream = file.getInputStream()) {
                expensesList = bankParserService.parseAndSaveExpenses(inputStream);
                FacesMessage message = new FacesMessage("Successful", file.getFileName() + " is uploaded and processed");
                FacesContext.getCurrentInstance().addMessage(null, message);
            } catch (IOException e) {
                FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "File processing failed");
                FacesContext.getCurrentInstance().addMessage(null, message);
            }
        }
    }

    public List<Category> getCategoriesList() {
        return expenseService.getCategoriesList();
    }

    public List<Expenses> getExpenses() {
        return expenseService.getExpensesListPaginated();
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            int rowIndex = event.getRowIndex();
            Expenses expense = expensesList.get(rowIndex);
            expense.setCategory((Category) newValue);

            expenseService.updateExpense(expense);

            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Expense Changed", "Old: " + oldValue + ", New: " + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        this.file = event.getFile();
        upload();
    }

    public void clearTable() {
        try {
            expenseService.truncateExpenses();
            expensesList = expenseService.getExpensesList();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Table Cleared", "All expenses have been removed."));
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Failed to clear table", "All expenses have been removed."));
        }


    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public void deleteExpense(int id) throws NotFoundException {
        expenseService.deleteExpense(id);
    }
    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
        this.firstResult = 0;  // Reset to the first page
        if (rowCount == 0) {
            expensesList = expenseService.getExpensesList();  // Get all records if rowCount is 0
        } else {
            expensesList = expenseService.getExpensesListPaginated();
        }
    }

    public long getTotalExpenseCount() {
        return expenseService.countExpenses();
    }

    public void setTotalExpenseCount(long totalExpenseCount) {
        this.totalExpenseCount = totalExpenseCount;
    }
}