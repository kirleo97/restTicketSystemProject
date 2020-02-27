package com.example.RestTicketSystem.service;

import com.example.RestTicketSystem.domain.Manager;
import com.example.RestTicketSystem.error.exception.ResourceAlreadyExistsException;
import com.example.RestTicketSystem.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Optional;

@Service
public class ManagerService {
    private final ManagerRepository managerRepository;

    @Autowired
    public ManagerService(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public boolean existsById(Integer id) {
        return managerRepository.existsById(id);
    }

    public Manager findById(Integer id) {
        Manager manager = managerRepository.findById(id).orElse(null);
        if (manager == null) {
            throw new ResourceNotFoundException("Manager with ID " + id + " doesn't exist!");
        }  else {
            return manager;
        }
    }

    public Manager findByTelephoneNumber(String telephoneNumber) {
        return managerRepository.findByManagerTelephoneNumber(telephoneNumber);
    }

    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    public Manager saveManager(Manager manager) throws ResourceAlreadyExistsException {
        String telephoneNumber = manager.getManagerTelephoneNumber();
        Manager checkManager = findByTelephoneNumber(telephoneNumber);
        if ( (checkManager != null) && (!checkManager.getId().equals(manager.getId())) ) {
            throw new ResourceAlreadyExistsException("Manager with telephone number [" + telephoneNumber + "] is already exist!");
        }
        return managerRepository.save(manager);
    }

    public void deleteById(Integer id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Manager with ID " + id + " doesn't exist!");
        }
        managerRepository.deleteById(id);
    }

    /*public boolean isValidationForManagerSuccessful(Manager manager, BindingResult bindingResult) {
        Manager checkManager = managerRepository.findByManagerTelephoneNumber(manager.getManagerTelephoneNumber());
        if (checkManager != null) {
            if(!checkManager.getId().equals(manager.getId())) {
                bindingResult.addError(new FieldError("manager", "managerTelephoneNumber", "Manager with telephone number '" + manager.getManagerTelephoneNumber() + "' already exist. Please enter a different number."));
            }
        }
        return !bindingResult.hasErrors();
    }*/
}