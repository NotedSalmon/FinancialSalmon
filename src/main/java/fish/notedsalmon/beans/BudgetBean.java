package fish.notedsalmon.beans;

import fish.notedsalmon.services.ExpenseService;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import software.xdev.chartjs.model.data.BarData;
import software.xdev.chartjs.model.dataset.BarDataset;
import software.xdev.chartjs.model.options.BarOptions;
import software.xdev.chartjs.model.options.Plugins;
import software.xdev.chartjs.model.options.scale.Scales;

import java.io.Serializable;
import java.math.BigDecimal;

@Named
@RequestScoped
public class BudgetBean implements Serializable {

    @Inject
    private ExpenseService expenseService;

    private double monthlyBudget;
    private double totalExpenses;
    private double remainingBudget;

    @PostConstruct
    public void init() {
        totalExpenses = expenseService.getTotalExpenses();
        remainingBudget = 0.0;
    }

    public void calculateBudget() {
        remainingBudget = monthlyBudget - totalExpenses;
    }

    // Getters and Setters

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public double getTotalExpenses() {
        return totalExpenses;
    }

    public double getRemainingBudget() {
        return remainingBudget;
    }
}
