package com.example.ExpenseSplitTracker.model;

import java.util.HashMap;
import java.util.Map;

public class Expense {
    private String description;
    private double amount;
    private String paidBy;
    private SplitType splitType;
    private Map<String, Double> splits = new HashMap<>(); // user → amount owed

    public Expense(String description, double amount, String paidBy, SplitType splitType) {
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splitType = splitType;
    }

    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public String getPaidBy() { return paidBy; }
    public SplitType getSplitType() { return splitType; }
    public Map<String, Double> getSplits() { return splits; }
}