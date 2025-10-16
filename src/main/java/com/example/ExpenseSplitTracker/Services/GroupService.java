package com.example.ExpenseSplitTracker.Services;

import com.example.ExpenseSplitTracker.model.*;
import com.example.ExpenseSplitTracker.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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
        System.out.println("DEBUG: Creating group with name: " + name);
        try {
            Group group = new Group();
            group.setName(name);
            System.out.println("DEBUG: Group object created: " + group.getName());

            Group savedGroup = groupRepository.save(group);
            System.out.println("DEBUG: Group saved with id: " + savedGroup.getId());

            return savedGroup;
        } catch (Exception e) {
            System.out.println("DEBUG: Error creating group: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    @Transactional(readOnly = true)
    public Group getGroup(String name) {
        System.out.println("DEBUG: Searching for group: '" + name + "'");
        Optional<Group> groupOpt = groupRepository.findByName(name);
        if (groupOpt.isPresent()) {
            System.out.println("DEBUG: Found group: " + groupOpt.get().getName() + " (id=" + groupOpt.get().getId() + ")");
            return groupOpt.get();
        } else {
            System.out.println("DEBUG: Group not found in database");
            // Let's also check all groups
            List<Group> allGroups = groupRepository.findAll();
            System.out.println("DEBUG: All groups in database: " +
                    allGroups.stream().map(Group::getName).collect(Collectors.toList()));
            return null;
        }
    }

    public User addUser(String groupName, String userName) {
        System.out.println("DEBUG: Starting addUser - groupName=" + groupName + ", userName=" + userName);

        try {
            // Add a small delay to ensure transaction completion
            Thread.sleep(100); // 100ms delay

            Group group = getGroup(groupName);
            if (group == null) {
                System.out.println("DEBUG: Group not found after delay, trying again...");
                // Try one more time
                Thread.sleep(200);
                group = getGroup(groupName);
            }

            if (group == null) {
                System.out.println("DEBUG: Group still not found: " + groupName);
                return null;
            }

            System.out.println("DEBUG: Found group: " + group.getName() + " (id=" + group.getId() + ")");

            User user = new User();
            user.setName(userName);
            user.setBalance(0.0);
            user.setGroup(group);

            User savedUser = userRepository.save(user);
            System.out.println("DEBUG: User saved successfully with id: " + savedUser.getId());

            return savedUser;

        } catch (Exception e) {
            System.out.println("DEBUG: Exception in addUser: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }



    public String debugUsers() {
        try {
            List<User> allUsers = userRepository.findAll();
            StringBuilder result = new StringBuilder();
            result.append("Found ").append(allUsers.size()).append(" users: ");

            for (User user : allUsers) {
                result.append(user.getName())
                        .append("(group:")
                        .append(user.getGroup() != null ? user.getGroup().getName() : "null")
                        .append(") ");
            }

            return result.toString();
        } catch (Exception e) {
            return "Error getting users: " + e.getMessage();
        }
    }


    public List<Group> getAllGroups() {
        return groupRepository.findAll();
    }
}
