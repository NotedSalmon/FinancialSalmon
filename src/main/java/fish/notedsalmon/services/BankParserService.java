package fish.notedsalmon.services;

import fish.notedsalmon.entities.Expenses;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.persistence.EntityManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankParserService {

    private EntityManager em;

    public List<Expenses> parseAndSaveExpenses(InputStream inputStream) {
        List<Expenses> expensesList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            String[] headers = reader.readLine().split(",");
            Map<String, Integer> columnIndexes = new HashMap<>();
            while ((line = reader.readLine()) != null) {

            }
        } catch (IOException e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Error",e.getMessage()));
        }
        return expensesList;
    }
}
