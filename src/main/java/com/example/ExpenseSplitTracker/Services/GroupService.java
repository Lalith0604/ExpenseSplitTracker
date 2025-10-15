package com.example.ExpenseSplitTracker.Services;

import com.example.ExpenseSplitTracker.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class GroupService {

    private final Map<String, Group> groups = new HashMap<>();

    public Group createGroup(String name) {
        Group g = new Group();
        g.setName(name);
        groups.put(name, g);
        return g;
    }

    public Group getGroup(String name) {
        return groups.get(name);
    }

    public User addUser(String groupName, String userName) {
        Group g = groups.get(groupName);
        if (g == null) return null;
        User user = new User(userName, 0);
        g.getUsers().put(userName, user);
        return user;
    }

    public Collection<Group> getAllGroups() {
        return groups.values();
    }
}
