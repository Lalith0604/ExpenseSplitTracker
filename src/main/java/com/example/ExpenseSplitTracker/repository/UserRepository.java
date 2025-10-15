package com.example.ExpenseSplitTracker.repository;

import com.example.ExpenseSplitTracker.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNameAndGroupId(String name, Long groupId);
    List<User> findByGroupId(Long groupId);
}