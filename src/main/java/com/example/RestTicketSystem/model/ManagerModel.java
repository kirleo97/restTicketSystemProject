package com.example.RestTicketSystem.model;

import com.example.RestTicketSystem.domain.Manager;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Relation(value = "manager", collectionRelation = "managers")
public class ManagerModel extends RepresentationModel<ManagerModel> {
    private final String managerName;
    private final String managerTelephoneNumber;

    public String getManagerName() {
        return managerName;
    }

    public String getManagerTelephoneNumber() {
        return managerTelephoneNumber;
    }

    public ManagerModel(Manager manager) {
        this.managerName = manager.getManagerName();
        this.managerTelephoneNumber = manager.getManagerTelephoneNumber();
    }
}