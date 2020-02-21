package com.example.RestTicketSystem.repository;

import com.example.RestTicketSystem.domain.EventType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventTypeRepository extends JpaRepository<EventType, Integer> {
    EventType findByEventTypeName(String eventTypeName);
}
