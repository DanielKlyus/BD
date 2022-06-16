package com.nsu.controllers;//package com.nsu.org.data.controller;
//
//import com.nsu.org.data.service.SupervisorDivisionService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@CrossOrigin(origins = "http://localhost:3000")
//@RestController
//@RequestMapping("/supervisors-division")
//public class SupervisorDivisionController {
//    @Autowired
//    private SupervisorDivisionService service;
//
//    @GetMapping("/headers")
//    public List<String> getHeaders() {
//
//        return fieldNames;
//    }
//
//    @GetMapping("/foreign-keys")
//    public List<String> getForeignKeys() {
//        return List.of(
//                "Division"
//        );
//    }
//
//    @GetMapping("/filters")
//    public List<String> getFilters() {
//        return List.of();
//    }
//
//}
