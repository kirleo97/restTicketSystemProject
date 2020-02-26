package com.example.RestTicketSystem.domain;

import com.example.RestTicketSystem.error.validator.EventTypeName;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "eventTypes")
public class EventType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "EVENTTYPE_ID")
    private Integer id;

    @Pattern(message = "The value must start with a capital letter. Irregular shape: ${validatedValue}",
            regexp = "^[A-Z]{1}.{1,99}")
    @Length(min = 2, max = 100, message = "The length must be between 2 and 100 letters")
    @NotBlank(message = "Please provide a name for EventType")
    @EventTypeName
    @Column(name = "EVENTTYPE_NAME", nullable = false, unique = true)
    private String eventTypeName;

    public EventType() {
    }

    @Autowired
    public EventType(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEventTypeName() {
        return eventTypeName;
    }

    public void setEventTypeName(String eventTypeName) {
        this.eventTypeName = eventTypeName;
    }
}