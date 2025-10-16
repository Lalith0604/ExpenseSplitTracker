package com.example.ExpenseSplitTracker.repository;

import com.example.ExpenseSplitTracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNameAndGroupId(String name, Long groupId);
    List<User> findByGroupId(Long groupId);
}
