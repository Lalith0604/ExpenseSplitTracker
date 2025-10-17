package com.example.ExpenseSplitTracker.Controller;

import com.example.ExpenseSplitTracker.datastore.DataStore;
import com.example.ExpenseSplitTracker.model.Group;
import com.example.ExpenseSplitTracker.model.PercentageExpenseRequest;
import com.example.ExpenseSplitTracker.model.User;
import com.example.ExpenseSplitTracker.Services.ExpenseService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping("/group/{groupName}")
    public String createGroup(@PathVariable String groupName) {
        return expenseService.createGroup(groupName);
    }

    @PostMapping("/group/{groupName}/addUser/{userName}")
    public String addUser(@PathVariable String groupName, @PathVariable String userName) {
        return expenseService.addUser(groupName, userName);
    }

    @GetMapping("/group/{groupName}/users")
    public Map<String, User> getUsers(@PathVariable String groupName) {
        return expenseService.getUsers(groupName);
    }

    // ✅ New endpoint: Add equal expense
    @PostMapping("/group/{groupName}/expense/equal")
    public String addEqualExpense(@PathVariable String groupName,
                                  @RequestParam String description,
                                  @RequestParam double amount,
                                  @RequestParam String paidBy) {
        return expenseService.addEqualExpense(groupName, description, amount, paidBy);
    }

    // endpoint: Add exact expense
    @PostMapping("/group/{groupName}/expense/exact")
    public String addExactExpense(@PathVariable String groupName,
                                  @RequestBody Map<String, Object> payload) {

        try {
            String description = (String) payload.get("description");
            double totalAmount = Double.parseDouble(payload.get("totalAmount").toString());
            String paidBy = (String) payload.get("paidBy");

            // Convert inner map safely
            Map<String, Double> userAmounts = new HashMap<>();
            Map<String, Object> rawUserAmounts = (Map<String, Object>) payload.get("userAmounts");

            for (Map.Entry<String, Object> entry : rawUserAmounts.entrySet()) {
                userAmounts.put(entry.getKey(), Double.parseDouble(entry.getValue().toString()));
            }

            return expenseService.addExactExpense(groupName, description, totalAmount, paidBy, userAmounts);

        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing request: " + e.getMessage();
        }
    }

    // ✅ New endpoint: Add percentage expense
    @PostMapping("/group/{groupName}/expense/percentage")
    public String addPercentageExpense(@PathVariable String groupName,
                                       @RequestBody PercentageExpenseRequest request) {

        return expenseService.addPercentageExpense(
                groupName,
                request.getDescription(),
                request.getTotalAmount(),
                request.getPaidBy(),
                request.getUserPercentages()
        );
    }

    @PostMapping("/group/{groupName}/settle")
    public String settleDebt(@PathVariable String groupName,
                             @RequestParam String fromUser,
                             @RequestParam String toUser,
                             @RequestParam double amount) {

        return expenseService.settleDebt(groupName, fromUser, toUser, amount);
    }

    //simplify debts
    @GetMapping("/group/{groupName}/simplify")
    public String simplifyDebts(@PathVariable String groupName) {
        return expenseService.simplifyDebts(groupName);
    }

    //dashboard
    @GetMapping("/group/{groupName}/dashboard")
    public Map<String, Object> getDashboard(@PathVariable String groupName) {
        Group group = DataStore.groups.get(groupName);
        Map<String, Object> dashboard = new HashMap<>();
        if (group == null) {
            dashboard.put("error", "Group not found");
            return dashboard;
        }

        // Current balances
        Map<String, Double> balances = new HashMap<>();
        for (User u : group.getUsers().values()) {
            balances.put(u.getName(), u.getBalance());
        }

        dashboard.put("balances", balances);
        dashboard.put("expenses", group.getExpenses());
        dashboard.put("settlements", group.getSettlements());

        // Optional: simplified debts
        dashboard.put("simplifiedDebts", expenseService.simplifyDebts(groupName));

        return dashboard;
    }

    // ✅ View balances
    @GetMapping("/group/{groupName}/balances")
    public String viewBalances(@PathVariable String groupName) {
        return expenseService.viewBalances(groupName);
    }
}