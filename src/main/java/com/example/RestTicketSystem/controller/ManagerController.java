package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.ManagerModelAssembler;
import com.example.RestTicketSystem.domain.Manager;
import com.example.RestTicketSystem.error.ManagerNotFoundException;
import com.example.RestTicketSystem.model.ManagerModel;
import com.example.RestTicketSystem.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/manager", produces = "application/json")
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
    public Manager getManagerById(@PathVariable Integer id) {
        return managerService.findById(id).orElseThrow(() -> new ManagerNotFoundException(id));
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
    public Manager patchManager(@PathVariable Integer id, @RequestBody Manager patchManager) {
        Manager manager = managerService.findById(id).orElseThrow(() -> new ManagerNotFoundException(id));
        //Set<String> fields = new HashSet<>();
        if (patchManager.getManagerName() != null) {
            manager.setManagerName(patchManager.getManagerName());
        }
        if (patchManager.getManagerTelephoneNumber() != null) {
            manager.setManagerTelephoneNumber(patchManager.getManagerTelephoneNumber());
        }
        /*if (fields.size() != 0) {
            throw new ManagerUnsupportedFieldPatchException(fields);
        }*/
        return managerService.saveManager(manager);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteManager(@PathVariable Integer id) {
        try {
            managerService.deleteById(id);
        } catch (EmptyResultDataAccessException e) { }
    }
}