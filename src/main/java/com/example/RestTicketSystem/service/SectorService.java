package com.example.RestTicketSystem.service;

import com.example.RestTicketSystem.domain.Sector;
import com.example.RestTicketSystem.domain.Stadium;
import com.example.RestTicketSystem.repository.SectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
public class SectorService {
    private final SectorRepository sectorRepository;

    @Autowired
    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public Optional<Sector> findById(Integer id) {
        return sectorRepository.findById(id);
    }

    public List<Sector> findAll() {
        return sectorRepository.findAll();
    }

    public Sector saveSector(Sector sector) {
        return sectorRepository.save(sector);
    }

    public void deleteById(Integer id) { sectorRepository.deleteById(id); }

    public List<Sector> findSectorsByStadium(Stadium stadium) {
        return sectorRepository.findAllByStadium(stadium);
    }

    public void checkValidationForSector(Sector checkSector, BindingResult bindingResult) {
        String checkSectorName = checkSector.getSectorName();
        List<Sector> sectorsToCheck = findSectorsByStadium(checkSector.getStadium());
        if (checkSector.getId() != null) {
            Sector sector = findById(checkSector.getId()).get();
            sectorsToCheck.remove(sector);
        }
        for (Sector sector : sectorsToCheck) {
            if (sector.getSectorName().equals(checkSectorName)) {
                bindingResult.addError(new FieldError("checkSector", "sectorName", "A sector with this name already exists in the selected stadium. Please enter a different name. [" + sector.getSectorName() + "]"));
            }
        }
    }
}