package fish.notedsalmon.beans;

import fish.notedsalmon.entities.Category;
import fish.notedsalmon.entities.Expenses;
import fish.notedsalmon.services.BankParserService;
import fish.notedsalmon.services.EditService;
import fish.notedsalmon.services.ExpenseService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
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

    @PostConstruct
    public void init() {
        expensesList = expenseService.getExpensesList();
        categoriesList = expenseService.getCategoriesList();

        addDefaultCategories();
    }

    private void addDefaultCategories() {
        String[] defaultCategories = {"ENTERTAINMENT", "UTILITIES", "FOOD", "SHOPPING","EMPTY"};
        for (String category : defaultCategories) {
            editService.addCategoryIfNotExists(category);
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
        return expensesList;
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

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
}