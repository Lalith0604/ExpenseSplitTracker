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

    private void addEqual(Group group, Double amount) {
        int count = group.getUsers().size();
        if (count == 0) return;

        double split = amount / count;
        // ✅ Fixed: Use group.getUsers() instead of .values()
        for (User user : group.getUsers()) {
            user.setBalance(user.getBalance() - split);
        }
    }

    private void addExact(Group group, Map<String, Double> details) {
        for (Map.Entry<String, Double> entry : details.entrySet()) {
            String userName = entry.getKey();
            Double amount = entry.getValue();

            // ✅ Fixed: Find user by name instead of Map.get()
            User user = findUserByName(group, userName);
            if (user != null) {
                user.setBalance(user.getBalance() - amount);
            }
        }
    }

    private void addPercentage(Group group, Double total, Map<String, Double> details) {
        for (Map.Entry<String, Double> entry : details.entrySet()) {
            String userName = entry.getKey();
            Double percentage = entry.getValue();

            // ✅ Fixed: Find user by name instead of Map.get()
            User user = findUserByName(group, userName);
            if (user != null) {
                double amount = (percentage / 100.0) * total;
                user.setBalance(user.getBalance() - amount);
            }
        }
    }

    // ✅ NEW: Helper method to find user by name in List<User>
    private User findUserByName(Group group, String userName) {
        return group.getUsers().stream()
                .filter(user -> user.getName().equals(userName))
                .findFirst()
                .orElse(null);
    }
}
