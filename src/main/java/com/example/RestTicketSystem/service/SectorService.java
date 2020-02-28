package com.example.RestTicketSystem.service;

import com.example.RestTicketSystem.domain.Sector;
import com.example.RestTicketSystem.domain.Stadium;
import com.example.RestTicketSystem.error.exception.ResourceAlreadyExistsException;
import com.example.RestTicketSystem.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Service
public class SectorService {
    private final SectorRepository sectorRepository;

    @Autowired
    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public boolean existsById(Integer id) {
        return sectorRepository.existsById(id);
    }

    public Sector findById(Integer id) {
        Sector sector = sectorRepository.findById(id).orElse(null);
        if (sector == null) {
            throw new ResourceNotFoundException("Sector with ID " + id + " doesn't exist!");
        }  else {
            return sector;
        }
    }

    public List<Sector> findAllSectorsByStadium(Stadium stadium) {
        return sectorRepository.findAllByStadium(stadium);
    }

    public List<Sector> findAll() {
        return sectorRepository.findAll();
    }

    public Sector saveSector(Sector sector) throws ResourceAlreadyExistsException {
        List<Sector> sectorsByStadium = findAllSectorsByStadium(sector.getStadium());
        for (Sector s : sectorsByStadium) {
            if (s.getSectorName().equals(sector.getSectorName()) && (!s.getId().equals(sector.getId()))) {
                throw new ResourceAlreadyExistsException("Sector with name [" + sector.getSectorName() + "] is already exist on the stadium [" + sector.getStadium().getStadiumName() + "]");
            }
        }
        return sectorRepository.save(sector);
    }

    public void deleteById(Integer id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Sector with ID " + id + " doesn't exist!");
        }
        sectorRepository.deleteById(id);
    }
}