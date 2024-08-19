package fish.notedsalmon.beans;

import fish.notedsalmon.services.ExportService;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@Named
@RequestScoped
public class ExportBean {

    @Inject
    ExportService exportService;

    public void exportToExcel() {
        if (exportService.exportDataToExcel()){
            FacesMessage message = new FacesMessage("Successful",  " File downloaded");
            FacesContext.getCurrentInstance().addMessage(null, message);
        } else {
            FacesMessage message = new FacesMessage("Failed",  " File not downloaded");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }

    }

}
