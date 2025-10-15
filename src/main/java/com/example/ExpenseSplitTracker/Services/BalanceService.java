package com.example.ExpenseSplitTracker.Services;


import com.example.ExpenseSplitTracker.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BalanceService {

    public Map<String, Double> viewBalances(Group group) {
        Map<String, Double> balances = new HashMap<>();
        for (User user : group.getUsers().values()) {
            balances.put(user.getName(), user.getBalance());
        }
        return balances;
    }

    public String settleDebt(Group group, String userName, double amount) {
        User user = group.getUsers().get(userName);
        if (user == null) return "User not found";
        if (amount > -user.getBalance()) return "❌ Cannot settle more than owed";

        user.setBalance(user.getBalance() + amount);
        return "✅ " + userName + " debt settled successfully.";
    }

    public List<String> simplifyDebts(Group group) {
        List<User> creditors = new ArrayList<>();
        List<User> debtors = new ArrayList<>();
        List<String> transactions = new ArrayList<>();

        for (User u : group.getUsers().values()) {
            if (u.getBalance() > 0) creditors.add(u);
            else if (u.getBalance() < 0) debtors.add(u);
        }

        int i = 0, j = 0;
        while (i < creditors.size() && j < debtors.size()) {
            User cr = creditors.get(i);
            User db = debtors.get(j);
            double settle = Math.min(cr.getBalance(), -db.getBalance());

            cr.setBalance(cr.getBalance() - settle);
            db.setBalance(db.getBalance() + settle);

            transactions.add(db.getName() + " pays " + settle + " to " + cr.getName());

            if (cr.getBalance() == 0) i++;
            if (db.getBalance() == 0) j++;
        }
        return transactions;
    }
}