package fish.notedsalmon.utils;

import fish.notedsalmon.records.CategorySum;
import fish.notedsalmon.services.CalculationService;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import software.xdev.chartjs.model.charts.PieChart;
import software.xdev.chartjs.model.data.PieData;
import software.xdev.chartjs.model.dataset.PieDataset;
import software.xdev.chartjs.model.color.Color;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

@Named
@RequestScoped
public class PieChartUtil implements Serializable {

    @Inject
    CalculationService calculationService;

    private static final long serialVersionUID = 1L;

    private String pieModel;
    private static final Logger LOGGER = Logger.getLogger(PieChartUtil.class.getName());

    @PostConstruct
    public void init() {
        createPieModel();
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
                dataset.setBackgroundColor(colors);
            }

            dataset.addBackgroundColors(new Color(0xcc, 0x99, 0xff), new Color(213, 0xff, 0xff));
            dataset.setLabel("Expenses by Category");

            PieData data = new PieData().addDataset(dataset).setLabels(labels.toArray(new String[0]));
            pieModel = new PieChart().setData(data).toJson();

            LOGGER.log(Level.INFO, "Pie chart model created successfully: {0}", pieModel);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error creating pie chart model", e);
            pieModel = "{}";
        }
    }

    public String getPieModel() {
        return pieModel;
    }

    public void setPieModel(String pieModel) {
        this.pieModel = pieModel;
    }
}
