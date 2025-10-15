package com.example.ExpenseSplitTracker.Services;

import com.example.ExpenseSplitTracker.model.*;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ExpenseService {

    public void addExpense(Group group, Expense expense) {
        String type = expense.getSplitType().toLowerCase();

        switch (type) {
            case "equal" -> addEqual(group, expense.getAmount());
            case "exact" -> addExact(group, expense.getSplitDetails());
            case "percentage" -> addPercentage(group, expense.getAmount(), expense.getSplitDetails());
            default -> throw new IllegalArgumentException("Invalid split type");
        }
    }

    private void addEqual(Group group, double amount) {
        int count = group.getUsers().size();
        double split = amount / count;

        for (User user : group.getUsers().values()) {
            user.setBalance(user.getBalance() - split);
        }
    }

    private void addExact(Group group, Map<String, Double> details) {
        for (Map.Entry<String, Double> e : details.entrySet()) {
            User user = group.getUsers().get(e.getKey());
            if (user != null)
                user.setBalance(user.getBalance() - e.getValue());
        }
    }

    private void addPercentage(Group group, double total, Map<String, Double> details) {
        for (Map.Entry<String, Double> e : details.entrySet()) {
            User user = group.getUsers().get(e.getKey());
            if (user != null) {
                double amt = (e.getValue() / 100) * total;
                user.setBalance(user.getBalance() - amt);
            }
        }
    }
}