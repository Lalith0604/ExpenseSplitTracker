package com.example.ExpenseSplitTracker.model;

import lombok.*;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group {
    private String name;
    private Map<String, User> users = new HashMap<>();

    public Group() {}

    public Group(String name) {
        this.name = name;
    }

    // ✅ Add these methods
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Map<String, User> getUsers() { return users; }
    public void setUsers(Map<String, User> users) { this.users = users; }
}