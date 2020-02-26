package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.ManagerModelAssembler;
import com.example.RestTicketSystem.domain.Manager;
import com.example.RestTicketSystem.error.exception.ManagerNotFoundException;
import com.example.RestTicketSystem.model.ManagerModel;
import com.example.RestTicketSystem.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/manager", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class ManagerController {
    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping
    public CollectionModel<ManagerModel> getAllManagers() {
        List<Manager> managers = managerService.findAll();
        CollectionModel<ManagerModel> collectionModel = new ManagerModelAssembler(ManagerController.class, ManagerModel.class).toCollectionModel(managers);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerController.class).getAllManagers()).withRel("allManagers"));
        return collectionModel;
    }

    @GetMapping("/{id}")
    public EntityModel<ManagerModel> getManagerById(@PathVariable Integer id) {
        //return managerService.findById(id).orElseThrow(() -> new ManagerNotFoundException(id));
        Manager manager = managerService.findById(id).orElseThrow(() -> new ManagerNotFoundException(id));
        ManagerModel managerModel = new ManagerModel(manager);
        EntityModel<ManagerModel> entityModel = new EntityModel<>(managerModel, WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerController.class).getManagerById(id)).withRel("eventTypeById"));
        return entityModel;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json")
    public Manager createManager(@RequestBody Manager manager) {
        return managerService.saveManager(manager);
    }

    @PutMapping("/{id}")
    public Manager putManager(@RequestBody Manager newManager, @PathVariable Integer id) {
        Manager manager = managerService.findById(id).orElse(null);
        if (manager != null) {
            manager.setManagerName(newManager.getManagerName());
            manager.setManagerTelephoneNumber(newManager.getManagerTelephoneNumber());
            return managerService.saveManager(manager);
        }
        return managerService.saveManager(newManager);
    }

    @PatchMapping(value = "/{id}", consumes = "application/json")
    public Manager patchManager(@RequestBody Map<String, Object> update, @PathVariable Integer id) {
        Manager manager = managerService.findById(id).orElseThrow(() -> new ManagerNotFoundException(id));
        if (update.containsKey("managerName")) {
            manager.setManagerName((String) update.get("managerName"));
        }
        if (update.containsKey("managerTelephoneNumber")) {
            manager.setManagerTelephoneNumber((String) update.get("managerTelephoneNumber"));
        }
        return managerService.saveManager(manager);
    }

    /*@PatchMapping(value = "/{id}", consumes = "application/json")
    public Manager patchManager(@PathVariable Integer id, @RequestBody Manager patchManager) {
        Manager manager = managerService.findById(id).orElseThrow(() -> new ManagerNotFoundException(id));
        //Set<String> fields = new HashSet<>();
        if (patchManager.getManagerName() != null) {
            manager.setManagerName(patchManager.getManagerName());
        }
        if (patchManager.getManagerTelephoneNumber() != null) {
            manager.setManagerTelephoneNumber(patchManager.getManagerTelephoneNumber());
        }
        *//*if (fields.size() != 0) {
            throw new ManagerUnsupportedFieldPatchException(fields);
        }*//*
        return managerService.saveManager(manager);
    }*/

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteManager(@PathVariable Integer id) {
        try {
            managerService.deleteById(id);
        } catch (EmptyResultDataAccessException e) { }
    }
}