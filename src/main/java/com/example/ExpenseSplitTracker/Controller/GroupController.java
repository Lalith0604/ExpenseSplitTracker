package com.example.ExpenseSplitTracker.Controller;

import com.example.ExpenseSplitTracker.model.*;
import com.example.ExpenseSplitTracker.Services.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
public class GroupController {

    private final GroupService groupService;
    private final ExpenseService expenseService;
    private final BalanceService balanceService;

    public GroupController(GroupService groupService, ExpenseService expenseService, BalanceService balanceService) {
        this.groupService = groupService;
        this.expenseService = expenseService;
        this.balanceService = balanceService;
    }



    @PostMapping("/group")
    public Group createGroup(@RequestParam String name) {
        return groupService.createGroup(name);
    }

    @PostMapping("/group/{groupName}/user")
    public User addUser(@PathVariable String groupName, @RequestParam String userName) {
        return groupService.addUser(groupName, userName);
    }

    @PostMapping("/group/{groupName}/expense")
    public String addExpense(@PathVariable String groupName, @RequestBody Expense expense) {
        Group group = groupService.getGroup(groupName);
        if (group == null) return "Group not found";
        expenseService.addExpense(group, expense);
        return "Expense added successfully.";
    }

    @GetMapping("/group/{groupName}/balances")
    public Map<String, Double> viewBalances(@PathVariable String groupName) {
        Group group = groupService.getGroup(groupName);
        return balanceService.viewBalances(group);
    }

    @PostMapping("/group/{groupName}/settle")
    public String settleDebt(@PathVariable String groupName,
                             @RequestParam String userName,
                             @RequestParam double amount) {
        Group group = groupService.getGroup(groupName);
        return balanceService.settleDebt(group, userName, amount);
    }

    @PostMapping("/group/{groupName}/simplify")
    public List<String> simplify(@PathVariable String groupName) {
        Group group = groupService.getGroup(groupName);
        return balanceService.simplifyDebts(group);
    }
}