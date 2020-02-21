package com.example.RestTicketSystem.repository;

import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.domain.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StadiumRepository extends JpaRepository<Stadium, Integer> {
    Stadium findByStadiumName(String stadiumName);
    List<Stadium> findAllByEventTypes(EventType eventTypes);
}