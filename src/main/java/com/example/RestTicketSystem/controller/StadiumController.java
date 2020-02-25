package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.StadiumModelAssembler;
import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.domain.Stadium;
import com.example.RestTicketSystem.error.StadiumNotFoundException;
import com.example.RestTicketSystem.model.StadiumModel;
import com.example.RestTicketSystem.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/stadium", produces = "application/json")
@CrossOrigin(origins = "*")
public class StadiumController {
    private final StadiumService stadiumService;

    @Autowired
    public StadiumController(StadiumService stadiumService) {
        this.stadiumService = stadiumService;
    }

    @GetMapping
    public CollectionModel<StadiumModel> getAllStadiums() {
        List<Stadium> stadiums = stadiumService.findAll();
        CollectionModel<StadiumModel> collectionModel = new StadiumModelAssembler(StadiumController.class, StadiumModel.class).toCollectionModel(stadiums);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(StadiumController.class).getAllStadiums()).withRel("allStadiums"));
        return collectionModel;
    }

    @GetMapping("{id}")
    public Stadium getStadiumById(@PathVariable Integer id) {
        return stadiumService.findById(id).orElseThrow(() -> new StadiumNotFoundException(id));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json")
    public Stadium createStadium(@RequestBody Stadium stadium) {
        return stadiumService.saveStadium(stadium);
    }

    @PutMapping("/{id}")
    public Stadium putStadium(@RequestBody Stadium newStadium, @PathVariable Integer id) {
        Stadium stadium = stadiumService.findById(id).orElse(null);
        if (stadium != null) {
            stadium.setStadiumName(newStadium.getStadiumName());
            stadium.setEventTypes(newStadium.getEventTypes());
            return stadiumService.saveStadium(stadium);
        }
        return stadiumService.saveStadium(newStadium);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    public Stadium patchStadium(@RequestBody Map<String, Object> update, @PathVariable Integer id) {
        Stadium stadium = stadiumService.findById(id).orElseThrow(() -> new StadiumNotFoundException(id));
        if (update.containsKey("stadiumName")) {
            stadium.setStadiumName((String) update.get("stadiumName"));
        }
        if (update.containsKey("eventTypes")) {
            stadium.setEventTypes((List<EventType>) update.get("eventTypes"));
        }
        return stadiumService.saveStadium(stadium);
    }

    /*@PatchMapping(value = "/{id}", consumes = "application/json")
    public Stadium patchStadium(@PathVariable Integer id, @RequestBody Stadium patchStadium) {
        Stadium stadium = stadiumService.findById(id).orElseThrow(() -> new ManagerNotFoundException(id));
        //Set<String> fields = new HashSet<>();
        if (patchStadium.getStadiumName() != null) {
            stadium.setStadiumName(stadium.getStadiumName());
        }
        if (patchStadium.getEventTypes()!= null) {
            stadium.setEventTypes(patchStadium.getEventTypes());
        }
        *//*if (fields.size() != 0) {
            throw new StadiumUnsupportedFieldPatchException(fields);
        }*//*
        return stadiumService.saveStadium(stadium);
    }*/

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteStadium(@PathVariable Integer id) {
        try {
            stadiumService.deleteById(id);
        } catch (EmptyResultDataAccessException e) { }
    }
}