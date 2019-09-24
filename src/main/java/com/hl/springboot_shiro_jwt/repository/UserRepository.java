package com.hl.springboot_shiro_jwt.repository;

import com.hl.springboot_shiro_jwt.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsernameAndPassword(String username, String password);

    User findByUsername(String username);
}
