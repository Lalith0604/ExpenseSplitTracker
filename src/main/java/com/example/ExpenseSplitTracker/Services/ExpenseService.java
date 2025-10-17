package com.example.ExpenseSplitTracker.Services;

import com.example.ExpenseSplitTracker.datastore.DataStore;
import com.example.ExpenseSplitTracker.model.*;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ExpenseService {

    // Create group
    public String createGroup(String groupName) {
        if (DataStore.groups.containsKey(groupName)) {
            return "Group already exists!";
        }
        DataStore.groups.put(groupName, new Group(groupName));
        return "Group '" + groupName + "' created successfully!";
    }

    // Add user
    public String addUser(String groupName, String userName) {
        Group group = DataStore.groups.get(groupName);
        if (group == null) return "Group not found!";
        if (group.getUsers().containsKey(userName)) return "User already in group!";
        group.addUser(new User(userName));
        return "User '" + userName + "' added to group '" + groupName + "'";
    }

    // View users
    public Map<String, User> getUsers(String groupName) {
        Group group = DataStore.groups.get(groupName);
        if (group == null) return null;
        return group.getUsers();
    }

    // ✅ Add expense (Equal Split)
    public String addEqualExpense(String groupName, String description, double amount, String paidBy) {
        Group group = DataStore.groups.get(groupName);
        if (group == null) return "Group not found!";
        if (!group.getUsers().containsKey(paidBy)) return "Payer not found in group!";

        int totalMembers = group.getUsers().size();
        if (totalMembers == 0) return "No users in group!";

        double splitAmount = amount / totalMembers;

        Expense expense = new Expense(description, amount, paidBy, SplitType.EQUAL);

        // Calculate split
        for (User user : group.getUsers().values()) {
            expense.getSplits().put(user.getName(), splitAmount);
            if (user.getName().equals(paidBy)) {
                // Payer paid the whole amount, so others owe them
                user.addBalance(amount - splitAmount);
            } else {
                // Non-payers owe their share
                user.subtractBalance(splitAmount);
            }
        }

        group.addExpense(expense);
        return "Expense added successfully! (" + description + " - split equally)";
    }

    // ✅ Add expense (Exact Split)
    public String addExactExpense(String groupName, String description,
                                  double totalAmount, String paidBy,
                                  Map<String, Double> userAmounts) {
        Group group = DataStore.groups.get(groupName);
        if (group == null) return "Group not found!";
        if (!group.getUsers().containsKey(paidBy)) return "Payer not found in group!";
        if (group.getUsers().size() == 0) return "No users in group!";

        // Validate total amount
        double sum = 0.0;
        for (double val : userAmounts.values()) sum += val;
        if (Math.abs(sum - totalAmount) > 0.001)
            return "Error: Split amounts (" + sum + ") do not match total expense (" + totalAmount + ")";

        Expense expense = new Expense(description, totalAmount, paidBy, SplitType.EXACT);

        // Store split details
        expense.getSplits().putAll(userAmounts);

        // Update balances
        for (Map.Entry<String, Double> entry : userAmounts.entrySet()) {
            String userName = entry.getKey();
            double amount = entry.getValue();

            User user = group.getUsers().get(userName);
            if (user == null) return "User " + userName + " not found in group!";

            if (userName.equals(paidBy)) {
                // Payer spent more, so they get reimbursed by others
                user.addBalance(totalAmount - amount);
            } else {
                // Non-payer owes their share
                user.subtractBalance(amount);
            }
        }

        group.addExpense(expense);
        return "Exact split expense added successfully! (" + description + ")";
    }

    // Percentage Split
    public String addPercentageExpense(String groupName, String description,
                                       double totalAmount, String paidBy,
                                       Map<String, Double> userPercentages) {

        Group group = DataStore.groups.get(groupName);
        if (group == null) return "Group not found!";
        if (!group.getUsers().containsKey(paidBy)) return "Payer not found in group!";

        // Validate percentages sum to 100
        double sumPercent = 0.0;
        for (double pct : userPercentages.values()) sumPercent += pct;
        if (Math.abs(sumPercent - 100.0) > 0.001)
            return "Error: Percentages must add up to 100. Current sum: " + sumPercent;

        Expense expense = new Expense(description, totalAmount, paidBy, SplitType.PERCENTAGE);

        // Calculate actual amount for each user
        Map<String, Double> userAmounts = new HashMap<>();
        for (Map.Entry<String, Double> entry : userPercentages.entrySet()) {
            String userName = entry.getKey();
            double pct = entry.getValue();
            double amount = totalAmount * pct / 100.0;
            userAmounts.put(userName, amount);
        }

        expense.getSplits().putAll(userAmounts);

        // Update balances
        for (Map.Entry<String, Double> entry : userAmounts.entrySet()) {
            String user = entry.getKey();
            double amount = entry.getValue();
            if (!group.getUsers().containsKey(user)) return "User " + user + " not found in group!";
            if (user.equals(paidBy)) {
                group.getUsers().get(user).addBalance(totalAmount - amount);
            } else {
                group.getUsers().get(user).subtractBalance(amount);
            }
        }

        group.addExpense(expense);
        return "Percentage split expense added successfully! (" + description + ")";
    }

    // ✅ Settle debt between a user and the payer
    public String settleDebt(String groupName, String fromUser, String toUser, double amount) {
        Group group = DataStore.groups.get(groupName);
        if (group == null) return "Group not found!";
        if (!group.getUsers().containsKey(fromUser)) return fromUser + " not found!";
        if (!group.getUsers().containsKey(toUser)) return toUser + " not found!";

        User payer = group.getUsers().get(toUser);
        User debtor = group.getUsers().get(fromUser);

        double owedAmount = -debtor.getBalance(); // debtor balance negative if owes money
        if (Math.abs(owedAmount) < 0.001) owedAmount = 0.0;

        if (amount - owedAmount > 0.001)
            return "Cannot settle more than owed amount (" + owedAmount + ")";

        // Update balances
        debtor.addBalance(amount);   // reduce debt
        payer.subtractBalance(amount); // payer receives payment

        // Log settlement
        group.addSettlement(fromUser + " paid $" + amount + " to " + toUser);

        return fromUser + " settled $" + amount + " to " + toUser + " successfully!";
    }

    // ✅ Simplify debts in a group
    public String simplifyDebts(String groupName) {
        Group group = DataStore.groups.get(groupName);
        if (group == null) return "Group not found!";

        List<User> positive = new ArrayList<>();
        List<User> negative = new ArrayList<>();

        for (User u : group.getUsers().values()) {
            double b = u.getBalance();
            if (b > 0.001) positive.add(u); // use original object
            else if (b < -0.001) negative.add(u);
        }

        List<String> transactions = new ArrayList<>();
        int i = 0, j = 0;

        while (i < positive.size() && j < negative.size()) {
            User creditor = positive.get(i);
            User debtor = negative.get(j);

            double min = Math.min(creditor.getBalance(), -debtor.getBalance());
            if (min < 0.001) break;

            transactions.add(debtor.getName() + " pays $" + min + " to " + creditor.getName());

            // ✅ Update original balances
            creditor.subtractBalance(min);
            debtor.addBalance(min);

            if (Math.abs(creditor.getBalance()) < 0.001) i++;
            if (Math.abs(debtor.getBalance()) < 0.001) j++;
        }

        if (transactions.isEmpty()) return "No debts to simplify!";

        return String.join("\n", transactions);
    }

    // View all user balances in a group
    public String viewBalances(String groupName) {
        Group group = DataStore.groups.get(groupName);
        if (group == null) return "Group not found!";
        StringBuilder sb = new StringBuilder();
        sb.append("Balances for group ").append(groupName).append(":\n");
        for (User u : group.getUsers().values()) {
            sb.append(u.getName()).append(": ").append(u.getBalance()).append("\n");
        }
        return sb.toString();
    }
}