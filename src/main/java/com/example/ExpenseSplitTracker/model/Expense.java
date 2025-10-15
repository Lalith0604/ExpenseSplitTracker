package com.example.ExpenseSplitTracker.model;

import lombok.*;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Expense {
    private double amount;
    private String splitType; // equal, exact, percentage
    private Map<String, Double> splitDetails; // userName -> share (amount or percentage)

    public Expense() {}

    public Expense(double amount, String splitType, Map<String, Double> splitDetails) {
        this.amount = amount;
        this.splitType = splitType;
        this.splitDetails = splitDetails;
    }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getSplitType() { return splitType; }
    public void setSplitType(String splitType) { this.splitType = splitType; }

    public Map<String, Double> getSplitDetails() { return splitDetails; }
    public void setSplitDetails(Map<String, Double> splitDetails) { this.splitDetails = splitDetails; }
}

