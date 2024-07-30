package fish.notedsalmon.beans;

import fish.notedsalmon.entities.Expenses;
import fish.notedsalmon.services.BankParserService;
import fish.notedsalmon.services.ExpenseService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.file.UploadedFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Named
@RequestScoped
public class ImportBean {

    @EJB
    private BankParserService bankParserService;

    @Inject
    ExpenseService expenseService;

    private UploadedFile file;
    private List<Expenses> expensesList;

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

    @PostConstruct
    public void init() {
        expensesList = expenseService.getExpensesList();
    }

    public List<Expenses> getExpenses() {
        return expensesList;
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
