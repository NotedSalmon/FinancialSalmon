package fish.notedsalmon.services;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import fish.notedsalmon.entities.Category;
import fish.notedsalmon.entities.Expenses;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class BankParserService {

    @PersistenceContext
    private EntityManager em;

    /**
     * Transactional annotation ensures that the database operations are handled within a transaction.
     * The description, amount and date are extracted from the CSV and then added to the database.
     * @param inputStream
     * @return
     */

    /**
     * TO:DO - Change code so it searches the headers for fields that contain description, amount etc...
     */
    public List<Expenses> parseAndSaveExpenses(InputStream inputStream) {
        List<Expenses> expensesList = new ArrayList<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> records = reader.readAll();
            boolean skipHeaders = true;

            Category defaultCategory = em.find(Category.class, 5);

            for (String[] record : records) {
                if (skipHeaders) {
                    skipHeaders = false;
                    continue;
                }
                //attaches header index to variables
                String description = record[4];

                BigDecimal amount;
                //Checks if the 5 index column is not null and not empty
                if (record[5] != null && !record[5].isEmpty()) {
                    amount = new BigDecimal(record[5]);
                } else {amount = BigDecimal.valueOf(0);} //if it is empty then sets the value to 0

                LocalDate date = null;
                if (record[0] != null && !record[0].trim().isEmpty()) {
                    try {
                        date = LocalDate.parse(record[0].trim(), dateFormatter);
                    } catch (DateTimeParseException e) {
                        System.err.println("Skipping record due to date parsing error: " + e.getMessage());
                        continue;
                    }
                } else {
                    System.err.println("Skipping record due to empty date field");
                    continue;
                }

                LocalDateTime localDateTime = date.atStartOfDay();

                //Adds data into an expense object and adds to DB
                Expenses expense = new Expenses();
                expense.setDescription(description);
                expense.setAmount(amount);
                expense.setExpense_date(localDateTime);
                expense.setCategory(defaultCategory);

                em.persist(expense);
                expensesList.add(expense);
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        return expensesList;
    }
}
