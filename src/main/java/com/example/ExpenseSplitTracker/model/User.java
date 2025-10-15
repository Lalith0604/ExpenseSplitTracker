package com.example.ExpenseSplitTracker.model;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private double balance;

    public User() {} // default no-arg constructor (for Spring)

    public User(String name, double balance) { // ✅ add this
        this.name = name;
        this.balance = balance;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}

