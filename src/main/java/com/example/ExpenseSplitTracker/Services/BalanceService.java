package com.example.ExpenseSplitTracker.Services;

import com.example.ExpenseSplitTracker.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BalanceService {

    public Map<String, Double> viewBalances(Group group) {
        Map<String, Double> balances = new HashMap<>();
        // ✅ Fixed: Use group.getUsers() instead of .values()
        for (User user : group.getUsers()) {
            balances.put(user.getName(), user.getBalance());
        }
        return balances;
    }

    public String settleDebt(Group group, String userName, Double amount) {
        // ✅ Fixed: Find user by name instead of Map.get()
        User user = findUserByName(group, userName);
        if (user == null) return "User not found";

        if (amount > -user.getBalance()) return "❌ Cannot settle more than owed";

        user.setBalance(user.getBalance() + amount);
        return "✅ " + userName + " debt settled successfully.";
    }

    public List<String> simplifyDebts(Group group) {
        List<User> creditors = new ArrayList<>();
        List<User> debtors = new ArrayList<>();
        List<String> transactions = new ArrayList<>();

        // ✅ Fixed: Use group.getUsers() instead of .values()
        for (User user : group.getUsers()) {
            if (user.getBalance() > 0) creditors.add(user);
            else if (user.getBalance() < 0) debtors.add(user);
        }

        int i = 0, j = 0;
        while (i < creditors.size() && j < debtors.size()) {
            User creditor = creditors.get(i);
            User debtor = debtors.get(j);

            double settle = Math.min(creditor.getBalance(), -debtor.getBalance());

            creditor.setBalance(creditor.getBalance() - settle);
            debtor.setBalance(debtor.getBalance() + settle);

            transactions.add(debtor.getName() + " pays " + settle + " to " + creditor.getName());

            if (creditor.getBalance() == 0) i++;
            if (debtor.getBalance() == 0) j++;
        }

        return transactions;
    }

    // ✅ NEW: Helper method to find user by name in List<User>
    private User findUserByName(Group group, String userName) {
        return group.getUsers().stream()
                .filter(user -> user.getName().equals(userName))
                .findFirst()
                .orElse(null);
    }
}
