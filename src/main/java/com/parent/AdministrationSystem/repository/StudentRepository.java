package com.parent.AdministrationSystem.repository;

import com.parent.AdministrationSystem.entity.Students;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Students, Long> {
    Optional<Students> findByUsersEmail(String email);
}
