package com.example.ExpenseSplitTracker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Group {
    private String name;
    private Map<String, User> users = new HashMap<>();
    private List<Expense> expenses = new ArrayList<>();
    private List<String> settlements=new ArrayList<>();

    public Group(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public Map<String, User> getUsers() { return users; }
    public List<Expense> getExpenses() { return expenses; }
    public List<String> getSettlements(){return settlements;}

    public void addUser(User user) {
        users.put(user.getName(), user);
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void addSettlement(String settlement){
        settlements.add(settlement);
    }
}