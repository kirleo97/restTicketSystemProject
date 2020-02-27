package com.example.RestTicketSystem.controller;

import com.example.RestTicketSystem.assembler.ManagerModelAssembler;
import com.example.RestTicketSystem.domain.Manager;
import com.example.RestTicketSystem.error.exception.ResourceAlreadyExistsException;
import com.example.RestTicketSystem.model.ManagerModel;
import com.example.RestTicketSystem.service.ManagerService;
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
@RequestMapping(path = "/manager", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
@Validated
public class ManagerController {
    private final ManagerService managerService;

    @Autowired
    public ManagerController(ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping
    public ResponseEntity<CollectionModel<ManagerModel>> getAllManagers() {
        List<Manager> managers = managerService.findAll();
        CollectionModel<ManagerModel> collectionModel = new ManagerModelAssembler(ManagerController.class, ManagerModel.class).toCollectionModel(managers);
        collectionModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerController.class).getAllManagers()).withRel("allManagers"));
        return ResponseEntity.ok(collectionModel);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerModel> getManagerById(@PathVariable @Min(1) Integer id) {
        Manager manager = managerService.findById(id);
        ManagerModel managerModel = new ManagerModel(manager);
        managerModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerController.class).getManagerById(id)).withRel("managerById"));
        return ResponseEntity.ok(managerModel);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagerModel> createManager(@Valid @RequestBody Manager manager) throws ResourceAlreadyExistsException {
        Integer id = manager.getId();
        if (id != null && managerService.existsById(id)) {
            throw new ResourceAlreadyExistsException("Manager with ID [" + manager.getId() + "] is already exist!");
        }
        Manager savedManager = managerService.saveManager(manager);
        ManagerModel managerModel = new ManagerModel(savedManager);
        managerModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerController.class).getManagerById(savedManager.getId())).withRel("createdManager"));
        return new ResponseEntity<>(managerModel, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagerModel> putManager(@Valid @RequestBody Manager newManager, @PathVariable @Min(1) Integer id) throws ResourceAlreadyExistsException {
        if (managerService.existsById(id)) {
            Manager manager = managerService.findById(id);
            BeanUtils.copyProperties(newManager, manager);
            manager.setId(id);
            Manager updatedManager = managerService.saveManager(manager);
            ManagerModel managerModel = new ManagerModel(updatedManager);
            managerModel.add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerController.class).getManagerById(id)).withRel("updatedManager"));
            return ResponseEntity.ok(managerModel);
        } else {
            Manager savedManager = managerService.saveManager(newManager);
            return new ResponseEntity<>(new ManagerModel(savedManager).add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerController.class).getManagerById(savedManager.getId())).withRel("savedManager")), HttpStatus.CREATED);
        }
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagerModel> patchManager(@Valid @RequestBody Manager patchManager, @PathVariable @Min(1) Integer id) throws ResourceAlreadyExistsException {
        Manager manager = managerService.findById(id);
        if (patchManager.getManagerName() != null) {
            manager.setManagerName(patchManager.getManagerName());
        }
        if (patchManager.getManagerTelephoneNumber() != null) {
            manager.setManagerTelephoneNumber(patchManager.getManagerTelephoneNumber());
        }
        Manager updatedManager = managerService.saveManager(manager);
        return ResponseEntity.ok(new ManagerModel(updatedManager).add(WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ManagerController.class).getManagerById(updatedManager.getId())).withRel("updatedManager")));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteManager(@PathVariable @Min(1) Integer id) {
        managerService.deleteById(id);
    }
}