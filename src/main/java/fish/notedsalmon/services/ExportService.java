package fish.notedsalmon.services;

import fish.notedsalmon.entities.Expenses;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static java.lang.System.out;

public class ExportService {

    private static final Log log = LogFactory.getLog(ExportService.class);
    @Inject
    ExpenseService expenseService;

    public Boolean exportDataToExcel() {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Expenses");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Date");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("Category");
        headerRow.createCell(3).setCellValue("Price");

        List<Expenses> expensesList = expenseService.getExpensesList();
        int rowIndex = 1;
        for (Expenses expense : expensesList) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(expense.getExpense_date().toString());
            row.createCell(1).setCellValue(expense.getDescription());
            row.createCell(2).setCellValue(expense.getCategory().getCategory());
            row.createCell(3).setCellValue(expense.getAmount().doubleValue());
        }

        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename=expenses.xlsx");

        try (FileOutputStream outputStream = new FileOutputStream("expenses.xlsx")) {
            workbook.write(outputStream);
            workbook.close();
            return true;
        } catch (IOException e) {
            log.error("Error writing excel file", e);
            return false;
        }

    }
}
