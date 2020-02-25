package com.example.RestTicketSystem.service;

import com.example.RestTicketSystem.domain.Event;
import com.example.RestTicketSystem.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private final EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public Optional<Event> findById(Integer id) { return eventRepository.findById(id); }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    public void deleteById(Integer id) {
        eventRepository.deleteById(id);
    }

    public void checkIfDateOfEventIsInPeriodOfPreparationAndDismantle(Event event, BindingResult bindingResult) {
        LocalDate dateOfEvent = LocalDate.from(event.getDateOfEvent());
        if (dateOfEvent.isBefore(event.getStartOfPreparationOfStadium()) || dateOfEvent.isAfter(event.getEndOfDismantleOfStadium())) {
            bindingResult.addError(new FieldError("event", "dateOfEvent", "The date of the event must be included in the period of preparation and completion of the event ( " + dateOfEvent + ")"));
        }
    }

    public void checkIfStartOfPreparationIsBeforeEndOfDismantleOrEquals(Event event, BindingResult bindingResult) {
        if (Period.between(event.getStartOfPreparationOfStadium(), event.getEndOfDismantleOfStadium()).isNegative()) {
            bindingResult.addError(new FieldError("event", "endOfDismantleOfStadium", "The selected period is not possible, since the beginning of the period should not be after its end ( " + event.getStartOfPreparationOfStadium() + " - " + event.getEndOfDismantleOfStadium()));
        }
    }

    public void checkIfPeriodOfEventIsNotIntersectWithOtherEventsOfStadium(Event checkEvent, BindingResult bindingResult) {
        List<Event> otherEventsByStadium = eventRepository.findAllByStadiumOfEvent(checkEvent.getStadiumOfEvent());
        if (checkEvent.getId() != null) {
            Event event = findById(checkEvent.getId()).get();
            otherEventsByStadium.remove(event);
        }
        LocalDate startOfCheckEvent = checkEvent.getStartOfPreparationOfStadium();
        LocalDate endOfCheckEvent = checkEvent.getEndOfDismantleOfStadium();
        LocalDate startOfOtherEvent;
        LocalDate endOfOtherEvent;
        for (Event event : otherEventsByStadium) {
            startOfOtherEvent = event.getStartOfPreparationOfStadium();
            endOfOtherEvent = event.getEndOfDismantleOfStadium();
            if (startOfCheckEvent.isBefore(startOfOtherEvent)) {
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
            }
        }
    }

    public void checkValidationFormForEvent(Event event, BindingResult bindingResult) {
        checkIfDateOfEventIsInPeriodOfPreparationAndDismantle(event, bindingResult);
        checkIfPeriodOfEventIsNotIntersectWithOtherEventsOfStadium(event, bindingResult);
        checkIfStartOfPreparationIsBeforeEndOfDismantleOrEquals(event, bindingResult);
    }
}