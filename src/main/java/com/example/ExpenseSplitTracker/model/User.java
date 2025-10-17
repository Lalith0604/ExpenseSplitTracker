package com.example.ExpenseSplitTracker.model;

public class User {
    private String name;
    private double balance;

    public User(String name) {
        this.name = name;
        this.balance = 0.0;
    }

    public User(String name,double balance) {
        this.name = name;
        this.balance = 0.0;
    }

    public String getName() { return name; }
    public double getBalance() { return balance; }

    public void addBalance(double amount) {
        this.balance += amount;
    }

    public void subtractBalance(double amount) {
        this.balance -= amount;
    }
}