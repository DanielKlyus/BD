package com.nsu.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/entity-manager")
public class EntityManagerController {

    @GetMapping
    public List<String> getEntitiesList() {
        return List.of(
                "employees",
                "department",
                "staffEquipment",
                "project",
                "client",
                "contract",
                "takenStaffEquipment",
                "participateProject",
                "otherContractor",
                "contractorProject"
        );
    }

    @GetMapping("/statistics")
    public List<String> getStatisticsList(){
        return List.of("");
    }
}