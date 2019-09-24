package com.hl.springboot_shiro_jwt.repository;

import com.hl.springboot_shiro_jwt.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, String> {

}
