package com.example.RestTicketSystem.repository;

import com.example.RestTicketSystem.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {
    Manager findByManagerTelephoneNumber(String managerTelephoneNumber);
}