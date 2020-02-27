package com.example.RestTicketSystem.service;

import com.example.RestTicketSystem.domain.Event;
import com.example.RestTicketSystem.error.exception.EventDataException;
import com.example.RestTicketSystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public boolean existsById(Integer id) {
        return eventRepository.existsById(id);
    }

    public Event findById(Integer id) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            throw new ResourceNotFoundException("Event with ID " + id + " doesn't exist!");
        }  else {
            return event;
        }
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event saveEvent(Event event) throws EventDataException {
        List<Exception> potentialExceptions = new ArrayList<>();
        if (!checkIfDateOfEventIsInPeriodOfPreparationAndDismantle(event)) {
            potentialExceptions.add(new Exception("The date of the event must be included in the period of preparation and completion of the event ( " + event.getDateOfEvent() + ")"));
        }
        if (!checkIfStartOfPreparationIsBeforeEndOfDismantleOrEquals(event)) {
            potentialExceptions.add(new Exception("The start of preparation must be before the end of dismantle"));
        }
        if (checkIfPeriodOfEventIntersectWithOtherEventsOfStadium(event)) {
            potentialExceptions.add(new Exception("The selected period of the event overlaps with the holding of other events: " + event.getStartOfPreparationOfStadium() + " - " + event.getEndOfDismantleOfStadium()));
        }
        if (potentialExceptions.size() > 0) {
            throw new EventDataException(potentialExceptions);
        }
        return eventRepository.save(event);
    }

    public void deleteById(Integer id) {
        eventRepository.deleteById(id);
    }

    public boolean checkIfDateOfEventIsInPeriodOfPreparationAndDismantle(Event event) {
        LocalDate dateOfEvent = LocalDate.from(event.getDateOfEvent());
        return dateOfEvent.isBefore(event.getStartOfPreparationOfStadium()) || dateOfEvent.isAfter(event.getEndOfDismantleOfStadium()) ? false : true;
    }

    public boolean checkIfStartOfPreparationIsBeforeEndOfDismantleOrEquals(Event event) {
        return Period.between(event.getStartOfPreparationOfStadium(), event.getEndOfDismantleOfStadium()).isNegative() ? false : true;
    }

    public boolean checkIfPeriodOfEventIntersectWithOtherEventsOfStadium(Event checkEvent) {
        List<Event> otherEventsByStadium = eventRepository.findAllByStadiumOfEvent(checkEvent.getStadiumOfEvent());
        if (checkEvent.getId() != null) {
            if (existsById(checkEvent.getId())) {
                Event event = findById(checkEvent.getId());
                otherEventsByStadium.remove(event);
            }
        }
        LocalDate startOfCheckEvent = checkEvent.getStartOfPreparationOfStadium();
        LocalDate endOfCheckEvent = checkEvent.getEndOfDismantleOfStadium();
        LocalDate startOfOtherEvent;
        LocalDate endOfOtherEvent;
        for (Event event : otherEventsByStadium) {
            startOfOtherEvent = event.getStartOfPreparationOfStadium();
            endOfOtherEvent = event.getEndOfDismantleOfStadium();
            /*if (startOfCheckEvent.isBefore(startOfOtherEvent)) {
                if (!endOfCheckEvent.isBefore(startOfOtherEvent)) {
                    bindingResult.addError(new FieldError("checkEvent", "endOfDismantleOfStadium", "The selected period of the event overlaps with the holding of other events: " + startOfOtherEvent + " - " + endOfOtherEvent));
                }
            }
            if (startOfCheckEvent.isAfter(startOfOtherEvent)) {
                if (!startOfCheckEvent.isAfter(endOfOtherEvent)) {
                    bindingResult.addError(new FieldError("checkEvent", "endOfDismantleOfStadium", "The selected period of the event overlaps with the holding of other events: " + startOfOtherEvent + " - " + endOfOtherEvent));
                }
            }
            if (startOfCheckEvent.isEqual(startOfOtherEvent)) {
                bindingResult.addError(new FieldError("checkEvent", "startOfPreparationOfStadium", "The selected period of the event overlaps with the holding of other events: " + startOfOtherEvent + " - " + endOfOtherEvent));
            }*/
            if ( (startOfCheckEvent.isBefore(startOfOtherEvent) && !endOfCheckEvent.isBefore(startOfOtherEvent)) || (startOfCheckEvent.isAfter(startOfOtherEvent) && !startOfCheckEvent.isAfter(endOfOtherEvent)) || startOfCheckEvent.isEqual(startOfOtherEvent)) {
                return true;
            }
        }
        return false;
    }

    /*public void checkValidationFormForEvent(Event event, BindingResult bindingResult) {
        checkIfDateOfEventIsInPeriodOfPreparationAndDismantle(event, bindingResult);
        checkIfPeriodOfEventIntersectWithOtherEventsOfStadium(event, bindingResult);
        checkIfStartOfPreparationIsBeforeEndOfDismantleOrEquals(event, bindingResult);
    }*/
}