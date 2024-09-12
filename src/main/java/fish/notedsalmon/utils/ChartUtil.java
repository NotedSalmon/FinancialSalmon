package fish.notedsalmon.utils;

import fish.notedsalmon.beans.BudgetBean;
import fish.notedsalmon.records.CategorySum;
import fish.notedsalmon.services.CalculationService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import software.xdev.chartjs.model.charts.BarChart;
import software.xdev.chartjs.model.charts.PieChart;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.data.PieData;
import software.xdev.chartjs.model.dataset.BarDataset;
import software.xdev.chartjs.model.dataset.PieDataset;
import software.xdev.chartjs.model.color.Color;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.Plugins;
import software.xdev.chartjs.model.options.Title;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

@Named
@RequestScoped
public class ChartUtil implements Serializable {

    @Inject
    CalculationService calculationService;

    @Inject
    BudgetBean budgetBean;

    private static final long serialVersionUID = 1L;

    private String pieModel;
    private String barModel;
    private Double budget;
    private Double defaultBudget;
    private static final Logger LOGGER = Logger.getLogger(ChartUtil.class.getName());

    @PostConstruct
    public void init() {
        defaultBudget = 450.00;
        createPieModel();
        createBarModel();
    }

    private void createPieModel() {
        try {
            List<CategorySum> summedCategories = calculationService.getSummedCategories();

            if (summedCategories == null || summedCategories.isEmpty()) {
                LOGGER.log(Level.WARNING, "No categories to display in pie chart");
                pieModel = "{}";
                return;
            }

            PieDataset dataset = new PieDataset();
            List<String> labels = new ArrayList<>();
            List<Color> colors = new ArrayList<>();

            for (CategorySum categorySum : summedCategories) {
                dataset.addData(categorySum.sum());
                labels.add(categorySum.categoryName());
                colors.add(new Color(categorySum.red(), categorySum.green(), categorySum.blue()));
                //dataset.setBackgroundColor(colors);
            }

            dataset.setBackgroundColor(colors);
            dataset.setLabel("Expenses by Category");

            PieData data = new PieData().addDataset(dataset).setLabels(labels.toArray(new String[0]));
            pieModel = new PieChart().setData(data).toJson();

            LOGGER.log(Level.INFO, "Pie chart model created successfully: {0}", pieModel);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating pie chart model", e);
            pieModel = "{}";
        }
    }

    public void createBarModel() {
        budget = budgetBean.getMonthlyBudget();
        BarData barData = new BarData();
        BarDataset budgetDataset = new BarDataset();
        BarDataset expensesDataset = new BarDataset();

        Map<YearMonth, BigDecimal> monthlyExpenses = calculationService.getMonthlyExpenses();
        List<Number> expensesList = new ArrayList<>();
        List<Number> budgetsList = new ArrayList<>();
        List<String> monthList = new ArrayList<>();

        //Stores map values of monthlyExpenses to the array which then gets called later in the .setData
        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(LocalDate.now().getYear(), month);
            BigDecimal expense = monthlyExpenses.getOrDefault(yearMonth, BigDecimal.ZERO);
            expensesList.add(expense.doubleValue());
            budgetsList.add(defaultBudget);
            String monthName = yearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            monthList.add(monthName);
        }

        budgetDataset.setData(budgetsList)
                .setLabel("Budget")
                .setBackgroundColor(new Color(255, 99, 132, 0.2))
                .setBorderColor(new Color(255, 99, 132))
                .setBorderWidth(1);



        expensesDataset.setData(expensesList)
                .setLabel("Expenses")
                .setBackgroundColor(new Color(255, 159, 64, 0.2))
                .setBorderColor(new Color(255, 159, 64))
                .setBorderWidth(1);

        //Use a for loop to populate values

        barData.addDataset(budgetDataset);
        barData.addDataset(expensesDataset);
        barData.setLabels(monthList);

        BarOptions barOptions = new BarOptions();
        barOptions.setResponsive(true)
                .setMaintainAspectRatio(false)
                .setIndexAxis(BarOptions.IndexAxis.X);

        Plugins plugins = new Plugins();
        Title title = new Title();
        title.setDisplay(true)
                .setText("Budget Bar Chart");
        plugins.setTitle(title);

        barOptions.setPlugins(plugins);

        BarChart barChart = new BarChart();
        barChart.setData(barData)
                .setOptions(barOptions);

        barModel = barChart.toJson();
    }

    public String getPieModel() {
        return pieModel;
    }

    public void setPieModel(String pieModel) {
        this.pieModel = pieModel;
    }

    public String getBarModel() {
        return barModel;
    }

    public void setBarModel(String barModel) {
        this.barModel = barModel;
    }
}
