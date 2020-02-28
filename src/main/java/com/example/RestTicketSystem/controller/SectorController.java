package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.SectorModelAssembler;
import com.example.RestTicketSystem.domain.Sector;
import com.example.RestTicketSystem.error.exception.ResourceAlreadyExistsException;
import com.example.RestTicketSystem.model.SectorModel;
import com.example.RestTicketSystem.service.SectorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping(path = "/sector", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@Validated
public class SectorController {
    private final SectorService sectorService;

    @Autowired
    public SectorController(SectorService sectorService) {
        this.sectorService = sectorService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<SectorModel>> getAllSectors() {
        List<Sector> sectors = sectorService.findAll();
        CollectionModel<SectorModel> collectionModel = new SectorModelAssembler(SectorController.class, SectorModel.class).toCollectionModel(sectors);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SectorController.class).getAllSectors()).withRel("allSectors"));
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("{id}")
    public ResponseEntity<SectorModel> getSectorById(@PathVariable @Min(1) Integer id) {
        Sector sector = sectorService.findById(id);
        SectorModel sectorModel = new SectorModel(sector);
        sectorModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SectorController.class).getSectorById(id)).withRel("sectorById"));
        return ResponseEntity.ok(sectorModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SectorModel> createSector(@Valid @RequestBody Sector sector) throws ResourceAlreadyExistsException {
        Integer id = sector.getId();
        if (id != null && sectorService.existsById(id)) {
            throw new ResourceAlreadyExistsException("Sector with ID [" + sector.getId() + "] is already exist!");
        }
        Sector savedSector = sectorService.saveSector(sector);
        SectorModel sectorModel = new SectorModel(savedSector);
        sectorModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SectorController.class).getSectorById(savedSector.getId())).withRel("createdSector"));
        return new ResponseEntity<>(sectorModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SectorModel> putSector(@Valid @RequestBody Sector newSector, @PathVariable @Min(1) Integer id) throws ResourceAlreadyExistsException {
        if (sectorService.existsById(id)) {
            Sector sector = sectorService.findById(id);
            BeanUtils.copyProperties(newSector, sector);
            sector.setId(id);
            Sector updatedSector = sectorService.saveSector(sector);
            SectorModel sectorModel = new SectorModel(updatedSector);
            sectorModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SectorController.class).getSectorById(id)).withRel("updatedSector"));
            return ResponseEntity.ok(sectorModel);
        } else {
            Sector savedSector = sectorService.saveSector(newSector);
            return new ResponseEntity<>(new SectorModel(savedSector).add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SectorController.class).getSectorById(savedSector.getId())).withRel("createdSector")), HttpStatus.CREATED);
        }
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SectorModel> patchSector(@Valid @RequestBody Sector patchSector, @PathVariable @Min(1) Integer id) throws ResourceAlreadyExistsException {
        Sector sector = sectorService.findById(id);
        if (patchSector.getStadium() != null) {
            sector.setStadium(patchSector.getStadium());
        }
        if (patchSector.getSectorName() != null) {
            sector.setSectorName(patchSector.getSectorName());
        }
        if (patchSector.getNumberOfSeats() != null) {
            sector.setNumberOfSeats(patchSector.getNumberOfSeats());
        }
        Sector updatedSector = sectorService.saveSector(sector);
        return ResponseEntity.ok(new SectorModel(updatedSector).add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SectorController.class).getSectorById(id)).withRel("updatedSector")));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteSector(@PathVariable @Min(1) Integer id) {
        sectorService.deleteById(id);
    }
}