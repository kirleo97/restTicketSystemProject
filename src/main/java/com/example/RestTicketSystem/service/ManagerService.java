package com.example.RestTicketSystem.service;

import com.example.RestTicketSystem.domain.Manager;
import com.example.RestTicketSystem.repository.ManagerRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<Manager> findById(Integer id) {
        return managerRepository.findById(id);
    }

    public List<Manager> findAll() {
        return managerRepository.findAll();
    }

    public Manager saveManager(Manager manager) {
        return managerRepository.save(manager);
    }

    public void deleteById(Integer id) {
        managerRepository.deleteById(id);
    }

    public boolean isValidationForManagerSuccessful(Manager manager, BindingResult bindingResult) {
        Manager checkManager = managerRepository.findByManagerTelephoneNumber(manager.getManagerTelephoneNumber());
        if (checkManager != null) {
            if(!checkManager.getId().equals(manager.getId())) {
                bindingResult.addError(new FieldError("manager", "managerTelephoneNumber", "Manager with telephone number '" + manager.getManagerTelephoneNumber() + "' already exist. Please enter a different number."));
            }
        }
        return !bindingResult.hasErrors();
    }
}