package com.example.ExpenseSplitTracker.model;

import jakarta.persistence.*;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDateTime;

@Entity  // ← THIS WAS MISSING!
@Table(name = "expenses")
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String splitType;

    @ElementCollection
    @CollectionTable(name = "expense_splits", joinColumns = @JoinColumn(name = "expense_id"))
    @MapKeyColumn(name = "user_name")
    @Column(name = "amount")
    private Map<String, Double> splitDetails = new HashMap<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Default constructor
    public Expense() {}

    // Constructor
    public Expense(Double amount, String splitType, Map<String, Double> splitDetails) {
        this.amount = amount;
        this.splitType = splitType;
        this.splitDetails = splitDetails;
        this.createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getSplitType() { return splitType; }
    public void setSplitType(String splitType) { this.splitType = splitType; }

    public Map<String, Double> getSplitDetails() { return splitDetails; }
    public void setSplitDetails(Map<String, Double> splitDetails) {
        this.splitDetails = splitDetails;
    }

    public Group getGroup() { return group; }
    public void setGroup(Group group) { this.group = group; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
