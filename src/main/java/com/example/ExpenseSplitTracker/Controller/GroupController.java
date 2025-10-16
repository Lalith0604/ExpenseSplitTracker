package com.example.ExpenseSplitTracker.Controller;

import com.example.ExpenseSplitTracker.model.*;
import com.example.ExpenseSplitTracker.Services.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.annotation.Transactional;
import com.example.ExpenseSplitTracker.repository.UserRepository;
import com.example.ExpenseSplitTracker.repository.UserRepository;


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
    @Transactional
    public ResponseEntity<?> createGroup(@RequestParam String name) {
        try {
            Group group = groupService.createGroup(name);
            return ResponseEntity.ok(group);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error creating group: " + e.getMessage());
        }
    }


    @PostMapping("/group/{groupName}/user")
    public ResponseEntity<?> addUser(@PathVariable String groupName, @RequestParam String userName) {
        try {
            System.out.println("DEBUG: Adding user " + userName + " to group " + groupName);

            // Check if group exists first
            Group group = groupService.getGroup(groupName);
            if (group == null) {
                System.out.println("DEBUG: Group not found: " + groupName);
                return ResponseEntity.badRequest().body("Group '" + groupName + "' not found");
            }

            System.out.println("DEBUG: Group found: " + group.getName() + " (id=" + group.getId() + ")");

            // Add user to group
            User user = groupService.addUser(groupName, userName);
            if (user == null) {
                System.out.println("DEBUG: Failed to create user");
                return ResponseEntity.badRequest().body("Failed to add user to group");
            }

            System.out.println("DEBUG: User created: " + user.getName() + " (id=" + user.getId() + ")");
            return ResponseEntity.ok(user);

        } catch (Exception e) {
            System.out.println("DEBUG: Exception occurred: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error adding user: " + e.getMessage());
        }
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

    @GetMapping("/debug/groups")
    public ResponseEntity<?> debugGroups() {
        try {
            List<Group> allGroups = groupService.getAllGroups();
            Map<String, Object> debug = new HashMap<>();
            debug.put("totalGroups", allGroups.size());
            debug.put("groupNames", allGroups.stream().map(Group::getName).collect(Collectors.toList()));
            return ResponseEntity.ok(debug);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/debug/users")
    public ResponseEntity<?> debugUsers() {
        try {
            String result = groupService.debugUsers();
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }



}