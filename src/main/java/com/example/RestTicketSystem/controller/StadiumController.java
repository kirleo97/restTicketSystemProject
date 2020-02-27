package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.StadiumModelAssembler;
import com.example.RestTicketSystem.domain.Stadium;
import com.example.RestTicketSystem.error.exception.ResourceAlreadyExistsException;
import com.example.RestTicketSystem.model.StadiumModel;
import com.example.RestTicketSystem.service.StadiumService;
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
@RequestMapping(path = "/stadium", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@Validated
public class StadiumController {
    private final StadiumService stadiumService;

    @Autowired
    public StadiumController(StadiumService stadiumService) {
        this.stadiumService = stadiumService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<StadiumModel>> getAllStadiums() {
        List<Stadium> stadiums = stadiumService.findAll();
        CollectionModel<StadiumModel> collectionModel = new StadiumModelAssembler(StadiumController.class, StadiumModel.class).toCollectionModel(stadiums);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StadiumController.class).getAllStadiums()).withRel("allStadiums"));
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("{id}")
    public ResponseEntity<StadiumModel> getStadiumById(@PathVariable @Min(1) Integer id) {
        Stadium stadium = stadiumService.findById(id);
        StadiumModel stadiumModel = new StadiumModel(stadium);
        stadiumModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StadiumController.class).getStadiumById(id)).withRel("stadiumById"));
        return ResponseEntity.ok(stadiumModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StadiumModel> createStadium(@Valid @RequestBody Stadium stadium) throws ResourceAlreadyExistsException {
        Integer id = stadium.getId();
        if (id != null && stadiumService.existsById(id)) {
            throw new ResourceAlreadyExistsException("Stadium with ID [" + stadium.getId() + "] is already exist!");
        }
        Stadium savedStadium = stadiumService.saveStadium(stadium);
        StadiumModel stadiumModel = new StadiumModel(savedStadium);
        stadiumModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StadiumController.class).getStadiumById(savedStadium.getId())).withRel("createdStadium"));
        return new ResponseEntity<>(stadiumModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StadiumModel> putStadium(@Valid @RequestBody Stadium newStadium, @PathVariable @Min(1) Integer id) throws ResourceAlreadyExistsException {
        if (stadiumService.existsById(id)) {
            Stadium stadium = stadiumService.findById(id);
            BeanUtils.copyProperties(newStadium, stadium);
            stadium.setId(id);
            Stadium updatedStadium = stadiumService.saveStadium(stadium);
            StadiumModel stadiumModel = new StadiumModel(updatedStadium);
            stadiumModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StadiumController.class).getStadiumById(id)).withRel("updatedStadium"));
            return ResponseEntity.ok(stadiumModel);
        } else {
            Stadium savedStadium = stadiumService.saveStadium(newStadium);
            return new ResponseEntity<>(new StadiumModel(savedStadium).add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StadiumController.class).getStadiumById(savedStadium.getId())).withRel("createdStadium")), HttpStatus.CREATED);
        }
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StadiumModel> patchStadium(@Valid @RequestBody Stadium patchStadium, @PathVariable @Min(1) Integer id) throws ResourceAlreadyExistsException {
        Stadium stadium = stadiumService.findById(id);
        if (patchStadium.getStadiumName() != null) {
            stadium.setStadiumName(patchStadium.getStadiumName());
        }
        if (patchStadium.getEventTypes() != null) {
            stadium.setEventTypes(patchStadium.getEventTypes());
        }
        Stadium updatedStadium = stadiumService.saveStadium(stadium);
        return ResponseEntity.ok(new StadiumModel(updatedStadium).add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StadiumController.class).getStadiumById(id)).withRel("updatedStadium")));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteStadium(@PathVariable @Min(1) Integer id) {
        stadiumService.deleteById(id);
    }
}