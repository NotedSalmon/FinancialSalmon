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
import java.util.ArrayList;
import java.util.List;
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
    private static final Logger LOGGER = Logger.getLogger(ChartUtil.class.getName());

    @PostConstruct
    public void init() {
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
        barModel = new BarChart()
                .setData(new BarData()
                        .addDataset(new BarDataset()
                                //Get data from db, new table?
                                .setData(450, 450, 450, 450, 450, 450, 450)
                                .setLabel("Budget")
                                .setBackgroundColor(new Color(255, 99, 132, 0.2))
                                .setBorderColor(new Color(255, 99, 132))
                                .setBorderWidth(1))
                        .addDataset(new BarDataset()
                                .setData(800, 300, 1200, 450, 433, 475, 200)
                                .setLabel("Expenses")
                                .setBackgroundColor(new Color(255, 159, 64, 0.2))
                                .setBorderColor(new Color(255, 159, 64))
                                .setBorderWidth(1)
                        )
                        .setLabels("January", "February", "March", "April", "May", "June", "July"))
                .setOptions(new BarOptions()
                        .setResponsive(true)
                        .setMaintainAspectRatio(false)
                        .setIndexAxis(BarOptions.IndexAxis.X)
                        .setPlugins(new Plugins()
                                .setTitle(new Title()
                                        .setDisplay(true)
                                        .setText("Budget Graph")))
                ).toJson();
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
