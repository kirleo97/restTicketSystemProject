package com.example.RestTicketSystem.repository;

import com.example.RestTicketSystem.domain.Sector;
import com.example.RestTicketSystem.domain.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectorRepository extends JpaRepository<Sector, Integer> {
    List<Sector> findAllByStadium(Stadium stadium);
}