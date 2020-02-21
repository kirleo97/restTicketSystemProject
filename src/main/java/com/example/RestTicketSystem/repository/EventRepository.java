package com.example.RestTicketSystem.repository;

import com.example.RestTicketSystem.domain.Event;
import com.example.RestTicketSystem.domain.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByStadiumOfEvent(Stadium stadiumOfEvent);
}