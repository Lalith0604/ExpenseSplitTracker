package com.example.ExpenseSplitTracker.model;

import java.util.Map;

public class PercentageExpenseRequest {
    private String description;
    private double totalAmount;
    private String paidBy;
    private Map<String, Double> userPercentages; // Use Number instead of Double

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getPaidBy() { return paidBy; }
    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }

    public Map<String, Double> getUserPercentages() { return userPercentages; }
    public void setUserPercentages(Map<String, Double> userPercentages) { this.userPercentages = userPercentages; }
}
