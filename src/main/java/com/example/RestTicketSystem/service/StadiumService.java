package com.example.RestTicketSystem.service;

import com.example.RestTicketSystem.domain.EventType;
import com.example.RestTicketSystem.domain.Stadium;
import com.example.RestTicketSystem.error.exception.ResourceAlreadyExistsException;
import com.example.RestTicketSystem.repository.StadiumRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Service
public class StadiumService {
    private final StadiumRepository stadiumRepository;

    @Autowired
    public StadiumService(StadiumRepository stadiumRepository) {
        this.stadiumRepository = stadiumRepository;
    }

    public boolean existsById(Integer id) {
        return stadiumRepository.existsById(id);
    }

    public Stadium findById(Integer id) {
        Stadium stadium = stadiumRepository.findById(id).orElse(null);
        if (stadium == null) {
            throw new ResourceNotFoundException("Stadium with ID " + id + " doesn't exist!");
        }  else {
            return stadium;
        }
    }

    public Stadium findByName(String stadiumName) {
        return stadiumRepository.findByStadiumName(stadiumName);
    }

    public List<Stadium> findAll() {
        return stadiumRepository.findAll();
    }

    public Stadium saveStadium(Stadium stadium) throws ResourceAlreadyExistsException {
        String stadiumName = stadium.getStadiumName();
        Stadium checkStadium = findByName(stadiumName);
        if ( (checkStadium != null) && (!checkStadium.getId().equals(stadium.getId())) ) {
            throw new ResourceAlreadyExistsException("Stadium with name [" + stadiumName + "] is already exist!");
        }
        return stadiumRepository.save(stadium);
    }

    public void deleteById(Integer id) {
        if (!existsById(id)) {
            throw new ResourceNotFoundException("Manager with ID " + id + " doesn't exist!");
        }
        stadiumRepository.deleteById(id);
    }

    public boolean isValidationForCreateStadiumSuccessful(Stadium stadium, BindingResult bindingResult) {
        String stadiumName = stadium.getStadiumName();
        if(isStadiumWithDefinedNameExist(stadiumName)) {
            bindingResult.addError(new FieldError("stadium", "stadiumName", "Stadium with name '" + stadiumName + "' already exists. Please enter a different name"));
        }
        return !bindingResult.hasErrors();
    }

    public boolean isValidationForUpdateStadiumSuccessful(Stadium stadium, BindingResult bindingResult) {
        String stadiumName = stadium.getStadiumName();
        if(isStadiumWithDefinedNameExist(stadiumName)) {
            Stadium checkStadium = stadiumRepository.findByStadiumName(stadiumName);
            if (!stadium.getId().equals(checkStadium.getId())) {
                bindingResult.addError(new FieldError("stadium", "stadiumName", "Stadium with name '" + stadiumName + "' already exists. Please enter a different name"));
                return false;
            }
        }
        return true;
    }

    public boolean isStadiumWithDefinedNameExist(String stadiumName) {
        return stadiumRepository.findByStadiumName(stadiumName) != null;
    }

    public List<Stadium> findAllStadiumsByEventType(EventType eventType) {
        return stadiumRepository.findAllByEventTypes(eventType);
    }
}