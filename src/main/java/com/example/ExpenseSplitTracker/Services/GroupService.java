package com.example.ExpenseSplitTracker.Services;

import com.example.ExpenseSplitTracker.model.*;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Transactional
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    
    public GroupService(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }
    
    public Group createGroup(String name) {
        Group group = new Group();
        group.setName(name);
        return groupRepository.save(group);
    }
    
    public Group getGroup(String name) {
        return groupRepository.findByName(name).orElse(null);
    }
    
    public User addUser(String groupName, String userName) {
        Group group = groupRepository.findByName(groupName).orElse(null);
        if (group == null) return null;
        
        User user = new User();
        user.setName(userName);
        user.setBalance(0.0);
        user.setGroup(group);
        return userRepository.save(user);
    }
}
