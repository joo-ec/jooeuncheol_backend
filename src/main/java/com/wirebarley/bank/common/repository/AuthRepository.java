package com.wirebarley.bank.common.repository;

import com.wirebarley.bank.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Member, Long> {

    Member findByUserId(String userId);

}
