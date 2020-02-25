package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.SectorModelAssembler;
import com.example.RestTicketSystem.domain.Sector;
import com.example.RestTicketSystem.domain.Stadium;
import com.example.RestTicketSystem.error.SectorNotFoundException;
import com.example.RestTicketSystem.model.SectorModel;
import com.example.RestTicketSystem.service.SectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/sector", produces = "application/json")
@CrossOrigin(origins = "*")
public class SectorController {
    private final SectorService sectorService;

    @Autowired
    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @GetMapping
    public CollectionModel<SectorModel> getAllSectors() {
        List<Sector> sectors = sectorService.findAll();
        CollectionModel<SectorModel> collectionModel = new SectorModelAssembler(SectorController.class, SectorModel.class).toCollectionModel(sectors);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SectorController.class).getAllSectors()).withRel("allSectors"));
        return collectionModel;
    }

    @GetMapping("{id}")
    public Sector getSectorById(@PathVariable Integer id) {
        return sectorService.findById(id).orElseThrow(() -> new SectorNotFoundException(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json")
    public Sector createSector(@RequestBody Sector sector) {
        return sectorService.saveSector(sector);
    }

    @PutMapping("/{id}")
    public Sector putSector(@RequestBody Sector newSector, @PathVariable Integer id) {
        Sector sector = sectorService.findById(id).orElse(null);
        if (sector != null) {
            sector.setStadium(newSector.getStadium());
            sector.setSectorName(newSector.getSectorName());
            return sectorService.saveSector(sector);
        }
        return sectorService.saveSector(newSector);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    public Sector patchSector(@RequestBody Map<String, Object> update, @PathVariable Integer id) {
        Sector sector = sectorService.findById(id).orElseThrow(() -> new SectorNotFoundException(id));
        if (update.containsKey("stadium")) {
            sector.setStadium((Stadium) update.get("stadium"));
        }
        if (update.containsKey("sectorName")) {
            sector.setSectorName((String) update.get("sectorName"));
        }
        if (update.containsKey("numberOfSeats")) {
            sector.setNumberOfSeats((Integer) update.get("numberOfSeats"));
        }
        return sectorService.saveSector(sector);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteSector(@PathVariable Integer id) {
        try {
            sectorService.deleteById(id);
        } catch (EmptyResultDataAccessException e) { }
    }
}